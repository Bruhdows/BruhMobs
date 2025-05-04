package com.bruhdows.mobs.manager

import com.bruhdows.mobs.BruhMobs
import com.bruhdows.mobs.model.Mob
import com.bruhdows.mobs.model.MobOption
import com.bruhdows.mobs.util.TextUtil
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import java.io.File
import java.util.*
import java.util.logging.Level

class MobManager(private val plugin: BruhMobs) {

    val mobs = mutableMapOf<String, Mob>()
    private val spawnedMobs = mutableMapOf<UUID, Mob>()

    private val defaultMob: Mob = Mob(
        type = EntityType.ZOMBIE,
        displayName = "<green>Goblin",
        health = 20.0,
        damage = 4.0,
        options = mapOf(
            MobOption.PREVENT_DROPS to true,
            MobOption.ALWAYS_SHOW_NAME to false
        )
    )

    private val mapper = ObjectMapper(YAMLFactory())
        .registerKotlinModule()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun loadMobs() {
        mobs.clear()
        val mobsFolder = File(plugin.dataFolder, "mobs")
        if (!mobsFolder.exists()) {
            mobsFolder.mkdirs()
            saveMob(mobsFolder, "goblin", defaultMob)
        }

        mobsFolder.listFiles { file -> file.extension == "yml" }?.forEach { file ->
            try {
                val yamlContent = file.readText()
                if (yamlContent.isBlank()) {
                    plugin.logger.warning("Skipped empty mob file: ${file.name}")
                    return@forEach
                }
                val mobMap: Map<String, Mob> = mapper.readValue(yamlContent)
                for ((name, mob) in mobMap) {
                    mobs[name] = mob
                }
                plugin.logger.info("Loaded mobs from ${file.name}: ${mobMap.keys}")
            } catch (ex: Exception) {
                plugin.logger.log(Level.WARNING, "Failed to load mobs from ${file.name}:", ex)
            }
        }

        plugin.logger.info("Total mobs loaded: ${mobs.size}")
    }

    private fun saveMob(directory: File, mobName: String, mob: Mob) {
        val mobMap = mapOf(mobName to mob)
        val file = File(directory, "$mobName.yml")
        mapper.writeValue(file, mobMap)
    }

    fun getMob(name: String): Mob? = mobs[name]
    fun getMobByUUID(uuid: UUID): Mob? = spawnedMobs[uuid]

    fun unregisterMobInstance(uuid: UUID) {
        spawnedMobs.remove(uuid)
    }

    private fun registerMobInstance(uuid: UUID, mob: Mob) {
        spawnedMobs[uuid] = mob
    }

    fun spawn(mob: Mob, location: org.bukkit.Location): LivingEntity? {
        val world = location.world ?: return null
        val entity = world.spawnEntity(location, mob.type)
        if (entity !is LivingEntity) return null

        entity.customName(TextUtil.color(mob.displayName))
        entity.isCustomNameVisible = mob.options[MobOption.ALWAYS_SHOW_NAME] ?: false

        setAttribute(entity, Attribute.GENERIC_MAX_HEALTH, mob.health)
        entity.health = mob.health
        setAttribute(entity, Attribute.GENERIC_ATTACK_DAMAGE, mob.damage)

        registerMobInstance(entity.uniqueId, mob)

        return entity
    }

    private fun setAttribute(entity: LivingEntity, attribute: Attribute, value: Double) {
        try {
            val attr = entity.getAttribute(attribute)
            attr?.baseValue = value
        } catch (_: Exception) {
        }
    }
}