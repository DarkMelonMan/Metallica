package ru.unclestalin.rotp_metallica.init;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.unclestalin.rotp_metallica.potion.LackOfIron;

import java.util.HashSet;
import java.util.Set;

import static com.github.standobyte.jojo.init.ModStatusEffects.setEffectAsTracked;

@Mod.EventBusSubscriber(
        modid = "rotp_metallica",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class InitEffects {
    public static final DeferredRegister<Effect> EFFECTS;
    public static final RegistryObject<LackOfIron> LACK_OF_IRON;

    public InitEffects() {
    }

    private static final Set<Effect> TRACKED_EFFECTS = new HashSet<>();
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void afterEffectsRegister(RegistryEvent.Register<Effect> event) {
        setEffectAsTracked(LACK_OF_IRON.get());
    }

    public static boolean isEffectTracked(Effect effect) {
        return TRACKED_EFFECTS.contains(effect);
    }

    static {
        EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, "rotp_metallica");
        LACK_OF_IRON = EFFECTS.register("lack_of_iron", () -> {
            return new LackOfIron(EffectType.HARMFUL, 16749568);
        });
    }
}