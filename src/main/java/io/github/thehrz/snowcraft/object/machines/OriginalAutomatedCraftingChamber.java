package io.github.thehrz.snowcraft.object.machines;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Thehrz
 */
public class OriginalAutomatedCraftingChamber extends AutomatedCraftingChamber {
    public static Map<String, ItemStack> recipes = new HashMap<>();

    public OriginalAutomatedCraftingChamber(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public int getEnergyConsumption() {
        return 32;
    }

    @Override
    protected void tick(Block b) {
        if ("false".equals(BlockStorage.getLocationInfo(b.getLocation(), "enabled")) || ChargableBlock.getCharge(b) < getEnergyConsumption()) {
            return;
        }
        BlockMenu menu = BlockStorage.getInventory(b);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (int j = 0; j < 9; j++) {
            if (i > 0) {
                builder.append(" </slot> ");
            }
            ItemStack item = menu.getItemInSlot(getInputSlots()[j]);
            if (item != null && item.getAmount() == 1) {
                return;
            }
            if (item == null) {
                continue;
            }
            builder.append(CustomItemSerializer.serialize(item, CustomItemSerializer.ItemFlag.DATA, CustomItemSerializer.ItemFlag.ITEMMETA_DISPLAY_NAME, CustomItemSerializer.ItemFlag.ITEMMETA_LORE, CustomItemSerializer.ItemFlag.MATERIAL));
            i++;
        }
        String input = builder.toString();
        if (OriginalAutomatedCraftingChamber.recipes.containsKey(input)) {
            final ItemStack output = OriginalAutomatedCraftingChamber.recipes.get(input).clone();
            if (this.fits(b, new ItemStack[]{output})) {
                this.pushItems(b, new ItemStack[]{output});
                ChargableBlock.addCharge(b, -this.getEnergyConsumption());
                for (int k = 0; k < 9; ++k) {
                    if (menu.getItemInSlot(this.getInputSlots()[k]) != null) {
                        menu.replaceExistingItem(this.getInputSlots()[k], InvUtils.decreaseItem(menu.getItemInSlot(this.getInputSlots()[k]), 1));
                    }
                }
            }
        }
    }
}
