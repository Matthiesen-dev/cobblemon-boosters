package dev.matthiesen.cobblemon_boosters.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import dev.matthiesen.cobblemon_boosters.common.services.ServiceManager;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.CheckQueues;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.ClearQueues;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.QueuePriority;
import dev.matthiesen.cobblemon_boosters.common.commands.subcommands.misc.Reload;
import dev.matthiesen.cobblemon_boosters.common.interfaces.ISubCommand;
import dev.matthiesen.matthiesen_core.common.api.command.CoreCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

// '/boosters reload' - Reload config
// '/boosters clear-queues'
// '/boosters check-queues <booster>'
// '/boosters queue-priority'

public final class BoostersCommand implements CoreCommand {
    public static final BoostersCommand CMD = new BoostersCommand();
    public static List<ISubCommand> SUB_COMMANDS = new ArrayList<>();
    public static Map<String, Consumer<CommandContext<CommandSourceStack>>> QUEUE_RESPONSE_HANDLERS = new ConcurrentHashMap<>();
    public static Map<String, Consumer<CommandContext<CommandSourceStack>>> QUEUE_CLEAR_HANDLERS = new ConcurrentHashMap<>();

    public static void registerSubCommand(ISubCommand subCommand) {
        SUB_COMMANDS.add(subCommand);
    }

    public static void registerQueueResponseHandler(String booster, Consumer<CommandContext<CommandSourceStack>> handler) {
        QUEUE_RESPONSE_HANDLERS.put(booster, handler);
    }

    public static void registerQueueClearHandler(String booster, Consumer<CommandContext<CommandSourceStack>> handler) {
        QUEUE_CLEAR_HANDLERS.put(booster, handler);
    }

    public static void clearQueues(CommandContext<CommandSourceStack> ctx) {
        for (Consumer<CommandContext<CommandSourceStack>> handler : QUEUE_CLEAR_HANDLERS.values()) {
            handler.accept(ctx);
        }
    }

    public BoostersCommand() {
    }

    static {
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

    public int action(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ServiceManager.getGuiAdapter().openMainMenuGUI(player);
        }
        return 1;
    }
}