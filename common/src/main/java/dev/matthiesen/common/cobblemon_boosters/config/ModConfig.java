package dev.matthiesen.common.cobblemon_boosters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.n1netails.n1netails.discord.DiscordColor;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.Constants;
import dev.matthiesen.common.cobblemon_boosters.data.CatchBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ExperienceBoost;
import dev.matthiesen.common.cobblemon_boosters.data.ShinyBoost;
import dev.matthiesen.common.cobblemon_boosters.data.SpawnBucketBoost;
import dev.matthiesen.common.cobblemon_boosters.interfaces.IBoost;
import net.kyori.adventure.bossbar.BossBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

public class ModConfig {

    // --- PERMISSIONS CONFIG ---

    @SerializedName("permissionlevels")
    public PermissionLevels permissionLevels = new PermissionLevels();

    public static class PermissionLevels {
        @SerializedName("command.boosters.reload")
        public int RELOAD_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch")
        public int CATCH_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.catch.start")
        public int CATCH_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch.stop")
        public int CATCH_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.catch.status")
        public int CATCH_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.experience")
        public int EXPERIENCE_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.experience.start")
        public int EXPERIENCE_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.experience.stop")
        public int EXPERIENCE_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.experience.status")
        public int EXPERIENCE_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.shiny")
        public int SHINY_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.shiny.start")
        public int SHINY_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.shiny.stop")
        public int SHINY_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.shiny.status")
        public int SHINY_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.bucket")
        public int BUCKET_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.bucket.start")
        public int BUCKET_START_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.bucket.stop")
        public int BUCKET_STOP_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.bucket.status")
        public int BUCKET_STATUS_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();

        @SerializedName("command.boosters.clear_queues")
        public int CLEAR_QUEUES_PERMISSION =
                Constants.PERMISSION_LEVELS.ALL_COMMANDS.getLevel();

        @SerializedName("command.boosters.check_queue")
        public int CHECK_QUEUE_PERMISSION =
                Constants.PERMISSION_LEVELS.NONE.getLevel();
    }

    // --- MESSAGES CONFIG ---

    @SerializedName("messages")
    public MessagesConfig messages = new MessagesConfig();

    public static class MessagesConfig {
        @SerializedName("prefix")
        public String prefix = "<gray>[<gold>Boosters<gray>]";

        @SerializedName("commandReload")
        public String commandReload = "%prefix% <green>Reloaded the config!";

        @SerializedName("shinyMessages")
        public ShinyMessagesConfig shinyMessages = new ShinyMessagesConfig();

        @SerializedName("catchBoostMessages")
        public CatchBoostMessagesConfig catchBoostMessages = new CatchBoostMessagesConfig();

        @SerializedName("experienceBoostMessages")
        public ExperienceBoostMessagesConfig experienceBoostMessages = new ExperienceBoostMessagesConfig();

        @SerializedName("spawnBucketBoostMessages")
        public SpawnBucketBoostMessagesConfig spawnBucketBoostMessages = new SpawnBucketBoostMessagesConfig();
    }

    public static class ShinyMessagesConfig {
        @SerializedName("barColor")
        public BossBar.Color barColor = BossBar.Color.YELLOW;

        @SerializedName("barOverlay")
        public BossBar.Overlay barOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<gold>%multiplier%x Shiny Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Shiny Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Shiny %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Shiny %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Shiny boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Shiny queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><gold>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Shiny Boosts in the queue!";
    }

    public static class CatchBoostMessagesConfig {
        @SerializedName("barColor")
        public BossBar.Color barColor = BossBar.Color.PURPLE;

        @SerializedName("barOverlay")
        public BossBar.Overlay barOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<light_purple>%multiplier%x Catch Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Catch Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Catch %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Catch %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Catch boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Catch queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><light_purple>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Catch Boosts in the queue!";
    }

    public static class ExperienceBoostMessagesConfig {
        @SerializedName("barColor")
        public BossBar.Color barColor = BossBar.Color.GREEN;

        @SerializedName("barOverlay")
        public BossBar.Overlay barOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<green>%multiplier%x Experience Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Experience Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a Experience %multiplier%x boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a Experience %multiplier%x boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Experience boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Experience queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Multiplier: <reset><green>%multiplier%x <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Experience Boosts in the queue!";
    }

    public static class SpawnBucketBoostMessagesConfig {
        @SerializedName("barColor")
        public BossBar.Color barColor = BossBar.Color.BLUE;

        @SerializedName("barOverlay")
        public BossBar.Overlay barOverlay = BossBar.Overlay.PROGRESS;

