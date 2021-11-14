package io.github.thehrz.snowcraft.`object`.machines

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_8.ParticleEffect
import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems
import me.mrCookieSlime.Slimefun.Objects.Category
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import me.mrCookieSlime.Slimefun.api.BlockStorage
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Directional
import org.bukkit.material.Sapling
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText

/**
 * @author Thehrz
 */
abstract class AbstractTreeGrowthDispenser(
    category: Category?,
    item: ItemStack?,
    id: String?,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : SlimefunItem(category, item, id, recipeType, recipe) {
    protected fun constructMenu(preset: BlockMenuPreset) {
        for (i in BORDER) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 9.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
    }

    abstract val progressBar: ItemStack
    abstract val energyConsumption: Int
    val inputSlots: IntArray
        get() = intArrayOf(10, 11, 12, 13, 14, 15, 16)

    fun isProcessing(b: Block?): Boolean {
        return getProcessing(b) != null
    }

    fun getProcessing(b: Block?): MachineRecipe? {
        return processing[b]
    }

    override fun register(slimefun: Boolean) {
        this.addItemHandler(object : BlockTicker() {
            override fun tick(b: Block, sf: SlimefunItem, data: Config) {
                try {
                    this@AbstractTreeGrowthDispenser.tick(b)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun uniqueTick() {}

            override fun isSynchronized() = true
        })
        super.register(slimefun)
    }

    @Throws(Exception::class)
    protected fun tick(block: Block) {
        val blockMenu = BlockStorage.getInventory(block)
        if (ChargableBlock.getCharge(block) >= ChargableBlock.getMaxCharge(block)) {
            val directional = block.state.data as Directional
            val b = block.getRelative(directional.facing)
            if (b.type != Material.SAPLING) {
                return
            }
            val sapling = b.state.data as Sapling
            grow(block, b, blockMenu, sapling)
        }
    }

    @Throws(Exception::class)
    private fun grow(machine: Block, block: Block, blockMenu: BlockMenu, sapling: Sapling) {
        for (slot in inputSlots) {
            if (SlimefunManager.isItemSimiliar(blockMenu.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
                ChargableBlock.addCharge(machine, -energyConsumption)
                block.state.data = sapling
                AbstractTreeGrowthAccelerator.growSapling(block)
                blockMenu.replaceExistingItem(slot, InvUtils.decreaseItem(blockMenu.getItemInSlot(slot), 1))
                ParticleEffect.VILLAGER_HAPPY.display(block.location.add(0.5, 0.5, 0.5), 0.1f, 0.1f, 0.1f, 0.0f, 4)
                break
            }
        }
    }

    companion object {
        private val BORDER = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
        var processing: MutableMap<Block?, MachineRecipe> = HashMap()
        var progress: MutableMap<Block, Int> = HashMap()
    }

    init {
        object : BlockMenuPreset(id, console().asLangText("abstract-tree-growth-dispenser")) {
            override fun init() {
                constructMenu(this)
            }

            override fun newInstance(blockMenu: BlockMenu, block: Block) {}
            override fun canOpen(block: Block, player: Player): Boolean {
                return player.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().protectionManager
                    .canAccessChest(player.uniqueId, block, true)
            }

            override fun getSlotsAccessedByItemTransport(itemTransportFlow: ItemTransportFlow): IntArray {
                return if (itemTransportFlow == ItemTransportFlow.INSERT) {
                    inputSlots
                } else IntArray(0)
            }
        }
        registerBlockHandler(id, object : SlimefunBlockHandler {
            override fun onPlace(player: Player, block: Block, slimefunItem: SlimefunItem) {}
            override fun onBreak(
                player: Player,
                block: Block,
                slimefunItem: SlimefunItem,
                unregisterReason: UnregisterReason
            ): Boolean {
                val inv = BlockStorage.getInventory(block)
                for (slot in inputSlots) {
                    if (inv.getItemInSlot(slot) == null) {
                        continue
                    }
                    block.world.dropItemNaturally(block.location, inv.getItemInSlot(slot))
                    inv.replaceExistingItem(slot, null)
                }
                progress.remove(block)
                processing.remove(block)
                return true
            }
        })
    }
}