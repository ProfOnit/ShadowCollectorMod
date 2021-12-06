package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onit.shadowcollectormod.ShadowCollectorMod;

@Mod.EventBusSubscriber
public class CapabilityHandler {
    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity && !(event.getObject() instanceof FakePlayer))
            event.addCapability(new ResourceLocation(ShadowCollectorMod.MODID, "mana"), new ManaProvider());
    }
}
