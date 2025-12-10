![GitHub Tag](https://img.shields.io/github/v/tag/toolstud-io/LlmBrains)
![GitHub Release](https://img.shields.io/github/v/release/toolstud-io/LlmBrains)
![GitHub commit activity](https://img.shields.io/github/commit-activity/y/toolstud-io/LlmBrains)
![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/28522)
![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/28522?logo=jetbrains)


# LLM Brains

[![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/28522?style=for-the-badge&logo=jetbrains)](https://plugins.jetbrains.com/plugin/28522-llm-brains)

**LLM Brains** is a JetBrains IDE plugin that adds a toolbar button to launch popular **CLI coding agents** directly in your IDE terminal. Works with all JetBrains IDEs (IntelliJ IDEA, PhpStorm, WebStorm, PyCharm, etc.).

![img.png](dropdown.png)

## Features

- **One-click access** to CLI coding agents from the IDE toolbar (🫴 icon)
- **10 supported agents** with auto-detection of installed tools
- **Check & Update utilities** to manage all agents at once
- **Configurable** - enable/disable agents via Settings > Tools > LLM Brains
- **Cross-platform** - works on macOS, Linux, and Windows

## Supported CLI Agents

| Agent                                                                        | Command    | Provider    | Installation                                                                                 |
|------------------------------------------------------------------------------|------------|-------------|----------------------------------------------------------------------------------------------|
| [Amp CLI](https://ampcode.com/manual#getting-started-command-line-interface) | `amp`      | Sourcegraph | `npm install -g @sourcegraph/amp`                                                            |
| [Claude Code](https://docs.claude.com/en/docs/claude-code/setup)             | `claude`   | Anthropic   | `npm install -g @anthropic-ai/claude-code`                                                   |
| [Codex CLI](https://developers.openai.com/codex/cli/)                        | `codex`    | OpenAI      | `npm install -g @openai/codex`                                                               |
| [Droid CLI](https://factory.ai/product/ide)                                  | `droid`    | Factory AI  | `curl -fsSL https://app.factory.ai/cli \| sh`                                                |
| [Gemini CLI](https://github.com/google-gemini/gemini-cli)                    | `gemini`   | Google      | `npm install -g @google/gemini-cli`                                                          |
| [Goose CLI](https://github.com/block/goose)                                  | `goose`    | Block       | `curl -fsSL https://github.com/block/goose/releases/download/stable/download_cli.sh \| bash` |
| [Grok CLI](https://github.com/superagent-ai/grok-cli)                        | `grok`     | xAI         | `npm install -g @vibe-kit/grok-cli`                                                          |
| [OpenCode](https://opencode.ai/docs)                                         | `opencode` | OpenCode AI | `npm install -g opencode-ai`                                                                 |
| [Qodo](https://qodo.ai/)                                                     | `qodo`     | Qodo        | `npm install -g @qodo/command`                                                               |
| [Warp CLI](https://docs.warp.dev/developers/cli)                             | `warp`     | Warp        | `brew install --cask warp-cli`                                                               |

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

Go to **Settings/Preferences > Tools > LLM Brains** to enable or disable specific agents in the dropdown menu.

## Requirements

- JetBrains IDE 2024.1+ (platform version 251, 252, 253)
- Terminal plugin (bundled with all JetBrains IDEs)

