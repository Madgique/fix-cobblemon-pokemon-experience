package com.madgique.fix_cobblemon_pokemon_experience

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.api.pokemon.experience.BattleExperienceSource
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import com.cobblemon.mod.common.util.getPlayer
import net.minecraft.network.chat.Component

object FixCobblemonPokemonExperience {
    const val MOD_ID = "fix_cobblemon_pokemon_experience"

    fun init() {
        registerBattleFaintedListener()
    }

    private fun registerBattleFaintedListener() {
        CobblemonEvents.BATTLE_FAINTED.subscribe { event ->
            val battle = event.battle
            val killedPokemon = event.killed

            // Trouver l'acteur du pokémon vaincu
            val killedActor = battle.actors.find { actor ->
                actor.pokemonList.contains(killedPokemon)
            } ?: return@subscribe

            // Trouver les acteurs adverses (ceux du côté opposé)
            val opposingSide = killedActor.getSide().getOppositeSide()

            // Pour chaque acteur adverse qui est un joueur (pas les PNJ/sauvages)
            opposingSide.actors.filter { it.type == ActorType.PLAYER }.forEach { opponentActor ->
                // Pour chaque pokémon de l'adversaire qui est encore en vie
                opponentActor.pokemonList.filter { it.health > 0 }.forEach { opponentPokemon ->
                    awardExperienceForKO(battle, opponentActor, opponentPokemon, killedPokemon)
                }
            }
        }
    }

    private fun awardExperienceForKO(
        battle: PokemonBattle,
        opponentActor: BattleActor,
        opponentPokemon: BattlePokemon,
        killedPokemon: BattlePokemon
    ) {
        val pokemon = opponentPokemon.effectedPokemon
        val facedKilled = opponentPokemon.facedOpponents.contains(killedPokemon)

        // Vérifier si le pokémon a un Exp Share
        val hasExpShare = pokemon.heldItem().item == CobblemonItems.EXP_SHARE

        // Calculer le multiplicateur (comme dans PokemonBattle.end())
        val multiplier = when {
            !facedKilled && hasExpShare -> Cobblemon.config.experienceShareMultiplier
            facedKilled -> 1.0
            else -> return // Ne pas donner d'XP si pas affronté et pas d'Exp Share
        }

        // Calculer l'expérience
        val experience = Cobblemon.experienceCalculator.calculate(opponentPokemon, killedPokemon, multiplier)

        // Donner l'XP si > 0
        if (experience > 0) {
            val oldLevel = pokemon.level

            // Donner l'XP avec gestion du level up
            if (opponentActor is PlayerBattleActor) {
                val player = opponentActor.uuid.getPlayer()
                if (player != null) {
                    val source = BattleExperienceSource(battle, opponentPokemon.facedOpponents.toList())
                    pokemon.addExperienceWithPlayer(player, source, experience)
                } else {
                    opponentActor.awardExperience(opponentPokemon, experience)
                }
            } else {
                opponentActor.awardExperience(opponentPokemon, experience)
            }

            val newLevel = pokemon.level

            // Envoyer le message d'XP dans le journal de combat
            val pokemonName = pokemon.getDisplayName()
            val message = Component.translatable("cobblemon.experience.gained", pokemonName, experience)
            battle.broadcastChatMessage(message)

            // Si le pokémon a level up, envoyer un message et mettre à jour les données
            if (newLevel > oldLevel) {
                val levelUpMessage = Component.translatable("cobblemon.experience.level_up", pokemonName, newLevel)
                battle.broadcastChatMessage(levelUpMessage)

                // Mettre à jour les données du pokémon côté client
                opponentPokemon.sendUpdate()
            }
        }
    }
}
