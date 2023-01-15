package dev.wand.modal;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent e) {
        Iterator<Map.Entry<Member, SoluteModal>> iterator = ModalManager.getModals().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Member, SoluteModal> modalEntry = iterator.next();
            if (modalEntry.getValue().getId().equals(e.getModalId()) && modalEntry.getKey().getId().equals(e.getMember().getId())) {
                ModalMapping[] mappings = e.getValues().toArray(new ModalMapping[0]);
                modalEntry.getValue().getOnSubmit().accept(e.getMember(), mappings, e);
                iterator.remove();
            }
        }
    }
}