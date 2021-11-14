package io.github.thehrz.snowcraft.`object`.machines

import io.github.thehrz.snowcraft.list.Items
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.SkullItem
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem
import me.mrCookieSlime.Slimefun.api.BlockStorage
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.module.lang.asLangText
import taboolib.platform.util.asLangText
import java.util.*

/**
 * @author Thehrz
 */
object AndroidPanel : Listener {
    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK
            && event.hasItem()
            && CustomItem(event.item, 1) == CustomItem(Items.ANDROID_PANEL, 1)
        ) {
            event.isCancelled = true
            event.player.closeInventory()
            if (BlockStorage.getStorage(event.clickedBlock.world).hasInventory(event.clickedBlock.location)) {
                if ("可编程机器人" == BlockStorage.getInventory(event.clickedBlock).toInventory().title
                    && Bukkit.getOfflinePlayer(
                        UUID.fromString(
                            BlockStorage.getLocationInfo(
                                event.clickedBlock.location,
                                "owner"
                            )
                        )
                    ).uniqueId == event.player.uniqueId || event.player.isOp
                ) {
                    openMenu(event.player, event.clickedBlock)
                }
            }
        }
    }

    @SubscribeEvent
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        if (CustomItem(event.player.inventory.itemInMainHand, 1) == CustomItem(Items.ANDROID_PANEL, 1)) {
            event.isCancelled = true
        }
    }

    fun openMenu(player: Player, block: Block) {
        val menu = ChestMenu(console().asLangText("android-panel"))
        menu.isEmptySlotsClickable = false
        for (i in 0..53) {
            val isBorder = i <= 8 || i % 9 == 0 || 45 <= i || i % 9 == 8
            if (isBorder) {
                menu.addItem(
                    i,
                    CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toShort()), " ")
                ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
            }
            menu.addItem(
                4,
                SlimefunItem.getItem(BlockStorage.getLocationInfo(block.location, "id"))
            ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
        }
        menu.addItem(
            10,
            SkullItem(
                player.asLangText(
                    "android-panel-menu-owner",
                    Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(block.location, "owner"))).name
                ),
                Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(block.location, "owner"))).name,
                console().asLangText("android-panel-menu-owner-lore")
            )
        ) { p: Player, _: Int, _: ItemStack?, _: ClickAction? ->
            openPlayerList(p, block)
            false
        }
        menu.open(player)
    }

    private fun openPlayerList(player: Player, block: Block) {
        val onlinePlayers: MutableList<UUID> = ArrayList<UUID>()
        val playlists = ChestMenu("§b选择玩家")
        playlists.isEmptySlotsClickable = false
        for (i in 0..8) {
            if (i != 4) {
                playlists.addItem(
                    i,
                    CustomItem(ItemStack(Material.STAINED_GLASS_PANE, 1, 7.toShort()), " ")
                ) { _: Player?, _: Int, _: ItemStack?, _: ClickAction? -> false }
            } else {
                playlists.addItem(
                    4,
                    CustomItem(MaterialData(Material.EMERALD), "§7⇦ 返回上一界面")
                ) { p: Player, _: Int, _: ItemStack?, _: ClickAction? ->
                    p.closeInventory()
                    openMenu(player, block)
                    false
                }
            }
        }
        Bukkit.getOnlinePlayers().forEach { p: Player -> onlinePlayers.add(p.uniqueId) }
        for (i in onlinePlayers.indices) {
            playlists.addItem(
                i + 9,
                SkullItem(
                    "§a玩家 §b" + Bukkit.getOfflinePlayer(onlinePlayers[i]).name,
                    Bukkit.getOfflinePlayer(onlinePlayers[i]).name
                )
            ) { p: Player, arg1: Int, _: ItemStack?, _: ClickAction? ->
                if (Bukkit.getOfflinePlayer(onlinePlayers[arg1 - 9]).isOnline) {
                    BlockStorage.addBlockInfo(block, "owner", onlinePlayers[arg1 - 9].toString(), true)
                    p.sendMessage(
                        "§a已设玩家 " + Bukkit.getOfflinePlayer(onlinePlayers[arg1 - 9]).name + "为此机器人主人"
                    )
                    p.closeInventory()
                } else {
                    p.sendMessage("§c玩家 " + Bukkit.getOfflinePlayer(onlinePlayers[arg1 - 9]).name + "不在线")
                }
                false
            }
        }
        player.closeInventory()
        playlists.open(player)
    }

}