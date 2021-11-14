package io.github.thehrz.snowcraft


import io.github.thehrz.snowcraft.`object`.machines.OriginalAutomatedCraftingChamber
import io.github.thehrz.snowcraft.setup.ItemsSetup
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.module.metrics.Metrics
import taboolib.platform.BukkitPlugin

/**
 * @author Thehrz
 */
object SnowCraft : Plugin() {
    @Config("config.yml")
    lateinit var config: SecuredFile
        private set

    override fun onDisable() {
        Bukkit.getScheduler().cancelTasks(BukkitPlugin.getInstance())
    }

    override fun onEnable() {
        OriginalAutomatedCraftingChamber.updateBlackList()
        FileWatcher.INSTANCE.addSimpleListener(config.file) {
            OriginalAutomatedCraftingChamber.updateBlackList()
            console().sendMessage("config-reload")
        }
        Metrics(10284, pluginVersion, runningPlatform)
        iteratorRecipes()
        ItemsSetup.setupItems()
    }

    private fun iteratorRecipes() {
        val recipeIterator = Bukkit.getServer().recipeIterator()
        while (recipeIterator.hasNext()) {
            val builder = StringBuilder()
            val recipe = recipeIterator.next()
            if (recipe is ShapedRecipe) {
                val ingredientMap = recipe.ingredientMap
                for (i in 0..2) {
                    var n = 0
                    var row = " "
                    if (recipe.shape.size == 3 || i < recipe.shape.size) {
                        row = recipe.shape[i]
                    }
                    val stringBuilderRow = StringBuilder()
                    for (j in 0..2) {
                        var item: ItemStack? = null
                        if (row.length == 3 || j < row.length) {
                            item = ingredientMap[row[j]]
                        }
                        if (item == null) {
                            n++
                        }
                        stringBuilderRow.append(" </slot> ")
                        stringBuilderRow.append(
                            CustomItemSerializer.serialize(
                                item,
                                CustomItemSerializer.ItemFlag.DATA,
                                CustomItemSerializer.ItemFlag.ITEMMETA_DISPLAY_NAME,
                                CustomItemSerializer.ItemFlag.ITEMMETA_LORE,
                                CustomItemSerializer.ItemFlag.MATERIAL
                            )
                        )
                    }
                    if (n != 3) {
                        builder.append(stringBuilderRow)
                    }
                }
                OriginalAutomatedCraftingChamber.recipes[builder.toString()] = recipe.result
            }
        }
    }
}