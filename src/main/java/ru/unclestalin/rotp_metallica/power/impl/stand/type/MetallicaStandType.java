package ru.unclestalin.rotp_metallica.power.impl.stand.type;

import com.github.standobyte.jojo.init.ModItems;
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
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;
import ru.unclestalin.rotp_metallica.util.IronTag;

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
        user.setInvisible(user.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()));
        if (user.isAlive() && user.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get())) {
            power.consumeStamina(4.5F, true);
            if (power.getStamina() <= 0F || !power.isActive())
                user.removeEffect(ModStatusEffects.FULL_INVISIBILITY.get());
            INonStandPower.getNonStandPowerOptional(user).ifPresent(vampirePower -> {
                if (vampirePower.getType() == ModPowers.VAMPIRISM.get() && user.hasEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get())) {
                    user.addEffect(new EffectInstance(ModStatusEffects.SUN_RESISTANCE.get(), 999999, 999999, false, false, false));
                    user.removeEffect(ModStatusEffects.VAMPIRE_SUN_BURN.get());
                }
            });
        }
        if (user.isAlive() && !user.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()) && user.hasEffect(ModStatusEffects.SUN_RESISTANCE.get())) {
            user.removeEffect(ModStatusEffects.SUN_RESISTANCE.get());
        }
        if (power.isActive()) {
            pullIron(user.level, user);
        }
        trackByCompass((PlayerEntity) user, power);
        RotpMetallicaAddon.getLogger().info(compassDataList.size());
        int i = 0;
        while (compassDataList.size() > i) {
            CompassData compassData = compassDataList.get(i);
            if (user.level.isClientSide()) {
                RotpMetallicaAddon.getLogger().info(compassData.compassItems);
                RotpMetallicaAddon.getLogger().info("Has tag:");
                RotpMetallicaAddon.getLogger().info(compassData.compassItems.hasTag());
                if (compassData.compassItems.hasTag()) {
                    RotpMetallicaAddon.getLogger().info("Tag:");
                    RotpMetallicaAddon.getLogger().info(compassData.compassItems.getOrCreateTag().getAsString());
                }
                RotpMetallicaAddon.getLogger().info("Power is active:");
                RotpMetallicaAddon.getLogger().info(power.isActive());
            }
            double range = getStats().getMaxRange();
            if (!power.isActive() || user.distanceTo(compassData.owner) > range) {
                removeLodestoneTags(compassData);
                RotpMetallicaAddon.getLogger().info("Tag removed from player's compass");
            } else {
                RotpMetallicaAddon.getLogger().info("Tag wasn't removed");
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
        return currentItem.getItem().is(IronTag.Items.IRON_ITEMS);
    }

    private void addLodestoneTags(RegistryKey<World> worldKey, BlockPos blockPos, CompoundNBT nbt) {
        nbt.put("LodestonePos", NBTUtil.writeBlockPos(blockPos));
        World.RESOURCE_KEY_CODEC.encodeStart(NBTDynamicOps.INSTANCE, worldKey).resultOrPartial(RotpMetallicaAddon.getLogger()::error).ifPresent((inbt) -> {
            nbt.put("LodestoneDimension", inbt);
        });
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
                if (ItemStack.isSame(player.getMainHandItem(), Items.COMPASS.getDefaultInstance()) // добавить условие другого стэндюзера с металликой
                        || ItemStack.isSame(player.getOffhandItem(), Items.COMPASS.getDefaultInstance())) {
                    ItemStack itemstack = ItemStack.isSame(player.getMainHandItem(), Items.COMPASS.getDefaultInstance()) ? player.getMainHandItem() : player.getOffhandItem();
//                if (Math.sqrt(NBTUtil.readBlockPos(itemstack.getOrCreateTag().getCompound("LodestonePos")).distSqr(x, y, z, false)) > range){
//                    prevNBT = itemstack.getOrCreateTag();
//                    previousCompassNBTTag = prevNBT;
//                }
                    if (player.inventory.items.stream().noneMatch((item) ->
                            (ItemStack.isSame(item, Items.COMPASS.getDefaultInstance()) && item.hasFoil())
                                    && item != player.getMainHandItem() && item != player.getOffhandItem())) {
                        boolean flag = false;
                        if (!player.abilities.instabuild && itemstack.getCount() == 1) {
                            CompassData data = new CompassData(player, itemstack);
                            if (!compassDataList.contains(data) && !itemstack.hasTag())
                                flag = true;
                            this.addLodestoneTags(user.level.dimension(), new BlockPos(user.position()), itemstack.getOrCreateTag());
                            if (flag) {
                                compassDataList.add(data);
                                RotpMetallicaAddon.getLogger().info("Compass tag added");
                            }
                        } else {
                            ItemStack itemstack1 = new ItemStack(Items.COMPASS, 1);
                            CompoundNBT compoundnbt = itemstack.hasTag() ? itemstack.getTag().copy() : new CompoundNBT();
                            itemstack1.setTag(compoundnbt);
                            if (!player.abilities.instabuild) {
                                itemstack.shrink(1);
                            }
                            this.addLodestoneTags(user.level.dimension(), new BlockPos(user.position()), compoundnbt);
                            if (!player.inventory.add(itemstack1)) {
                                player.drop(itemstack1, false);
                            }
                            compassDataList.add(new CompassData(player, itemstack1));
                            RotpMetallicaAddon.getLogger().info("Compass tag added");
                        }
                    }
                }
            }
        }
    }

    private void removeLodestoneTags(CompassData data) {
        PlayerEntity owner = data.owner;
        ItemStack itemStack = data.compassItems;
        RotpMetallicaAddon.getLogger().info("Owner has compass items:");
        RotpMetallicaAddon.getLogger().info(owner.inventory.items.contains(itemStack));
        if (owner.inventory.items.contains(itemStack)) {
            itemStack.shrink(1);
            ItemStack itemStack1 = new ItemStack(Items.COMPASS, 1);
            if (!owner.inventory.add(itemStack1)) {
                owner.drop(itemStack1, false);
            }
            RotpMetallicaAddon.getLogger().info("Compass tag removed");
        }
        compassDataList.remove(data);
//        double x = user.getX();
//        double y = user.getY();
//        double z = user.getZ();
//        double range = getStats().getMaxRange() * 1.5;
//        AxisAlignedBB area = new AxisAlignedBB(x - range, y - range, z - range, x + range + 1.0D, y + range, z + range + 1.0D);
//        List<PlayerEntity> playersInRange = user.level.getEntitiesOfClass(PlayerEntity.class, area);
//        for (PlayerEntity player : playersInRange) {
//            if (player)
//        }
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
            } // maybe add pulling iron golems
            List<AbstractMinecartEntity> minecarts = worldIn.getEntitiesOfClass(AbstractMinecartEntity.class, area);
            if (minecarts.size() != 0) {
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

