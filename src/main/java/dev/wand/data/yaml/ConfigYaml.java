package dev.wand.data.yaml;

import dev.wand.util.ConsoleUtil;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.implementation.api.QuoteStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author xWand
 */
public class ConfigYaml {
    private static YamlFile botConfig;

    public static void setup() {
        final YamlFile file = new YamlFile("config.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
                ConsoleUtil.log("Created configuration file: " + file.getFilePath() + ".");

                file.setComment("token", "TOKEN - Obtained from https://discord.com/developers/applications");
                file.addDefault("token", "");
                file.setComment("guildId", "GUILD ID - Obtained from right-clicking the server icon and clicking \"Copy ID\"");
                file.addDefault("guildId", "");
                file.setComment("applicationId", "APPLICATION ID - The ID of the bot.");
                file.addDefault("applicationId", "");
                file.setComment("role-hierarchy", "ROLE HIERARCHY - The order of roles from highest to lowest. The first role in the list is the highest role. Make sure to include the Member role at the end of the list.");
                file.addDefault("role-hierarchy", new ArrayList<String>());
                file.setComment("database", "DATABASE - The database to use. (MySQL)");
                file.set("database.host", "localhost", QuoteStyle.DOUBLE);
                file.set("database.port", 3306);
                file.set("database.database", "database", QuoteStyle.DOUBLE);
                file.set("database.username", "username", QuoteStyle.DOUBLE);
                file.set("database.password", "password", QuoteStyle.DOUBLE);
                file.save();

                ConsoleUtil.log("Created necessary run files. Please fill out the configuration files and restart the bot.");
                System.exit(0);
            } else {
                ConsoleUtil.log(file.getFilePath() + " already existed, loading configurations...");
            }
            file.loadWithComments();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        botConfig = file;
    }

    public static YamlFile get() {
        return botConfig;
    }
}
