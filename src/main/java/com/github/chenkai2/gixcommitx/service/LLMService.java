package com.github.chenkai2.gixcommitx.service;

import com.github.chenkai2.gixcommitx.settings.CommitMessageGeneratorSettings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * LLM服务连接器，负责与不同的LLM API进行通信
 */
public class LLMService {
    private static final Logger LOG = Logger.getInstance(LLMService.class);
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final Project project;
    private final CommitMessageGeneratorSettings settings;

    public LLMService(Project project) {
        this.project = project;
        this.settings = CommitMessageGeneratorSettings.getInstance();
    }

    /**
     * 生成提交信息
     *
     * @param stagedFiles 暂存的文件列表
     * @param diffContent 差异内容
     * @return 生成的提交信息
     */
    public CompletableFuture<String> generateCommitMessage(String[] stagedFiles, String diffContent) {
        CompletableFuture<String> future = new CompletableFuture<>();

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "生成Commit Message", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("正在连接LLM服务...");

                try {
                    // 替换模板变量
                    String prompt = settings.getLlmPrompt()
                            .replace("${files}", String.join("\n", stagedFiles))
                            .replace("${diff}", diffContent);

                    // 构建请求体
                    JsonObject requestBody = buildRequestBody(prompt);

                    // 构建请求
                    Request request = buildRequest(requestBody);

                    // 发送请求
                    try (Response response = CLIENT.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            throw new IOException("请求失败: " + response.code() + " " + response.message());
                        }

                        String result = processResponse(response);
                        future.complete(result);
                    }
                } catch (Exception e) {
                    LOG.error("生成提交信息失败", e);
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * 构建请求体
     */
    private JsonObject buildRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();
        String protocol = settings.getLlmProtocol();

        if ("openai".equals(protocol)) {
            requestBody.addProperty("model", settings.getLlmModel());
            
            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", settings.getLlmSystem());
            
            JsonObject userMessage = new JsonObject();
            userMessage.addProperty("role", "user");
            userMessage.addProperty("content", prompt);
            
            JsonArray messages = new JsonArray();
            messages.add(systemMessage);
            messages.add(userMessage);
            
            requestBody.add("messages", messages);
            requestBody.addProperty("temperature", settings.getLlmTemperature());
            requestBody.addProperty("top_p", settings.getLlmTopP());
            requestBody.addProperty("max_tokens", settings.getLlmMaxTokens());
            requestBody.addProperty("stream", false);
        } else if ("ollama".equals(protocol)) {
            requestBody.addProperty("model", settings.getLlmModel());
            requestBody.addProperty("system", settings.getLlmSystem());
            requestBody.addProperty("prompt", prompt);
            requestBody.addProperty("temperature", settings.getLlmTemperature());
            requestBody.addProperty("top_p", settings.getLlmTopP());
            requestBody.addProperty("max_tokens", settings.getLlmMaxTokens());
            requestBody.addProperty("stream", false);
        }

        return requestBody;
    }

    /**
     * 构建HTTP请求
     */
    private Request buildRequest(JsonObject requestBody) throws Exception {
        String apiUrl = settings.getLlmUrl();
        URL url = new URL(apiUrl);
        String protocol = settings.getLlmProtocol();
        String apiKey = settings.getLlmApiKey();

        Request.Builder requestBuilder = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.parse("application/json")
                ));

        // 添加认证头
        if ("openai".equals(protocol) && !apiKey.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + apiKey);
        }

        return requestBuilder.build();
    }

    /**
     * 处理API响应
     */
    private String processResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        String protocol = settings.getLlmProtocol();
        
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            
            if ("openai".equals(protocol)) {
                return jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            } else if ("ollama".equals(protocol)) {
                return jsonResponse.get("response").getAsString();
            }
            
            return responseBody; // 默认返回原始响应
        } catch (Exception e) {
            LOG.error("解析响应失败", e);
            return responseBody; // 解析失败时返回原始响应
        }
    }
}