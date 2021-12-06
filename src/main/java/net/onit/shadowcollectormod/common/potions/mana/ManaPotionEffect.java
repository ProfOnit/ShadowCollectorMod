package net.onit.shadowcollectormod.common.potions.mana;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaPotionEffect extends Effect {
    private final ResourceLocation potionIcon;
    public ManaPotionEffect() {
        super(EffectType.BENEFICIAL, -10053121);
        setRegistryName("mana_effect");
        potionIcon = new ResourceLocation(ShadowCollectorMod.MODID, "textures/mana_effect.png");
    }

    @ObjectHolder(ShadowCollectorMod.MODID + ":mana_effect")
    public static final Effect potion = null;
    @SubscribeEvent
    public static void registerEffect(RegistryEvent.Register<Effect> event) {
        event.getRegistry().register(new ManaPotionEffect());
    }


    @Override
    public String getName() {
        return "effect.mana_effect";
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public boolean shouldRenderInvText(EffectInstance effect) {
        return true;
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return true;
    }

    @Override
    public boolean shouldRenderHUD(EffectInstance effect) {
        return true;
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, LivingEntity entity, int amplifier, double health) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                switch (amplifier) {
                    case 0: mana.fill(mana.getManaMax() / 4);
                    break;
                    case 1: mana.fill(mana.getManaMax() / 2);
                    break;
                    case 2: mana.fill(mana.getManaMax());
                    break;
                    default: mana.fill(1);
                }
            });
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
