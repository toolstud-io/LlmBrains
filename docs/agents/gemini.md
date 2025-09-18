# Gemini CLI - by Google

## Installation

```shell
# Install with npm
npm install -g @google/gemini-cli

# Install with Homebrew
brew install gemini-cli
```

> via [github.com/google-gemini/gemini-cli](https://github.com/google-gemini/gemini-cli)


## Get Version

```
% gemini -v
0.4.1
```

## Usage
```
Usage: gemini [options] [command]

Gemini CLI - Launch an interactive CLI, use -p/--prompt for non-interactive mode

Commands:
  gemini [promptWords...]      Launch Gemini CLI                                                                                                                                                                                                  [default]
  gemini mcp                   Manage MCP servers
  gemini extensions <command>  Manage Gemini CLI extensions.

Options:
  -m, --model                     Model                                                                                                                                                                                                            [string]
  -p, --prompt                    Prompt. Appended to input on stdin (if any).                                                                     [deprecated: Use the positional prompt instead. This flag will be removed in a future version.] [string]
  -i, --prompt-interactive        Execute the provided prompt and continue in interactive mode                                                                                                                                                     [string]
  -s, --sandbox                   Run in sandbox?                                                                                                                                                                                                 [boolean]
      --sandbox-image             Sandbox image URI.                                                                                                       [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string]
  -d, --debug                     Run in debug mode?                                                                                                                                                                             [boolean] [default: false]
  -a, --all-files                 Include ALL files in context?                                                          [deprecated: Use @ includes in the application instead. This flag will be removed in a future version.] [boolean] [default: false]
      --show-memory-usage         Show memory usage in status bar                                                                        [deprecated: Use settings.json instead. This flag will be removed in a future version.] [boolean] [default: false]
  -y, --yolo                      Automatically accept all actions (aka YOLO mode, see https://www.youtube.com/watch?v=xvFZjo5PgG0 for more details)?                                                                            [boolean] [default: false]
      --approval-mode             Set the approval mode: default (prompt for approval), auto_edit (auto-approve edit tools), yolo (auto-approve all tools)                                               [string] [choices: "default", "auto_edit", "yolo"]
      --telemetry                 Enable telemetry? This flag specifically controls if telemetry is sent. Other --telemetry-* flags set specific values but do not enable telemetry on their own.
                                                                                                                                                          [deprecated: Use settings.json instead. This flag will be removed in a future version.] [boolean]
      --telemetry-target          Set the telemetry target (local or gcp). Overrides settings files.                             [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string] [choices: "local", "gcp"]
      --telemetry-otlp-endpoint   Set the OTLP endpoint for telemetry. Overrides environment variables and settings files.                                 [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string]
      --telemetry-otlp-protocol   Set the OTLP protocol for telemetry (grpc or http). Overrides settings files.                  [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string] [choices: "grpc", "http"]
      --telemetry-log-prompts     Enable or disable logging of user prompts for telemetry. Overrides settings files.                                      [deprecated: Use settings.json instead. This flag will be removed in a future version.] [boolean]
      --telemetry-outfile         Redirect all telemetry output to the specified file.                                                                     [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string]
  -c, --checkpointing             Enables checkpointing of file edits                                                                    [deprecated: Use settings.json instead. This flag will be removed in a future version.] [boolean] [default: false]
      --experimental-acp          Starts the agent in ACP mode                                                                                                                                                                                    [boolean]
      --allowed-mcp-server-names  Allowed MCP server names                                                                                                                                                                                          [array]
      --allowed-tools             Tools that are allowed to run without confirmation                                                                                                                                                                [array]
  -e, --extensions                A list of extensions to use. If not provided, all extensions are used.                                                                                                                                            [array]
  -l, --list-extensions           List all available extensions and exit.                                                                                                                                                                         [boolean]
      --proxy                     Proxy for gemini client, like schema://user:password@host:port                                                           [deprecated: Use settings.json instead. This flag will be removed in a future version.] [string]
      --include-directories       Additional directories to include in the workspace (comma-separated or multiple --include-directories)                                                                                                            [array]
      --screen-reader             Enable screen reader mode for accessibility.                                                                                                                                                   [boolean] [default: false]
      --session-summary           File to write session summary to.                                                                                                                                                                                [string]
  -v, --version                   Show version number                                                                                                                                                                                             [boolean]
  -h, --help                      Show help                                                                                                                                                                                                       [boolean]
```