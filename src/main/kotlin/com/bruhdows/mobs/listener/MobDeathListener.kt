package com.bruhdows.mobs.listener

import com.bruhdows.mobs.BruhMobs
import com.bruhdows.mobs.model.MobOption
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class MobDeathListener(private val plugin: BruhMobs) : Listener {

    @EventHandler
    fun onMobDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val mob = plugin.mobManager.getMobByUUID(entity.uniqueId) ?: return

        if (mob.options.getOrDefault(MobOption.PREVENT_DROPS,false)) {
            event.drops.clear()
        }

        plugin.mobManager.unregisterMobInstance(entity.uniqueId)
    }
}