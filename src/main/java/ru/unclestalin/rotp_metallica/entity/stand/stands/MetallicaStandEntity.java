package ru.unclestalin.rotp_metallica.entity.stand.stands;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;


public class MetallicaStandEntity extends StandEntity {
    
    public MetallicaStandEntity(StandEntityType<MetallicaStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!level.isClientSide() && hasUser()) {
            LivingEntity user = this.getUser();
            user.removeEffect(ModStatusEffects.FULL_INVISIBILITY.get());
        }
    }
}
