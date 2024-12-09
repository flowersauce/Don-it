package net.flowersauce.don_it;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class OnDonitCommand
{
    // 处理命令的静态方法
    public static int onDonitCommand(CommandContext<ServerCommandSource> context) {
        // 获取命令的源（CommandSource），可以用于发送反馈或执行其他操作
        ServerCommandSource source = context.getSource();
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

                String name = headItem.getName().getString();
                System.out.println(name);

                player.sendMessage(Text.literal("§a 你戴上了 §6" + name), true);
            }
            else
            {
                if (!player.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
                {
                    player.sendMessage(Text.literal("§c 你尝试佩戴两顶帽子，但意识到只有一个头"), true);
                }
                else
                {
                    player.sendMessage(Text.literal("§b 你戴上了 §6空气"), true);
                }
            }
        }

        return 1;  // 返回值表示命令成功执行
    }
}
