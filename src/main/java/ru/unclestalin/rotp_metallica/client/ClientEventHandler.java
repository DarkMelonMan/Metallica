package ru.unclestalin.rotp_metallica.client;

import com.github.standobyte.jojo.capability.entity.EntityUtilCapProvider;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonPowerType;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mc.reflection.ClientReflection;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.client.render.entity.layerrenderer.LackOfIronLayer;
import ru.unclestalin.rotp_metallica.entity.stand.stands.MetallicaStandEntity;
import ru.unclestalin.rotp_metallica.init.InitEffects;
import ru.unclestalin.rotp_metallica.init.InitStands;

import java.util.List;
import java.util.OptionalInt;

public class ClientEventHandler {
    private static ClientEventHandler instance = null;

    private final Minecraft mc;

    private Timer clientTimer;

    private ClientEventHandler(Minecraft mc) {
        this.mc = mc;
        this.clientTimer = ClientReflection.getTimer(mc);
    }

    public static void init(Minecraft mc) {
        if (instance == null) {
            instance = new ClientEventHandler(mc);
            MinecraftForge.EVENT_BUS.register(instance);
        }
    }

    private boolean modPostedEvent = false;

    @SuppressWarnings("resource")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderHand(RenderHandEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (!event.isCanceled() && !modPostedEvent && event.getHand() == Hand.MAIN_HAND && !player.isInvisible() && player.getItemInHand(Hand.MAIN_HAND) == ItemStack.EMPTY) {
            if (player.hasEffect(InitEffects.LACK_OF_IRON.get())) {
                event.setCanceled(true);
                renderHand(Hand.MAIN_HAND, event.getMatrixStack(), event.getBuffers(), event.getLight(),
                        event.getPartialTicks(), event.getInterpolatedPitch(), player);
            }
        }
    }

    private void renderHand(Hand hand, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
                            float partialTick, float interpolatedPitch, LivingEntity entity) {
        FirstPersonRenderer renderer = mc.getItemInHandRenderer();
        ClientPlayerEntity player = mc.player;
        Hand swingingArm = MoreObjects.firstNonNull(player.swingingArm, Hand.MAIN_HAND);
        float swingProgress = swingingArm == hand ? player.getAttackAnim(partialTick) : 0.0F;
        float equipProgress = hand == Hand.MAIN_HAND ?
                1.0F - MathHelper.lerp(partialTick, ClientReflection.getMainHandHeightPrev(renderer), ClientReflection.getMainHandHeight(renderer))
                : 1.0F - MathHelper.lerp(partialTick, ClientReflection.getOffHandHeightPrev(renderer), ClientReflection.getOffHandHeight(renderer));

        modPostedEvent = true;
        if (!ForgeHooksClient.renderSpecificFirstPersonHand(hand,
                matrixStack, buffer, light,
                partialTick, interpolatedPitch,
                swingProgress, equipProgress, entity.getItemInHand(hand))) {
            HandSide handSide = MCUtil.getHandSide(player, hand);

            matrixStack.pushPose();
            ClientReflection.renderPlayerArm(matrixStack, buffer, light, equipProgress,
                    swingProgress, handSide, renderer);
            LackOfIronLayer.renderFirstPerson(handSide, matrixStack, buffer, light, player);
            matrixStack.popPose();
        }
        modPostedEvent = false;
    }
}