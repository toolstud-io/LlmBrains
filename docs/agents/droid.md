# Droid CLI - by Factory.ai

## Installation

```shell
# Install with npm
curl -fsSL https://app.factory.ai/cli | sh
```

> via [factory.ai/product/ide](https://factory.ai/product/ide)


## Get Version

```
% droid --version
0.18.2
```

## Usage
```
Usage: droid [options] [command]

Droid - Factory's AI coding agent in your terminal

Options:
  -v, --version            output the version number
  -h, --help               display help for command

Commands:
  exec [options] [prompt]  Execute a single command (non-interactive mode)

Running 'droid' without any options starts interactive mode.

Examples:
  droid                               Start interactive mode (default)
  droid exec "analyze this file"      Run non-interactively (for scripts/automation)
  droid exec - < prompt.txt           Execute from stdin (non-interactive)
  droid exec --help                   Show exec command options

For more details, see: https://docs.factory.ai/factory-cli/getting-started/overview
```