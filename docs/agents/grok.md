# Grok CLI - by xAI

## Installation

```shell
# Install with npm
npm install -g @vibe-kit/grok-cli
# update to latest version with npm
npm update -g @vibe-kit/grok-cli
```

> via [github.com/superagent-ai/grok-cli](https://github.com/superagent-ai/grok-cli)


## Get Version

```
% grok -V
1.0.1
```

## Usage
```
Usage: grok [options] [command] [message...]

A conversational AI CLI tool powered by Grok with text editor capabilities

Arguments:
  message                     Initial message to send to Grok

Options:
  -V, --version               output the version number
  -d, --directory <dir>       set working directory (default: "/Users/pforret/Code/pforret/LlmBrains")
  -k, --api-key <key>         Grok API key (or set GROK_API_KEY env var)
  -u, --base-url <url>        Grok API base URL (or set GROK_BASE_URL env var)
  -m, --model <model>         AI model to use (e.g., grok-code-fast-1, grok-4-latest) (or set GROK_MODEL env var)
  -p, --prompt <prompt>       process a single prompt and exit (headless mode)
  --max-tool-rounds <rounds>  maximum number of tool execution rounds (default: 400) (default: "400")
  -h, --help                  display help for command

Commands:
  git                         Git operations with AI assistance
  mcp                         Manage MCP (Model Context Protocol) servers
```