package net.onit.shadowcollectormod.common.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.Hand;

public class ShadowMeleeAttackGoal extends MeleeAttackGoal {
    private static final int ANIMATION_LEN = 22;
    private static final int ANIMATION_ATTACK = 16;
    private int animationTimer = 0;

    public ShadowMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double squaredDistance) {
        double d = this.getAttackReachSqr(target);
        if (squaredDistance <= d && animationTimer <= 0) {
            ((CommonShadowEntity)this.attacker).setIsAttacking(true);
            animationTimer = ANIMATION_LEN;
        }
        if (animationTimer > 0) {
            animationTimer--;
            if (animationTimer == ANIMATION_ATTACK) {
                this.attacker.swingArm(Hand.MAIN_HAND);
                this.attacker.attackEntityAsMob(target);
            }
            if (animationTimer == 0) {
                ((CommonShadowEntity)this.attacker).setIsAttacking(false);
            }
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        ((CommonShadowEntity)this.attacker).setIsAttacking(false);
        animationTimer = 0;
    }
}
