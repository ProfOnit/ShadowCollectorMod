package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.entity.Entity;

public interface IMana {
    public void consume(double points);
    public void fill(double points);
    public void enlarge(double points);
    public void setManaPerSec(double value);
    public void setMana(double value);
    public void setManaMax(double value);

    public double getManaMax();
    public double getMana();
    public double getManaPerSec();

    public void addShadow(boolean despawn);
    public void removeShadow();
    public void setShadowSummoned(boolean value);
    public void setShadow(int value);
    public void setShadowMax(int value);
    public void setShadowCurrent(int value);

    public int getShadowN();
    public int getShadowMaxN();
    public int getCurrentShadowN();
    public boolean getShadowSummoned();

    public void sync(Entity entity);
}
