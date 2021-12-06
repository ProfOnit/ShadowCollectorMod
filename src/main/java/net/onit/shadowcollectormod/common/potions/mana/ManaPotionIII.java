package net.onit.shadowcollectormod.common.potions.mana;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import net.onit.shadowcollectormod.ShadowCollectorMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaPotionIII extends Potion{
    @ObjectHolder(ShadowCollectorMod.MODID + ":mana_potion_iii")
    public static final Potion potionType = null;
    @SubscribeEvent
    public static void registerPotion(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(new ManaPotionIII());
    }
    public ManaPotionIII() {
        super(new EffectInstance(ManaPotionEffect.potion, 0, 2, false, false));
            setRegistryName("mana_potion_iii");
    }
}
