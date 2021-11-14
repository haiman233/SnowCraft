package io.github.thehrz.snowcraft.`object`.machines

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler
import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Objects.Category
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import me.mrCookieSlime.Slimefun.SlimefunStartup
import me.mrCookieSlime.Slimefun.api.BlockStorage
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

/**
 * @author Thehrz
 */
class IronGolemAssembler(
    category: Category?,
    item: ItemStack?,
    name: String?,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : SlimefunItem(category, item, name, recipeType, recipe) {
    private fun constructMenu(preset: BlockMenuPreset) {
        for (i in BORDER) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        for (i in BORDER_1) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 12.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        for (i in BORDER_2) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 0.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        preset.addItem(
            1,
            CustomItem(ItemStack(Material.PUMPKIN, 1, 1.toShort()), "§7南瓜槽", "", "§r这个槽位用于放置南瓜")
        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        preset.addItem(
            7,
            CustomItem(MaterialData(Material.IRON_BLOCK), "§7铁块槽", "", "§r这个槽位用于放置铁块")
        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        preset.addItem(
            13,
            CustomItem(MaterialData(Material.WATCH), "§7冷却: §b30 秒", "", "§r这个机器需要半分钟的时间来作运转准备", "§r请耐心等待!")
        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
    }

    val inventoryTitle: String
        get() = "§5铁傀儡组装机"

    val inputSlots: IntArray
        get() = intArrayOf(19, 28, 25, 34)

    val ironGolemPumpkinSlots: IntArray
        get() = intArrayOf(19, 28)

    val ironBlockSlots: IntArray
        get() = intArrayOf(25, 34)

    override fun register(slimefun: Boolean) {
        addItemHandler(object : BlockTicker() {
            override fun tick(b: Block, sf: SlimefunItem, data: Config) {
                if ("false" == BlockStorage.getLocationInfo(b.location, "enabled")) {
                    return
                }
                if (lifetime % 60 == 0) {
                    if (ChargableBlock.getCharge(b) < energyConsumption) {
                        return
                    }
                    var ironblock = 0
                    var pumpkin = 0
                    for (slot in ironBlockSlots) {
                        if (SlimefunManager.isItemSimiliar(
                                BlockStorage.getInventory(b).getItemInSlot(slot), ItemStack(
                                    Material.IRON_BLOCK
                                ), true, SlimefunManager.DataType.ALWAYS
                            )
                        ) {
                            ironblock += BlockStorage.getInventory(b).getItemInSlot(slot).amount
                            if (ironblock > 3) {
                                ironblock = 4
                                break
                            }
                        }
                    }
                    for (slot in ironGolemPumpkinSlots) {
                        if (SlimefunManager.isItemSimiliar(
                                BlockStorage.getInventory(b).getItemInSlot(slot), ItemStack(
                                    Material.PUMPKIN
                                ), true, SlimefunManager.DataType.ALWAYS
                            )
                        ) {
                            pumpkin += BlockStorage.getInventory(b).getItemInSlot(slot).amount
                            if (pumpkin > 0) {
                                pumpkin = 1
                                break
                            }
                        }
                    }
                    if (ironblock > 3 && pumpkin > 0) {
                        for (slot in ironBlockSlots) {
                            if (SlimefunManager.isItemSimiliar(
                                    BlockStorage.getInventory(b).getItemInSlot(slot), ItemStack(
                                        Material.IRON_BLOCK
                                    ), true, SlimefunManager.DataType.ALWAYS
                                )
                            ) {
                                val amount: Int = BlockStorage.getInventory(b).getItemInSlot(slot).amount
                                if (amount >= ironblock) {
                                    BlockStorage.getInventory(b).replaceExistingItem(
                                        slot,
                                        InvUtils.decreaseItem(
                                            BlockStorage.getInventory(b).getItemInSlot(slot),
                                            ironblock
                                        )
                                    )
                                    break
                                }
                                ironblock -= amount
                                BlockStorage.getInventory(b).replaceExistingItem(slot, null)
                            }
                        }
                        for (slot in ironGolemPumpkinSlots) {
                            if (SlimefunManager.isItemSimiliar(
                                    BlockStorage.getInventory(b).getItemInSlot(slot), ItemStack(
                                        Material.PUMPKIN
                                    ), true, SlimefunManager.DataType.ALWAYS
                                )
                            ) {
                                val amount: Int = BlockStorage.getInventory(b).getItemInSlot(slot).amount
                                if (amount >= pumpkin) {
                                    BlockStorage.getInventory(b).replaceExistingItem(
                                        slot,
                                        InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), pumpkin)
                                    )
                                    break
                                }
                                pumpkin -= amount
                                BlockStorage.getInventory(b).replaceExistingItem(slot, null)
                            }
                        }
                        ChargableBlock.addCharge(b, -energyConsumption)
                        val offset: Double = BlockStorage.getLocationInfo(b.location, "offset").toDouble()
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunStartup.instance) {
                            b.world.spawnEntity(
                                Location(b.world, b.x + 0.5, b.y + offset, b.z + 0.5), EntityType.IRON_GOLEM
                            )
                        }
                    }
                }
            }

            override fun isSynchronized() = false

            override fun uniqueTick() {
                lifetime++
            }
        })
        super.register(slimefun)
    }

    val energyConsumption: Int
        get() = 4096

    companion object {
        private val BORDER = intArrayOf(0, 2, 3, 4, 5, 6, 8, 12, 14, 21, 23, 30, 32, 39, 40, 41)
        private val BORDER_1 = intArrayOf(9, 10, 11, 18, 20, 27, 29, 36, 37, 38)
        private val BORDER_2 = intArrayOf(15, 16, 17, 24, 26, 33, 35, 42, 43, 44)
        private var lifetime = 0
    }

    init {
        object : BlockMenuPreset(name, inventoryTitle) {
            override fun init() {
                constructMenu(this)
            }

            override fun newInstance(menu: BlockMenu, b: Block) {
                try {
                    if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(
                            b.location,
                            "enabled"
                        ) == null || "false" == BlockStorage.getLocationInfo(b.location, "enabled")
                    ) {
                        menu.replaceExistingItem(
                            22,
                            CustomItem(MaterialData(Material.SULPHUR), "§7激活状态: §4✘", "", "§e> 点击激活这个机器")
                        )
                        menu.addMenuClickHandler(
                            22
                        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? ->
                            BlockStorage.addBlockInfo(b, "enabled", "true")
                            newInstance(menu, b)
                            false
                        }
                    } else {
                        menu.replaceExistingItem(
                            22,
                            CustomItem(MaterialData(Material.REDSTONE), "§7激活状态: §2✔", "", "§e> 点击停止这个机器")
                        )
                        menu.addMenuClickHandler(
                            22
                        ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? ->
                            BlockStorage.addBlockInfo(b, "enabled", "false")
                            newInstance(menu, b)
                            false
                        }
                    }
                    val offset = if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(
                            b.location,
                            "offset"
                        ) == null
                    ) 3.0 else BlockStorage.getLocationInfo(b.location, "offset").toDouble()
                    menu.replaceExistingItem(
                        31,
                        CustomItem(
                            MaterialData(Material.PISTON_BASE),
                            "§7偏移: §3$offset 方块",
                            "",
                            "§r左键点击: §7+0.1",
                            "§r右键点击: §7-0.1"
                        )
                    )
                    menu.addMenuClickHandler(
                        31
                    ) { _: Player?, _: Int, _: ItemStack?, clickAction: ClickAction ->
                        val offset1: Double = DoubleHandler.fixDouble(
                            BlockStorage.getLocationInfo(b.location, "offset")
                                .toDouble() + if (clickAction.isRightClicked) -0.1f else 0.1f
                        )
                        BlockStorage.addBlockInfo(b, "offset", offset1.toString())
                        newInstance(menu, b)
                        false
                    }
                } catch (x: Exception) {
                    x.printStackTrace()
                }
            }

            override fun canOpen(b: Block, p: Player): Boolean {
                return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().protectionManager
                    .canAccessChest(p.uniqueId, b, true)
            }

            override fun getSlotsAccessedByItemTransport(flow: ItemTransportFlow): IntArray {
                return if (flow == ItemTransportFlow.INSERT) {
                    inputSlots
                } else IntArray(0)
            }

            override fun getSlotsAccessedByItemTransport(
                menu: BlockMenu,
                flow: ItemTransportFlow,
                item: ItemStack
            ): IntArray {
                return if (flow == ItemTransportFlow.INSERT) {
                    if (SlimefunManager.isItemSimiliar(item, ItemStack(Material.SOUL_SAND), true)) {
                        ironBlockSlots
                    } else ironGolemPumpkinSlots
                } else IntArray(0)
            }
        }
        registerBlockHandler(name, object : SlimefunBlockHandler {
            override fun onPlace(p: Player, b: Block, item: SlimefunItem) {
                BlockStorage.addBlockInfo(b, "offset", "3.0")
                BlockStorage.addBlockInfo(b, "enabled", "false")
            }

            override fun onBreak(p: Player, b: Block, item: SlimefunItem, reason: UnregisterReason): Boolean {
                if (reason == UnregisterReason.EXPLODE) {
                    return false
                }
                val inv: BlockMenu = BlockStorage.getInventory(b)
                for (slot in ironBlockSlots) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.world.dropItemNaturally(b.location, inv.getItemInSlot(slot))
                        inv.replaceExistingItem(slot, null)
                    }
                }
                for (slot in ironGolemPumpkinSlots) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.world.dropItemNaturally(b.location, inv.getItemInSlot(slot))
                        inv.replaceExistingItem(slot, null)
                    }
                }
                return true
            }
        })
    }
}