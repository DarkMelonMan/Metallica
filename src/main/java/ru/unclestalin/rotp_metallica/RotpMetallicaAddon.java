package ru.unclestalin.rotp_metallica;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.unclestalin.rotp_metallica.init.InitEffects;
import ru.unclestalin.rotp_metallica.init.InitEntities;
import ru.unclestalin.rotp_metallica.init.InitSounds;
import ru.unclestalin.rotp_metallica.init.InitStands;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RotpMetallicaAddon.MOD_ID)
public class RotpMetallicaAddon {
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_metallica";
    private static final Logger LOGGER = LogManager.getLogger();

    public RotpMetallicaAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);
        InitEffects.EFFECTS.register(modEventBus);
    }
    public static Logger getLogger() {
        return LOGGER;
    }
}
