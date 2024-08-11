package ru.unclestalin.rotp_metallica.init;

import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mod.StoryPart;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.action.stand.*;
import ru.unclestalin.rotp_metallica.entity.stand.stands.MetallicaStandEntity;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import ru.unclestalin.rotp_metallica.power.impl.stand.type.MetallicaStandType;

import static com.github.standobyte.jojo.init.ModEntityTypes.ENTITIES;

public class InitStands {
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpMetallicaAddon.MOD_ID);

    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpMetallicaAddon.MOD_ID);


    public static final RegistryObject<Invisibility> INVISIBILITY = ACTIONS.register("invisibility", () ->
            new Invisibility(((new StandEntityAction.Builder()).holdType()).standWindupDuration(30).standSound(StandEntityAction.Phase.WINDUP, InitSounds.USER_INVISIBILITY).standSound(StandEntityAction.Phase.WINDUP, InitSounds.INVISIBILITY_ON).standSound(StandEntityAction.Phase.WINDUP, InitSounds.INVISIBILITY_OFF).staminaCost(200.0F).resolveLevelToUnlock(2).holdToFire(30, false)));

    public static final RegistryObject<Heal> HEAL_USER = ACTIONS.register("heal", () ->
            new Heal(((new StandEntityAction.Builder())).standSound(InitSounds.HEAL).staminaCost(200.0F).cooldown(150).resolveLevelToUnlock(4)));

    public static final RegistryObject<VictimBladeCreation> VICTIM_BLADES_CREATION = ACTIONS.register("create_blades_in_victim", () ->
        new VictimBladeCreation(((new StandEntityAction.Builder())).staminaCostTick(6.5F).cooldown(300)
                .standSound(StandEntityAction.Phase.PERFORM, InitSounds.CREATE_BLADES_IN_VICTIM)
                .resolveLevelToUnlock(2).holdType(100)));

    public static final RegistryObject<SingleKnifeThrow> METALLICA_SINGLE_KNIFE_THROW = ACTIONS.register("metallica_single_knife_throw",
            () -> new SingleKnifeThrow(new StandEntityAction.Builder().standWindupDuration(5).staminaCost(30).cooldown(5)
                    .standSound(StandEntityAction.Phase.WINDUP, ModSounds.KNIFE_THROW)
                    .standSound(StandEntityAction.Phase.WINDUP, InitSounds.USER_KNIFE_THROW)));

    public static final RegistryObject<MultipleKnivesThrow> METALLICA_MULTIPLE_KNIVES_THROW = ACTIONS.register("metallica_multiple_knives_throw",
            () -> new MultipleKnivesThrow(new StandEntityAction.Builder().standWindupDuration(5).staminaCost(120).cooldown(40)
                    .resolveLevelToUnlock(1).shiftVariationOf(METALLICA_SINGLE_KNIFE_THROW)
                    .standSound(StandEntityAction.Phase.WINDUP, ModSounds.KNIVES_THROW, InitSounds.USER_KNIVES_THROW)));


    public static final EntityStandRegistryObject<MetallicaStandType<StandStats>, StandEntityType<MetallicaStandEntity>> METALLICA_STAND =
            new EntityStandRegistryObject<>("metallica",
                    STANDS,
                    () -> new MetallicaStandType.Builder<StandStats>()
                            .color(0x4C005B)
                            .storyPartName(StoryPart.GOLDEN_WIND.getName())
                            .leftClickHotbar(
                                    METALLICA_SINGLE_KNIFE_THROW.get(),
                                    VICTIM_BLADES_CREATION.get()
                            )
                            .rightClickHotbar(
                                    INVISIBILITY.get(),
                                    HEAL_USER.get()
                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .power(9)
                                    .speed(9)
                                    .range(5, 10)
                                    .durability(15)
                                    .precision(9)
                                    .randomWeight(1)
                            )
                            .addOst(InitSounds.METALLICA_OST)
                            .disableManualControl()
                            .disableStandLeap()
                            .addSummonShout(InitSounds.USER_METALLICA)
                            .build(),

                    ENTITIES,
                    () -> new StandEntityType<MetallicaStandEntity>(MetallicaStandEntity::new, 0F, 0F)
                            .summonSound(InitSounds.STAND_SUMMON)
                            .unsummonSound(InitSounds.STAND_UNSUMMON))
                    .withDefaultStandAttributes();
}
