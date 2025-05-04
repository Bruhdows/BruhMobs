package com.bruhdows.mobs.command

import com.bruhdows.mobs.BruhMobs
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class MobCommand(private val plugin: BruhMobs) : BukkitCommand("bruhmobs") {

    init {
        aliases = arrayListOf("br", "mobs", "mob")
        permission = "bruhmobs.command"
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendRichMessage("<red>Only players can use this command.")
            return false
        }

        if (args.isEmpty()) {
            sender.sendRichMessage(
                "<red>Usage:<br>" +
                        "<red>- <white>/$commandLabel spawn <name><br>" +
                        "<red>- <white>/$commandLabel reload"
            )
            return false
        }

        when (args[0]) {
            "spawn" -> {
                if (args.size != 2) {
                    sender.sendRichMessage("<red>Usage: <white>/mob spawn <name>")
                    return false
                }

                val mob = plugin.mobManager.getMob(args[1])

                if (mob == null) {
                    sender.sendRichMessage("<red>Mob not found.")
                    return false
                }

                plugin.mobManager.spawn(mob, sender.location)
            }

            "reload" -> {
                plugin.mobManager.loadMobs()

                sender.sendRichMessage("<green>Reloaded.")
            }
        }

        return true
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()

        if (args.size == 1) {
            val subs = listOf("spawn", "reload")
            completions.addAll(subs.filter { it.startsWith(args[0], ignoreCase = true) })
        } else if (args.size == 2 && args[0].equals("spawn", ignoreCase = true)) {
            completions.addAll(
                plugin.mobManager.mobs.keys
                    .filter { it.startsWith(args[1], ignoreCase = true) }
            )
        }

        return completions
    }

}