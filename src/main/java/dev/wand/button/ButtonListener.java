package dev.wand.button;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

/**
 * @author xWand
 */
public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Iterator<Map.Entry<Member, WandButton>> iterator =  ButtonManager.getButtons().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Member, WandButton> buttonEntry = iterator.next();
            if (buttonEntry.getValue().getId().equals(event.getComponentId()) && buttonEntry.getKey().getId().equals(event.getUser().getId())) {
                buttonEntry.getValue().getOnClick().accept(buttonEntry.getKey(), event);
                if (!buttonEntry.getValue().isInfinite()) {
                    iterator.remove();
                }
                break;
            }
        }
    }
}
