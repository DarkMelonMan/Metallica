package ru.unclestalin.rotp_metallica.client.render.entity.layerrenderer;

import com.github.standobyte.jojo.client.ClientUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.init.InitEffects;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class LackOfIronLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final Map<PlayerRenderer, LackOfIronLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> RENDERER_LAYERS = new HashMap<>();

    public LackOfIronLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
        if (renderer instanceof PlayerRenderer) {
            RENDERER_LAYERS.put((PlayerRenderer) renderer, (LackOfIronLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>) this);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight,
                       T entity, float walkAnimPos, float walkAnimSpeed, float partialTick,
                       float ticks, float headYRotation, float headXRotation) {
        if (!entity.isInvisible()) {
            M model = getParentModel();
            ResourceLocation texture = getTexture(model, entity);
            if (texture == null) return;

            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(texture));
            model.renderToBuffer(matrixStack, vertexBuilder, ClientUtil.MAX_MODEL_LIGHT, LivingRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Nullable
    private ResourceLocation getTexture(EntityModel<?> model, LivingEntity entity) {
        EffectInstance lackOfIron = entity.getEffect(InitEffects.LACK_OF_IRON.get());
        if (lackOfIron != null) {
            int level = 0;
            if (entity.level.getLightEmission(new BlockPos(entity.position())) < 8) {
                level = 1;
            }
            TextureSize size = TextureSize.getClosestTexSize(model);
            return LAYER_TEXTURES.get(size)[level];
        }
        return null;
    }


    private static final Map<TextureSize, ResourceLocation[]> LAYER_TEXTURES = Util.make(new EnumMap<>(TextureSize.class), map -> {
        map.put(TextureSize._64x32, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t64x32/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t64x32/2.png"),
        });
        map.put(TextureSize._64x64, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t64x64/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t64x64/2.png"),
        });
        map.put(TextureSize._128x64, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t128x64/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t128x64/2.png"),
        });
        map.put(TextureSize._128x128, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t128x128/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t128x128/2.png"),
        });
        map.put(TextureSize._256x128, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t256x128/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t256x128/2.png"),
        });
        map.put(TextureSize._256x256, new ResourceLocation[] {
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t256x256/1.png"),
                new ResourceLocation(RotpMetallicaAddon.MOD_ID, "textures/entity/biped/layer/lack_of_iron/t256x256/2.png"),
        });
    });

    private static enum TextureSize {
        _64x32(6, 5),
        _64x64(6, 6),
        _128x64(7, 6),
        _128x128(7, 7),
        _256x128(8, 7),
        _256x256(8, 8);

        private final int widthLog2;
        private final int heightLog2;

        private TextureSize(int widthLog2, int heightLog2) {
            this.widthLog2 = widthLog2;
            this.heightLog2 = heightLog2;
        }

        private static TextureSize getClosestTexSize(Model model) {
            int widthLog = MathHelper.ceillog2(model.texWidth);
            int heightLog = MathHelper.ceillog2(model.texHeight);

            widthLog = MathHelper.clamp(widthLog, 6, 8);
            heightLog = MathHelper.clamp(heightLog, widthLog - 1, widthLog);
            int i = (widthLog - 6) * 2;
            if (heightLog == widthLog) i++;

            return TextureSize.values()[i];
        }
    }

    public static void renderFirstPerson(HandSide side, MatrixStack matrixStack,
                                         IRenderTypeBuffer buffer, int light, AbstractClientPlayerEntity player) {
        EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (renderer instanceof PlayerRenderer) {
            PlayerRenderer playerRenderer = (PlayerRenderer) renderer;
            if (RENDERER_LAYERS.containsKey(playerRenderer)) {
                LackOfIronLayer<?, ?> layer = RENDERER_LAYERS.get(playerRenderer);
                if (layer != null) {
                    layer.renderHandFirstPerson(side, matrixStack,
                            buffer, light, player, playerRenderer);
                }
            }
        }
    }

    private void renderHandFirstPerson(HandSide side, MatrixStack matrixStack,
                                       IRenderTypeBuffer buffer, int light, AbstractClientPlayerEntity player,
                                       PlayerRenderer playerRenderer) {
        if (player.isSpectator()) return;

        PlayerModel<AbstractClientPlayerEntity> model = playerRenderer.getModel();
        ResourceLocation texture = getTexture(model, player);
        if (texture == null) return;

        ClientUtil.setupForFirstPersonRender(model, player);
        IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.entityTranslucent(texture));
        ModelRenderer arm = ClientUtil.getArm(model, side);
        ModelRenderer armOuter = ClientUtil.getArmOuter(model, side);
        arm.xRot = 0.0F;
        arm.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
        armOuter.xRot = 0.0F;
        armOuter.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
    }
}
