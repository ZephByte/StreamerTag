package com.zephbyte.streamertag

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object ModCommands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            registerReloadCommand(dispatcher)
            registerStreamerTagCommand(dispatcher)
        }
    }

    private fun registerReloadCommand(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            CommandManager.literal("streamertag")
                .then(CommandManager.literal("reload")
                    .executes { context ->
                        ConfigManager.loadConfig()
                        context.source.sendFeedback(
                            { Text.literal("Streamer Tag config reloaded") },
                            false
                        )
                        1 // Return 1 to indicate success
                    }
                )
        )
    }

    private fun registerStreamerTagCommand(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            CommandManager.literal("streaming")
                .then(
                    CommandManager.literal("on")
                        .executes { context ->
                            val player = context.source.playerOrThrow
                            StreamerTagNbt.setStreamerTagEnabled(player, true)
                            context.source.sendFeedback(
                                { Text.literal("Streamer Tag Enabled") },
                                false
                            )
                            1 // Return 1 to indicate success
                        }
                )
                .then(
                    CommandManager.literal("off")
                        .executes { context ->
                            val player = context.source.playerOrThrow
                            StreamerTagNbt.setStreamerTagEnabled(player, false)
                            context.source.sendFeedback(
                                { Text.literal("Streamer Tag Disabled") },
                                false
                            )
                            1 // Return 1 to indicate success
                        }
                )
                // Handles /streamertag <player> {on:off} (for another player)
                // Should be staff-only command
                .then(
                    CommandManager.argument("target", EntityArgumentType.player())
                        .then(
                            CommandManager.literal("on")
                                .executes { context ->
                                    val target = EntityArgumentType.getPlayer(context, "target")
                                    StreamerTagNbt.setStreamerTagEnabled(target, true)
                                    context.source.sendFeedback(
                                        { Text.literal("Streamer Tag Enabled for ${target.name.string}") },
                                        false
                                    )
                                    1 // Return 1 to indicate success
                                }
                        )
                        .then(
                            CommandManager.literal("off")
                                .executes { context ->
                                    val target = EntityArgumentType.getPlayer(context, "target")
                                    StreamerTagNbt.setStreamerTagEnabled(target, false)
                                    context.source.sendFeedback(
                                        { Text.literal("Streamer Tag Disabled for ${target.name.string}") },
                                        false
                                    )
                                    1 // Return 1 to indicate success
                                }
                        )
                )
        )
    }
}