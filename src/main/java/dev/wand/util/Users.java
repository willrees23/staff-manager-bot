package dev.wand.util;

import dev.wand.BotMain;
import lombok.experimental.UtilityClass;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

/**
 * @author xWand
 */
@UtilityClass
public class Users {

    public Member findMember(String member) {
        Guild guild = BotMain.getGuild();
        if (member.startsWith("<@") && member.endsWith(">")) { // Mention
            member = member.substring(2, member.length() - 1);
            if (member.startsWith("!")) {
                member = member.substring(1);
            }

            return guild.getMemberById(member);
        }

        //check if member is a number
        if (member.matches("\\d+")) {
            return guild.getMemberById(member);
        }
        return null;
    }

    public void selectUser(TextChannel channel, List<User> users) {
        channel.sendMessage("Select a user:").queue();

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (User user : users) {
            count++;
            builder.append("`").append(count).append("`  ").append(user.getAsTag()).append(" (").append(user.getId()).append(")\n");
        }
        channel.sendMessage(builder.toString()).setActionRow(
                Button.primary("solute_selectUser", "Select User").withEmoji(Emoji.fromUnicode("✅"))
        ).queue();
    }

    public User retrieveUser(String id) {
        return BotMain.getJDA().getUserById(id);
    }

    public User retrieveUser(long id) {
        return BotMain.getJDA().getUserById(id);
    }
}
