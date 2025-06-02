package com.zephbyte.streamertag

import com.mojang.brigadier.Command
import eu.pb4.placeholders.api.PlaceholderResult
import eu.pb4.placeholders.api.Placeholders
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory
import java.util.UUID

class StreamerTag : ModInitializer {

    companion object {
        const val MOD_ID = "streamertag"
        val LOGGER = LoggerFactory.getLogger(MOD_ID)

        // This set will store the UUIDs of players who have streaming mode enabled.
        private val streamingPlayers = mutableSetOf<UUID>()

        // Pre-construct the text for the streaming tag for efficiency.
        // This creates: <dark_gray>[</dark_gray><red>⦿</red><dark_gray>]
        val STREAMING_TAG_TEXT: Text = Text.empty()
            .append(Text.literal("⦿").formatted(Formatting.RED)) // The "recording" symbol

        /**
         * Checks if a player has streaming mode enabled.
         * @param player The player to check.
         * @return True if streaming mode is enabled, false otherwise.
         */
        fun isPlayerStreaming(player: ServerPlayerEntity): Boolean {
            return streamingPlayers.contains(player.uuid)
        }

        /**
         * Sets the streaming mode for a player.
         * @param player The player whose streaming mode is to be set.
         * @param streaming True to enable streaming mode, false to disable.
         */
        fun setPlayerStreaming(player: ServerPlayerEntity, streaming: Boolean) {
            if (streaming) {
                streamingPlayers.add(player.uuid)
            } else {
                streamingPlayers.remove(player.uuid)
            }
        }
    }

    override fun onInitialize() {
        LOGGER.info("Initializing StreamerTag Mod...")

        // Register the placeholder: %streamertag:status%
        Placeholders.register(Identifier.of(MOD_ID, "status")) { ctx, _ ->
            // ctx.player can be null if the placeholder is used in a non-player context
            val player = ctx.player
            if (player != null && isPlayerStreaming(player)) {
                PlaceholderResult.value(STREAMING_TAG_TEXT)
            } else {
                // If not streaming or no player context, display nothing
                PlaceholderResult.value(Text.empty()) // or PlaceholderResult.value("")
            }
        }

        // Register the /streaming command
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            dispatcher.register(
                literal("streaming")
                    // Ensures the command can only be run by a player
                    .requires { source -> source.player != null }
                    .then(
                        literal("on")
                            .executes { context ->
                                val player = context.source.playerOrThrow
                                setPlayerStreaming(player, true)
                                context.source.sendFeedback(
                                    { Text.literal("Streaming mode ").append(Text.literal("ON").formatted(Formatting.GREEN)).append(". Your tag is now visible.") },
                                    false // false means it doesn't broadcast to OPs
                                )
                                Command.SINGLE_SUCCESS
                            }
                    )
                    .then(
                        literal("off")
                            .executes { context ->
                                val player = context.source.playerOrThrow
                                setPlayerStreaming(player, false)
                                context.source.sendFeedback(
                                    { Text.literal("Streaming mode ").append(Text.literal("OFF").formatted(Formatting.RED)).append(". Your tag is now hidden.") },
                                    false
                                )
                                Command.SINGLE_SUCCESS
                            }
                    )
            )
        }

        LOGGER.info("StreamerTag Mod Initialized!")
        LOGGER.info("Use '/streaming on' or '/streaming off' to toggle your streaming status.")
        LOGGER.info("Use placeholder '%${MOD_ID}:status%' to display the tag (e.g., in chat).")
    }
}