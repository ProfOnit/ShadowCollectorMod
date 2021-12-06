package net.onit.shadowcollectormod.mixins.mana;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.onit.shadowcollectormod.ShadowCollectorMod;
import net.onit.shadowcollectormod.mixins.mana.IMana;

public class Mana implements IMana {
    private double manaMax = 10f;
    private double mana = 0f;
    private double manaPerSec = 0.01f;

    private int shadowMaxN = 3;
    private int shadowN = 0;
    private int currentShadowN = 0;
    private boolean shadowSummoned = false;

    @Override
    public void consume(double points) {
        this.mana -= points;
        if (this.mana < 0) this.mana = 0;
    }

    @Override
    public void fill(double points) {
        this.mana += points;
        if (this.mana > this.manaMax) this.mana = this.manaMax;
    }

    @Override
    public void enlarge(double points) {
        this.manaMax += points;
        this.shadowMaxN = (int)this.manaMax / 3;
    }

    @Override
    public void setManaPerSec(double value) {
        this.manaPerSec = value;
    }

    @Override
    public void setMana(double value) {
        this.mana = value;
    }

    @Override
    public void setManaMax(double value) {
        this.manaMax = value;
    }

    @Override
    public double getManaMax() {
        return this.manaMax;
    }

    @Override
    public double getMana() {
        return this.mana;
    }

    @Override
    public double getManaPerSec() {
        return this.manaPerSec;
    }

    @Override
    public void addShadow(boolean despawn) {
        if (this.shadowN < this.shadowMaxN) {
            this.shadowN += 1;
            if(!despawn && this.currentShadowN < this.shadowMaxN)
                this.currentShadowN +=1;
        }
    }

    @Override
    public void removeShadow() {
        this.shadowN -= 1;
    }

    @Override
    public void setShadowSummoned(boolean value) {
        this.shadowSummoned = value;
    }

    @Override
    public void setShadow(int value) {
        this.shadowN = value;
    }

    @Override
    public void setShadowMax(int value) {
        this.shadowMaxN = value;
    }

    @Override
    public void setShadowCurrent(int value) {
        this.currentShadowN = value;
    }

    @Override
    public int getShadowN() {
        return this.shadowN;
    }

    @Override
    public int getShadowMaxN() {
        return this.shadowMaxN;
    }

    @Override
    public int getCurrentShadowN() {
        return this.currentShadowN;
    }

    @Override
    public boolean getShadowSummoned() {
        return this.shadowSummoned;
    }

    @Override
    public void sync(Entity entity) {
        if (entity instanceof ServerPlayerEntity)
            ShadowCollectorMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new ManaSyncMessage(this));
    }
}
