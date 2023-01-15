package dev.wand.util;

import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;

/**
 * @author xWand
 */
@UtilityClass
public class MessageUtil {

    public EmbedBuilder seeThroughEmbed(String title, String... lines) {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            if (Arrays.asList(lines).indexOf(line) == lines.length - 1)
                builder.append(line);
            else
                builder.append(line).append("\n");
        }
        return new EmbedBuilder()
                .setTitle(title)
                .setColor(Color.decode("#2F3136"))
                .setDescription(builder.toString());
    }

    public MessageEmbed embed(String title, Color color, String... strings) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(color);
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append("\n");
        }
        builder.setDescription(sb.toString());
        return builder.build();
    }

    public MessageEmbed embed(Color color, String... strings) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(color);
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string).append("\n");
        }
        builder.setDescription(sb.toString());
        return builder.build();
    }

    public MessageEmbed embedFielded(String title, Color color, MessageEmbed.Field... fields) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(color);
        for (MessageEmbed.Field field : fields) {
            builder.addField(field);
        }

        return builder.build();
    }

    public static MessageEmbed errorEmbed(String title, String... strings) {
        return embed(errorSymbol() + " " + title, errorColor(), strings);
    }

    public static MessageEmbed warningEmbed(String title, String... strings) {
        return embed(warningSymbol() + " " + title, warningColor(), strings);
    }

    public static MessageEmbed successEmbed(String title, String... strings) {
        return embed(successSymbol() + " " + title, successColor(), strings);
    }

    public static MessageEmbed defaultEmbed(String title, String... strings) {
        return embed(defaultSymbol() + " " + title, defaultColor(), strings);
    }

    public static MessageEmbed defaultEmbedFielded(String title, MessageEmbed.Field... fields) {
        return embedFielded(defaultSymbol() + " " + title, defaultColor(), fields);
    }

    public static MessageEmbed errorEmbedFielded(String title, MessageEmbed.Field... fields) {
        return embedFielded(errorSymbol() + " " + title, errorColor(), fields);
    }

    public static MessageEmbed warningEmbedFielded(String title, MessageEmbed.Field... fields) {
        return embedFielded(warningSymbol() + " " + title, warningColor(), fields);
    }

    public static MessageEmbed successEmbedFielded(String title, MessageEmbed.Field... fields) {
        return embedFielded(successSymbol() + " " + title, successColor(), fields);
    }

    public static MessageEmbed defaultEmbedFielded(String title, boolean quotedFields, MessageEmbed.Field... fields) {
        if (quotedFields) {
            for (int i = 0; i < fields.length; i++) {
                MessageEmbed.Field field = fields[i];
                fields[i] = new MessageEmbed.Field(field.getName(), "> " + field.getValue(), field.isInline());
            }
        }
        return embedFielded(defaultSymbol() + " " + title, defaultColor(), fields);
    }

    public static Color errorColor() {
        return Color.decode("#FF0000");
    }

    public static Color successColor() {
        return Color.decode("#43B581");
    }

    public static Color warningColor() {
        return Color.decode("#FFA500");
    }

    public static Color defaultColor() {
        return Color.decode("#2F3136");
    }

    public static String successSymbol() {
        return "<:tick:1041815277509287976>";
    }

    public static String errorSymbol() {
        return "<:cross:1041449371771863111>";
    }

    public static String warningSymbol() {
        return "<:warning:1041815279753232394>";
    }

    public static String defaultSymbol() {
        return "<:information:1041829786433110136>";
    }
}
