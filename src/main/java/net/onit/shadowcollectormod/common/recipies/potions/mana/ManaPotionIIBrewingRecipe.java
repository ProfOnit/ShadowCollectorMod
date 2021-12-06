package net.onit.shadowcollectormod.common.recipies.potions.mana;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.onit.shadowcollectormod.common.potions.mana.ManaPotionII;

public class ManaPotionIIBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION)
                && PotionUtils.getPotionFromItem(input) == Potions.STRONG_HEALING;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == Blocks.LAPIS_BLOCK.asItem();
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) {
            return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), ManaPotionII.potionType);
        }
        return ItemStack.EMPTY;
    }
}
