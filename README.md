# Fix Cobblemon Pokemon Experience

A Fabric/NeoForge mod that fixes the experience gain system in Cobblemon to work like the mainline Pokemon games.

## Downloads

[![CurseForge](https://cf.way2muchnoise.eu/full_1435842_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/fix-cobblemon-pokemon-experience/files/all?page=1&pageSize=20&showAlphaFiles=show)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/UsGc71LY?style=for-the-badge&logo=modrinth&logoColor=white&labelColor=1AAB89)](https://modrinth.com/mod/UsGc71LY/versions)

## The Problem

In Cobblemon, experience points (XP) are only awarded at the **end of a battle**. This works fine for wild Pokemon encounters (1v1), but becomes problematic when using trainer mods like [Radical Cobblemon Trainers (RCT)](https://modrinth.com/mod/rctmod).

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

## FAQ / Known Limitations

### My Pokemon leveled up during battle but the stats in combat didn't change?

**This is a known limitation.** When your Pokemon levels up mid-battle:
- A level up message is displayed in the battle chat
- The Pokemon's data is updated on the server (level, stats, etc.)
- The party overlay and summary screen show the correct new level

**However**, the battle stats (HP, Attack, Defense, etc.) used for damage calculations remain those from the start of the battle. This is because Cobblemon uses [Pokemon Showdown](https://pokemonshowdown.com/) as its battle simulator, and Showdown calculates stats once at the beginning of the battle with no built-in way to update them mid-combat.

**Workaround:** If you need the new stats to take effect during the battle, you can switch out your Pokemon and switch it back in. The stats will be recalculated when it re-enters battle.

This behavior is consistent with how some official Pokemon games handle mid-battle level ups (stats update after the battle or upon switching).

## License

LGPLv3 License - See [LICENSE](LICENSE) file.
