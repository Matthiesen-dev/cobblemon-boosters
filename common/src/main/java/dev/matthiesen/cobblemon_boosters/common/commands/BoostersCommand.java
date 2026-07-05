package dev.matthiesen.cobblemon_boosters.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters.Bucket;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters.Catch;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters.Experience;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.boosters.Shiny;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.CheckQueues;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.ClearQueues;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.QueuePriority;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.Reload;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

// '/boosters reload' - Reload config

// '/boosters catch start <multiplier> <duration> <seconds/minutes/hours/days>'
// '/boosters catch stop'
// '/boosters catch status'

// '/boosters experience start <multiplier> <duration> <seconds/minutes/hours/days>'
// '/boosters experience stop'
// '/boosters experience status'

// '/boosters shiny start <multiplier> <duration> <seconds/minutes/hours/days>'
// '/boosters shiny stop'
// '/boosters shiny status'

// '/boosters bucket start <common/uncommon/rare/ultra-rare> <multiplier> <duration> <seconds/minutes/hours/days>'
// '/boosters bucket stop'
// '/boosters bucket status'

// '/boosters clear-queues'

// '/boosters check-queues <booster>'

// '/boosters queue-priority'

public final class BoostersCommand extends AbstractCommand {
    public static final BoostersCommand CMD = new BoostersCommand();
    public static List<ISubCommand> SUB_COMMANDS = new ArrayList<>();

    public BoostersCommand() {
    }

    static {
        // Boosters Sub Commands
        SUB_COMMANDS.add(Bucket.CMD);
        SUB_COMMANDS.add(Catch.CMD);
        SUB_COMMANDS.add(Experience.CMD);
        SUB_COMMANDS.add(Shiny.CMD);

        // Misc Sub Commands
        SUB_COMMANDS.add(Reload.CMD);
        SUB_COMMANDS.add(ClearQueues.CMD);
        SUB_COMMANDS.add(CheckQueues.CMD);
        SUB_COMMANDS.add(QueuePriority.CMD);
    }

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        var rootCommand = Commands.literal("boosters").executes(this::action);
        for (ISubCommand subCommand : SUB_COMMANDS) {
            rootCommand = rootCommand.then(subCommand.getCmd());
        }
        dispatcher.register(rootCommand);
    }

    @Override
    public int action(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ServiceManager.getGuiAdapter().openMainMenuGUI(player);
        }
        return 1;
    }
}