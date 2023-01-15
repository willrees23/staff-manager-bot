package dev.wand.modal;

import dev.wand.util.TriConsumer;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author xWand
 */
@Getter
public class SoluteModal {

    private final String title;
    private final String id;
    private final ArrayList<Function<Member, ItemComponent>> components = new ArrayList<>();
    private TriConsumer<Member, ModalMapping[], ModalInteractionEvent> onSubmit;


    /**
     * Creates a new Modal instance (you must extend this class, don't create an instance of this class!!)
     * @param title The title of the modal
     * @param id The id of the modal
     */
    public SoluteModal(String title, String id) {
        this.title = title;
        this.id = id;
    }

    /**
     * Adds a component to the modal.
     * @param component The component to add.
     */
    public void addComponent(Function<Member, ItemComponent> component) {
        components.add(component);
    }

    /**
     * Opens the modal
     * @param member The member to show the modal to
     * @param interaction The interaction to reply to
     */
    public void open(Member member, ButtonInteractionEvent interaction) {
        ModalManager.open(this, member, interaction);
    }

    /**
     * Opens the modal
     * @param member The member to show the modal to
     * @param interaction The interaction to reply to
     */
    public void open(Member member, SlashCommandInteractionEvent interaction) {
        ModalManager.open(this, member, interaction);
    }

    /**
     * Selects the method to be called when the modal is submitted.
     * @param onSubmit The method to be called when the modal is submitted.
     */
    public void onSubmit(TriConsumer<Member, ModalMapping[], ModalInteractionEvent> onSubmit) {
        this.onSubmit = onSubmit;
    }
}
