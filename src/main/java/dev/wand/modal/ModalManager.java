package dev.wand.modal;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author xWand
 */
public class ModalManager {

    @Getter
    private static final ArrayList<Map.Entry<Member, SoluteModal>> modals = new ArrayList<>();

    /**
     * Opens the modal
     * @param modal The modal to open
     * @param member The member to show the modal to
     * @param interaction The interaction to reply to
     */
    public static void open(SoluteModal modal, Member member, Interaction interaction) {
        ArrayList<ItemComponent> components = new ArrayList<>();
        modal.getComponents().forEach(component -> {
            components.add(component.apply(member));
        });

        Modal.Builder jdaModal = Modal.create(
                modal.getId(), modal.getTitle()
        );

        components.forEach(component -> {
            jdaModal.addActionRows(ActionRow.of(component));
        });

        if (interaction instanceof SlashCommandInteractionEvent) {
            SlashCommandInteractionEvent e = (SlashCommandInteractionEvent) interaction;
            e.replyModal(jdaModal.build()).queue();
        } else if (interaction instanceof ButtonInteractionEvent) {
            ButtonInteractionEvent e = (ButtonInteractionEvent) interaction;
            e.replyModal(jdaModal.build()).queue();
        } else
            throw new IllegalStateException("Interaction is not a SlashCommandInteractionEvent or ButtonInteractionEvent");


        modals.add(Map.entry(member, modal));
    }
}