package io.github.thehrz.snowcraft.`object`.machines

import io.github.thehrz.snowcraft.SnowCraft
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItemSerializer
import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Objects.Category
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.AutomatedCraftingChamber
import me.mrCookieSlime.Slimefun.api.BlockStorage
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import java.util.*

/**
 * @author Thehrz
 */
class OriginalAutomatedCraftingChamber(
    category: Category?,
    item: ItemStack?,
    name: String?,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : AutomatedCraftingChamber(category, item, name, recipeType, recipe) {
    override fun constructMenu(preset: BlockMenuPreset) {
        for (i in intArrayOf(0, 1, 3, 4, 5, 7, 8, 13, 14, 15, 16, 17, 50, 51, 52, 53)) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        for (i in intArrayOf(9, 10, 11, 12, 13, 18, 22, 27, 31, 36, 40, 45, 46, 47, 48, 49)) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 11.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        for (i in intArrayOf(23, 24, 25, 26, 32, 35, 41, 42, 43, 44)) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 1.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        for (i in outputSlots) {
            preset.addMenuClickHandler(i, object : ChestMenu.AdvancedMenuClickHandler {
                override fun onClick(p: Player, slot: Int, cursor: ItemStack, action: ClickAction): Boolean {
                    return false
                }

                override fun onClick(
                    e: InventoryClickEvent,
                    p: Player,
                    slot: Int,
                    cursor: ItemStack,
                    action: ClickAction
                ): Boolean {
                    return cursor.type == null || cursor.type == Material.AIR
                }
            })
        }
        preset.addItem(
            2,
            CustomItem(MaterialData(Material.WORKBENCH), "&e合成蓝本", "", "&b放入合成方式示例(按合成方式摆放)", "&4只能是强化合成台所属的合成公式")
        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
    }

    override fun getEnergyConsumption(): Int = 32

    override fun tick(b: Block) {
        if ("false" == BlockStorage.getLocationInfo(b.location, "enabled") || ChargableBlock.getCharge(b) < energyConsumption) {
            return
        }
        val menu = BlockStorage.getInventory(b)
        val builder = StringBuilder()
        var x = 1
        var m = 0
        while (x <= 3) {
            var n = 0
            val row = StringBuilder()
            var y = 1
            while (y <= 3) {
                val item: ItemStack? = menu.getItemInSlot(inputSlots[m])
                if (item != null && item.amount == 1) {
                    return
                }
                if (item == null) {
                    n++
                }
                row.append(" </slot> ")
                row.append(
                    CustomItemSerializer.serialize(
                        item,
                        CustomItemSerializer.ItemFlag.DATA,
                        CustomItemSerializer.ItemFlag.ITEMMETA_DISPLAY_NAME,
                        CustomItemSerializer.ItemFlag.ITEMMETA_LORE,
                        CustomItemSerializer.ItemFlag.MATERIAL
                    )
                )
                y++
                m++
            }
            if (n != 3) {
                builder.append(row)
            }
            x++
        }
        val input = builder.toString()
        if (recipes.containsKey(input)) {
            val output = recipes[input]!!
                .clone()
            if (!blacklist.contains(output)) {
                if (this.fits(b, arrayOf(output))) {
                    this.pushItems(b, arrayOf(output))
                    ChargableBlock.addCharge(b, -energyConsumption)
                    for (k in 0..8) {
                        if (menu.getItemInSlot(inputSlots[k]) != null) {
                            menu.replaceExistingItem(
                                inputSlots[k],
                                InvUtils.decreaseItem(menu.getItemInSlot(inputSlots[k]), 1)
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        var recipes: MutableMap<String, ItemStack> = mutableMapOf()
        var blacklist: MutableList<ItemStack> = ArrayList()

        fun updateBlackList() {
            blacklist.clear()
            for (blacklist in SnowCraft.config.getStringList("OriginalAutomatedCraftingChamber.blacklist")) {
                if (Material.getMaterial(blacklist) != null) {
                    this.blacklist.add(ItemStack(Material.getMaterial(blacklist)))
                }
            }
        }
    }
}