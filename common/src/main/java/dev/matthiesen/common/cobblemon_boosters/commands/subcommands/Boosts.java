package dev.matthiesen.common.cobblemon_boosters.commands.subcommands;

import dev.matthiesen.common.cobblemon_boosters.commands.subcommands.boosters.*;
import dev.matthiesen.common.cobblemon_boosters.interfaces.ISubCommand;

import java.util.List;

public final class Boosts {
    public static final Bucket BucketCMD = new Bucket();
    public static final Catch CatchCMD = new Catch();
    public static final Experience ExperienceCMD = new Experience();
    public static final Shiny ShinyCMD = new Shiny();

    public static List<ISubCommand> getSubCommands() {
        return List.of(BucketCMD, CatchCMD, ExperienceCMD, ShinyCMD);
    }
}
