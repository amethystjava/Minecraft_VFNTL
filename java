// 主模组类
package com.example.usefulmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class UsefulMod implements ModInitializer {
    private static KeyBinding toggleInfoKey;
    private static boolean showInfo = false;
    
    @Override
    public void onInitialize() {
        // 注册按键绑定
        toggleInfoKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.usefulmod.toggleinfo",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.usefulmod.test"
        ));
        
        // 注册客户端tick事件
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleInfoKey.wasPressed()) {
                showInfo = !showInfo;
                if (client.player != null) {
                    client.player.sendMessage(Text.literal("信息显示: " + (showInfo ? "开启" : "关闭")), false);
                }
            }
            
            if (showInfo && client.player != null) {
                displayUsefulInfo(client);
            }
        });
    }
    
    private void displayUsefulInfo(net.minecraft.client.MinecraftClient client) {
        // 显示有用的游戏信息（坐标、生物群系等）
        if (client.player != null && client.world != null) {
            String pos = String.format("坐标: X:%.1f Y:%.1f Z:%.1f", 
                client.player.getX(), client.player.getY(), client.player.getZ());
            String biome = "生物群系: " + client.world.getBiome(client.player.getBlockPos()).getKey().toString();
            String fps = "FPS: " + client.fpsCounter;
            
            // 在实际模组中，这里可以渲染到HUD上
            if (client.player.age % 20 == 0) { // 每秒更新一次
                client.player.sendMessage(Text.literal(pos + " | " + biome + " | " + fps), true);
            }
        }
    }
}
