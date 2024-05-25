package ru.unclestalin.rotp_metallica.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import ru.unclestalin.rotp_metallica.init.InitEffects;

import java.util.List;
import java.util.Random;


public class VictimBladeCreation extends StandEntityAction {
    public VictimBladeCreation(Builder builder) {
        super(builder);
    }

    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide) {
            double range = standEntity.getMaxRange();
            LivingEntity user = userPower.getUser();
            float standPower = (float) standEntity.getAttackDamage();
            List<LivingEntity> entitiesInRange = world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(range), EntityPredicates.ENTITY_STILL_ALIVE);
            for (LivingEntity livingEntity : entitiesInRange) {
                if (livingEntity != null && livingEntity.isAlive() && !(livingEntity instanceof StandEntity) && livingEntity != user
                && !(livingEntity instanceof MonsterEntity && !(livingEntity instanceof CreeperEntity
                || livingEntity instanceof EvokerEntity || livingEntity instanceof GuardianEntity
                || livingEntity instanceof PillagerEntity || livingEntity instanceof RavagerEntity || livingEntity instanceof SilverfishEntity
                || livingEntity instanceof SpiderEntity || livingEntity instanceof VindicatorEntity || livingEntity instanceof WitchEntity
                || livingEntity instanceof AbstractPiglinEntity || livingEntity instanceof IllusionerEntity)) && !(livingEntity instanceof SlimeEntity) && !(livingEntity instanceof PhantomEntity)
                && !(livingEntity instanceof SkeletonHorseEntity) && !(livingEntity instanceof ZombieHorseEntity)
                && !(livingEntity instanceof GolemEntity) && !(livingEntity instanceof GhastEntity) && !(livingEntity instanceof ArmorStandEntity)){
                    livingEntity.addEffect(new EffectInstance(InitEffects.LACK_OF_IRON.get(), 200, 0, false, false));
                    livingEntity.hurt(DamageSource.GENERIC, standPower / 12);
                    if (livingEntity.getMainHandItem() == ItemStack.EMPTY){
                        Random random = new Random();
                        float randValue = random.nextFloat();
                        if (randValue > 0.9F && randValue < 0.95F && livingEntity.tickCount % 20 == 0){
                            livingEntity.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.SHEARS));
                        }
                        else if (randValue > 0.95F && livingEntity.tickCount % 20 == 0){
                            livingEntity.setItemInHand(Hand.MAIN_HAND, new ItemStack(ModItems.KNIFE.get()));
                        }
                    }
                }
            }
        }
    }
}
