# Git Commit Message Generator for JetBrains IDEs

一个强大的Git提交信息生成器，基于AI模型自动分析暂存的代码变更并生成规范的commit message。支持Ollama和OpenAI等多种LLM服务，可自定义提示词模板、温度参数等配置选项，让Git提交更加智能和高效。

## 功能特点

- 基于AI自动分析暂存的代码变更
- 生成符合规范的commit message
- 支持多种LLM服务，包括Ollama、OpenAI等
- 可自定义提示词模板和系统指令
- 支持调整温度、top_p等参数

## 安装

1. 在JetBrains IDE中打开设置（Settings/Preferences）
2. 选择「Plugins」
3. 点击「Marketplace」标签
4. 搜索「Git Commit Message Generator」
5. 点击安装即可

## 使用方法

1. 在Git工具窗口中暂存要提交的文件
2. 在提交对话框中点击「生成 Commit Message」按钮
3. 插件会自动分析暂存的文件变更并生成提交信息
4. 生成的提交信息会自动填充到提交信息编辑框中

## 配置选项

在IDE设置中，可以自定义以下配置：

- `LLM API URL`: LLM API的URL地址
- `LLM Model`: 使用的AI模型
- `API Protocol`: API协议（ollama/openai）
- `API Key`: API密钥
- `Prompt Template`: 生成提交信息的提示词模板
- `System Instruction`: 系统指令
- `Temperature`: 生成结果的随机性（0-1）
- `Top P`: 采样时的累积概率阈值（0-1）
- `Max Tokens`: 生成结果的最大token数量

## 支持的IDE

- IntelliJ IDEA
- PyCharm
- WebStorm
- PhpStorm
- Rider
- CLion
- GoLand
- RubyMine
- AppCode
- DataGrip
- 其他基于IntelliJ平台的IDE

## 开发指南

如果您想要参与项目开发，请按照以下步骤设置开发环境：

1. 确保您的开发环境满足以下要求：
   - IntelliJ IDEA 2023.1 或更高版本
   - JDK 17 或更高版本
   - Gradle 8.0 或更高版本

2. 克隆项目并导入：
   ```bash
   git clone https://github.com/your-username/jetbrains-git-commitx.git
   cd jetbrains-git-commitx
   ```

3. 使用IntelliJ IDEA打开项目：
   - 选择「File」→「Open」
   - 选择项目目录
   - 等待Gradle同步完成

4. 构建项目：
   ```bash
   ./gradlew build
   ```
   或在IDEA中点击Gradle工具窗口的build任务

## 调试方法

1. 配置运行/调试配置：
   - 在IDEA中，选择「Run」→「Edit Configurations」
   - 点击「+」，选择「Gradle」
   - 设置以下参数：
     - Gradle project：选择当前项目
     - Tasks：runIde
     - Arguments：--stacktrace

2. 开始调试：
   - 在代码中设置断点
   - 点击工具栏的调试按钮或按Shift+F9
   - 将会启动一个新的IDE实例，您可以在其中测试插件功能

3. 常见调试技巧：
   - 使用IDEA的「Debug」工具窗口查看变量值
   - 在「Console」窗口查看日志输出
   - 使用「Evaluate Expression」(Alt+F8)执行表达式

## 贡献

欢迎提交问题和功能请求！如果您想贡献代码，请随时提交PR。

## 许可证

[MIT](LICENSE)