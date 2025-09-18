# JetBrains mkdox button

JetBrains plugin (for e.g. usage in PhpStorm) where there's a specific [pforret/mkdox](https://github.com/pforret/mkdox) functionality button in the top right corner of the editor, right next to the Claude Code button.

* if there is no `/docs` folder OR no `/mkdocs.yml` file
    - the icon is inactive (light gray)
    - when the mouse moves over the icon, the only option is 'Create mkdox' 
    - if Docker is running, 
        - this executes `mkdox -E "$(basename $root_folder)" new .` and 
        - opens `/.mkdocs.yml` file in editor
    - otherwise, it just warns that Docker should be running
* if there is a `/docs` folder in the project AND there is a `/mkdocs.yml` file in the root AND there is a `mkdox.sh` script in the path
    - then the icon become active (bright light blue color)
    - when the mouse pointer moves over it, there is an option 'Serve mkdox' => this executes `mkdox serve` in the root folder
    - if Docker is running,
        - this executes `mkdox serve` in a new terminal window
    - otherwise, it just warns that Docker should be running
