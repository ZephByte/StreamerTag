package com.zephbyte.streamertag

import net.fabricmc.api.ModInitializer

class StreamerTag : ModInitializer {

    override fun onInitialize() {
        LOGGER.info("Streamer Tag mod initializing...")

        ConfigManager.loadConfig()
        ModCommands.register()

        LOGGER.info("Streamer Tag mod initialized.")
    }
}
