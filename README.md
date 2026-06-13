
# Scarpet Scripts

These are lightweight [scarpet](https://github.com/gnembon/fabric-carpet) scripts for Minecraft servers running Carpet that add QOL without needing Fabric mods

## Current Scripts

### `ping.sc`

Adds a `/ping` command.

#### Commands

```mcfunction
/ping
```

Shows your current ping in milliseconds.

```mcfunction
/ping <player>
```

Shows another online player's ping.

#### Features

- Uses Carpet/Scarpet's built-in player ping query
- Supports online player autocomplete
- Configurable cooldown
- Available to all players
- No separate mod required beyond Carpet

---

### `seen.sc`

Adds a `/seen <player>` command.

#### Commands

```mcfunction
/seen <player>
```

Shows whether a player is currently online, or when they were last seen.

#### Features

- Tracks player joins and disconnects
- Configurable cooldown
- Saves data using Scarpet app data
- Supports autocomplete from previously seen players
- Case-insensitive lookups
- Marks online players offline when the server shuts down
- Available to all players

#### Data File

The seen data is stored by Scarpet app data, typically under:

```text
world/scripts/seen.data.nbt
```

## Usage

### Ping

```mcfunction
/ping
```

Example output:

```text
Pong! Your ping is 42 ms
```

```mcfunction
/ping dontflex
```

Example output:

```text
dontflex's ping is 42 ms
```

### Seen

```mcfunction
/seen dontflex
```

Example output if online:

```text
dontflex is online right now.
```

Example output if offline:

```text
dontflex was last seen: 2026-06-12 19:45:03 UTC
```

Example output if no data exists:

```text
No seen data for dontflex.
```

## Notes

These scripts are intentionally simple.

The autocomplete behavior uses Scarpet command suggesters so commands suggest real player names instead of generic Brigadier examples like `word` or `words_with_underscores`.

## License

Use, modify, and share freely.
