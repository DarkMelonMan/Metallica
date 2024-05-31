package ru.unclestalin.rotp_metallica.potion;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.potion.IApplicableEffect;
import com.github.standobyte.jojo.potion.UncurableEffect;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ForgeMod;


import javax.annotation.Nonnull;

public class LackOfIron extends UncurableEffect implements IApplicableEffect {
    public LackOfIron(EffectType type, int liquidColor) {
        super(type, liquidColor);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "1aec786d-2eff-43d4-85c5-6972bd8a7ec7", -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL).addAttributeModifier(Attributes.ATTACK_SPEED, "4f43df46-75b7-4b58-b56a-6f02d90061fb", -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL).addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "c73d6d8c-6e4a-428d-ae60-b9e1620fcc07", -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL).addAttributeModifier(Attributes.MOVEMENT_SPEED,"fdc4fde7-8fea-4301-a899-7fcd94eea9a4" , -0.25D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public void applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level.isClientSide){
            DamageUtil.suffocateTick(livingEntity, 0.01F);
            if (livingEntity instanceof PlayerEntity && INonStandPower.getPlayerNonStandPower((PlayerEntity)livingEntity).getType() == ModPowers.VAMPIRISM.get()){
                INonStandPower.getPlayerNonStandPower((PlayerEntity)livingEntity).consumeEnergy(2.5F);
            }
        }
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public boolean isApplicable(LivingEntity entity) {
        return true;
    }

}
