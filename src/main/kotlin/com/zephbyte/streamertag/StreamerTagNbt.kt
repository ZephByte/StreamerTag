package com.zephbyte.streamertag

import net.minecraft.server.network.ServerPlayerEntity

object StreamerTagNbt {
    private val streamerTagEnabled = mutableMapOf<ServerPlayerEntity, Boolean>()

    fun setStreamerTagEnabled(player: ServerPlayerEntity, enabled: Boolean) {
        streamerTagEnabled[player] = enabled
    }

    fun getStreamerTagEnabled(player: ServerPlayerEntity): Boolean {
        return streamerTagEnabled[player] ?: false
    }
}