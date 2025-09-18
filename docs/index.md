# LLM Brains

"LLM Brains" is a JetBrains IDE plugin (for e.g. usage in PhpStorm) to open any (popular) CLI coding agent in a new terminal window.
It's like the Claude Code button but also provides OpenAI Codex and Google's Gemini CLI, and maybe more in the future.

It shows this icon in the top right corner of the IDE: 🫴. Mouseover text: "Open any CLI coding agent in a new terminal window."

when you click on that icon, gives you the following options:

* "Claude (Anthropic)" ⇒ run 'claude' in an IDE terminal window with title '🫴 Claude'
* "Codex (OpenAI)" ⇒ run 'codex' in an IDE terminal window with title '🫴 Codex'
* "Gemini (Google)" ⇒ run 'gemini' in an IDE terminal window with title '🫴 Gemini'
* "Check what's installed" ⇒ run a bash script in an IDE terminal window: for each of the CLI programs above: if it's installed, show the version. If it's not, show how to install it.

