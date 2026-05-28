package dev.matthiesen.common.cobblemon_boosters.registry;

import dev.matthiesen.common.cobblemon_boosters.commands.BoostersCommand;
import dev.matthiesen.common.matthiesen_lib_api.registry.AbstractCommandRegistry;

public class CommandRegistry extends AbstractCommandRegistry {
    private static final CommandRegistry INSTANCE = new CommandRegistry();

    protected CommandRegistry() {
        super();
    }

    public static void init() {}

    static {
        INSTANCE.register(new BoostersCommand());
    }
}
