# Fuzzy

![Frame 27dark](https://github.com/darmiel/fuzzy-mc/assets/71837281/fde804da-b9b8-4003-aa89-fb9bd00af111)

The [fzf](https://github.com/junegunn/fzf) inspired command introduces a "fuzzy search"-based command search and execution system. It allows you to search previously entered commands, regardless of the exact phrasing, and quickly reuse them.

## Usage

1. Press the `J` key (default keybind, configurable) to open the Fuzzy Command Search screen.
2. Start typing your search query to filter through previously used commands.
3. Commands matching your search will be displayed in a list, which you can navigate using arrow keys, or execute directly with the `Enter` key.
4. The `Tab` key will suggest the selected command, allowing you to edit it further in the chat bar.

## Demo

![java_tsRFGfsO3Y](https://github.com/darmiel/fuzzy-mc/assets/71837281/7bdcf837-7aaf-43da-a4cf-744452547c05)

## KeyBinds

- Press `J` to open the fuzzy command finder (this keybinding can be re-configured)

## Features

### Quick Actions

When you select an entry you can do one of the following quick actions:

- Execute Command: `Enter`
- Suggest Command in Chat: `Tab`
- Next Entry in List: `Arrow Down` or `Shift + J`
- Previous Entry in List: `Arrow Up` or `Shift + K`
- Remove Entry: `Shift` + `X`
- Copy Command to Clipboard: `Shift` + `C`
- Give Command Block with Suggested Command: `Shift + B`

### Command Blocks

When you interact with a command block and change its command, if you have the`Enable Command Block Logging` option enabled, Fuzzy will automatically log that command into its search history. This means you'll be able to pull up the Command Search later on and reuse the command you entered into the command block, even after closing the command block's interface.

Command Block commands appear in yellow and they have a `@` prefix in the command search.

**TIP:** Use command block logging in combination with `Shift + B` to quickly swap between command blocks:
https://github.com/darmiel/fuzzy-mc/assets/71837281/e6fe10b2-d4d3-473e-88a1-c7f2c10b8897


## Credits

Fuzzy uses [xdrop/fuzzywuzzy](https://github.com/xdrop/fuzzywuzzy) for the fuzzy search.
