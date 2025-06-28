package com.zephbyte.streamertag

import eu.pb4.placeholders.api.PlaceholderResult
import eu.pb4.placeholders.api.Placeholders
import eu.pb4.placeholders.api.TextParserUtils
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object PlaceholderHandler {
    fun registerPlaceholder() {

        Placeholders.register(Identifier.of(MOD_ID, "status")) { context, _ ->
            val player = context.player
            if (player != null && StreamerTagNbt.getStreamerTagEnabled(player)) {
                // TODO ZEPH: This is deprecated, will need to come back and update later
                val parsed = TextParserUtils.formatText(ConfigManager.streamerTagString)
                PlaceholderResult.value(parsed)
            } else {
                // If not streaming or no player context, display nothing
                PlaceholderResult.value(Text.empty())
            }
        }
    }
}