package net.onit.shadowcollectormod.common.recipies.potions.mana;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.onit.shadowcollectormod.common.potions.mana.ManaPotion;

public class ManaPotionBrewingRecipe implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        Item inputItem = input.getItem();
        return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION)
                && PotionUtils.getPotionFromItem(input) == Potions.STRONG_HEALING;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == Blocks.BLUE_ORCHID.asItem();
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient)) {
            return PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), ManaPotion.potionType);
        }
        return ItemStack.EMPTY;
    }
}
