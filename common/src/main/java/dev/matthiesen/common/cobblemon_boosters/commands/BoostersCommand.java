package dev.matthiesen.common.cobblemon_boosters.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.common.cobblemon_boosters.CobblemonBoosters;
import dev.matthiesen.common.cobblemon_boosters.commands.subcommands.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;
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

public final class BoostersCommand extends AbstractCommand {
    public static final BoostersCommand CMD = new BoostersCommand();
    public static List<ISubCommand> SUB_COMMANDS = new ArrayList<>();

    public BoostersCommand() {
    }

    static {
        SUB_COMMANDS.addAll(Boosts.getSubCommands());
        SUB_COMMANDS.addAll(Misc.getSubCommands());
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
            CobblemonBoosters.INSTANCE.guiAdapter.openMainMenuGUI(player);
        }
        return 1;
    }
}