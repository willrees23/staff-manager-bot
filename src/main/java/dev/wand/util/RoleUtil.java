package dev.wand.util;

import dev.wand.BotMain;
import dev.wand.data.yaml.ConfigYaml;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xWand
 */
public class RoleUtil {

    public static Role getNextRoleInHierarchy(User user) {
        List<String> list = ConfigYaml.get().getStringList("role-hierarchy"); //get the role hierarchy from the config
        ArrayList<Role> roles = new ArrayList<>();
        roles.addAll(BotMain.getGuild().getMemberById(user.getId()).getRoles()); //user roles

        Collections.reverse(list); //reverse the list so that the highest role is at the top

        Role role = null;
        for (String s : list) {
            for (Role r : roles) {
                if (r.getId().equalsIgnoreCase(s)) {
                    role = r;
                    break;
                }
            }
        }

        if (role == null) {
            return null;
        }

        Role nextRole = BotMain.getGuild().getRoleById(list.get(list.indexOf(role.getId()) + 1));
        return nextRole;
    }
}
