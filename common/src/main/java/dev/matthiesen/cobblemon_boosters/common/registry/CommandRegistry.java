package dev.matthiesen.cobblemon_boosters.common.registry;

import dev.matthiesen.cobblemon_boosters.common.commands.BoostersCommand;
import dev.matthiesen.common.matthiesen_lib_api.registry.AbstractCommandRegistry;

public final class CommandRegistry extends AbstractCommandRegistry {
    private static final CommandRegistry INSTANCE = new CommandRegistry();

    public CommandRegistry() {
        super();
    }

    public static void init() {}

    static {
        INSTANCE.register(BoostersCommand.CMD);
    }
}
