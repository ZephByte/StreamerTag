package com.zephbyte.streamertag

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import net.fabricmc.loader.api.FabricLoader

object ConfigManager {

    // Default values
    private val CONFIG_PATH = FabricLoader.getInstance().configDir.resolve("$MOD_ID.toml")

    private const val DEFAULT_STREAMER_TAG_PLACEHOLDER = "<dark_gray>[</dark_gray><red>LIVE</red><dark_gray>]</dark_gray>"

    // Config properties
    var streamerTagString: String = DEFAULT_STREAMER_TAG_PLACEHOLDER

    fun loadConfig() {
        val builder = CommentedFileConfig.builder(CONFIG_PATH)
            .autosave()
            .writingMode(WritingMode.REPLACE)

        val config = builder.build()
        config.load()

        streamerTagString = config.getOptional<String>("general.streamerTagPlaceholder").orElse(DEFAULT_STREAMER_TAG_PLACEHOLDER)

        if (!config.contains("general.streamerTagPlaceholder")) {
            config.set<String>("general.streamerTagPlaceholder", DEFAULT_STREAMER_TAG_PLACEHOLDER)
            config.setComment("general.streamerTagPlaceholder", " The placeholder to display when a player uses /streaming")
        }

        config.save() // Save any changes or defaults
        config.close() // Close the file

        LOGGER.info("Config loaded.")
    }
}