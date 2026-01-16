package com.example;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleMod implements ModInitializer {
    // Логгер для отладки
    public static final Logger LOGGER = LogManager.getLogger("ExampleMod");

    @Override
    public void onInitialize() {
        // Инициализация мода
        LOGGER.info("ExampleMod загружен! Speed с обходом HolyWorld активирован.");
    }
}
