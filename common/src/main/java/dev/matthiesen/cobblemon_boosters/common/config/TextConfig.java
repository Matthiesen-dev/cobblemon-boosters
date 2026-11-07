package dev.matthiesen.cobblemon_boosters.common.config;

import dev.matthiesen.matthiesen_core.common.api.text_parsers.BuiltInTextParsers;
import dev.matthiesen.matthiesen_core.common.utility.chat.ChatColors;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class TextConfig {
    public ModConfigSpec.EnumValue<BuiltInTextParsers> textParser;

    // Spawn Buckets
    public ModConfigSpec.ConfigValue<String> spawnBucket_common;
    public ModConfigSpec.ConfigValue<String> spawnBucket_uncommon;
    public ModConfigSpec.ConfigValue<String> spawnBucket_rare;
    public ModConfigSpec.ConfigValue<String> spawnBucket_ultraRare;

    // GUI Configuration
    public ModConfigSpec.ConfigValue<String> gui_mainMenu_unknownBoosterType;
    public ModConfigSpec.ConfigValue<String> gui_CheckQueues_title;
    public ModConfigSpec.ConfigValue<String> gui_stopActiveBoost_title;
    public ModConfigSpec.ConfigValue<String> gui_baseTitle;
    public ModConfigSpec.ConfigValue<String> gui_text_previousPage;
    public ModConfigSpec.ConfigValue<String> gui_text_nextPage;

    public ModConfigSpec.ConfigValue<String> gui_adminGui_title;
    public ModConfigSpec.ConfigValue<String> gui_adminGui_button_reloadTitle;
    public ModConfigSpec.ConfigValue<String> gui_adminGui_button_clearQueuesTitle;

    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_title;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_mode_current;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_mode_notSet;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_noValue;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_multiplier;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_boostType;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_bucket;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_duration;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_unit;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_details;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_label_multiplier;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_label_duration;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_label_unit;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_label_bucket;
    public ModConfigSpec.ConfigValue<String> gui_boosterBuilder_confirm_title;

    public ModConfigSpec.ConfigValue<String> gui_queue_title;
    public ModConfigSpec.ConfigValue<String> gui_queue_duration;
    public ModConfigSpec.ConfigValue<String> gui_queue_bucket;

    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_catch;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_catch_displayName;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_catch_menuTitle;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_experience;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_experience_displayName;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_experience_menuTitle;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_shiny;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_shiny_displayName;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_shiny_menuTitle;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_spawnBucket;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_spawnBucket_displayName;
    public ModConfigSpec.ConfigValue<String> gui_boosterTitles_spawnBucket_menuTitle;

    public ModConfigSpec.ConfigValue<String> gui_buttons_confirmBoost;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_confirmBoost_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_boostQueueEntry;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_boostQueueEntry_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_viewBoostDetails;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_viewBoostDetails_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_increment;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_increment_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_decrement;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_decrement_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_spawnBucketBoosters;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_spawnBucketBoosters_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_shinyBoosters;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_shinyBoosters_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_catchBoosters;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_catchBoosters_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_experienceBoosters;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_experienceBoosters_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_viewQueue;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_viewQueue_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_viewQueue_multiple;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_viewQueue_multiple_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_adminMenu;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_adminMenu_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_reloadConfig;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_reloadConfig_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_clearAllQueues;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_clearAllQueues_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_cancel;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_cancel_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_confirm;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_confirm_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_stopActiveBoost;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_stopActiveBoost_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_viewActiveBoost;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_viewActiveBoost_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_startBoost;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_startBoost_color;
    public ModConfigSpec.ConfigValue<String> gui_buttons_pageText;
    public ModConfigSpec.EnumValue<ChatColors> gui_buttons_pageText_color;

    // Messages Configuration
    public ModConfigSpec.ConfigValue<String> messages_prefix;
    public ModConfigSpec.ConfigValue<String> messages_sidebarTitle;
    public ModConfigSpec.ConfigValue<String> messages_commandReload;
    public ModConfigSpec.ConfigValue<String> messages_queuePriorityStatus;
    public ModConfigSpec.ConfigValue<String> messages_queuePriorityUpdated;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_shiny_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_shiny_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_shiny_barText;
    public ModConfigSpec.ConfigValue<String> messages_shiny_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_shiny_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_shiny_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_shiny_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_catch_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_catch_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_catch_barText;
    public ModConfigSpec.ConfigValue<String> messages_catch_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_catch_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_catch_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_catch_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_experience_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_experience_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_experience_barText;
    public ModConfigSpec.ConfigValue<String> messages_experience_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_experience_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_experience_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_experience_noQueuedBoosts;

    public ModConfigSpec.EnumValue<BossEvent.BossBarColor> messages_spawnBucket_barColor;
    public ModConfigSpec.EnumValue<BossEvent.BossBarOverlay> messages_spawnBucket_barOverlay;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_barText;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_sidebarLine;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_noActiveBoosts;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostStarted;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostAddedToQueue;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostStopped;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostQueueCleared;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_boostInfo;
    public ModConfigSpec.ConfigValue<String> messages_spawnBucket_noQueuedBoosts;

    public ModConfigSpec.ConfigValue<String> messages_error_unknownBoostType;
    public ModConfigSpec.ConfigValue<String> messages_error_invalidMode;
    public ModConfigSpec.ConfigValue<String> messages_error_invalidDirection;
    public ModConfigSpec.ConfigValue<String> messages_error_selectModeFirst;
    public ModConfigSpec.ConfigValue<String> messages_error_missingFields;

    public TextConfig(ModConfigSpec.Builder builder) {

        textParser = builder.comment(
                        "The text parser to use for messages.",
                        "Note: Switching to another parser may break existing messages if they rely on specific formatting.",
                        "For example, if you switch from Vanilla, to Adventure MiniMessage, the Adventure parser does not support",
                        "legacy color codes, so any messages using them will appear broken, or return errors."
                )
                .defineEnum("textParser", BuiltInTextParsers.VANILLA);

        builder.comment("Spawn Bucket Configuration").push("spawnBuckets");
        spawnBucket_common = builder.comment("The display name for the common spawn bucket.")
                .define("common", "Common");
        spawnBucket_uncommon = builder.comment("The display name for the uncommon spawn bucket.")
                .define("uncommon", "Uncommon");
        spawnBucket_rare = builder.comment("The display name for the rare spawn bucket.")
                .define("rare", "Rare");
        spawnBucket_ultraRare = builder.comment("The display name for the ultra-rare spawn bucket.")
                .define("ultraRare", "Ultra Rare");
        builder.pop(); // Closes "spawnBuckets" block

        builder.comment("GUI Configuration").push("gui");
        gui_mainMenu_unknownBoosterType = builder.comment("Message displayed when an unknown booster type is encountered in the main menu.")
                .define("mainMenu_unknownBoosterType", "%prefix% &cUnknown booster type: %s&c.");
        gui_CheckQueues_title = builder.comment("The title for the Check Queues GUI.")
                .define("CheckQueues_title", "&aAll Boost Queues&r");
        gui_stopActiveBoost_title = builder.comment("The title for the Stop Active Boost GUI.")
                .define("stopActiveBoost_title", "&cConfirm to stop active boost!");
        gui_baseTitle = builder.comment("The base title for all booster GUIs.")
                .define("baseTitle", "&6Cobblemon Boosters&r");
        gui_text_nextPage = builder.comment("The text displayed for the next page button in GUIs.")
                .define("text_nextPage", "Next");
        gui_text_previousPage = builder.comment("The text displayed for the previous page button in GUIs.")
                .define("text_previousPage", "Previous");

        builder.comment("Admin GUI Configuration").push("adminGui");
        gui_adminGui_title = builder.comment("The title for the Admin GUI.")
                .define("title", "&cAdmin Menu&r");
        gui_adminGui_button_reloadTitle = builder.comment("The title for the Reload button in the Admin GUI.")
                .define("button_reloadTitle", "&cConfirm to reload");
        gui_adminGui_button_clearQueuesTitle = builder.comment("The title for the Clear Queues button in the Admin GUI.")
                .define("button_clearQueuesTitle", "&cConfirm to clear all Queues");
        builder.pop();

        builder.comment("Booster Builder").push("boosterBuilder");
        gui_boosterBuilder_title = builder.comment("The title for the Booster Builder GUI.")
                .define("boosterBuilder_title", "&6%booster_name% Booster Builder&r");
        gui_boosterBuilder_mode_current = builder.comment("The text displayed for the current mode in the Booster Builder GUI.")
                .define("boosterBuilder_mode_current", "&aCurrent Mode: &f%s");
        gui_boosterBuilder_mode_notSet = builder.comment("The text displayed when the mode is not set in the Booster Builder GUI.")
                .define("boosterBuilder_mode_notSet", "&cCurrent Mode: &fNot Set");
        gui_boosterBuilder_noValue = builder.comment("The text displayed when a value is not set in the Booster Builder GUI.")
                .define("boosterBuilder_noValue", "&cNot Set");
        gui_boosterBuilder_multiplier = builder.comment("The text displayed for the multiplier in the Booster Builder GUI.")
                .define("boosterBuilder_multiplier", "&7Multiplier: &f%s");
        gui_boosterBuilder_duration = builder.comment("The text displayed for the duration in the Booster Builder GUI.")
                .define("boosterBuilder_duration", "&7Duration: &f%s");
        gui_boosterBuilder_boostType = builder.comment("The text displayed for the boost type in the Booster Builder GUI.")
                .define("boosterBuilder_boostType", "&7Boost Type: &f%s");
        gui_boosterBuilder_bucket = builder.comment("The text displayed for the bucket in the Booster Builder GUI.")
                .define("boosterBuilder_bucket", "&7Bucket: &f%s");
        gui_boosterBuilder_unit = builder.comment("The text displayed for the unit in the Booster Builder GUI.")
                .define("boosterBuilder_unit", "&7Unit: &f%s");
        gui_boosterBuilder_details = builder.comment("The text displayed for the details in the Booster Builder GUI.")
                .define("boosterBuilder_details", "&6Details");
        gui_boosterBuilder_label_multiplier = builder.comment("The label for the multiplier button in the Booster Builder GUI.")
                .define("boosterBuilder_label_multiplier", "Multiplier");
        gui_boosterBuilder_label_duration = builder.comment("The label for the duration button in the Booster Builder GUI.")
                .define("boosterBuilder_label_duration", "Duration");
        gui_boosterBuilder_label_unit = builder.comment("The label for the unit button in the Booster Builder GUI.")
                .define("boosterBuilder_label_unit", "Unit");
        gui_boosterBuilder_label_bucket = builder.comment("The label for the bucket button in the Booster Builder GUI.")
                .define("boosterBuilder_label_bucket", "Bucket");
        gui_boosterBuilder_confirm_title = builder.comment("The title for the confirm button in the Booster Builder GUI.")
                .define("boosterBuilder_confirm_title", "&aConfirm to start/queue boost!");
        builder.pop(); // Closes "boosterBuilder" block

        builder.comment("Queue GUI Configuration").push("queueGui");
        gui_queue_title = builder.comment("The title for the Queue GUI.")
                .define("queue_title", "&b%s Queue");
        gui_queue_duration = builder.comment("The text displayed for the duration in the Queue GUI.")
                .define("queue_duration", "&fDuration: &a%duration%");
        gui_queue_bucket = builder.comment("The text displayed for the bucket in the Queue GUI.")
                .define("queue_bucket", "&aBucket: &c%bucket%");
        builder.pop(); // Closes "queueGui" block

        builder.comment("Booster Titles Configuration").push("boosterTitles");
        gui_boosterTitles_catch = builder.comment("The title for Catch Boosters.")
                .define("catch", "&a%multiplier%x Catch Boost&r");
        gui_boosterTitles_catch_displayName = builder.comment("The display name for Catch Boosters.")
                .define("catch_displayName", "Catch");
        gui_boosterTitles_catch_menuTitle = builder.comment("The menu title for Catch Boosters.")
                .define("catch_menuTitle", "&aCatch Boosters&r");
        gui_boosterTitles_experience = builder.comment("The title for Experience Boosters.")
                .define("experience", "&a%multiplier%x Experience Boost&r");
        gui_boosterTitles_experience_displayName = builder.comment("The display name for Experience Boosters.")
                .define("experience_displayName", "Experience");
        gui_boosterTitles_experience_menuTitle = builder.comment("The menu title for Experience Boosters.")
                .define("experience_menuTitle", "&aExperience Boosters&r");
        gui_boosterTitles_shiny = builder.comment("The title for Shiny Boosters.")
                .define("shiny", "&a%multiplier%x Shiny Boost&r");
        gui_boosterTitles_shiny_displayName = builder.comment("The display name for Shiny Boosters.")
                .define("shiny_displayName", "Shiny");
        gui_boosterTitles_shiny_menuTitle = builder.comment("The menu title for Shiny Boosters.")
                .define("shiny_menuTitle", "&aShiny Boosters&r");
        gui_boosterTitles_spawnBucket = builder.comment("The title for Spawn Bucket Boosters.")
                .define("spawnBucket", "&a%bucket% Spawn Bucket Boost&r");
        gui_boosterTitles_spawnBucket_displayName = builder.comment("The display name for Spawn Bucket Boosters.")
                .define("spawnBucket_displayName", "Spawn Bucket");
        gui_boosterTitles_spawnBucket_menuTitle = builder.comment("The menu title for Spawn Bucket Boosters.")
                .define("spawnBucket_menuTitle", "&aSpawn Bucket Boosters&r");
        builder.pop(); // Closes "boosterTitles" block

        builder.comment("Buttons Configuration").push("buttons");
        gui_buttons_confirmBoost = builder.comment("The text displayed for the confirm boost button.")
                .define("confirmBoost", "Confirm Boost");
        gui_buttons_confirmBoost_color = builder.comment("The color of the confirm boost button.")
                .defineEnum("confirmBoost_color", ChatColors.GREEN);
        gui_buttons_boostQueueEntry = builder.comment("The text displayed for the boost queue entry button.")
                .define("boostQueueEntry", "Boost Queue Entry");
        gui_buttons_boostQueueEntry_color = builder.comment("The color of the boost queue entry button.")
                .defineEnum("boostQueueEntry_color", ChatColors.AQUA);
        gui_buttons_viewBoostDetails = builder.comment("The text displayed for the view boost details button.")
                .define("viewBoostDetails", "View Boost Details");
        gui_buttons_viewBoostDetails_color = builder.comment("The color of the view boost details button.")
                .defineEnum("viewBoostDetails_color", ChatColors.AQUA);
        gui_buttons_increment = builder.comment("The text displayed for the increment button.")
                .define("increment", "Increment");
        gui_buttons_increment_color = builder.comment("The color of the increment button.")
                .defineEnum("increment_color", ChatColors.GREEN);
        gui_buttons_decrement = builder.comment("The text displayed for the decrement button.")
                .define("decrement", "Decrement");
        gui_buttons_decrement_color = builder.comment("The color of the decrement button.")
                .defineEnum("decrement_color", ChatColors.RED);
        gui_buttons_spawnBucketBoosters = builder.comment("The text displayed for the spawn bucket boosters button.")
                .define("spawnBucketBoosters", "Spawn Bucket Boosters");
        gui_buttons_spawnBucketBoosters_color = builder.comment("The color of the spawn bucket boosters button.")
                .defineEnum("spawnBucketBoosters_color", ChatColors.AQUA);
        gui_buttons_shinyBoosters = builder.comment("The text displayed for the shiny boosters button.")
                .define("shinyBoosters", "Shiny Boosters");
        gui_buttons_shinyBoosters_color = builder.comment("The color of the shiny boosters button.")
                .defineEnum("shinyBoosters_color", ChatColors.AQUA);
        gui_buttons_catchBoosters = builder.comment("The text displayed for the catch boosters button.")
                .define("catchBoosters", "Catch Boosters");
        gui_buttons_catchBoosters_color = builder.comment("The color of the catch boosters button.")
                .defineEnum("catchBoosters_color", ChatColors.AQUA);
        gui_buttons_experienceBoosters = builder.comment("The text displayed for the experience boosters button.")
                .define("experienceBoosters", "Experience Boosters");
        gui_buttons_experienceBoosters_color = builder.comment("The color of the experience boosters button.")
                .defineEnum("experienceBoosters_color", ChatColors.AQUA);
        gui_buttons_viewQueue = builder.comment("The text displayed for the view queue button.")
                .define("viewQueue", "View %s Queue");
        gui_buttons_viewQueue_color = builder.comment("The color of the view queue button.")
                .defineEnum("viewQueue_color", ChatColors.AQUA);
        gui_buttons_viewQueue_multiple = builder.comment("The text displayed for the view queue button when there are multiple queues.")
                .define("viewQueue_multiple", "View %s Queues");
        gui_buttons_viewQueue_multiple_color = builder.comment("The color of the view queue button when there are multiple queues.")
                .defineEnum("viewQueue_multiple_color", ChatColors.AQUA);
        gui_buttons_adminMenu = builder.comment("The text displayed for the admin menu button.")
                .define("adminMenu", "Admin Menu");
        gui_buttons_adminMenu_color = builder.comment("The color of the admin menu button.")
                .defineEnum("adminMenu_color", ChatColors.RED);
        gui_buttons_reloadConfig = builder.comment("The text displayed for the reload config button.")
                .define("reloadConfig", "Reload Config");
        gui_buttons_reloadConfig_color = builder.comment("The color of the reload config button.")
                .defineEnum("reloadConfig_color", ChatColors.RED);
        gui_buttons_clearAllQueues = builder.comment("The text displayed for the clear all queues button.")
                .define("clearAllQueues", "Clear All Queues");
        gui_buttons_clearAllQueues_color = builder.comment("The color of the clear all queues button.")
                .defineEnum("clearAllQueues_color", ChatColors.RED);
        gui_buttons_cancel = builder.comment("The text displayed for the cancel button.")
                .define("cancel", "Cancel");
        gui_buttons_cancel_color = builder.comment("The color of the cancel button.")
                .defineEnum("cancel_color", ChatColors.RED);
        gui_buttons_confirm = builder.comment("The text displayed for the confirm button.")
                .define("confirm", "Confirm");
        gui_buttons_confirm_color = builder.comment("The color of the confirm button.")
                .defineEnum("confirm_color", ChatColors.GREEN);
        gui_buttons_stopActiveBoost = builder.comment("The text displayed for the stop active boost button.")
                .define("stopActiveBoost", "Stop Active Boost");
        gui_buttons_stopActiveBoost_color = builder.comment("The color of the stop active boost button.")
                .defineEnum("stopActiveBoost_color", ChatColors.RED);

        gui_buttons_viewActiveBoost = builder.comment("The text displayed for the view active boost button.")
                .define("viewActiveBoost", "View Active Boost");
        gui_buttons_viewActiveBoost_color = builder.comment("The color of the view active boost button.")
                .defineEnum("viewActiveBoost_color", ChatColors.AQUA);
        gui_buttons_startBoost = builder.comment("The text displayed for the start boost button.")
                .define("startBoost", "Start New %s");
        gui_buttons_startBoost_color = builder.comment("The color of the start boost button.")
                .defineEnum("startBoost_color", ChatColors.GREEN);
        gui_buttons_pageText = builder.comment("The text displayed for the page text in GUIs.")
                .define("pageText", "Page %s of %s");
        gui_buttons_pageText_color = builder.comment("The color of the page text in GUIs.")
                .defineEnum("pageText_color", ChatColors.GRAY);
        builder.pop(); // Closes "buttons" block

        builder.pop(); // Closes "gui" block

        builder.comment("Messages Configuration").push("messages");
        messages_prefix = builder.comment("The prefix for all messages.")
                .define("prefix", "&7[&6Boosters&7]");
        messages_sidebarTitle = builder.comment("The title for the sidebar display.")
                .define("sidebarTitle", "&6&lBoosters");
        messages_commandReload = builder.comment("Message displayed when the booster services are reloaded.")
                .define("commandReload", "%prefix% &aReloaded Cobblemon Boosters!");
        messages_queuePriorityStatus = builder.comment("Message displayed for queue priority status.")
                .define("queuePriorityStatus", "%prefix% &7Queue priority -> enabled: &f%s&7, mode: &f%s&7, timeDirection: &f%s&7, preemption: &f%s");
        messages_queuePriorityUpdated = builder.comment("Message displayed when queue priority is updated.")
                .define("queuePriorityUpdated", "%prefix% &aUpdated queue priority setting: &f%s");

        builder.comment("Shiny Boost Messages").push("shinyMessages");
        messages_shiny_barColor = builder.comment("The color of the boss bar for shiny boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.YELLOW);
        messages_shiny_barOverlay = builder.comment("The overlay style of the boss bar for shiny boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_shiny_barText = builder.comment("The text displayed on the boss bar for shiny boosts.")
                .define("barText", "&6%multiplier%x Shiny Boost &7| &a%time_remaining% Remaining");
        messages_shiny_sidebarLine = builder.comment("The line text for the sidebar display for shiny boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &6Shiny &f%multiplier%x");
        messages_shiny_noActiveBoosts = builder.comment("Message displayed when there are no active shiny boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Shiny Boosts!");
        messages_shiny_boostStarted = builder.comment("Message displayed when a shiny boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Shiny %multiplier%x boost for %duration%!");
        messages_shiny_boostAddedToQueue = builder.comment("Message displayed when a shiny boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Shiny %multiplier%x boost with a %duration% duration to queue!");
        messages_shiny_boostStopped = builder.comment("Message displayed when a shiny boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Shiny boost!");
        messages_shiny_boostQueueCleared = builder.comment("Message displayed when the shiny boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Shiny queued boosts!");
        messages_shiny_boostInfo = builder.comment("Message displayed for shiny boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&6%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_shiny_noQueuedBoosts = builder.comment("Message displayed when there are no queued shiny boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Shiny Boosts in the queue!");
        builder.pop(); // Closes "messages.shinyMessages" block

        builder.comment("Catch Boost Messages").push("catchBoostMessages");
        messages_catch_barColor = builder.comment("The color of the boss bar for catch boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.PURPLE);
        messages_catch_barOverlay = builder.comment("The overlay style of the boss bar for catch boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_catch_barText = builder.comment("The text displayed on the boss bar for catch boosts.")
                .define("barText", "&d%multiplier%x Catch Boost &7| &a%time_remaining% Remaining");
        messages_catch_sidebarLine = builder.comment("The line text for the sidebar display for catch boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &dCatch &f%multiplier%x");
        messages_catch_noActiveBoosts = builder.comment("Message displayed when there are no active catch boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Catch Boosts!");
        messages_catch_boostStarted = builder.comment("Message displayed when a catch boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Catch %multiplier%x boost for %duration%!");
        messages_catch_boostAddedToQueue = builder.comment("Message displayed when a catch boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Catch %multiplier%x boost with a %duration% duration to queue!");
        messages_catch_boostStopped = builder.comment("Message displayed when a catch boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Catch boost!");
        messages_catch_boostQueueCleared = builder.comment("Message displayed when the catch boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Catch queued boosts!");
        messages_catch_boostInfo = builder.comment("Message displayed for catch boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&d%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_catch_noQueuedBoosts = builder.comment("Message displayed when there are no queued catch boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Catch Boosts in the queue!");
        builder.pop(); // Closes "messages.catchBoostMessages" block

        builder.comment("Experience Boost Messages").push("experienceBoostMessages");
        messages_experience_barColor = builder.comment("The color of the boss bar for experience boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.GREEN);
        messages_experience_barOverlay = builder.comment("The overlay style of the boss bar for experience boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_experience_barText = builder.comment("The text displayed on the boss bar for experience boosts.")
                .define("barText", "&a%multiplier%x Experience Boost &7| &a%time_remaining% Remaining");
        messages_experience_sidebarLine = builder.comment("The line text for the sidebar display for experience boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &aExp &f%multiplier%x");
        messages_experience_noActiveBoosts = builder.comment("Message displayed when there are no active experience boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Experience Boosts!");
        messages_experience_boostStarted = builder.comment("Message displayed when an experience boost is started.")
                .define("boostStarted", "%prefix% &aStarted an Experience %multiplier%x boost for %duration%!");
        messages_experience_boostAddedToQueue = builder.comment("Message displayed when an experience boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded an Experience %multiplier%x boost with a %duration% duration to queue!");
        messages_experience_boostStopped = builder.comment("Message displayed when an experience boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Experience boost!");
        messages_experience_boostQueueCleared = builder.comment("Message displayed when the experience boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Experience queued boosts!");
        messages_experience_boostInfo = builder.comment("Message displayed for experience boost information.")
                .define("boostInfo", "%prefix% &f&lMultiplier: &r&a%multiplier%x &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_experience_noQueuedBoosts = builder.comment("Message displayed when there are no queued experience boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Experience Boosts in the queue!");
        builder.pop(); // Closes "messages.experienceBoostMessages" block

        builder.comment("Spawn Bucket Boost Messages").push("spawnBucketBoostMessages");
        messages_spawnBucket_barColor = builder.comment("The color of the boss bar for spawn bucket boosts.")
                .defineEnum("barColor", BossEvent.BossBarColor.BLUE);
        messages_spawnBucket_barOverlay = builder.comment("The overlay style of the boss bar for spawn bucket boosts.")
                .defineEnum("barOverlay", BossEvent.BossBarOverlay.PROGRESS);
        messages_spawnBucket_barText = builder.comment("The text displayed on the boss bar for spawn bucket boosts.")
                .define("barText", "&b%bucket% Spawn Bucket Boost &7| &a%time_remaining% Remaining");
        messages_spawnBucket_sidebarLine = builder.comment("The line text for the sidebar display for spawn bucket boosts.")
                .define("sidebarLine", "&7%time_remaining_short2% &b%bucket% &f%multiplier%x");
        messages_spawnBucket_noActiveBoosts = builder.comment("Message displayed when there are no active spawn bucket boosts.")
                .define("noActiveBoosts", "%prefix% &aThere are currently no active Spawn Bucket Boosts!");
        messages_spawnBucket_boostStarted = builder.comment("Message displayed when a spawn bucket boost is started.")
                .define("boostStarted", "%prefix% &aStarted a %bucket% Spawn Bucket boost for %duration%!");
        messages_spawnBucket_boostAddedToQueue = builder.comment("Message displayed when a spawn bucket boost is added to the queue.")
                .define("boostAddedToQueue", "%prefix% &aAdded a %bucket% Spawn Bucket Boost with a %duration% duration to queue!");
        messages_spawnBucket_boostStopped = builder.comment("Message displayed when a spawn bucket boost is stopped.")
                .define("boostStopped", "%prefix% &aStopped the current Spawn Bucket boost!");
        messages_spawnBucket_boostQueueCleared = builder.comment("Message displayed when the spawn bucket boost queue is cleared.")
                .define("boostQueueCleared", "%prefix% &aCleared the Spawn Bucket queued boosts!");
        messages_spawnBucket_boostInfo = builder.comment("Message displayed for spawn bucket boost information.")
                .define("boostInfo", "%prefix% &f&lBucket: &r&b%bucket% &7&l| &r&f&lTimer: &r&a%time_remaining% &7/ &a%duration%");
        messages_spawnBucket_noQueuedBoosts = builder.comment("Message displayed when there are no queued spawn bucket boosts.")
                .define("noQueuedBoosts", "%prefix% &aThere are currently no Spawn Bucket Boosts in the queue!");
        builder.pop(); // Closes "messages.spawnBucketBoostMessages" block

        builder.comment("Error Messages").push("errorMessages");
        messages_error_unknownBoostType = builder.comment("Message displayed when an unknown boost type is encountered.")
                .define("unknownBoostType", "%prefix% &cUnknown booster type. Valid types are: %s");
        messages_error_invalidMode = builder.comment("Message displayed when an invalid mode is encountered.")
                .define("invalidMode", "%prefix% &cInvalid mode. Valid values: fifo, multiplier, time");
        messages_error_invalidDirection = builder.comment("Message displayed when an invalid direction is encountered.")
                .define("invalidDirection", "%prefix% &cInvalid direction. Valid values: shortest, longest");
        messages_error_selectModeFirst = builder.comment("Message displayed when a mode is not selected before setting direction.")
                .define("selectModeFirst", "&cPlease select a field to modify first by clicking on its button!");
        messages_error_missingFields = builder.comment("Message displayed when not all fields are filled out before confirming.")
                .define("missingFields", "&cYou must fill out all fields before confirming!");
        builder.pop(); // Closes "errorMessages" block

        builder.pop(); // Closes "messages" block
    }
}
