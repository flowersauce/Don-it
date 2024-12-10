package net.flowersauce.don_it;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.concurrent.atomic.AtomicInteger;

public class DonIt implements ModInitializer
{
    // 注册自定义组件
    public static final ComponentType<String> DONIT_CURSE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("don_it", "donit_curse"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    @Override
    public void onInitialize()
    {
        // 自定义指令注册
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {
            dispatcher.register(CommandManager.literal("donit")
                    .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                    .executes(OnDonitCommand::on_donit)
                    .then(CommandManager.literal("help")
                            .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                            .executes(OnDonitCommand::on_donit_help)
                    )
                    .then(CommandManager.literal("info")
                            .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                            .executes(OnDonitCommand::on_donit_info)
                            .then(CommandManager.argument("player", StringArgumentType.word())
                                    .requires(source -> source.hasPermissionLevel(2))  // 仅管理员可执行
                                    .executes(OnDonitCommand::on_donit_info_player)
                            )
                    )
                    .then(CommandManager.literal("for")
                            .requires(source -> source.hasPermissionLevel(2))  // 仅管理员可执行
                            .then(CommandManager.argument("player", StringArgumentType.word())
                                    .executes(OnDonitCommand::on_donit_for_player)
                            )
                    )
                    .then(CommandManager.literal("curse")
                            .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                            .then(CommandManager.literal("add")
                                    .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                                    .executes(OnDonitCommand::on_donit_curse_add)
                            )
                            .then(CommandManager.literal("clean")
                                    .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                                    .executes(OnDonitCommand::on_donit_curse_clean)
                            )
                    )
            );
        });

        AtomicInteger tickCounter = new AtomicInteger(); // 初始化Tick计数器

        // 事件监听器注册
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tickCounter.addAndGet(1);
            if (tickCounter.get() == 40)
            {
                tickCounter.set(0);

                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList())
                {
                    ItemStack headItem = player.getEquippedStack(EquipmentSlot.HEAD);
                    String headItemId = headItem.toString();
                    if (!headItem.isEmpty() && headItem.getItem() == Items.NETHERITE_INGOT)
                    {
                        System.out.println("OK");
                    }
                }
            }
        });
    }
}
