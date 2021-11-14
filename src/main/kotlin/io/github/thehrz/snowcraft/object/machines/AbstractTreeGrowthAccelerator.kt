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
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import me.mrCookieSlime.Slimefun.api.BlockStorage
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow
import net.minecraft.server.v1_12_R1.BlockPosition
import net.minecraft.server.v1_12_R1.ItemDye
import net.minecraft.server.v1_12_R1.World
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.Sapling
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import java.lang.reflect.InvocationTargetException

/**
 * @author Thehrz
 */
abstract class AbstractTreeGrowthAccelerator(
    category: Category?,
    item: ItemStack?,
    name: String?,
    recipeType: RecipeType?,
    recipe: Array<ItemStack?>?
) : SlimefunItem(category, item, name, recipeType, recipe) {
    protected fun constructMenu(preset: BlockMenuPreset) {
        for (i in BORDER) {
            preset.addItem(
                i,
                CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 9.toShort()), " ")
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
    }

    abstract val energyConsumption: Int
    abstract val radius: Int
    val inputSlots: IntArray
        get() = intArrayOf(10, 11, 12, 13, 14, 15, 16)

    override fun register(slimefun: Boolean) {
        this.addItemHandler(object : BlockTicker() {
            override fun tick(b: Block, sf: SlimefunItem, data: Config) {
                try {
                    this@AbstractTreeGrowthAccelerator.tick(b)
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
            for (x in -radius..radius) {
                for (z in -radius..radius) {
                    val b = block.getRelative(x, 0, z)
                    if (b.type == Material.SAPLING) {
                        val sapling = b.state.data as Sapling
                        grow(block, b, blockMenu, sapling)
                    }
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun grow(machine: Block, block: Block, blockMenu: BlockMenu, sapling: Sapling) {
        for (slot in inputSlots) {
            if (SlimefunManager.isItemSimiliar(blockMenu.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
                ChargableBlock.addCharge(machine, -energyConsumption)
                block.state.data = sapling
                growSapling(block)
                blockMenu.replaceExistingItem(slot, InvUtils.decreaseItem(blockMenu.getItemInSlot(slot), 1))
                ParticleEffect.VILLAGER_HAPPY.display(block.location.add(0.5, 0.5, 0.5), 0.1f, 0.1f, 0.1f, 0.0f, 4)
                break
            }
        }
    }

    companion object {
        private val BORDER = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)
        @Throws(
            ClassNotFoundException::class,
            InstantiationException::class,
            IllegalAccessException::class,
            InvocationTargetException::class,
            NoSuchMethodException::class
        )
        fun growSapling(block: Block) {
            val aClass = Class.forName("net.minecraft.server.v1_12_R1.ItemDye")
            val itemDye: ItemDye = aClass.getDeclaredConstructor().newInstance() as ItemDye
            aClass.getDeclaredMethod(
                "a",
                net.minecraft.server.v1_12_R1.ItemStack::class.java,
                World::class.java,
                BlockPosition::class.java
            )
                .invoke(
                    itemDye,
                    net.minecraft.server.v1_12_R1.ItemStack(CraftMagicNumbers.getBlock(block)),
                    (block.world as CraftWorld).handle,
                    BlockPosition(block.x, block.y, block.z)
                )
        }
    }

    init {
        object : BlockMenuPreset(name, console().asLangText("abstract-tree-growth-accelerator")) {
            override fun init() {
                constructMenu(this)
            }

            override fun newInstance(menu: BlockMenu, b: Block) {}
            override fun canOpen(b: Block, p: Player): Boolean {
                return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().protectionManager
                    .canAccessChest(p.uniqueId, b, true)
            }

            override fun getSlotsAccessedByItemTransport(flow: ItemTransportFlow): IntArray {
                return if (flow == ItemTransportFlow.INSERT) {
                    inputSlots
                } else IntArray(0)
            }
        }
        registerBlockHandler(name, object : SlimefunBlockHandler {
            override fun onPlace(p: Player, b: Block, item: SlimefunItem) {}
            override fun onBreak(p: Player, b: Block, item: SlimefunItem, reason: UnregisterReason): Boolean {
                val inv: BlockMenu = BlockStorage.getInventory(b)
                for (slot in inputSlots) {
                    if (inv.getItemInSlot(slot) == null) {
                        continue
                    }
                    b.world.dropItemNaturally(b.location, inv.getItemInSlot(slot))
                    inv.replaceExistingItem(slot, null)
                }
                return true
            }
        })
    }
}