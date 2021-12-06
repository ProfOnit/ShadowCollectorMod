package net.onit.shadowcollectormod.client.entities.CommonShadow;

import net.minecraft.util.ResourceLocation;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.common.entities.CommonShadowEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CommonShadowModel extends AnimatedGeoModel<CommonShadowEntity>{
    @Override
    public ResourceLocation getModelLocation(CommonShadowEntity object) {
        return new ResourceLocation(ShadowCollectorMod.MODID, "geo/common_shadow.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CommonShadowEntity object) {
        return new ResourceLocation(ShadowCollectorMod.MODID, "textures/common_shadow.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CommonShadowEntity animatable) {
        return new ResourceLocation(ShadowCollectorMod.MODID, "animations/animations.common_shadow.json");
    }
}