package ru.unclestalin.rotp_metallica.power.impl.stand.type;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.entity.stand.stands.MetallicaStandEntity;
import ru.unclestalin.rotp_metallica.init.InitStands;
import ru.unclestalin.rotp_metallica.util.ModTags;

import javax.annotation.Nonnull;
import java.util.*;

public class MetallicaStandType<T extends StandStats> extends EntityStandType<T> {
    protected MetallicaStandType(AbstractBuilder<?, T> builder) {
        super(builder);
    }

    private static final Random random = new Random();
    private List<CompassData> compassDataList = new ArrayList<>();

    @Override
    public void tickUser(LivingEntity user, IStandPower power) {
        if (user.isAlive() && user.hasEffect(Effects.INVISIBILITY.getEffect())) {
            power.consumeStamina(6F, true);
            if (power.getStamina() <= 0F || !power.isActive())
                user.removeEffect(Effects.INVISIBILITY.getEffect());
            INonStandPower.getNonStandPowerOptional(user).ifPresent(vampirePower -> {
                if (vampirePower.getType() == ModPowers.VAMPIRISM.get() && user.hasEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get())) {
                    user.addEffect(new EffectInstance(ModStatusEffects.SUN_RESISTANCE.get(), 999999, 999999, false, false, false));
                    user.removeEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get());
                }
            });
        }
        if (user.isAlive() && !user.hasEffect(Effects.INVISIBILITY.getEffect()) && user.hasEffect(ModStatusEffects.SUN_RESISTANCE.get())) {
            user.removeEffect(ModStatusEffects.SUN_RESISTANCE.get());
        }
        if (power.isActive()) {
            pullIron(user.level, user);
        }
        trackByCompass((PlayerEntity) user, power);
        int i = 0;
        while (compassDataList.size() > i) {
            CompassData compassData = compassDataList.get(i);
            double range = getStats().getMaxRange();
            if (!power.isActive() || user.distanceTo(compassData.owner) > range) {
                removeLodestoneTags(compassData);
            } else {
                i++;
            }
        }
        if (!user.level.isClientSide() && power.isActive()) {
            World world = user.level;
            double x = user.getX();
            double y = user.getY();
            double z = user.getZ();
            StandStats stats = getStats();
            double range = stats.getMaxRange();
            AxisAlignedBB area = new AxisAlignedBB(x - range, y - range, z - range, x + range + 1.0D, y + range, z + range + 1.0D);
            List<LivingEntity> entitiesInRange = world.getEntitiesOfClass(LivingEntity.class, area);
            for (LivingEntity entity : entitiesInRange) {
                if (entity != user && entity.isAlive() && entity.tickCount % 20 == 0) {
                    float randValue = random.nextFloat();
                    if (isIronItem(entity.getMainHandItem())) {
                        if (randValue > 0.75F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getMainHandItem());
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getOffhandItem())) {
                        if (randValue < 0.15F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getOffhandItem());
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.HEAD))) {
                        if (randValue > 0.15F && randValue < 0.3F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.HEAD));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.CHEST))) {
                        if (randValue > 0.3F && randValue < 0.45F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.CHEST));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.LEGS))) {
                        if (randValue > 0.45F && randValue < 0.6F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.LEGS));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.FEET))) {
                        if (randValue > 0.6F && randValue < 0.75F) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.FEET));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.FEET, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
        super.tickUser(user, power);
    }

    public boolean isIronItem(ItemStack currentItem) {
        return currentItem.getItem().is(ModTags.Items.IRON_ITEMS);
    }

    private void addLodestoneTags(RegistryKey<World> worldKey, BlockPos blockPos, CompoundNBT nbt) {
        nbt.put("LodestonePos", NBTUtil.writeBlockPos(blockPos));
        World.RESOURCE_KEY_CODEC.encodeStart(NBTDynamicOps.INSTANCE, worldKey).resultOrPartial(RotpMetallicaAddon.getLogger()::error).ifPresent((inbt) -> nbt.put("LodestoneDimension", inbt));
        nbt.putBoolean("LodestoneTracked", true);
    }
    private void trackByCompass(PlayerEntity user, IStandPower power) {
        if (power.isActive()) {
            double x = user.getX();
            double y = user.getY();
            double z = user.getZ();
            double range = getStats().getMaxRange();
            AxisAlignedBB area = new AxisAlignedBB(x - range, y - range, z - range, x + range + 1.0D, y + range, z + range + 1.0D);
            List<PlayerEntity> playersInRange = user.level.getEntitiesOfClass(PlayerEntity.class, area);
            for (PlayerEntity player : playersInRange) {

                IStandPower playerPower = IStandPower.getStandPowerOptional(player).orElse(null);
                if (playerPower.getType() == InitStands.METALLICA_STAND.getStandType())
                    continue;

                AxisAlignedBB playerArea = new AxisAlignedBB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range + 1.0D, player.getY() + range, player.getZ() + range + 1.0D);
                List<MetallicaStandEntity> metallicaStands = player.level.getEntitiesOfClass(MetallicaStandEntity.class, playerArea);
                boolean needsToBeTracked = true;
                if (metallicaStands.size() > 1) {
                    for (MetallicaStandEntity metallica : metallicaStands) {
                        if (player.distanceTo(metallica.getUser()) < player.distanceTo(user)) {
                            needsToBeTracked = false;
                            break;
                        }
                    }
                }

                if (ItemStack.isSame(player.getMainHandItem(), new ItemStack(Items.COMPASS, player.getMainHandItem().getCount()))
                        || ItemStack.isSame(player.getOffhandItem(), new ItemStack(Items.COMPASS, player.getOffhandItem().getCount()))) {
                    ItemStack itemstack = ItemStack.isSame(player.getMainHandItem(), new ItemStack(Items.COMPASS, player.getMainHandItem().getCount())) ? player.getMainHandItem() : player.getOffhandItem();
                    if (player.inventory.items.stream().noneMatch((item) ->
                            (ItemStack.isSame(item, new ItemStack(Items.COMPASS, item.getCount())) && item.hasFoil())
                                    && item != player.getMainHandItem() && item != player.getOffhandItem())) {

                        if (needsToBeTracked) {
                            RotpMetallicaAddon.getLogger().info("YES! I AM!");
                            boolean flag = false;
                            CompassData data = new CompassData(player, itemstack);
                            if (!compassDataList.contains(data) && !itemstack.hasTag())
                                flag = true;
                            this.addLodestoneTags(user.level.dimension(), new BlockPos(user.position()), itemstack.getOrCreateTag());
                            if (flag)
                                compassDataList.add(data);
                        } else
                            RotpMetallicaAddon.getLogger().info("NOT AM I!");
                    }
                }
            }
        }
    }

    private void removeLodestoneTags(CompassData data) {
        PlayerEntity owner = data.owner;
        ItemStack itemStack = data.compassItems;
        if (owner.inventory.items.contains(itemStack) || owner.getOffhandItem().sameItem(itemStack)) {
            int count = itemStack.getCount();
            itemStack.shrink(count);
            ItemStack itemStack1 = new ItemStack(Items.COMPASS, count);
            if (!owner.inventory.add(itemStack1)) {
                owner.drop(itemStack1, false);
            }
        }
        compassDataList.remove(data);
    }

    public void pullIron(World worldIn, @Nonnull Entity entityIn) {
        if (!worldIn.isClientSide()) {
            PlayerEntity playerIn = (PlayerEntity) entityIn;
            double x = entityIn.getX();
            double y = entityIn.getY();
            double z = entityIn.getZ();
            StandStats stats = getStats();
            double range = stats.getMaxRange();

            AxisAlignedBB area = new AxisAlignedBB(x - range, y - range, z - range, x + range + 1.0D, y + range, z + range + 1.0D);
            List<ItemEntity> ironItems = worldIn.getEntitiesOfClass(ItemEntity.class, area);
            if (ironItems.size() != 0) {
                for (ItemEntity itemIE : ironItems) {
                    ItemStack currentItem = itemIE.getItem();
                    if (itemIE.isAlive() && isIronItem(currentItem)) {
                        double distance = itemIE.distanceTo(playerIn);
                        Vector3d vec = playerIn.position().subtract(itemIE.getBoundingBox().getCenter())
                                .normalize().scale(0.15);
                        itemIE.setDeltaMovement(distance > 2 ?
                                itemIE.getDeltaMovement().add(vec.scale(1 / distance))
                                : vec.scale(Math.max(distance - 1, 0)));
                        if (distance <= 1F)
                            playerIn.playerTouch(playerIn);
                    }
                }
            } // maybe add pulling iron from iron golems
            List<AbstractMinecartEntity> minecarts = worldIn.getEntitiesOfClass(AbstractMinecartEntity.class, area);
            for (AbstractMinecartEntity minecart : minecarts) {
                if (!minecart.getPassengers().contains(playerIn) && minecart.isAlive()) {
                    double distance = minecart.distanceTo(playerIn);
                    Vector3d vec = playerIn.position().subtract(minecart.getBoundingBox().getCenter())
                            .normalize().scale(0.15);
                    minecart.setDeltaMovement(distance > 2 ?
                            minecart.getDeltaMovement().add(vec.scale(1 / distance))
                            : vec.scale(Math.max(distance - 1, 0)));
                }
            }
        }
    }

    public static class Builder<T extends StandStats> extends EntityStandType.AbstractBuilder<Builder<T>, T> {

        @Override
        protected Builder<T> getThis() {
            return this;
        }

        @Override
        public MetallicaStandType<T> build() {
            return new MetallicaStandType<>(this);
        }

    }
}

class CompassData {
    PlayerEntity owner;
    ItemStack compassItems;

    CompassData(PlayerEntity owner, ItemStack compassItems) {
        this.owner = owner;
        this.compassItems = compassItems;
    }
}

