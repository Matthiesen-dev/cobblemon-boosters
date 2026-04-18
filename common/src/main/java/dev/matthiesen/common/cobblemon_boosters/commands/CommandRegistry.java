package dev.matthiesen.common.cobblemon_boosters.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ICommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.List;

public class CommandRegistry {
    public static final List<ICommand> COMMANDS = List.of(
            new BoostersCommand()
    );

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection context) {
        for (ICommand command : COMMANDS) {
            command.register(dispatcher, registry, context);
        }
    }
}
