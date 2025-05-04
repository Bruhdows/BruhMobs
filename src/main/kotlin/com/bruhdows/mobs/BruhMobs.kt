package com.bruhdows.mobs

import com.bruhdows.mobs.command.MobCommand
import com.bruhdows.mobs.listener.MobCombustListener
import com.bruhdows.mobs.listener.MobDeathListener
import com.bruhdows.mobs.manager.MobManager
import org.bukkit.plugin.java.JavaPlugin

class BruhMobs: JavaPlugin() {

    lateinit var mobManager: MobManager

    override fun onEnable() {
        mobManager = MobManager(this)
        mobManager.loadMobs()

        listOf(
            MobDeathListener(this),
            MobCombustListener(this)
        ).forEach { server.pluginManager.registerEvents(it, this) }

        server.commandMap.register("bruhmobs", MobCommand(this))
    }
}