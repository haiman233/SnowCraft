package io.github.thehrz.snowcraft.object.machines;

import io.github.thehrz.snowcraft.SnowCraft;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thehrz
 */
public class OriginalAutomatedCraftingChamber extends AutomatedCraftingChamber {
    private static final int[] BORDER = new int[]{0, 1, 3, 4, 5, 7, 8, 13, 14, 15, 16, 17, 50, 51, 52, 53};

    private static final int[] BORDER_IN = new int[]{9, 10, 11, 12, 13, 18, 22, 27, 31, 36, 40, 45, 46, 47, 48, 49};

    private static final int[] BORDER_OUT = new int[]{23, 24, 25, 26, 32, 35, 41, 42, 43, 44};

    public static Map<String, ItemStack> recipes = new HashMap<>();
    public static List<ItemStack> blacklist = new ArrayList<>();

    public OriginalAutomatedCraftingChamber(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
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
    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "), (player, j, itemStack, clickAction) -> false);
        }
        for (int i : BORDER_IN) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11), " "), (player, j, itemStack, clickAction) -> false);
        }
        for (int i : BORDER_OUT) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1), " "), (player, j, itemStack, clickAction) -> false);
        }
        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return (cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR);
                }
            });
        }

        preset.addItem(2, new CustomItem(new MaterialData(Material.WORKBENCH), "&e合成蓝本", "", "&b放入合成方式示例(按合成方式摆放)", "&4只能是强化合成台所属的合成公式"), (player, i, itemStack, clickAction) -> false);
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
