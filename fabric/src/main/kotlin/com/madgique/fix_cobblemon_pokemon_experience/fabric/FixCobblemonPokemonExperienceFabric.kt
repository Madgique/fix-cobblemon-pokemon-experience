package com.madgique.fix_cobblemon_pokemon_experience.fabric

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import net.fabricmc.api.ModInitializer
import net.minecraft.network.chat.Component

class FixCobblemonPokemonExperienceFabric : ModInitializer {
    override fun onInitialize() {
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
            opponentActor.awardExperience(opponentPokemon, experience)

            // Envoyer le message dans le journal de combat
            val pokemonName = pokemon.getDisplayName()
            val message = Component.translatable("cobblemon.experience.gained", pokemonName, experience)
            battle.broadcastChatMessage(message)
        }
    }
}
