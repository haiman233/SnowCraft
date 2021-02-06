package io.github.thehrz.snowcraft.object.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */
public class MagnesiumGenerator extends AGenerator {
    public MagnesiumGenerator(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
    }

    @Override
    public String getInventoryTitle() {
        return "§c镁发电机";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public void registerDefaultRecipes() {
        this.registerFuel(new MachineFuel(32, SlimefunItems.MAGNESIUM_DUST));
    }

    @Override
    public int getEnergyProduction() {
        return 64;
    }
}
