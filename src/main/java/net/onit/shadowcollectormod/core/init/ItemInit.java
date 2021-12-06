package net.onit.shadowcollectormod.core.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.common.items.CommonShadowSoulItem;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShadowCollectorMod.MODID);

    public static final RegistryObject<CommonShadowSoulItem> COMMON_SHADOW_SOUL = ITEMS.register("common_shadow_soul", CommonShadowSoulItem::new);
}
