![GitHub Tag](https://img.shields.io/github/v/tag/toolstud-io/LlmBrains)
![GitHub Release](https://img.shields.io/github/v/release/toolstud-io/LlmBrains)
![GitHub commit activity](https://img.shields.io/github/commit-activity/y/toolstud-io/LlmBrains)
![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/28522)
![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/28522?logo=jetbrains)


# LLM Brains

[![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/28522?style=for-the-badge&logo=jetbrains)](https://plugins.jetbrains.com/plugin/28522-llm-brains)

[plugins.jetbrains.com/plugin/28522-llm-brains](https://plugins.jetbrains.com/plugin/28522-llm-brains)

**LLM Brains** is a JetBrains IDE plugin that adds a toolbar button to launch popular **CLI coding agents** directly in their own IDE terminal window. Works with all JetBrains IDEs (IntelliJ IDEA, PhpStorm, WebStorm, PyCharm, etc.).

![img.png](dropdown.png)

## Features

- **One-click access** to CLI coding agents from the IDE toolbar (🫴 icon)
- **14 built-in agents** with auto-detection of installed tools
- **Custom agent support** - add your own CLI tool with configurable name, command, and URL
- **Check & Update utilities** to manage all agents at once
- **Configurable** - enable/disable agents via Settings > Tools > LLM Brains
- **Cross-platform** - works on macOS, Linux, and Windows

## Supported CLI Agents

| Agent                                                                         | Command    | Provider    | Installation                                              |
|-------------------------------------------------------------------------------|------------|-------------|-----------------------------------------------------------|
| [Amp CLI](https://ampcode.com/manual#getting-started-command-line-interface)  | `amp`      | Sourcegraph | `npm install -g @sourcegraph/amp`                         |
| [Claude Code](https://docs.claude.com/en/docs/claude-code/setup)              | `claude`   | Anthropic   | `npm install -g @anthropic-ai/claude-code`                |
| [Codex CLI](https://developers.openai.com/codex/cli/)                         | `codex`    | OpenAI      | `npm install -g @openai/codex`                            |
| [Copilot CLI](https://github.com/features/copilot/cli)                        | `copilot`  | GitHub      | `npm install -g @github/copilot`                          |
| [Crush CLI](https://github.com/charmbracelet/crush/)                          | `crush`    | Charm       | `npm install -g @charmland/crush`                         |
| [Droid CLI](https://factory.ai/product/ide)                                   | `droid`    | Factory AI  | `curl -fsSL https://app.factory.ai/cli \| sh`             |
| [Gemini CLI](https://github.com/google-gemini/gemini-cli)                     | `gemini`   | Google      | `npm install -g @google/gemini-cli`                       |
| [Goose CLI](https://github.com/block/goose)                                   | `goose`    | Block       | `curl -fsSL https://.../download_cli.sh \| bash`          |
| [Grok CLI](https://github.com/superagent-ai/grok-cli)                         | `grok`     | xAI         | `npm install -g @vibe-kit/grok-cli`                       |
| [OpenCode](https://opencode.ai/docs)                                          | `opencode` | OpenCode AI | `npm install -g opencode-ai`                              |
| [Qodo](https://qodo.ai/)                                                      | `qodo`     | Qodo        | `npm install -g @qodo/command`                            |
| [Qwen Code](https://qwenlm.github.io/qwen-code-docs/en/)                      | `qwen`     | Alibaba     | `npm install -g @qwen-code/qwen-code@latest`              |
| [VT Code](https://github.com/vinhnx/vtcode)                                   | `vtcode`   | vinhnx      | `npm install -g @vinhnx/vtcode`                           |
| [Warp CLI](https://docs.warp.dev/developers/cli)                              | `warp`     | Warp        | `brew install --cask warp-cli`                            |

## Custom Agent

In addition to the built-in agents, you can configure your own custom CLI agent:

1. Go to **Settings/Preferences > Tools > LLM Brains**
2. Enable the **Custom Agent** checkbox
3. Configure:
   - **Name**: Display name shown in the dropdown (e.g., "My Agent")
   - **Command**: The CLI command to execute (e.g., `myagent`)
   - **URL**: Documentation URL for reference

Your custom agent will appear in the dropdown menu alongside the built-in agents.

## Usage

Click the 🫴 icon in the top right corner of the IDE to access:

- **Agent actions** - Launch any enabled agent in a new terminal tab
- **Check what's installed** - Shows version info for all agents (or install hints for missing ones)
- **Update all agents** - Updates all enabled agents to their latest versions

## Installation

1. Open your JetBrains IDE
2. Go to **Settings/Preferences > Plugins > Marketplace**
3. Search for "LLM Brains"
4. Click **Install** and restart the IDE

Or install from the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/28522-llm-brains).

## Configuration

Go to **Settings/Preferences > Tools > LLM Brains** to:

- Enable or disable specific built-in agents in the dropdown menu
- Configure a custom agent with your own CLI tool

## Requirements

- JetBrains IDE 2024.1+ (platform version 251, 252, 253)
- Terminal plugin (bundled with all JetBrains IDEs)

