package net.onit.shadowcollectormod.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.onit.shadowcollectormod.common.entities.CommonShadowEntity;
import net.onit.shadowcollectormod.core.init.EntityInit;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;

public final class SummonShadows {
    public static void summon(PlayerEntity player) {
        World world = player.getEntityWorld();
        player.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
            if (mana.getShadowN() > 0 && mana.getMana() >= 1) {
                world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 2.5f, 1f);
                mana.setShadowSummoned(true);
                if (world instanceof ServerWorld) {
                    int shadowToSpawn = mana.getShadowN();
                    if (mana.getMana() < mana.getShadowN())
                        shadowToSpawn = (int)mana.getMana();
                    for (int i = 0; i < shadowToSpawn; i++) {
                        mana.consume(1);
                        Entity entityToSpawn = new CommonShadowEntity((EntityType<? extends TameableEntity>) EntityInit.COMMON_SHADOW.get(), world);
                        entityToSpawn.setLocationAndAngles(player.getPosX(), player.getPosY(), player.getPosZ(), (float) 0, (float) 0);
                        ((TameableEntity) entityToSpawn).setTamed(true);
                        ((TameableEntity) entityToSpawn).setTamedBy(player);
                        world.addEntity(entityToSpawn);
                        mana.removeShadow();
                    }
                }
                mana.sync(player);
            }
        });
    }

    public static void unsummon(PlayerEntity player) {
        World world = player.getEntityWorld();
        player.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
            if (mana.getShadowN() < mana.getShadowMaxN()) {
                world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 2.5f, 1f);
                mana.setShadowSummoned(false);
                mana.sync(player);
            }
        });
    }
}
