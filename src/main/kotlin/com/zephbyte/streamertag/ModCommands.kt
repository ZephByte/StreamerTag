package com.zephbyte.streamertag

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object ModCommands {
    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, registryAccess, environment ->
            registerStreamerTagToggle(dispatcher)
            registerStreamerTagToggleOthers(dispatcher)
        }
    }

    fun registerStreamerTagToggle(
        dispatcher: CommandDispatcher<ServerCommandSource>
    ) {
        dispatcher.register(
            CommandManager.literal(MOD_ID)
                .then(
                    CommandManager.literal("toggle")
                        .requires{ source -> source.hasPermissionLevel(2) }
                        .executes{ context ->
                            //TODO: Do the stuff
                            context.source.sendFeedback({ Text.literal("Streamer tag toggled.") }, true)
                            1 // Return 1 to indicate success
                        }
                )
        )
    }

    fun registerStreamerTagToggleOthers(
        dispatcher: CommandDispatcher<ServerCommandSource>
    ) {
        dispatcher.register(
            CommandManager.literal(MOD_ID)
                .then(
                    CommandManager.literal("toggle")
                        .requires{ source -> source.hasPermissionLevel(2) }
                        .then(
                            CommandManager.argument("player", EntityArgumentType.player())
                                .executes { context ->
                                    val player = EntityArgumentType.getPlayer(context, "player")

                                    //TODO: Do the stuff for the player

                                    context.source.sendFeedback(
                                        { Text.literal("Toggling Streamer Tag for ${player.name.string}") },
                                        true
                                    )
                                    1 // Return 1 to indicate success
                                }
                        )
                )
        )
    }
}