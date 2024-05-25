package ru.unclestalin.rotp_metallica.init;

import ru.unclestalin.rotp_metallica.entity.stand.stands.MetallicaStandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject.EntityStandSupplier;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;

public class AddonStands {

    public static final EntityStandSupplier<EntityStandType<StandStats>, StandEntityType<MetallicaStandEntity>>
            METALLICA_STAND = new EntityStandSupplier<>(InitStands.METALLICA_STAND);
}
