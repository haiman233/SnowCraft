package io.github.thehrz.snowcraft.object.machines;

import io.github.thehrz.snowcraft.SnowCraft;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thehrz
 */
public class OriginalAutomatedCraftingChamber extends AutomatedCraftingChamber {
    public static Map<String, ItemStack> recipes = new HashMap<>();
    public static List<ItemStack> blacklist = new ArrayList<>();

    public OriginalAutomatedCraftingChamber(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
    }

    @Override
    public int getEnergyConsumption() {
        return 32;
    }

    public static void updateBlackList() {
        blacklist.clear();
        SnowCraft.getConf().get("OriginalAutomatedCraftingChamber.blacklist");
        for (String blacklist : SnowCraft.getConf().getStringList("OriginalAutomatedCraftingChamber.blacklist")) {
            if (Material.getMaterial(blacklist) != null) {
                OriginalAutomatedCraftingChamber.blacklist.add(new ItemStack(Material.getMaterial(blacklist)));
            }
        }
    }

    @Override
    protected void tick(Block b) {
        if ("false".equals(BlockStorage.getLocationInfo(b.getLocation(), "enabled")) || ChargableBlock.getCharge(b) < getEnergyConsumption()) {
            return;
        }
        BlockMenu menu = BlockStorage.getInventory(b);
        StringBuilder builder = new StringBuilder();
        for (int x = 1, m = 0; x <= 3; x++) {
            int n = 0;
            StringBuilder row = new StringBuilder();
            for (int y = 1; y <= 3; y++, m++) {
                ItemStack item = menu.getItemInSlot(getInputSlots()[m]);
                if (item != null && item.getAmount() == 1) {
                    return;
                }
                if (item == null) {
                    n++;
                }
                row.append(" </slot> ");
                row.append(CustomItemSerializer.serialize(item, CustomItemSerializer.ItemFlag.DATA, CustomItemSerializer.ItemFlag.ITEMMETA_DISPLAY_NAME, CustomItemSerializer.ItemFlag.ITEMMETA_LORE, CustomItemSerializer.ItemFlag.MATERIAL));
            }
            if (n != 3) {
                builder.append(row);
            }
        }

        String input = builder.toString();
        if (OriginalAutomatedCraftingChamber.recipes.containsKey(input)) {
            final ItemStack output = OriginalAutomatedCraftingChamber.recipes.get(input).clone();
            if (!blacklist.contains(output)) {
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
}
