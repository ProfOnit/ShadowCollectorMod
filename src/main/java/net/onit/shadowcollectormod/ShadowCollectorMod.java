package net.onit.shadowcollectormod;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.onit.shadowcollectormod.client.entities.CommonShadow.CommonShadowRenderer;
import net.onit.shadowcollectormod.common.entities.CommonShadowEntity;
import net.onit.shadowcollectormod.common.recipies.potions.mana.ManaPotionBrewingRecipe;
import net.onit.shadowcollectormod.common.recipies.potions.mana.ManaPotionIIBrewingRecipe;
import net.onit.shadowcollectormod.common.recipies.potions.mana.ManaPotionIIIBrewingRecipe;
import net.onit.shadowcollectormod.core.init.EntityInit;
import net.onit.shadowcollectormod.core.init.ItemInit;
import net.onit.shadowcollectormod.keybinds.KeyBindings;
import net.onit.shadowcollectormod.mixins.mana.*;
import software.bernie.geckolib3.GeckoLib;

@Mod(ShadowCollectorMod.MODID)
public class ShadowCollectorMod {
    private static final String PROTOCOL_VERSION = "1";
    public static final String MODID = "shadowcollectormod";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"), () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

    public ShadowCollectorMod() {

        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        manaStuff();
        // Init Keybindings
        new KeyBindings();
        // Init animation lib GeckoLib
        GeckoLib.initialize();
        registries();
        entityAttrs();
        recipies();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        PACKET_HANDLER.registerMessage(0, KeyBindings.KeyBindingPressedMessage.class, KeyBindings.KeyBindingPressedMessage::buffer, KeyBindings.KeyBindingPressedMessage::new, KeyBindings.KeyBindingPressedMessage::handler);
        PACKET_HANDLER.registerMessage(1, ManaSyncMessage.class, ManaSyncMessage::buffer, ManaSyncMessage::new, ManaSyncMessage::handler);
        CapabilityManager.INSTANCE.register(IMana.class, new ManaStorage(), Mana::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler((EntityType<CommonShadowEntity>) EntityInit.COMMON_SHADOW.get(),
                manager -> new CommonShadowRenderer(manager));
    }

    private void registries() {
        EntityInit.ENTITIES.register(eventBus);
        ItemInit.ITEMS.register(eventBus);
    }

    private void manaStuff() {
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    private void entityAttrs() {
        eventBus.register(new CommonShadowEntity.CommonShadowEntityAttrs());
    }

    private void recipies () {
        BrewingRecipeRegistry.addRecipe(new ManaPotionBrewingRecipe());
        BrewingRecipeRegistry.addRecipe(new ManaPotionIIBrewingRecipe());
        BrewingRecipeRegistry.addRecipe(new ManaPotionIIIBrewingRecipe());
    }
}
