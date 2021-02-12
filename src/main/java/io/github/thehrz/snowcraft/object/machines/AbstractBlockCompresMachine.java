package io.github.thehrz.snowcraft.object.machines;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */
public abstract class AbstractBlockCompresMachine extends AContainer {
    private final String id;

    public AbstractBlockCompresMachine(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
        this.id = id;
    }


    @Override
    public String getInventoryTitle() {
        return "§a方块压缩机";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.PISTON_BASE);
    }

    @Override
    public void registerDefaultRecipes() {
        this.registerRecipe(5, new ItemStack[]{new ItemStack(Material.GRAVEL)}, new ItemStack[]{new ItemStack(Material.NETHERRACK, 2)});
        this.registerRecipe(3, new ItemStack[]{new ItemStack(Material.PACKED_ICE)}, new ItemStack[]{new ItemStack(Material.SNOW_BLOCK)});
    }

    @Override
    public String getMachineIdentifier() {
        return this.id;
    }

}
