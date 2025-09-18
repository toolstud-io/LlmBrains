---
title: Start of project
date: 2025-09-18
categories: 
    - codex
---

Just an idea I had, inspired by the Claude Code button in JetBrains IDEs

![Claude Code icon in JB](../img/cc_icon.png):

What if there was also a button for [pforret/mkdox](https://github.com/pforret/mkdox), my wrapper for easily creating and serving `mkdocs` documentation sites (using Docker)?
A button that would appear in the top right corner of the editor, next to the Claude Code button.

It would allow to easily run

- `mkdox new .` (if no /docs folder or no mkdocs.yml file is found)
- `mkdox serve` (if /docs folder and mkdocs.yml file are found)
- `mkdox post`  (if /docs folder, /docs/blog and mkdocs.yml file are found)