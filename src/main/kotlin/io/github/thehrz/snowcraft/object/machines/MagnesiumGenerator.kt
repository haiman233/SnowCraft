package io.github.thehrz.snowcraft.`object`.machines

import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems
import me.mrCookieSlime.Slimefun.Objects.Category
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineFuel
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Thehrz
 */
class MagnesiumGenerator(
    category: Category?,
    item: ItemStack?,
    id: String?,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : AGenerator(category, item, id, recipeType, recipe) {
    override fun getInventoryTitle() = "§c镁发电机"

    override fun getProgressBar() = ItemStack(Material.FLINT_AND_STEEL)

    override fun registerDefaultRecipes() {
        this.registerFuel(MachineFuel(32, SlimefunItems.MAGNESIUM_DUST))
    }

    override fun getEnergyProduction() = 64
}