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

在IDE设置中，可以自定义以下配置（通过搜`commit`可以找到配置）：

- `LLM API URL`: LLM API的URL地址
- `LLM Model`: 使用的AI模型
- `API Protocol`: API协议（ollama/openai）
- `API Key`: API密钥
- `Prompt Template`: 生成提交信息的提示词模板
- `System Instruction`: 系统指令
- `Temperature`: 生成结果的随机性（0-1）
- `Top P`: 采样时的累积概率阈值（0-1）
- `Max Tokens`: 生成结果的最大token数量

## 建议
1. 在设置中配置AI服务的API相关信息，开通模型后记下该模型的 apiKey
   - 默认使用阿里云百炼的AI接口，模型为`deepseek-v3`
     - 获取API密钥：[阿里云百炼](https://bailian.console.aliyun.com/?apiKey=1#/api-key)
     - 生成密钥后，可以直接使用各种模型，新用户半年内每种模型免费 100w tokens，可以用的模型有：
       - `deepseek-v3`
       - `deepseek-r1`
       - `qwen2.5-32b-instruct`
       - `deepseek-r1-distill-qwen-32b`
       - `qwen-plus`
       - `deepseek-r1-distill-llama-70b` 这个模型 free，只是用的人太多有点慢
       - `qwen2-7b-instruct`
   - 其次推荐[火山引擎](https://console.volcengine.com/ark/region:ark+cn-beijing/apiKey?apikey=%7B%7D)，截止2025年5月31日，每天每个模型免费 50w tokens
     - 生成api后需要手动开通需要开通的模型
     - 支持的模型较少，只有deepseek系的和doubao系的，比如：
     - `deepseek-r1-250120` 每天50w tokens
     - `deepseek-r1-distill-qwen-32b-250120` 每天50w tokens
     - `deepseek-v3-250324` 每天50w tokens （推荐）
     - `doubao-1-5-pro-256k-250115` 每天50w tokens
   - 支持其他兼容openai接口的大模型服务，比如腾讯元宝、Anthropic、硅基流动、DeepSeek等
   - 本扩展支持本地部署的Ollama，只需要把 protocol 改为 ollama，url 改为`http://localhost:11434/api/generate`即可


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
   - IntelliJ IDEA 2022.3 或更高版本
   - JDK 17
   - Gradle 7.6 或更高版本，不支持8.0+

2. 克隆项目并导入：
   ```bash
   git clone https://github.com/chenkai2/jetbrains-git-commitx.git
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

## Q&A
如果在构建过程中，IDEA 尚未生成 `gradlew` 文件就已经报错，这通常表明问题出现在项目的初始化阶段，而不是 Gradle Wrapper 的执行阶段。以下是可能的原因及解决方法：

---

### 1. **检查项目配置**
在 IDEA 中打开项目时，确保项目的配置正确，尤其是 JDK 和 Gradle 的设置。

#### 方法：
1. 打开 **Project Structure** 设置：
   - 在 IDEA 中，点击菜单栏的 `File > Project Structure`。
   - 确保 **Project SDK** 设置为正确的 JDK（例如 OpenJDK 17）。

2. 检查 Gradle 配置：
   - 打开 `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`。
   - 确保 **Gradle JVM** 设置为与项目匹配的 JDK（例如 OpenJDK 17）。
   - 确保 **Gradle Distribution** 选择`Wrapper`。

---

### 2. **手动下载并使用 Gradle**
如果项目尚未生成 `gradlew` 文件，可以尝试手动安装和使用 Gradle 来完成构建。

#### 步骤：
1. 下载 Gradle：
   - 前往 [Gradle 官方网站](https://gradle.org/releases/) 下载最新版本的 Gradle。
   - 解压到本地目录，例如 `/opt/gradle`。

2. 配置环境变量：
   编辑你的 shell 配置文件（如 `.zshrc` 或 `.bashrc`），添加以下内容：
   ```bash
   export GRADLE_HOME=/opt/gradle
   export PATH=$GRADLE_HOME/bin:$PATH
   ```
   然后重新加载配置文件：
   ```bash
   source ~/.zshrc  # 或 source ~/.bashrc
   ```

3. 使用手动安装的 Gradle 构建项目：
   在项目根目录下运行以下命令：
   ```bash
   gradle wrapper
   gradle build
   ```

---

### 3. **检查项目的 `build.gradle` 文件**
如果 `gradlew` 文件尚未生成，可能是 `build.gradle` 文件中存在错误，导致 Gradle Wrapper 无法正确初始化。

#### 检查点：
1. **插件版本兼容性**：
   确保 `build.gradle` 中使用的插件版本与你的 JDK 和 Gradle 版本兼容。例如：
   ```groovy
   plugins {
       id 'org.jetbrains.intellij' version '1.17.4'
   }
   ```
   如果你使用的是较新的 JDK（如 17），建议升级插件到最新稳定版本。

2. **依赖项冲突**：
   检查 `dependencies` 部分是否有不兼容的依赖项。例如，某些依赖可能需要特定版本的 JDK。

3. **任务定义错误**：
   检查 `build.gradle` 中的任务定义是否有语法错误或逻辑问题。

---

### 4. **清理缓存并重新导入项目**
有时，IDEA 的缓存可能导致问题。可以尝试清理缓存并重新导入项目。

#### 步骤：
1. 清理 IDEA 缓存：
   - 点击菜单栏的 `File > Invalidate Caches / Restart > Invalidate and Restart`。

2. 删除 `.idea` 和 `*.iml` 文件：
   - 关闭 IDEA。
   - 删除项目根目录下的 `.idea` 文件夹和所有 `*.iml` 文件。
   - 重新打开项目并重新导入。

3. 重新同步 Gradle：
   - 点击 IDEA 右上角的 `Reload All Gradle Projects` 按钮。

---

### 5. **检查 JDK 的安装路径**
如果你确认系统中已经安装了 OpenJDK 17，但 IDEA 或 Gradle 仍然找不到它，可能是因为路径配置不正确。

#### 检查点：
1. 确认 JDK 的安装路径：
   ```bash
   /usr/libexec/java_home -v 17
   ```
   输出应类似于 `/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home`。

2. 在 IDEA 中手动指定 JDK 路径：
   - 打开 `File > Project Structure > SDKs`。
   - 添加一个新的 JDK，并指向上述路径。

---

### 6. **尝试使用命令行构建**
在 IDEA 中遇到问题时，可以尝试直接通过命令行构建项目，以排除 IDEA 配置的问题。

#### 步骤：
1. 打开终端，进入项目根目录。
2. 运行以下命令：
   ```bash
   gradle wrapper
   ./gradlew clean build
   ```
   如果命令行构建成功，则问题可能出在 IDEA 的配置上。

---

### 7. **常见问题排查**
- **Gradle 版本过低**：确保 Gradle 版本支持你的 JDK。例如，Gradle 7.x 支持 Java 17。
- **网络问题**：如果依赖项无法下载，可能是网络问题。可以尝试配置代理或使用国内镜像（如阿里云 Maven 仓库）。
- **插件问题**：某些插件可能与高版本 JDK 不兼容。可以尝试降级 JDK 或升级插件。

---

### 总结
按照以下步骤解决问题：
1. 确保 IDEA 的 JDK 和 Gradle 配置正确。
2. 检查 `build.gradle` 文件是否有错误。
3. 清理 IDEA 缓存并重新导入项目。
4. 尝试通过命令行构建项目。
5. 如果问题仍未解决，请提供以下信息以便进一步分析：
   - `build.gradle` 文件的内容。
   - IDEA 的完整错误日志。
   - `gradle -v` 和 `java --version` 的输出。

## 贡献

欢迎提交问题和功能请求！如果您想贡献代码，请随时提交PR。

## 许可证

[MIT](LICENSE)