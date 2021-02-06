package io.github.thehrz.snowcraft.setup;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */

public class CategoriesSetup {

    public static Category SnowCraft_Technology = null;
    public static Category SnowCraft_Misc = null;

    static {
        try {
            SnowCraft_Technology = new Category(new CustomItem(new ItemStack(Material.SNOW_BLOCK), "§a白雪工艺-§b科技", "", "§a> 点击打开"), 4);
            SnowCraft_Misc = new Category(new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRmNzU1NWQwNWY1MGZhMTMzYjZhMjEwMTE3NDIzNDY0MDFkZjczNDM5OWQ1YWE2YzI5ODgwYmIxZTM1YjkzZCJ9fX0="), "§a白雪工艺-§b杂项", "", "§a> 点击打开"), 4);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
