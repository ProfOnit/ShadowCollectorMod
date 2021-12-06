package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ManaSyncMessage {
    public Mana data;

    public ManaSyncMessage(PacketBuffer buffer) {
        this.data = new Mana();
        new ManaStorage().readNBT(null, this.data, null, buffer.readCompoundTag());
    }

    public ManaSyncMessage(Mana data) {
        this.data = data;
    }

    public static void buffer(ManaSyncMessage message, PacketBuffer buffer) {
        buffer.writeCompoundTag((CompoundNBT) new ManaStorage().writeNBT(null, message.data, null));
    }

    public static void handler(ManaSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) {
                Minecraft.getInstance().player.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                    mana.setMana(message.data.getMana());
                    mana.setManaMax(message.data.getManaMax());
                    mana.setManaPerSec(message.data.getManaPerSec());
                    mana.setShadow(message.data.getShadowN());
                    mana.setShadowMax(message.data.getShadowMaxN());
                    mana.setShadowCurrent(message.data.getCurrentShadowN());
                    mana.setShadowSummoned(message.data.getShadowSummoned());
                });
            }
        });
        context.setPacketHandled(true);
    }
}