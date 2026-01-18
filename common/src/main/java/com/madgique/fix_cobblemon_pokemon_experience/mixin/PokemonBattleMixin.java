package com.madgique.fix_cobblemon_pokemon_experience.mixin;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin to prevent experience from being awarded at the end of battle.
 * Experience is instead awarded immediately when each Pokemon faints,
 * handled by the BATTLE_FAINTED event listener.
 */
@Mixin(PokemonBattle.class)
public class PokemonBattleMixin {

    /**
     * Redirects the awardExperience call in the end() method to do nothing.
     * This prevents double XP since we already award XP on each faint.
     */
    @Redirect(
        method = "end",
        at = @At(
            value = "INVOKE",
            target = "Lcom/cobblemon/mod/common/api/battles/model/actor/BattleActor;awardExperience(Lcom/cobblemon/mod/common/battles/pokemon/BattlePokemon;I)V"
        ),
        remap = false
    )
    private void redirectAwardExperience(BattleActor actor, BattlePokemon battlePokemon, int experience) {
        // Do nothing - experience is awarded on faint instead
    }
}
