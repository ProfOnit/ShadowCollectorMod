package net.onit.shadowcollectormod.common.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.onit.shadowcollectormod.core.init.EntityInit;
import net.onit.shadowcollectormod.mixins.mana.IMana;
import net.onit.shadowcollectormod.mixins.mana.Mana;
import net.onit.shadowcollectormod.mixins.mana.ManaProvider;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.*;

public class CommonShadowEntity extends TameableEntity implements IAnimatable {
    private static final DataParameter<Boolean> IS_ATTACKING = EntityDataManager.createKey(CommonShadowEntity.class, DataSerializers.BOOLEAN);

    private final AnimationFactory animationFactory = new AnimationFactory(this);

    public CommonShadowEntity(EntityType<? extends TameableEntity> commonShadow, World world) {
        super(commonShadow, world);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(IS_ATTACKING, false);
    }

    public void setIsAttacking(boolean value) {
        dataManager.set(IS_ATTACKING, value);
    }

    public boolean isAttacking() {
        return dataManager.get(IS_ATTACKING);
    }

    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {
        return !((target instanceof TameableEntity) && ((TameableEntity) target).getOwner() == this.getOwner());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this, 1, (float) 20, (float) 10, false));
        this.goalSelector.addGoal(2, new ShadowMeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(3, new OwnerHurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 1));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(10, new SwimGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, MonsterEntity.class, true, false));
        this.targetSelector.addGoal(6, new HurtByTargetGoal(this).setCallsForHelp(this.getClass()));

    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
        return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.blaze.hurt"));
    }

    @Override
    public net.minecraft.util.SoundEvent getDeathSound() {
        return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.ghast.death"));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getImmediateSource() instanceof PotionEntity)
            return false;
        if (source == DamageSource.DROWN)
            return false;
        if (source == DamageSource.WITHER)
            return false;
        if (source.getDamageType().equals("witherSkull"))
            return false;
        if (this.getOwner().getCapability(ManaProvider.MANA_CAP, null).isPresent()) {
            IMana mana = this.getOwner().getCapability(ManaProvider.MANA_CAP, null).orElseThrow(RuntimeException::new);
            if (amount <= mana.getMana()) {
                mana.consume(amount);
                return false;
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getOwner() != null) {
            this.getOwner().getCapability(ManaProvider.MANA_CAP , null).ifPresent(mana -> {
                if (!mana.getShadowSummoned()) {
                    mana.fill(1);
                    mana.addShadow(true);
                    mana.sync(this.getOwner());
                    if (!this.world.isRemote())
                        this.remove();
                }
            });
        }
    }

    public void livingTick() {
        super.livingTick();
        if (this.world.isRemote) {
            for(int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.PORTAL, this.getPosXRandom(0.5D), this.getPosYRandom() - 0.25D, this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
    }

    @Override
    public void awardKillScore(Entity entity, int score, DamageSource damageSource) {
        super.awardKillScore(entity, score, damageSource);
        if (entity instanceof LivingEntity)
            if (!(((LivingEntity)entity).getCreatureAttribute() == CreatureAttribute.UNDEAD)) {
                this.getOwner().getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
                    mana.enlarge(1);
                    mana.sync(this.getOwner());
                });
            }
    }


    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        this.getOwner().getCapability(ManaProvider.MANA_CAP, null).ifPresent(mana -> {
            mana.addShadow(true);
            mana.sync(this.getOwner());
        });
    }

    private <E extends CommonShadowEntity> PlayState predicate(AnimationEvent<E> event)
    {
        AnimationController animationController = event.getController();
        if (this.isAttacking()) {
            animationController.setAnimation(new AnimationBuilder().addAnimation("animation.attack", true));
        } else
            animationController.setAnimation(new AnimationBuilder().addAnimation("animation.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0f, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Mod.EventBusSubscriber
    public static class CommonShadowEntityAttrs {
        @SubscribeEvent
        public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
            AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
            ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
            ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 15);
            ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
            ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5);
            event.put((EntityType<? extends TameableEntity>) EntityInit.COMMON_SHADOW.get(), ammma.create());
        }
    }
}
