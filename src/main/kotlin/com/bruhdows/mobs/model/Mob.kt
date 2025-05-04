package com.bruhdows.mobs.model

import org.bukkit.entity.EntityType

/*
TODO:
 - Add skills
 - More customization options
*/
data class Mob(
    var type: EntityType,
    var displayName: String,
    var health: Double,
    var damage: Double,
    var options: Map<MobOption, Boolean>
)