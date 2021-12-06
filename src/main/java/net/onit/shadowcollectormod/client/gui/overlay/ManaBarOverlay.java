package net.onit.shadowcollectormod.client.gui.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.mixins.mana.IMana;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;

@Mod.EventBusSubscriber
public class ManaBarOverlay {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            int posX = event.getWindow().getScaledWidth();
            int posY = event.getWindow().getScaledHeight();
            PlayerEntity entity = Minecraft.getInstance().player;
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableAlphaTest();

            entity.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                // MANA
                //Minecraft.getInstance().fontRenderer.drawString(event.getMatrixStack(), (int)mana.getMana() + "/" + (int)(mana.getManaMax()), 104, 8, -12829636);
                Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(ShadowCollectorMod.MODID, "textures/mana_bar_behind.png"));
                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), 1, 8,  0, 0, 102, 6, 102, 6);
                Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(ShadowCollectorMod.MODID, "textures/mana_bar.png"));
                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), 1, 8,  0, 0, (int)((mana.getMana() * 102) / mana.getManaMax()), 6, 102, 6);

                // SHADOWS
                Minecraft.getInstance().fontRenderer.drawString(event.getMatrixStack(), mana.getShadowN() + "/" + mana.getShadowMaxN(), 18, 22, -12829636);
                Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(ShadowCollectorMod.MODID, "textures/items/common_shadow_soul.png"));
                Minecraft.getInstance().ingameGUI.blit(event.getMatrixStack(), 1, 16,  0, 0, 16, 16, 16, 16);
            });
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableAlphaTest();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}