        @SerializedName("barText")
        public String barText = "<aqua>%bucket% Spawn Bucket Boost <gray>| <green>%time_remaining% Remaining";

        @SerializedName("noActiveBoosts")
        public String noActiveBoosts = "%prefix% <green>There are currently no active Spawn Bucket Boosts!";

        @SerializedName("boostStarted")
        public String boostStarted = "%prefix% <green>Started a %bucket% Spawn Bucket Boost for %duration%!";

        @SerializedName("boostAddedToQueued")
        public String boostAddedToQueued = "%prefix% <green>Added a %bucket% Spawn Bucket Boost with a %duration% duration to queue!";

        @SerializedName("boostStopped")
        public String boostStopped = "%prefix% <green>Stopped the current Spawn Bucket boost!";

        @SerializedName("boostQueueCleared")
        public String boostQueueCleared = "%prefix% <green>Cleared the Spawn Bucket queued boosts!";

        @SerializedName("boostInfo")
        public String boostInfo = "%prefix% <white><bold>Bucket: <reset><aqua>%bucket% <gray><bold>| <reset><white><bold>Timer: <reset><green>%time_remaining% <gray>/ <green>%duration%";

        @SerializedName("noQueuedBoosts")
        public String noQueuedBoosts = "%prefix% <green>There are currently no Spawn Bucket Boosts in the queue!";
    }

    // --- DISCORD WEBHOOK ---

    @SerializedName("discordWebhookConfig")
    public DiscordWebhookConfig discordWebhookConfig = new DiscordWebhookConfig();

    public static class DiscordWebhookConfig {
        @SerializedName("enabled")
        public boolean enabled = false;

        @SerializedName("webhookUrl")
        public String webhookUrl = "DISCORD_WEBHOOK_URL_HERE";

        @SerializedName("discordAuthorName")
        public String discordAuthorName = "Cobblemon Boosters";

        @SerializedName("discordAuthorIconUrl")
        public String discordAuthorIconUrl = "https://raw.githubusercontent.com/Matthiesen-Dev/cobblemon-boosters/refs/heads/main/assets/logo.png";

