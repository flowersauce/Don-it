package net.flowersauce.don_it;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

public class DonIt implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {
            dispatcher.register(CommandManager.literal("donit")
                    .requires(source -> source.hasPermissionLevel(0))  // 普通玩家可执行
                    .executes(OnDonitCommand::onDonitCommand));
        });
    }
}
