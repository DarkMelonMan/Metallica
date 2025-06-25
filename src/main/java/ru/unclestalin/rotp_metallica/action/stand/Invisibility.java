package ru.unclestalin.rotp_metallica.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.LazySupplier;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import ru.unclestalin.rotp_metallica.init.InitSounds;

import javax.annotation.Nullable;

public class Invisibility extends StandEntityAction {

        private final LazySupplier<ResourceLocation> disableTex = new LazySupplier(() -> {
            return makeIconVariant(this, "_disable");
        });

    public Invisibility(StandEntityAction.Builder builder) {
            super(builder);
        }

        public void standPerform (World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
            if (!world.isClientSide()) {
                LivingEntity user = userPower.getUser();
                if (!user.hasEffect(Effects.INVISIBILITY)) {
                    user.addEffect(new EffectInstance(Effects.INVISIBILITY.getEffect(), 99999999, 999999, false, false, false));
                } else {
                    user.removeEffect(Effects.INVISIBILITY.getEffect());
                }
            }
        }

    @Override
    protected void playSoundAtStand(World world, StandEntity standEntity, SoundEvent sound, IStandPower standPower, Phase phase) {
        if (world.isClientSide() && phase == Phase.WINDUP && (sound == InitSounds.INVISIBILITY_ON.get() || sound == InitSounds.USER_INVISIBILITY.get()) && !standPower.getUser().hasEffect(Effects.INVISIBILITY.getEffect())) {
            super.playSoundAtStand(world, standEntity, sound, standPower, phase);
        } else if (world.isClientSide && phase == Phase.WINDUP && sound == InitSounds.INVISIBILITY_OFF.get() && standPower.getUser().hasEffect(Effects.INVISIBILITY.getEffect()))
            super.playSoundAtStand(world, standEntity, sound, standPower, phase);
    }

        public IFormattableTextComponent getTranslatedName (IStandPower power, String key){
            return new TranslationTextComponent(key, new Object[]{String.format("%.2f", 4999.95F)});
        }

        public ResourceLocation getIconTexturePath (@Nullable IStandPower power){
            return power != null && power.getUser().hasEffect(Effects.INVISIBILITY.getEffect()) ? (ResourceLocation) this.disableTex.get() : super.getIconTexturePath(power);
        }
}
