package ru.unclestalin.rotp_metallica.util;

import net.minecraft.tags.ItemTags;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import ru.unclestalin.rotp_metallica.RotpMetallicaAddon;

public class IronTag {
    public static class Items {
        public static final Tags.IOptionalNamedTag<Item> IRON_ITEMS = createTag("iron_items");

        private static Tags.IOptionalNamedTag<Item> createTag(String name) {
            return ItemTags.createOptional(new ResourceLocation(RotpMetallicaAddon.MOD_ID, name));
        }
    }
}
