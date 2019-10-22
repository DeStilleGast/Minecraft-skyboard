package com.destillegast.skyboard.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

/**
 * Created by DeStilleGast 21-10-2019
 */
public class ConfigHandler {

    private static File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), "Skyboard/config.json");
    private static Config currentConfig;

    public static Config readConfig() {
        if (!configFile.exists()) {
            try {
                saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        try {
            return currentConfig = gson.fromJson(new FileReader(configFile), Config.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (currentConfig == null) currentConfig = new Config();

        // default to default
        return currentConfig;
    }

    public static void saveConfig() throws IOException {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        if (currentConfig == null) currentConfig = new Config();

        String json = gson.toJson(currentConfig);

        if (!new File(configFile.getParent()).exists())
            new File(configFile.getParent()).mkdir();

        if (!configFile.exists())
            configFile.createNewFile();

        FileWriter fw = new FileWriter(configFile);
        fw.write(json);
        fw.close();
    }

    public static Config getConfig() {
        if (currentConfig == null) {
            currentConfig = readConfig();
        }

        return currentConfig;
    }
}
