# Fix Cobblemon Pokemon Experience

A Fabric/NeoForge mod that fixes the experience gain system in Cobblemon to work like the mainline Pokemon games.

## The Problem

In Cobblemon, experience points (XP) are only awarded at the **end of a battle**. This works fine for wild Pokemon encounters (1v1), but becomes problematic when using trainer mods like [Radical Cobblemon Trainers (RCT)](https://modrinth.com/mod/radical-cobblemon-trainers).

In trainer battles with multiple Pokemon, you only receive XP after defeating **all** of the trainer's Pokemon, instead of after each knockout.

## The Fix

This mod changes the XP system to award experience **immediately after each Pokemon is knocked out**, just like in the official Pokemon games.

**Features:**
- XP is awarded instantly when you KO a Pokemon
- Works with Exp Share items (same behavior as vanilla Cobblemon)
- Compatible with trainer mods like RCT
- No configuration needed

## How It Works

1. Listens to Cobblemon's `BATTLE_FAINTED` event
2. Awards XP to your Pokemon immediately when an opponent faints
3. Blocks the default XP distribution at the end of battle (to prevent double XP)

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) or [NeoForge](https://neoforged.net/) for Minecraft 1.21.1
2. Install [Cobblemon](https://cobblemon.com/) 1.7.0+
3. Download the mod JAR and place it in your `mods` folder

## Compatibility

- Minecraft: 1.21.1
- Cobblemon: 1.7.0+
- Fabric Loader: 0.18.4+ / NeoForge: 21.1.172+
- Works with: Radical Cobblemon Trainers (RCT) and other trainer mods

## License

MIT License - See [LICENSE](LICENSE) file.
