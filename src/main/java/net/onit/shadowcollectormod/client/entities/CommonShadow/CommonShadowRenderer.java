package net.onit.shadowcollectormod.client.entities.CommonShadow;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.common.entities.CommonShadowEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CommonShadowRenderer  extends GeoEntityRenderer<CommonShadowEntity> {
    public CommonShadowRenderer(EntityRendererManager renderManager) {
        super(renderManager, new CommonShadowModel());
    }

    @Override
    public ResourceLocation getEntityTexture(CommonShadowEntity entity) {
        return new ResourceLocation(ShadowCollectorMod.MODID, "textures/common_shadow.png");
    }
}