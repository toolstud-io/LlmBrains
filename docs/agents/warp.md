# Warp CLI

## Installation

```shell
# Install on macOS
$ brew tap warpdotdev/warp
$ brew update
$ brew install --cask warp-cli
```

> via [docs.warp.dev/developers/cli](https://docs.warp.dev/developers/cli)


## Get Version

```
% warp dump-debug-info
Warp version: Some("v0.2025.09.17.08.11.stable_02")
```

## Usage
```
Warp, the Agentic Development Environment.

The Warp CLI is a tool for programming agents. You can use it to:
* Create autonomous, asynchronous agents that respond to event triggers,
  run on schedules, or respond to queries in third-party services.
* Run one-off agents on remote machines.
* Check Warp configuration in order to set up new agents.

Usage: warp [OPTIONS]
       warp <COMMAND>

Commands:
  agent                               Interact with Warp's agent
  mcp                                 Manage MCP servers
  login                               Log in to Warp
  dump-debug-info, --dump-debug-info  Print debugging information and exit
  help                                Print this message or the help of the given subcommand(s)

Options:
      --api-key <API_KEY>  API key for server authentication (available to all subcommands)
  -h, --help               Print help

Examples:

  $ warp agent run --prompt "Warp anything"

  $ warp mcp list

Learn more:
* Use warp help to learn more about each command
* Read the documentation at https://docs.warp.dev/developers/cli
```