package net.onit.shadowcollectormod.core.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.common.entities.CommonShadowEntity;

public final class EntityInit {
    private EntityInit() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ShadowCollectorMod.MODID);

    public static final RegistryObject<EntityType<?>> COMMON_SHADOW = ENTITIES.register("common_shadow",
            () -> EntityType.Builder.<CommonShadowEntity>create(CommonShadowEntity::new, EntityClassification.CREATURE)
                    .size(1.5f, 2f).build(new ResourceLocation(ShadowCollectorMod.MODID, "common_shadow").toString()));
}
