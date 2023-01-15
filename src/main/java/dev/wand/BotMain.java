package dev.wand;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import dev.wand.button.ButtonListener;
import dev.wand.command.PromoteCommand;
import dev.wand.data.yaml.ConfigYaml;
import dev.wand.modal.ModalListener;
import dev.wand.sql.MySQL;
import dev.wand.sql.SQLManager;
import dev.wand.util.ConsoleUtil;
import dev.wand.util.MessageUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.SQLException;

/**
 * @author xWand
 */
public class BotMain {


    public static final String LOG_PREFIX = "[DiscordBot] ";
    private static JDA jda;
    private static EventWaiter waiter;
    private static Guild guild;
    private static MySQL sql;
    private static SQLManager sqlManager;

    public static void main(String[] args) {
        //set up bot.yml
        long startTime = System.currentTimeMillis();
        ConsoleUtil.log("Setting up configuration files...");
        ConfigYaml.setup();

        String token = ConfigYaml.get().getString("token");
        String guildId = ConfigYaml.get().getString("guildId");
        String ownerId = ConfigYaml.get().getString("applicationId");

        //////////////////
        // BUILDING JDA //
        //////////////////
        ConsoleUtil.log("Building the JDA instance...");
        JDABuilder builder = JDABuilder.createDefault(token);
        ConsoleUtil.log("Setting intents and cache policies...");
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        jda = builder.build();

        jda.addEventListener(new ButtonListener());
        jda.addEventListener(new ModalListener());

        waiter = new EventWaiter();

        //////////////////////////
        // REGISTERING COMMANDS //
        //////////////////////////
        ConsoleUtil.log("Registering commands...");
        CommandClientBuilder cmdBuilder = new CommandClientBuilder();
        cmdBuilder.forceGuildOnly(guildId);
        cmdBuilder.setOwnerId(ownerId);
        cmdBuilder.setEmojis(MessageUtil.successSymbol(), MessageUtil.warningSymbol(), MessageUtil.errorSymbol());
        cmdBuilder.setActivity(Activity.playing("with the API"));

        cmdBuilder.addSlashCommand(new PromoteCommand());

        CommandClient cmdClient = cmdBuilder.build();
        jda.addEventListener(cmdClient);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConsoleUtil.log("Shutting down...");
            jda.shutdown();
            try {
                sql.disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        /////////////////////////
        // DATABASE CONNECTION //
        /////////////////////////
        sql = new MySQL(
                ConfigYaml.get().getString("database.host"),
                ConfigYaml.get().getInt("database.port"),
                ConfigYaml.get().getString("database.database"),
                ConfigYaml.get().getString("database.username"),
                ConfigYaml.get().getString("database.password")
        );
        try {
            sql.connect();
        } catch (SQLException e) {
            ConsoleUtil.log("Failed to connect to database. Please check your configuration file.");
            ConsoleUtil.log("Database connection error: Login info incorrect or database is down?");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        sqlManager = new SQLManager();

        if (sql.isConnected()) {
            ConsoleUtil.log("Connected to database.");
        }

        sqlManager.initStaffLogs();
        sqlManager.initOtherLogs();


        ////////////////////
        // AWAITING READY //
        ////////////////////
        try {
            ConsoleUtil.log("Awaiting for JDA to be ready...");
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        guild = jda.getGuildById(guildId);

        ConsoleUtil.log(ConsoleUtil.ANSI_YELLOW + "--------------------------------------");
        ConsoleUtil.log(ConsoleUtil.ANSI_GREEN + "All systems loaded successfully in " + (System.currentTimeMillis() - startTime) + "ms.");
        ConsoleUtil.log(ConsoleUtil.ANSI_GREEN + "~ Logged in as " + ConsoleUtil.ANSI_CYAN + jda.getSelfUser().getAsTag() + ConsoleUtil.ANSI_GREEN + ".");
        ConsoleUtil.log(ConsoleUtil.ANSI_GREEN + "~ Developed by " + ConsoleUtil.ANSI_CYAN + "wand.#9642" + ConsoleUtil.ANSI_GREEN + ".");
        ConsoleUtil.log(ConsoleUtil.ANSI_YELLOW + "--------------------------------------");
    }

    public static JDA getJDA() {
        return jda;
    }

    public static EventWaiter getWaiter() {
        return waiter;
    }

    public static Guild getGuild() {
        return guild;
    }

    public static MySQL getSQL() {
        return sql;
    }

    public static SQLManager getSQLManager() {
        return sqlManager;
    }
}
