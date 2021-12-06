package net.onit.shadowcollectormod.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.network.NetworkEvent;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.mixins.SummonShadows;
import org.lwjgl.glfw.GLFW;
import java.util.function.Supplier;


public class KeyBindings {
    KeyBinding[] keyBindings = new KeyBinding[2];

    public static final int SUMMON = 0;
    public static final int UNSUMMON = 1;

    public KeyBindings() {
        keyBindings[SUMMON] = new KeyBinding("key.shadowcollectormod.summon_shadows", GLFW.GLFW_KEY_Z, "key.categories.misc");
        keyBindings[UNSUMMON] = new KeyBinding("key.shadowcollectormod.unsummon_shadows", GLFW.GLFW_KEY_X, "key.categories.misc");
        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().currentScreen == null) {
            for (int i = 0; i < keyBindings.length; ++i) {
                if (event.getKey() == keyBindings[i].getKey().getKeyCode()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {

                    } else if (event.getAction() == GLFW.GLFW_RELEASE) {
                        ShadowCollectorMod.PACKET_HANDLER.sendToServer(new KeyBindingPressedMessage(i));
                        pressAction(Minecraft.getInstance().player, i);
                    }
                }
            }
        }
    }

    public static class KeyBindingPressedMessage {
        int type;
        public KeyBindingPressedMessage(int type) {
            this.type = type;
        }

        public KeyBindingPressedMessage(PacketBuffer buffer) {
            this.type = buffer.readInt();
        }

        public static void buffer(KeyBindingPressedMessage message, PacketBuffer buffer) {
            buffer.writeInt(message.type);
        }

        public static void handler(KeyBindingPressedMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                pressAction(context.getSender(), message.type);
            });
            context.setPacketHandled(true);
        }
    }

    private static void pressAction(PlayerEntity player, int type) {
        switch (type) {
            case SUMMON: SummonShadows.summon(player);
            break;
            case UNSUMMON: SummonShadows.unsummon(player);
            break;
            default: break;
        }
    }

}
