# Goose CLI - by Block

## Installation

```shell
# Install with curl
curl -fsSL https://github.com/block/goose/releases/download/stable/download_cli.sh | bash

# update to latest version
goose update
```

> via [github.com/block/goose](https://github.com/block/goose)


## Get Version

```
% goose --version
```

## Usage

Goose is an open-source AI agent from Block that automates engineering tasks. It can read, write, and run code, helping you with various development tasks.

```
Usage: goose [options] [command]

Goose - an open source, extensible AI agent that goes beyond code suggestions

Commands:
  session    Start an interactive session (default)
  run        Run a single command
  update     Update Goose to the latest version
  configure  Configure Goose settings

Options:
  -v, --version      Display version information
  -h, --help         Display help for command
```

## Features

- Reads and writes files
- Runs shell commands
- Understands project context
- Extensible with plugins
- Works with multiple LLM providers
