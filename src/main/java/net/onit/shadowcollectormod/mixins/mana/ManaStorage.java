package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.onit.shadowcollectormod.mixins.mana.IMana;

import javax.annotation.Nullable;

public class ManaStorage implements Capability.IStorage<IMana> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMana> capability, IMana instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble("mana", instance.getMana());
        nbt.putDouble("manaMax", instance.getManaMax());
        nbt.putDouble("manaPerSec", instance.getManaPerSec());
        nbt.putDouble("shadowNumber", instance.getShadowN());
        nbt.putDouble("shadowMaxNumber", instance.getShadowMaxN());
        nbt.putBoolean("shadowsSummoned", instance.getShadowSummoned());
        nbt.putDouble("shadowCurrentNumber", instance.getCurrentShadowN());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IMana> capability, IMana instance, Direction side, INBT inbt) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        instance.setMana(nbt.getDouble("mana"));
        instance.setManaMax(nbt.getDouble("manaMax"));
        instance.setManaPerSec(nbt.getDouble("manaPerSec"));
        instance.setShadow(nbt.getInt("shadowNumber"));
        instance.setShadowMax(nbt.getInt("shadowMaxNumber"));
        instance.setShadowSummoned(nbt.getBoolean("shadowsSummoned"));
        instance.setShadowCurrent(nbt.getInt("shadowCurrentNumber"));
    }
}
