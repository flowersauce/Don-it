package net.flowersauce.don_it;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.item.ComponentSubPredicate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.awt.*;

/*  关于诅咒
*   祭品          效果
*   龙头          失明
*   下届合金锭     缓慢
*
*/

public class OnDonitCommand
{
    // /donit help
    public static int on_donit_help(CommandContext<ServerCommandSource> context)
    {
        String helpMessage = """
            /donit - 执行基础命令。
            /donit help - 显示帮助信息。
            /donit info - 查看你自己所戴物品的详细信息。
            /donit info <玩家名> - 查看指定玩家所戴物品的详细信息（仅限管理员）。
            /donit for <玩家名> - 对指定玩家执行操作（仅限管理员）。
            """;

        System.out.println(helpMessage);
        return 1;
    }

    // /donit
    public static int on_donit(CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();   // 获取命令源
        ServerPlayerEntity player = source.getPlayer();

        // 获取玩家主手中的物品
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

    // /donit info
    public static int on_donit_info(CommandContext<ServerCommandSource> context)
    {
        System.out.println("on_donit_info");
        return 1;
    }

    // /donit info Player
    public static int on_donit_info_player(CommandContext<ServerCommandSource> context)
    {
        System.out.println("on_donit_info_player");
        return 1;
    }

    // /donit for Player
    public static int on_donit_for_player(CommandContext<ServerCommandSource> context)
    {
        System.out.println("on_donit_for_player");
        return 1;
    }

    // /donit curse add
    public static int on_donit_curse_add(CommandContext<ServerCommandSource> context)
    {
        ServerCommandSource source = context.getSource();   // 获取命令源
        ServerPlayerEntity player = source.getPlayer();


        if (player != null)
        {
            ItemStack mainHeadItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
            ItemStack offHeadItem = player.getEquippedStack(EquipmentSlot.OFFHAND);
            if (mainHeadItem.getCount() == 1 && offHeadItem.getCount() == 1)
            {
                if (offHeadItem.getItem() == Items.NETHERITE_INGOT)
                {
                    offHeadItem.getOrDefault("lore", );

                    System.out.println(offHeadItem.getComponents());
                }
                else if (offHeadItem.getItem() == Items.DRAGON_HEAD)
                {
                    System.out.println("DRAGON_HEAD");
                }
                else
                {
                    System.out.println("Sorry");
                }
            }
            else
            {
                player.sendMessage(Text.literal("§c 使用一份祭品来诅咒一件物品"), true);
            }
        }

        return 1;
    }

    // /donit curse clean
    public static int on_donit_curse_clean(CommandContext<ServerCommandSource> context)
    {
        System.out.println("on_donit_curse_clean");
        return 1;
    }
}
