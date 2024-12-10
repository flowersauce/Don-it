package net.flowersauce.don_it;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class DonIt implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        // 发送欢迎消息
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();

            player.sendMessage(Text.literal("§6§lDon it §a模组已加载，使用 §7/donit help §a查看!"), false);
        });

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
                    .then(CommandManager.literal("for")
                            .requires(source -> source.hasPermissionLevel(2))  // 仅管理员可执行
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .executes(OnDonitCommand::on_donit_for_player)
                            )
                    )
            );
        });
    }
}
