package ru.unclestalin.rotp_metallica.action.stand;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.damaging.projectile.CDBloodCutterEntity;
import com.github.standobyte.jojo.entity.itemprojectile.KnifeEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
public class SingleKnifeThrow extends StandEntityAction {
    public SingleKnifeThrow(Builder builder) {
        super(builder);
    }
    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()) {
            LivingEntity user = userPower.getUser();
            KnifeEntity knife = new KnifeEntity(world, user);
            knife.setTimeStopFlightTicks(5);
            knife.shootFromRotation(user, 3F,1.0F);
            world.addFreshEntity(knife);
        }
    }
}
