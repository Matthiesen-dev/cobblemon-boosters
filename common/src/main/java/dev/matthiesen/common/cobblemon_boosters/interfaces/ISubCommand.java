package dev.matthiesen.common.cobblemon_boosters.interfaces;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ISubCommand {
    LiteralArgumentBuilder<CommandSourceStack> getCmd();
}
