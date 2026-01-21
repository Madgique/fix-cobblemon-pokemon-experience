package com.madgique.fix_cobblemon_pokemon_experience.fabric

import com.madgique.fix_cobblemon_pokemon_experience.FixCobblemonPokemonExperience
import net.fabricmc.api.ModInitializer

class FixCobblemonPokemonExperienceFabric : ModInitializer {
    override fun onInitialize() {
        FixCobblemonPokemonExperience.init()
    }
}
