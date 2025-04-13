package com.github.chenkai2.gixcommitx.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 存储插件设置的持久化组件
 */
@State(
    name = "com.github.chenkai2.gixcommitx.settings.CommitMessageGeneratorSettings",
    storages = {@Storage("GixCommitXSettings.xml")}
)
public class CommitMessageGeneratorSettings implements PersistentStateComponent<CommitMessageGeneratorSettings> {
    // LLM API的URL地址
    private String llmUrl = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
    // 使用的AI模型
    private String llmModel = "deepseek-v3";
    // 生成提交信息的提示词模板
    private String llmPrompt = "根据以下Git变更生成Git提交信息，格式为 <type>: <description>。\n文件：${files}\n变更内容：${diff}";
    // 系统指令
    private String llmSystem = "标题行格式为 <type>: <description>，字数不要超过50个，description如果不是中文，则翻译成中文。两个换行后，输出正文内容，每个要点作为一个符号列表，不超过70个字。type使用英文，description和正文用中文，如果不是，则翻译成中文。要点简洁清晰。";
    // 生成结果的随机性（0-1）
    private double llmTemperature = 0.7;
    // 采样时的累积概率阈值（0-1）
    private double llmTopP = 0.9;
    // API密钥
    private String llmApiKey = "";
    // API协议（ollama/openai）
    private String llmProtocol = "openai";
    // 生成结果的最大token数量
    private int llmMaxTokens = 2048;

    /**
     * 获取设置实例
     */
    public static CommitMessageGeneratorSettings getInstance() {
        return ServiceManager.getService(CommitMessageGeneratorSettings.class);
    }

    @Nullable
    @Override
    public CommitMessageGeneratorSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CommitMessageGeneratorSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    // Getters and Setters
    public String getLlmUrl() {
        return llmUrl;
    }

    public void setLlmUrl(String llmUrl) {
        this.llmUrl = llmUrl;
    }

    public String getLlmModel() {
        return llmModel;
    }

    public void setLlmModel(String llmModel) {
        this.llmModel = llmModel;
    }

    public String getLlmPrompt() {
        return llmPrompt;
    }

    public void setLlmPrompt(String llmPrompt) {
        this.llmPrompt = llmPrompt;
    }

    public String getLlmSystem() {
        return llmSystem;
    }

    public void setLlmSystem(String llmSystem) {
        this.llmSystem = llmSystem;
    }

    public double getLlmTemperature() {
        return llmTemperature;
    }

    public void setLlmTemperature(double llmTemperature) {
        this.llmTemperature = llmTemperature;
    }

    public double getLlmTopP() {
        return llmTopP;
    }

    public void setLlmTopP(double llmTopP) {
        this.llmTopP = llmTopP;
    }

    public String getLlmApiKey() {
        return llmApiKey;
    }

    public void setLlmApiKey(String llmApiKey) {
        this.llmApiKey = llmApiKey;
    }

    public String getLlmProtocol() {
        return llmProtocol;
    }

    public void setLlmProtocol(String llmProtocol) {
        this.llmProtocol = llmProtocol;
    }

    public int getLlmMaxTokens() {
        return llmMaxTokens;
    }

    public void setLlmMaxTokens(int llmMaxTokens) {
        this.llmMaxTokens = llmMaxTokens;
    }
}