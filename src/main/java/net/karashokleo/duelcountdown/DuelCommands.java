package net.karashokleo.duelcountdown;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class DuelCommands
{
    private static void msg(CommandContext<ServerCommandSource> context, Text message)
    {
        context.getSource().sendMessage(message);
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("duel")
                .then(CommandManager.literal("start")
                        .executes(
                                context ->
                                {
                                    if (DuelCountdown.getDuelManager().startRegularDuel())
                                    {
                                        msg(context, Text.translatable("duel.commands.auto.start"));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    else {
                                        msg(context, Text.translatable("duel.bar.sufficient"));
                                        return 0;
                                    }
                                }
                        )
                )
                .then(CommandManager.literal("auto").requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("start")
                                .executes(
                                        context ->
                                        {
                                            DuelCountdown.getDuelManager().startCountdown();
                                            msg(context, Text.translatable("duel.commands.auto.start"));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                )
                        )
                        .then(CommandManager.literal("pause")
                                .executes(
                                        context ->
                                        {
                                            DuelCountdown.getDuelManager().pauseCountdown();
                                            msg(context, Text.translatable("duel.commands.auto.pause"));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                )
                        )
                        .then(CommandManager.literal("stop")
                                .executes(
                                        context ->
                                        {
                                            DuelCountdown.getDuelManager().stopCountdown();
                                            msg(context, Text.translatable("duel.commands.auto.stop"));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                )
                        )
                        .then(CommandManager.literal("total")
                                .then(CommandManager.literal("get")
                                        .executes(
                                                context ->
                                                {
                                                    int bar = DuelInfos.CONFIG.countdownBar;
                                                    msg(context, Text.translatable("duel.commands.auto.gap.get", bar));
                                                    return Command.SINGLE_SUCCESS;
                                                }
                                        )
                                )
                                .then(CommandManager.literal("set")
                                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                                .executes(
                                                        context ->
                                                        {
                                                            int bar = IntegerArgumentType.getInteger(context, "seconds");
                                                            DuelInfos.CONFIG.countdownGap = bar;
                                                            msg(context, Text.translatable("duel.commands.auto.gap.set", bar));
                                                            return Command.SINGLE_SUCCESS;
                                                        }
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("rest")
                                .then(CommandManager.literal("get")
                                        .executes(
                                                context ->
                                                {
                                                    int countdown = DuelCountdown.getDuelManager().getCountdown();
                                                    msg(context, Text.translatable("duel.commands.auto.rest.get", countdown < 0 ? 0 : DuelInfos.CONFIG.countdownBar - countdown / 20));
                                                    return Command.SINGLE_SUCCESS;
                                                }
                                        )
                                )
                                .then(CommandManager.literal("set")
                                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer())
                                                .executes(
                                                        context ->
                                                        {
                                                            int countdown = IntegerArgumentType.getInteger(context, "seconds");
                                                            DuelCountdown.getDuelManager().setCountdown((DuelInfos.CONFIG.countdownBar - countdown) * 20);
                                                            msg(context, Text.translatable("duel.commands.auto.rest.set", countdown));
                                                            return Command.SINGLE_SUCCESS;
                                                        }
                                                )
                                        )
                                )
                        )
                )
        );
    }
}
