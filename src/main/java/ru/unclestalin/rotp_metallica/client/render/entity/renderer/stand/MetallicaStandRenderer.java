package ru.unclestalin.rotp_metallica.client.render.entity.renderer.stand;

import com.github.standobyte.jojo.client.render.entity.model.stand.StandModelRegistry;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.client.render.entity.model.stand.MetallicaStandModel;
import ru.unclestalin.rotp_metallica.entity.stand.stands.MetallicaStandEntity;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class MetallicaStandRenderer extends StandEntityRenderer<MetallicaStandEntity, MetallicaStandModel> {
    
    public MetallicaStandRenderer(EntityRendererManager renderManager) {
        super(renderManager,
                StandModelRegistry.registerModel(new ResourceLocation(RotpMetallicaAddon.MOD_ID, "metallica"), MetallicaStandModel::new),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/stand/metallica.png"), 0);
    }
}
