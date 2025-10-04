# OpenCode CLI

## Installation

```shell
# Install with npm
npm i -g opencode-ai
# update to latest version with npm
npm update -g opencode-ai
```

> via [opencode.ai/docs](https://opencode.ai/docs)


## Get Version

```
% opencode --version
0.14.1
```

## Usage
```
█▀▀█ █▀▀█ █▀▀ █▀▀▄ █▀▀ █▀▀█ █▀▀▄ █▀▀
█░░█ █░░█ █▀▀ █░░█ █░░ █░░█ █░░█ █▀▀
▀▀▀▀ █▀▀▀ ▀▀▀ ▀  ▀ ▀▀▀ ▀▀▀▀ ▀▀▀  ▀▀▀

Commands:
  opencode [project]           start opencode tui                      [default]
  opencode attach <server>     attach to a running opencode server
  opencode run [message..]     run opencode with a message
  opencode auth                manage credentials
  opencode agent               manage agents
  opencode upgrade [target]    upgrade opencode to the latest or a specific
                               version
  opencode serve               starts a headless opencode server
  opencode models              list all available models
  opencode export [sessionID]  export session data as JSON
  opencode github              manage GitHub agent

Positionals:
  project  path to start opencode in                                    [string]

Options:
      --help        show help                                          [boolean]
  -v, --version     show version number                                [boolean]
      --print-logs  print logs to stderr                               [boolean]
      --log-level   log level
                            [string] [choices: "DEBUG", "INFO", "WARN", "ERROR"]
  -m, --model       model to use in the format of provider/model        [string]
  -c, --continue    continue the last session                          [boolean]
  -s, --session     session id to continue                              [string]
  -p, --prompt      prompt to use                                       [string]
      --agent       agent to use                                        [string]
      --port        port to listen on                      [number] [default: 0]
  -h, --hostname    hostname to listen on        [string] [default: "127.0.0.1"]
```