package ru.unclestalin.rotp_metallica.power.impl.stand.type;

import com.github.standobyte.jojo.init.ModItems;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.NonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MetallicaStandType<T extends StandStats> extends EntityStandType<T> {
    protected MetallicaStandType(AbstractBuilder<?, T> builder) {
        super(builder);
    }

    private static final Random random = new Random();

    private static final List<Item> ironItems = new ArrayList<>(Arrays.asList(Items.IRON_NUGGET, Items.IRON_INGOT,
            Items.IRON_ORE, Items.COMPASS, Items.CHAIN, Items.CAULDRON, Items.HOPPER, Items.MINECART,
            Items.HOPPER_MINECART, Items.CHEST_MINECART, Items.FURNACE_MINECART, Items.TNT_MINECART,
            Items.COMMAND_BLOCK_MINECART, Items.IRON_DOOR, Items.IRON_TRAPDOOR, Items.IRON_HORSE_ARMOR,
            Items.LANTERN, Items.IRON_BLOCK, Items.ANVIL, Items.CHIPPED_ANVIL, Items.DAMAGED_ANVIL,
            Items.STONECUTTER, Items.BLAST_FURNACE, Items.PISTON, Items.GRINDSTONE, Items.SMITHING_TABLE,
            Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.TRIPWIRE_HOOK, Items.BUCKET, Items.WATER_BUCKET, Items.LAVA_BUCKET,
            Items.COD_BUCKET, Items.MILK_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET, Items.TROPICAL_FISH_BUCKET,
            Items.RAIL, Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Items.POWERED_RAIL, ModItems.KNIFE.get(),
            ModItems.METEORIC_INGOT.get(), ModItems.METEORIC_IRON.get(), ModItems.METEORIC_SCRAP.get(),
            ModItems.CLACKERS.get(), ModItems.ROAD_ROLLER.get(), ModItems.WALKMAN.get(), ModItems.BREATH_CONTROL_MASK.get(),
            ModItems.CASSETTE_BLANK.get(), ModItems.CASSETTE_RECORDED.get(), Items.IRON_SWORD, Items.IRON_AXE,
            Items.IRON_SHOVEL, Items.SHEARS, Items.IRON_HOE, Items.IRON_PICKAXE, ModItems.BLADE_HAT.get(),
            ModItems.IRON_SLEDGEHAMMER.get(), ModItems.STAND_ARROW.get(), ModItems.STAND_ARROW_BEETLE.get(),
            Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.CHAINMAIL_HELMET,
            Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS, Items.SHIELD, Items.FLINT_AND_STEEL,
            Items.CROSSBOW));

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
        if (user.isAlive() && !user.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()) && user.hasEffect(ModStatusEffects.SUN_RESISTANCE.get()))
            user.removeEffect(ModStatusEffects.SUN_RESISTANCE.get());
        if (power.isActive())
            pullIron(user.level, user);
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
                if (entity != user && entity.isAlive()) {
                    float randValue = random.nextFloat();
                    if (isIronItem(entity.getMainHandItem())) {
                        if (randValue > 0.75F && entity.tickCount % 20 == 0) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getMainHandItem());
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getOffhandItem())) {
                        if (randValue < 0.15F && entity.tickCount % 20 == 0) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getOffhandItem());
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.HEAD))) {
                        if (randValue > 0.15F && randValue < 0.3F && entity.tickCount % 20 == 0) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.HEAD));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.CHEST))) {
                        if (randValue > 0.3F && randValue < 0.45F && entity.tickCount % 20 == 0) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.CHEST));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.CHEST, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.LEGS))) {
                        if (randValue > 0.45F && randValue < 0.6F && entity.tickCount % 20 == 0) {
                            ItemEntity item = new ItemEntity(world, entity.getX(), entity.getY() + 0.5, entity.getZ(), entity.getItemBySlot(EquipmentSlotType.LEGS));
                            item.setPickUpDelay(20);
                            world.addFreshEntity(item);
                            entity.setItemSlot(EquipmentSlotType.LEGS, ItemStack.EMPTY);
                        }
                    } else if (isIronItem(entity.getItemBySlot(EquipmentSlotType.FEET))) {
                        if (randValue > 0.6F && randValue < 0.75F && entity.tickCount % 20 == 0) {
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
        for (Item item : ironItems) {
            if (ItemStack.isSameIgnoreDurability(currentItem, (item.getDefaultInstance()))) {
                return true;
            }
        }
        return false;

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
            }
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
