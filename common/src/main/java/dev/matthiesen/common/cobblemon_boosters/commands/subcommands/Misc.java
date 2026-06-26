package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import dev.matthiesen.common.cobblemon_boosters.commands.subcommands.misc.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;

import java.util.List;

public final class Misc {
    public static Reload ReloadCMD = new Reload();
    public static ClearQueues ClearQueuesCMD = new ClearQueues();
    public static CheckQueues CheckQueuesCMD = new CheckQueues();
    public static QueuePriority QueuePriorityCMD = new QueuePriority();

    public static List<ISubCommand> getSubCommands() {
        return List.of(
                ReloadCMD,
                ClearQueuesCMD,
                CheckQueuesCMD,
                QueuePriorityCMD
        );
    }
}
