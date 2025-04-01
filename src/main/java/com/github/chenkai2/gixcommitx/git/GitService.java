package com.github.chenkai2.gixcommitx.git;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.ChangeListManager;
import com.intellij.openapi.vcs.changes.ContentRevision;
import git4idea.GitContentRevision;
import git4idea.GitRevisionNumber;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Git操作服务，用于获取暂存的文件和差异内容
 */
public class GitService {
    private static final Logger LOG = Logger.getInstance(GitService.class);
    private final Project project;

    public GitService(Project project) {
        this.project = project;
    }

    /**
     * 获取当前Git仓库
     */
    public GitRepository getCurrentRepository() {
        GitRepositoryManager manager = GitRepositoryManager.getInstance(project);
        List<GitRepository> repositories = manager.getRepositories();
        if (repositories.isEmpty()) {
            return null;
        }
        return repositories.get(0); // 默认使用第一个仓库
    }

    /**
     * 检查是否有暂存的文件
     */
    public boolean hasStagedChanges() {
        ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        Collection<Change> stagedChanges = changeListManager.getChangesIn(project.getBaseDir());
        return !stagedChanges.isEmpty();
    }

    /**
     * 获取暂存的文件列表
     */
    public String[] getStagedFiles() {
        ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        Collection<Change> stagedChanges = changeListManager.getChangesIn(project.getBaseDir());
        List<String> stagedFiles = new ArrayList<>();

        for (Change change : stagedChanges) {
            String filePath = getChangePath(change);
            String changeType = getChangeType(change);
            stagedFiles.add(changeType + " " + filePath);
        }

        return stagedFiles.toArray(new String[0]);
    }

    /**
     * 获取暂存文件的差异内容
     */
    public String getDiffContent() {
        ChangeListManager changeListManager = ChangeListManager.getInstance(project);
        Collection<Change> stagedChanges = changeListManager.getChangesIn(project.getBaseDir());
        StringBuilder diffContent = new StringBuilder();

        for (Change change : stagedChanges) {
            String filePath = getChangePath(change);
            String changeType = getChangeType(change);
            diffContent.append("\n文件: ").append(changeType).append(" ").append(filePath).append("\n");

            try {
                String diff = getChangeDiff(change);
                diffContent.append(diff).append("\n");
            } catch (Exception e) {
                LOG.error("获取文件 " + filePath + " 的diff失败", e);
                diffContent.append("无法获取差异内容: ").append(e.getMessage()).append("\n");
            }
        }

        return diffContent.toString();
    }

    /**
     * 获取变更的文件路径
     */
    private String getChangePath(@NotNull Change change) {
        if (change.getAfterRevision() != null) {
            return change.getAfterRevision().getFile().getPath();
        } else if (change.getBeforeRevision() != null) {
            return change.getBeforeRevision().getFile().getPath();
        }
        return "未知文件";
    }

    /**
     * 获取变更类型
     */
    private String getChangeType(@NotNull Change change) {
        switch (change.getType()) {
            case NEW:
                return "A";
            case DELETED:
                return "D";
            case MOVED:
                return "R";
            case MODIFICATION:
            default:
                return "M";
        }
    }

    /**
     * 获取变更的差异内容
     */
    private String getChangeDiff(@NotNull Change change) throws Exception {
        ContentRevision beforeRevision = change.getBeforeRevision();
        ContentRevision afterRevision = change.getAfterRevision();

        if (change.getType() == Change.Type.DELETED) {
            // 处理删除的文件
            if (beforeRevision != null) {
                return "--- 文件已删除 ---\n" + beforeRevision.getContent();
            }
            return "--- 文件已删除 ---";
        } else if (change.getType() == Change.Type.NEW) {
            // 处理新增的文件
            if (afterRevision != null) {
                return "+++ 新增文件 +++\n" + afterRevision.getContent();
            }
            return "+++ 新增文件 +++";
        } else {
            // 处理修改的文件
            String beforeContent = beforeRevision != null ? beforeRevision.getContent() : "";
            String afterContent = afterRevision != null ? afterRevision.getContent() : "";
            return getDiffBetweenContents(beforeContent, afterContent);
        }
    }

    /**
     * 计算两个内容之间的差异
     * 简单实现，实际项目中可以使用更复杂的差异算法
     */
    private String getDiffBetweenContents(String before, String after) {
        // 简单实现，实际项目中可以使用JGit或其他库计算差异
        String[] beforeLines = before.split("\n");
        String[] afterLines = after.split("\n");

        StringBuilder diff = new StringBuilder();
        diff.append("@@ -1,").append(beforeLines.length).append(" +1,").append(afterLines.length).append(" @@\n");

        // 添加删除的行
        for (String line : beforeLines) {
            diff.append("-").append(line).append("\n");
        }

        // 添加新增的行
        for (String line : afterLines) {
            diff.append("+").append(line).append("\n");
        }

        return diff.toString();
    }
}