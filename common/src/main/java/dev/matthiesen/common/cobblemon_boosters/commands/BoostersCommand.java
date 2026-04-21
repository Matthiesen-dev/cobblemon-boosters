package dev.matthiesen.common.cobblemon_boosters.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.matthiesen.common.cobblemon_boosters.commands.subcommands.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ICommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BoostersCommand implements ICommand {
    public BoostersCommand() {}

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

    // '/boosters check-queues'

    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        dispatcher.register(
                getCmd()
                    .then(new Reload().getCmd())
                    .then(new Catch().getCmd())
                    .then(new Experience().getCmd())
                    .then(new Shiny().getCmd())
                    .then(new Bucket().getCmd())
                    .then(new ClearQueues().getCmd())
                    .then(new CheckQueues().getCmd())
        );
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCmd() {
        return Commands.literal("boosters");
    }
}