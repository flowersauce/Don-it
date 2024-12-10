package net.flowersauce.don_it;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;


public class OnDonitCommand
{
    // /donit help
    public static int on_donit_help(CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String helpMessage = """
        §4§l==========================
        §6§lDon it - 指令帮助
        §4§l==========================

        §a/donit help §7- 显示帮助信息
        §a/donit §7- 戴上主手中的物品
        §a/donit for <Player> §7- 为选中的玩家戴上调用者主手中的物品 (仅管理员)
        """;

        if (player != null)
        {
            player.sendMessage(Text.literal(helpMessage), false);
        }
        return 1;
    }

    // /donit
    public static int on_donit(CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (player != null)
        {
            ItemStack mainHandItem = player.getMainHandStack();

            if (!mainHandItem.isEmpty() && player.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
            {
                // 创建一个新的 ItemStack，将主手物品装备到头部
                ItemStack headItem = mainHandItem.copy();
                player.equipStack(EquipmentSlot.HEAD, headItem);

                // 清空玩家的主手物品
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                String itemName = headItem.getName().getString();
                player.sendMessage(Text.literal("§a 你戴上了 §6" + itemName), true);
            } else
            {
                if (!player.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
                {
                    player.sendMessage(Text.literal("§c 你尝试佩戴两顶帽子，但意识到只有一个头"), true);
                } else
                {
                    player.sendMessage(Text.literal("§b 你戴上了 §6空气"), true);
                }
            }
        }

        return 1;  // 返回值表示命令成功执行
    }

    // /donit for Player
    public static int on_donit_for_player(CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        ServerPlayerEntity targetPlayer;
        try
        {
            targetPlayer = EntityArgumentType.getPlayer(context, "player");
        }
        catch (CommandSyntaxException error)
        {
            context.getSource().sendError(Text.of("Invalid player argument!"));
            if (player != null)
            {
                player.sendMessage(Text.literal("§c选中的玩家不在游戏中"), false);
            }
            return 0;
        }

        if (player != null)
        {
            ItemStack mainHandItem = player.getMainHandStack();
            String playerName = player.getName().getString();
            String targetPlayerName = targetPlayer.getName().getString();

            if (!mainHandItem.isEmpty() && targetPlayer.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
            {
                // 创建一个新的 ItemStack，将主手物品装备到头部
                ItemStack headItem = mainHandItem.copy();
                targetPlayer.equipStack(EquipmentSlot.HEAD, headItem);

                // 清空玩家的主手物品
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                String itemName = headItem.getName().getString();
                player.sendMessage(Text.literal("§a 你为 §7" + targetPlayerName + " §a戴上了 §6" + itemName), true);
                targetPlayer.sendMessage(Text.literal("§7" + playerName + " §a为你戴上了 §6" + itemName), true);
            }
            else
            {
                if (!targetPlayer.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
                {
                    player.sendMessage(Text.literal("§c 你尝试为 §7" + targetPlayerName + " §c佩戴两顶帽子，但意识到TA只有一个头"), true);
                    targetPlayer.sendMessage(Text.literal("§7" + playerName + " §c尝试为你佩戴两顶帽子，但意识到你只有一个头"), true);
                } else
                {
                    player.sendMessage(Text.literal("§b 你为 §7" + targetPlayerName + " §b戴上了 §6空气"), true);
                    targetPlayer.sendMessage(Text.literal("§7" + playerName + " §b为你戴上了 §6空气"), true);
                }
            }
        }

        return 1;
    }
}
