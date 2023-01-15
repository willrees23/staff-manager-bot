package dev.wand.button;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author xWand
 */
public class ButtonManager {

    public static final Button YES = Button.of(ButtonStyle.SUCCESS, "yes", Emoji.fromUnicode("✅"));
    public static final Button NO = Button.of(ButtonStyle.DANGER, "no", Emoji.fromUnicode("❌"));

    @Getter
    private static final ArrayList<Map.Entry<Member, WandButton>> buttons = new ArrayList<>();
}