        @SerializedName("catchEventStartEmbed")
        public DiscordEmbed catchEventStartEmbed = new DiscordEmbed().create(
                "Catch Event Started!",
                "A new Catch Boost event has started! Here are the details:",
                DiscordColor.PURPLE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("catchEventEndEmbed")
        public DiscordEmbed catchEventEndEmbed = new DiscordEmbed().create(
                "Catch Event Ended!",
                "The %multiplier%x Catch Boost event has ended!",
                DiscordColor.PURPLE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("experienceEventStartEmbed")
        public DiscordEmbed experienceEventStartEmbed = new DiscordEmbed().create(
                "Experience Event Started!",
                "A new Experience Boost event has started! Here are the details:",
                DiscordColor.GREEN.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("experienceEventEndEmbed")
        public DiscordEmbed experienceEventEndEmbed = new DiscordEmbed().create(
                "Experience Event Ended!",
                "The %multiplier%x Experience Boost event has ended!",
                DiscordColor.GREEN.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("shinyEventStartEmbed")
        public DiscordEmbed shinyEventStartEmbed = new DiscordEmbed().create(
                "Shiny Event Started!",
                "A new Shiny Boost event has started! Here are the details:",
                DiscordColor.GOLD.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Multiplier",
                                "%multiplier%x",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("shinyEventEndEmbed")
        public DiscordEmbed shinyEventEndEmbed = new DiscordEmbed().create(
                "Shiny Event Ended!",
                "The %multiplier%x Shiny Boost event has ended!",
                DiscordColor.GOLD.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );

        @SerializedName("spawnBucketEventStartEmbed")
        public DiscordEmbed spawnBucketEventStartEmbed = new DiscordEmbed().create(
                "Spawn Bucket Event Started!",
                "A new Spawn Bucket Boost event has started! Here are the details:",
                DiscordColor.BLUE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                List.of(
                        new DiscordEmbedField().create(
                                "Bucket",
                                "%bucket%",
                                true
                        ),
                        new DiscordEmbedField().create(
                                "Duration",
                                "%duration%",
                                true
                        )
                ),
                "%timestamp%"
        );

        @SerializedName("spawnBucketEventEndEmbed")
        public DiscordEmbed spawnBucketEventEndEmbed = new DiscordEmbed().create(
                "Spawn Bucket Event Ended!",
                "The %bucket% Spawn Bucket Boost event has ended!",
                DiscordColor.BLUE.getValue(),
                new DiscordAuthor().create(
                        "%discord_webhook_author_name%",
                        null,
                        "%discord_webhook_author_icon_url%"
                ),
                null,
                "%timestamp%"
        );
    }

    public static class DiscordEmbed {
        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("color")
        public String color;

        @SerializedName("author")
        public DiscordAuthor author;

        @SerializedName("fields")
        public List<DiscordEmbedField> fields;

        @SerializedName("timestamp")
        public String timestamp;

        public DiscordEmbed create(
                String title,
                String description,
                String color,
                DiscordAuthor author,
                List<DiscordEmbedField> fields,
                String timestamp
        ) {
            DiscordEmbed embed = new DiscordEmbed();
            embed.title = title;
            embed.description = description;
            embed.color = color;
            embed.author = author;
            embed.fields = fields;
            embed.timestamp = timestamp;
            return embed;
        }
    }

    public static class DiscordAuthor {
        @SerializedName("name")
        public String name;

        @SerializedName("url")
        public String url;

        @SerializedName("icon_url")
        public String icon_url;

        public DiscordAuthor create(String name, String url, String icon_url) {
            DiscordAuthor author = new DiscordAuthor();
            author.name = name;
            author.url = url;
            author.icon_url = icon_url;
            return author;
        }
    }

    public static class DiscordEmbedField {
        @SerializedName("name")
        public String name;

        @SerializedName("value")
        public String value;

        @SerializedName("inline")
        public boolean inline;

        public DiscordEmbedField create(String name, String value, boolean inline) {
            DiscordEmbedField field = new DiscordEmbedField();
            field.name = name;
            field.value = value;
            field.inline = inline;
            return field;
        }
    }

    // --- LOCAL CACHE ---

    @SerializedName("activeShinyBoost")
    public ShinyBoost activeShinyBoost = null;

    @SerializedName("activeCatchBoost")
    public CatchBoost activeCatchBoost = null;

    @SerializedName("activeExperienceBoost")
    public ExperienceBoost activeExperienceBoost = null;

    @SerializedName("activeSpawnBucketBoost")
    public SpawnBucketBoost activeSpawnBucketBoost = null;

    @SerializedName("queuedShinyBoosts")
    public List<ShinyBoost> queuedShinyBoosts = new ArrayList<>();

    @SerializedName("queuedCatchBoosts")
    public List<CatchBoost> queuedCatchBoosts = new ArrayList<>();

    @SerializedName("queuedExperienceBoosts")
    public List<ExperienceBoost> queuedExperienceBoosts = new ArrayList<>();

    @SerializedName("queuedSpawnBucketBoosts")
    public List<SpawnBucketBoost> queuedSpawnBucketBoosts = new ArrayList<>();

    private static <T extends IBoost> void saveBoostData(
            T runtimeActive,
            Queue<? extends T> runtimeQueue,
            Consumer<T> configActiveSetter,
            List<? super T> configQueue
    ) {
        configActiveSetter.accept(runtimeActive);
        configQueue.clear();
        configQueue.addAll(runtimeQueue.stream().toList());
    }

    public void saveGlobalBoostData() {
        saveBoostData(
                CobblemonBoosters.INSTANCE.activeShinyBoost,
                CobblemonBoosters.INSTANCE.queuedShinyBoosts,
                value -> CobblemonBoosters.INSTANCE.config.activeShinyBoost = value,
                CobblemonBoosters.INSTANCE.config.queuedShinyBoosts
        );
        saveBoostData(
                CobblemonBoosters.INSTANCE.activeCatchBoost,
                CobblemonBoosters.INSTANCE.queuedCatchBoosts,
                value -> CobblemonBoosters.INSTANCE.config.activeCatchBoost = value,
                CobblemonBoosters.INSTANCE.config.queuedCatchBoosts
        );
        saveBoostData(
                CobblemonBoosters.INSTANCE.activeExperienceBoost,
                CobblemonBoosters.INSTANCE.queuedExperienceBoosts,
                value -> CobblemonBoosters.INSTANCE.config.activeExperienceBoost = value,
                CobblemonBoosters.INSTANCE.config.queuedExperienceBoosts
        );
        saveBoostData(
                CobblemonBoosters.INSTANCE.activeSpawnBucketBoost,
                CobblemonBoosters.INSTANCE.queuedSpawnBucketBoosts,
                value -> CobblemonBoosters.INSTANCE.config.activeSpawnBucketBoost = value,
                CobblemonBoosters.INSTANCE.config.queuedSpawnBucketBoosts
        );
    }

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
}
