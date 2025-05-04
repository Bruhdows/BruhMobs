package com.bruhdows.mobs.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage

object TextUtil {

    private val MINI_MESSAGE = MiniMessage.miniMessage()

    fun color(input: String): Component {
        return MINI_MESSAGE.deserialize(input).decoration(TextDecoration.ITALIC, false)
    }
}
