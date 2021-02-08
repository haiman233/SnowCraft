package io.github.thehrz.snowcraft;

import io.github.thehrz.snowcraft.object.machines.OriginalAutomatedCraftingChamber;
import io.github.thehrz.snowcraft.setup.ItemsSetup;
import io.izzel.taboolib.loader.Plugin;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Thehrz
 */
public final class SnowCraft extends Plugin {

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this.getPlugin());
    }

    @Override
    public void onEnable() {
        Iterator<Recipe> recipeIterator = Bukkit.getServer().recipeIterator();

        while (recipeIterator.hasNext()) {
            StringBuilder builder = new StringBuilder();
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();

                Iterator<String> iterator = Arrays.stream(shapedRecipe.getShape()).iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    for (char c : iterator.next().toCharArray()) {
                        if (i > 0) {
                            builder.append(" </slot> ");
                        }
                        if (ingredientMap.get(c) == null) {
                            continue;
                        }
                        builder.append(CustomItemSerializer.serialize(ingredientMap.get(c), CustomItemSerializer.ItemFlag.DATA, CustomItemSerializer.ItemFlag.ITEMMETA_DISPLAY_NAME, CustomItemSerializer.ItemFlag.ITEMMETA_LORE, CustomItemSerializer.ItemFlag.MATERIAL));

                        i++;
                    }
                }

                OriginalAutomatedCraftingChamber.recipes.put(builder.toString(), shapedRecipe.getResult());
            }
        }

        ItemsSetup.setupItems();
    }
}
