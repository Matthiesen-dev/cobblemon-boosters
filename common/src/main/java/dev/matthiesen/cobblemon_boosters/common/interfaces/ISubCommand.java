package dev.matthiesen.cobblemon_boosters.common.interfaces;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ISubCommand {
    LiteralArgumentBuilder<CommandSourceStack> getCmd();
}
