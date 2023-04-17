package com.won983212.kpatch;

import org.apache.logging.log4j.LogManager;

public class Logger {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    public static void info(Object obj){
        LOGGER.info("[" + KoreanInputPatch.MODID + "] " + obj);
    }

    public static void warn(Object obj){
        LOGGER.warn("[" + KoreanInputPatch.MODID + "] " + obj);
    }

    public static void error(Object obj){
        LOGGER.error("[" + KoreanInputPatch.MODID + "] " + obj);
    }
}
