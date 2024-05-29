package ru.unclestalin.rotp_metallica.entity.stand.stands;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.init.ModItems;
import net.minecraft.data.RecipeProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class MetallicaStandEntity extends StandEntity {
    
    public MetallicaStandEntity(StandEntityType<MetallicaStandEntity> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (this.getUser().hasEffect(Effects.INVISIBILITY)){
            this.getUserPower().consumeStamina(4.5F, true);
        }
        pullIron(this.getUser().level, this.getUser());
        super.tick();
    }

    public void pullIron(World worldIn, @Nonnull Entity entityIn) {
        if (!worldIn.isClientSide()) {
                PlayerEntity playerIn = (PlayerEntity)entityIn;
                double x = entityIn.getX();
                double y = entityIn.getY();
                double z = entityIn.getZ();
                double range = this.getMaxRange();
                AxisAlignedBB area = new AxisAlignedBB(x - range, y - range, z - range, x + range + 1.0D, y + range, z + range + 1.0D);
                List<ItemEntity> ironItems = worldIn.getEntitiesOfClass(ItemEntity.class, area);
                if (ironItems.size() != 0) {
                    for (ItemEntity itemIE : ironItems) {
                        ItemStack currentItem = itemIE.getItem();
                        boolean isIronItem = ItemStack.isSame(currentItem, Items.IRON_NUGGET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_INGOT.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_BARS.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_ORE.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.COMPASS.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.CHAIN.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.CAULDRON.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.HOPPER.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.HOPPER_MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.CHEST_MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.FURNACE_MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.TNT_MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.COMMAND_BLOCK_MINECART.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_DOOR.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_TRAPDOOR.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_HORSE_ARMOR.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.LANTERN.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.IRON_BLOCK.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.ANVIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.CHIPPED_ANVIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.DAMAGED_ANVIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.STONECUTTER.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.BLAST_FURNACE.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.PISTON.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.GRINDSTONE.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.SMITHING_TABLE.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.HEAVY_WEIGHTED_PRESSURE_PLATE.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.TRIPWIRE_HOOK.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.WATER_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.LAVA_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.COD_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.MILK_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.PUFFERFISH_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.SALMON_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.TROPICAL_FISH_BUCKET.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.RAIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.ACTIVATOR_RAIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.DETECTOR_RAIL.getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, Items.POWERED_RAIL.getDefaultInstance()) ||


                                ItemStack.isSame(currentItem, ModItems.KNIFE.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.METEORIC_INGOT.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.METEORIC_IRON.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.METEORIC_SCRAP.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.CLACKERS.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.ROAD_ROLLER.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.WALKMAN.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.BREATH_CONTROL_MASK.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.CASSETTE_BLANK.get().getDefaultInstance()) ||
                                ItemStack.isSame(currentItem, ModItems.CASSETTE_RECORDED.get().getDefaultInstance()) ||

                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_SWORD.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_AXE.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_SHOVEL.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.SHEARS.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_HOE.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_PICKAXE.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, ModItems.BLADE_HAT.get().getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, ModItems.IRON_SLEDGEHAMMER.get().getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, ModItems.STAND_ARROW.get().getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, ModItems.STAND_ARROW_BEETLE.get().getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_HELMET.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_CHESTPLATE.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_LEGGINGS.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.IRON_BOOTS.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.CHAINMAIL_HELMET.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.CHAINMAIL_CHESTPLATE.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.CHAINMAIL_LEGGINGS.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.CHAINMAIL_BOOTS.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.SHIELD.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.FLINT_AND_STEEL.getDefaultInstance()) ||
                                ItemStack.isSameIgnoreDurability(currentItem, Items.CROSSBOW.getDefaultInstance());

                        if (itemIE.isAlive() && isIronItem) {
                            itemIE.setNoPickUpDelay();
                            double distance = itemIE.distanceTo(playerIn);
                            Vector3d vec = playerIn.position().subtract(itemIE.getBoundingBox().getCenter())
                                    .normalize().scale(0.15 * this.getStandEfficiency());
                            itemIE.setDeltaMovement(itemIE.getDeltaMovement().add(vec.scale(1 / distance)));
                        }
                }
            }
        }
    }
}
