package dev.wand.button;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author xWand
 */
@Getter
public class WandButton {

    private final String title, id;
    private final ButtonStyle style;
    private Emoji emoji;
    private boolean infinite = false;
    private BiConsumer<Member, ButtonInteractionEvent> onClick;

    public WandButton(@NotNull Member owner, String title, String id, ButtonStyle style) {
        this.title = title;
        this.id = id;
        this.style = style;

        setup(owner);
    }

    public WandButton setInfinite(boolean infinite) {
        this.infinite = infinite;
        return this;
    }

    public Button getButton() {
        return Button.of(style, id, title).withEmoji(emoji);
    }

    public WandButton setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    private void setup(Member member) {
        ButtonManager.getButtons().add(Map.entry(member, this));
    }

    public void onClick(BiConsumer<Member, ButtonInteractionEvent> onClick) {
        this.onClick = onClick;
    }
}
