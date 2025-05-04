package com.bruhdows.mobs.listener

import com.bruhdows.mobs.BruhMobs
import com.bruhdows.mobs.model.MobOption
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityCombustEvent

class MobCombustListener(private val plugin: BruhMobs) : Listener {

    @EventHandler
    fun onCombust(event: EntityCombustEvent) {
        val entity = event.entity
        val mob = plugin.mobManager.getMobByUUID(entity.uniqueId) ?: return

        if (mob.options.getOrDefault(MobOption.PREVENT_SUNBURN,false)) {
            event.isCancelled = true
            entity.fireTicks = 0
        }
    }
}