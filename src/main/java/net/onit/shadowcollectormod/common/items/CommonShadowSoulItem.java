package net.onit.shadowcollectormod.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.onit.shadowcollectormod.core.init.ItemInit;
import net.onit.shadowcollectormod.mixins.mana.IMana;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;

import java.util.List;

public class CommonShadowSoulItem extends Item {
    public CommonShadowSoulItem() {
        super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).rarity(Rarity.COMMON));
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack itemstack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemstack, world, list, flag);
        list.add(new StringTextComponent("Add a new common Shadow to your collection"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
        ActionResult<ItemStack> ar = super.onItemRightClick(world, entity, hand);
        entity.getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
            if (mana.getCurrentShadowN() < mana.getShadowMaxN() && mana.getShadowN() < mana.getShadowMaxN()) {
                mana.addShadow(false);
                boolean isCreative = new Object() {
                    public boolean checkGamemode(Entity _ent) {
                        if (_ent instanceof ServerPlayerEntity) {
                            return ((ServerPlayerEntity) _ent).interactionManager.getGameType() == GameType.CREATIVE;
                        } else if (_ent instanceof PlayerEntity && _ent.world.isRemote()) {
                            NetworkPlayerInfo _npi = Minecraft.getInstance().getConnection()
                                    .getPlayerInfo(((AbstractClientPlayerEntity) _ent).getGameProfile().getId());
                            return _npi != null && _npi.getGameType() == GameType.CREATIVE;
                        }
                        return false;
                    }
                }.checkGamemode(entity);

                if (!isCreative) {
                    ItemStack toremove = new ItemStack(ItemInit.COMMON_SHADOW_SOUL.get());
                    entity.inventory.func_234564_a_(p -> toremove.getItem() == p.getItem(), (int) 1, entity.container.func_234641_j_());
                }
            }
            mana.sync(entity);
        });
        return ar;
    }
}
