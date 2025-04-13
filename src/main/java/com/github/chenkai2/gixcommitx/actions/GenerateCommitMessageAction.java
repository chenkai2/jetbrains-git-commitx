package com.github.chenkai2.gixcommitx.actions;

import com.github.chenkai2.gixcommitx.git.GitService;
import com.github.chenkai2.gixcommitx.service.LLMService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.CommitMessageI;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.ui.Refreshable;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * 生成Commit Message的Action
 */
public class GenerateCommitMessageAction extends AnAction {
    private static final Logger LOG = Logger.getInstance(GenerateCommitMessageAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        // 获取Git服务
        GitService gitService = new GitService(project);
        GitRepository repository = gitService.getCurrentRepository();

        if (repository == null) {
            Messages.showErrorDialog(project, "无法获取Git仓库", "错误");
            return;
        }

        // 检查是否有暂存的文件
        if (!gitService.hasStagedChanges()) {
            Messages.showWarningDialog(project, "没有暂存的文件，请先添加文件到暂存区", "警告");
            return;
        }

        // 获取提交信息编辑器
        CommitMessageI commitMessageI = getCommitMessageI(e);
        if (commitMessageI == null) {
            Messages.showErrorDialog(project, "无法获取提交信息编辑器", "错误");
            return;
        }

        // 显示状态通知
        Messages.showInfoMessage(project, "正在分析暂存的文件变更...", "生成Commit Message");

        // 获取暂存的文件和差异内容
        String[] stagedFiles = gitService.getStagedFiles();
        String diffContent = gitService.getDiffContent();

        // 创建LLM服务
        LLMService llmService = new LLMService(project);

        // 在后台线程中生成提交信息
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "生成Commit Message", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("正在生成提交信息...");

                try {
                    // 调用LLM服务生成提交信息
                    CompletableFuture<String> future = llmService.generateCommitMessage(stagedFiles, diffContent);
                    future.thenAccept(commitMessage -> {
                        // 确保在UI线程中设置提交信息
                        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater(() -> {
                            try {
                                // 设置提交信息
                                commitMessageI.setCommitMessage(commitMessage);
                                // 确保提交信息编辑器刷新
                                if (commitMessageI instanceof Refreshable) {
                                    ((Refreshable) commitMessageI).refresh();
                                }
                                Messages.showInfoMessage(project, "已生成提交信息", "成功");
                            } catch (Exception e) {
                                LOG.error("设置提交信息失败", e);
                                Messages.showErrorDialog(project, "设置提交信息失败: " + e.getMessage(), "错误");
                            }
                        });
                    }).exceptionally(ex -> {
                        LOG.error("生成提交信息失败", ex);
                        com.intellij.openapi.application.ApplicationManager.getApplication().invokeLater(() -> {
                            Messages.showErrorDialog(project, "生成提交信息失败: " + ex.getMessage(), "错误");
                        });
                        return null;
                    });
                } catch (Exception ex) {
                    LOG.error("生成提交信息失败", ex);
                    Messages.showErrorDialog(project, "生成提交信息失败: " + ex.getMessage(), "错误");
                }
            }
        });
    }

    /**
     * 获取提交信息编辑器
     */
    private CommitMessageI getCommitMessageI(AnActionEvent e) {
        Refreshable refreshable = Refreshable.PANEL_KEY.getData(e.getDataContext());
        if (refreshable instanceof CommitMessageI) {
            return (CommitMessageI) refreshable;
        }

        return VcsDataKeys.COMMIT_MESSAGE_CONTROL.getData(e.getDataContext());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}