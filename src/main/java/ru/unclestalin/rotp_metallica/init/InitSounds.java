package ru.unclestalin.rotp_metallica.init;

import java.util.function.Supplier;

import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpMetallicaAddon.MOD_ID);
    
    public static final RegistryObject<SoundEvent> USER_METALLICA = SOUNDS.register("risotto_nero_metallica",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "risotto_nero_metallica")));

    public static final RegistryObject<SoundEvent> USER_KNIFE_THROW = SOUNDS.register("risotto_nero_knife_throw",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "risotto_nero_knife_throw")));

    public static final RegistryObject<SoundEvent> USER_KNIVES_THROW = SOUNDS.register("risotto_nero_knives_throw",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "risotto_nero_knives_throw")));

    public static final RegistryObject<SoundEvent> USER_INVISIBILITY = SOUNDS.register("risotto_nero_invisibility",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "risotto_nero_invisibility")));

    public static final RegistryObject<SoundEvent> INVISIBILITY_ON = SOUNDS.register("metallica_invisibility_on",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica_invisibility_on")));

    public static final RegistryObject<SoundEvent> INVISIBILITY_OFF = SOUNDS.register("metallica_invisibility_off",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica_invisibility_off")));

    public static final RegistryObject<SoundEvent> CREATE_BLADES_IN_VICTIM = SOUNDS.register("risotto_nero_create_blades_in_victim",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "risotto_nero_create_blades_in_victim")));

    public static final RegistryObject<SoundEvent> METALLICA_SUMMON_STAND = SOUNDS.register("metallica_summon_stand",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica_summon_stand")));

    public static final RegistryObject<SoundEvent> METALLICA_UNSUMMON_STAND = SOUNDS.register("metallica_unsummon_stand",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica_unsummon_stand")));

    public static final RegistryObject<SoundEvent> HEAL = SOUNDS.register("heal",
            () -> new SoundEvent(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "heal")));

    public static final Supplier<SoundEvent> STAND_SUMMON = METALLICA_SUMMON_STAND;

    public static final Supplier<SoundEvent> STAND_UNSUMMON = METALLICA_UNSUMMON_STAND;

    static final OstSoundList METALLICA_OST = new OstSoundList(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica_ost"), SOUNDS);

}
