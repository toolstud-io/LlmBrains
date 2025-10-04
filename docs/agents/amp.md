# Amp CLI (by SourceGraph)

## Installation

```shell
# Install with npm
npm install -g @sourcegraph/amp

# update to latest version with npm
npm update -g @sourcegraph/amp
```

> via [ampcode.com/manual](https://ampcode.com/manual#getting-started-command-line-interface)


## Get Version

```
% amp --version
0.0.1759608074-g34c1da (released 2025-10-04T20:05:53.134Z)
```

## Usage
```
Amp CLI

Usage: amp [options] [command]

Commands:

  logout       Log out by removing stored API key
  login        Log in to Amp
  threads      [alias: t, thread] Manage threads
    new        [alias: n] Create a new thread
    continue   [alias: c] Continue an existing thread
    fork       [alias: f] Fork an existing thread
    list       [alias: l, ls] List all threads
    share      [alias: s] Share a thread
    compact    [alias: co] Compact a thread
  tools        [alias: tool] Tool management commands
    list       [alias: ls] List all active tools (including MCP tools)
    show       Show details about an active tool
    make       Sets up a skeleton tool in your toolbox
    use        Invoke a tool with arguments or JSON input from stdin
  permissions  [alias: permission] Manage permissions
    list       [alias: ls] List permissions
    test       Test permissions
    edit       Edit permissions
    add        Add permission rule
  mcp          Manage MCP servers
    add        Add an MCP server configuration
  connect      Connect CLI to web interface for multi-thread management
  doctor       Generate support bundle
  update       Update Amp CLI

Options:

  --visibility <visibility>
      Set thread visibility (private, public, workspace, group)
  -V, --version
      output the version number
  --notifications
      Enable sound notifications (enabled by default when not in execute mode)
  --no-notifications
      Disable sound notifications (enabled by default when not in execute mode)
  --settings-file <value>
      Custom settings file path (overrides the default location /Users/pforret/.config/amp/settings.json)
  --log-level <value>
      Set log level (error, warn, info, debug, audit)
  --log-file <value>
      Set log file location (overrides the default location /Users/pforret/.cache/amp/logs/cli.log)
  --dangerously-allow-all
      Disable all command confirmation prompts (agent will execute all commands without asking)
  --jetbrains
      Enable JetBrains integration. When enabled, Amp automatically includes your open JetBrains file and text selection with every message.
  --no-jetbrains
      Disable JetBrains integration.
  --mcp-config <value>
      JSON configuration or file path for MCP servers to merge with existing settings
  -x, --execute [message]
      Use execute mode, optionally with user message. In execute mode, agent will execute provided prompt (either as argument, or via stdin). Only last assistant message is printed. Enabled automatically when redirecting stdout.

Environment variables:

  AMP_API_KEY        API key for Amp (see https://ampcode.com/settings)
  AMP_URL            URL for the Amp service (default is https://ampcode.com/)
  AMP_LOG_LEVEL      Set log level (can also use --log-level)
  AMP_LOG_FILE       Set log file location (can also use --log-file)
  AMP_SETTINGS_FILE  Set settings file path (can also use --settings-file, default: /Users/pforret/.config/amp/settings.json)

Examples:

Start an interactive session:

  $ amp

Start an interactive session with a user message:

  $ echo "commit all my unstaged changes" | amp

Use execute mode (--execute or -x) to send a command to an agent, have it execute it, print only the agent's last message, and then exit:

  $ amp -x "what file in this folder is in markdown format?"
  All Markdown files in this folder:
  - README.md (root)
  - AGENT.md (root)
  - Documentation (7 files in doc/)
  - Various README.md files in subdirectories
  Total: **13 Markdown files** found across the project.

Use execute mode and allow agent to use tools that would require approval:

  $ amp --dangerously-allow-all -x "Rename all .markdown files to .md. Only print list of renamed files."
  - readme.markdown → readme.md
  - ghostty.markdown → ghostty.md

Pipe a command to the agent and use execute mode:

  $ echo "commit all my unstaged changes" | amp -x --dangerously-allow-all
  Done. I have committed all your unstaged changes.

Pipe data to the agent and send along a prompt in execute mode:

  $ cat ~/.zshrc | amp -x "what does the 'beautiful' function do?"
  The `beautiful` function creates an infinite loop that prints the letter "o" in cycling colors every 0.2 seconds.

Execute a prompt from a file and store final assistant message output in a file (redirecting stdout is equivalent to providing -x/--execute):

  $ amp < prompt.txt > output.txt

Add an MCP server with a local command:

  $ amp mcp add context7 -- npx -y @upstash/context7-mcp

Add an MCP server with environment variables:

  $ amp mcp add postgres --env PGUSER=orb -- npx -y @modelcontextprotocol/server-postgres postgresql://localhost/orbing

Add a remote MCP server:

  $ amp mcp add hugging-face https://huggingface.co/mcp

Configuration:

Amp can be configured using a JSON settings file located at /Users/pforret/.config/amp/settings.json. All settings use the "amp." prefix.

Settings reference:

  amp.notifications.enabled
      Enable system sound notifications when agent completes tasks
  amp.notifications.system.enabled
      Enable system notifications when terminal is not focused
  amp.mcpServers
      Model Context Protocol servers to connect to for additional tools
  amp.tools.disable
      Array of tool names to disable. Use 'builtin:toolname' to disable only the builtin tool with that name (allowing an MCP server to provide a tool by that name).
  amp.permissions
      Permission rules for tool calls. See amp permissions --help
  amp.guardedFiles.allowlist
      Array of file glob patterns that are allowed to be accessed without confirmation. Takes precedence over the built-in denylist.
  amp.dangerouslyAllowAll
      Disable all command confirmation prompts (agent will execute all commands without asking)
  amp.git.commit.coauthor.enabled
      Enable adding Amp as co-author in git commits
  amp.git.commit.ampThread.enabled
      Enable adding Amp-Thread trailer in git commits
  amp.proxy
      Proxy URL used for both HTTP and HTTPS requests to the Amp server
  amp.updates.mode
      Control update checking behavior: "warn" shows update notifications, "disabled" turns off checking, "auto" automatically runs update.
                                                                             
```