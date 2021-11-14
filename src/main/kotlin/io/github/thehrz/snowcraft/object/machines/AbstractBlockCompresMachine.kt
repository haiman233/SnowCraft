package io.github.thehrz.snowcraft.`object`.machines

import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Objects.Category
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText

/**
 * @author Thehrz
 */
abstract class AbstractBlockCompresMachine(
    category: Category?,
    item: ItemStack?,
    id: String,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : AContainer(category, item, id, recipeType, recipe) {
    override fun registerDefaultRecipes() {
        registerRecipe(5, arrayOf(ItemStack(Material.GRAVEL)), arrayOf(ItemStack(Material.NETHERRACK, 2)))
        registerRecipe(3, arrayOf(ItemStack(Material.PACKED_ICE)), arrayOf(ItemStack(Material.SNOW_BLOCK)))
    }

    override fun getInventoryTitle() = console().asLangText("block-compres-machine")

    override fun getProgressBar() = ItemStack(Material.PISTON_BASE)

    override fun getMachineIdentifier() = id!!
}