package net.onit.shadowcollectormod.mixins;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onit.shadowcollectormod.core.init.ItemInit;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;

@Mod.EventBusSubscriber
public class SoulsDrop {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null) {
            Entity entity = event.getEntity();
            Entity sourceentity = event.getSource().getImmediateSource();
            World world = entity.world;

            if (sourceentity instanceof PlayerEntity) {
                if (entity instanceof LivingEntity && sourceentity != null)
                    if (((LivingEntity) entity).getCreatureAttribute() != CreatureAttribute.UNDEAD) {
                        sourceentity.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                            if (Math.random() < mana.getManaMax() * 0.01)
                                if (!world.isRemote()) {
                                    ItemEntity entityToSpawn = new ItemEntity(world, sourceentity.getPosX(), sourceentity.getPosY(), sourceentity.getPosZ(), new ItemStack(ItemInit.COMMON_SHADOW_SOUL.get()));
                                    entityToSpawn.setPickupDelay(10);
                                    world.addEntity(entityToSpawn);
                                }
                        });
                    }
            }
        }
    }
}
