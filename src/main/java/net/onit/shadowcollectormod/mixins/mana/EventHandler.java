package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PlayerEntity entity = event.player;
            World world = entity.world;

            new Object() {
                private int ticks = 0;
                private float waitTicks;
                private IWorld world;

                public void start(IWorld world, int waitTicks) {
                    this.waitTicks = waitTicks;
                    MinecraftForge.EVENT_BUS.register(this);
                    this.world = world;
                }

                @SubscribeEvent
                public void tick(TickEvent.ServerTickEvent event) {
                    if (event.phase == TickEvent.Phase.END) {
                        this.ticks += 1;
                        if (this.ticks >= this.waitTicks)
                            run();
                    }
                }

                private void run() {
                    entity.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                        mana.fill(mana.getManaPerSec());
                        mana.sync(entity);
                    });
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }.start(world, (int) 20);
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event != null && event.getEntity() != null && event.getSource() != null) {
            Entity entity = event.getEntity();
            Entity sourceentity = event.getSource().getTrueSource();

            if (entity instanceof LivingEntity) {
                if (((LivingEntity)entity).getCreatureAttribute() != CreatureAttribute.UNDEAD) {
                    if (sourceentity != null) {
                        sourceentity.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                            mana.enlarge(1);
                            mana.sync(entity);
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event)
    {
        PlayerEntity player = event.getPlayer();
        IMana mana = player.getCapability(ManaProvider.MANA_CAP, null).orElseThrow(RuntimeException::new);
        IMana oldMana = event.getOriginal().getCapability(ManaProvider.MANA_CAP, null).orElseThrow(RuntimeException::new);
        mana.setMana(oldMana.getMana());
        mana.sync(player);
    }
}
