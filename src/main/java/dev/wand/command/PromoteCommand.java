package dev.wand.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import dev.wand.BotMain;
import dev.wand.button.WandButton;
import dev.wand.data.yaml.ConfigYaml;
import dev.wand.util.RoleUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xWand
 */
public class PromoteCommand extends SlashCommand {
    public PromoteCommand() {
        this.name = "promote";
        this.help = "Promote a user to a specific role.";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.userMissingPermMessage = "You do not have permission to use this command.";
        this.options = Arrays.asList(
                new OptionData(OptionType.USER, "user", "The user to promote.", true),
                new OptionData(OptionType.STRING, "reason", "The reason for the promotion.", true),
                new OptionData(OptionType.ROLE, "role", "The role to promote the user to.", false),
                new OptionData(OptionType.BOOLEAN, "announce", "Whether or not this promotion should be announced.", false)
        );
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        User user = slashCommandEvent.optUser("user");
        String reason = slashCommandEvent.optString("reason");
        Role role = slashCommandEvent.optRole("role");

        //get current highest role
        Role highestRole = RoleUtil.getHighestRoleInHierarchy(user);

        // If the role is null, promote to the next role in the hierarchy.
        if (role == null) { //if no role is specified, PROMOTE TO NEXT ROLE
            try {
                Role nextRole = RoleUtil.getNextRoleInHierarchy(user);
                if (nextRole == null) {
                    slashCommandEvent.reply("Something went wrong. Does that user have a role in the hierarchy?").setEphemeral(true).queue();
                    return;
                }
                BotMain.getGuild().addRoleToMember(UserSnowflake.fromId(user.getId()), nextRole).queue();
                slashCommandEvent.reply("Promoted user to " + nextRole.getName() + ".").setEphemeral(true).queue();

                BotMain.getSQLManager().createStaffLog(
                        slashCommandEvent.getMember().getId(),
                        user.getId(),
                        "Promotion",
                        highestRole.getName(),
                        nextRole.getName(),
                        System.currentTimeMillis(),
                        reason
                );
                return;
            } catch (IndexOutOfBoundsException e) {
                slashCommandEvent.reply("User is already at the highest role.").setEphemeral(true).queue();
                return;
            }

        }

        WandButton button = new WandButton(slashCommandEvent.getMember(), "Confirm", "promote-confirm", ButtonStyle.PRIMARY);
        Role finalRole = role;
        button.onClick((member, event) -> {
            BotMain.getGuild().addRoleToMember(UserSnowflake.fromId(user.getId()), finalRole).queue();
            event.deferEdit().setContent("Promoted user to " + finalRole.getName() + ".").setComponents().queue();

            BotMain.getGuild().addRoleToMember(UserSnowflake.fromId(user.getIdLong()), finalRole).queue();

            BotMain.getSQLManager().createStaffLog(
                    slashCommandEvent.getMember().getId(),
                    user.getId(),
                    "Promotion",
                    highestRole.getName(),
                    finalRole.getName(),
                    System.currentTimeMillis(),
                    reason
            );
        });

        slashCommandEvent.reply("Are you sure you want to promote " + user.getAsMention() + " to " + role.getName() + " for reason: " + reason).setEphemeral(true).setActionRow(
                button.getButton()
        ).queue();
        //give user role
    }
}
