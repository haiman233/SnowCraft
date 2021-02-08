package io.github.thehrz.snowcraft.object.machines;

import io.github.thehrz.snowcraft.list.Items;
import io.izzel.taboolib.module.inject.TListener;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.SkullItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Thehrz
 */
@TListener
public class AndroidPanel implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public static void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasItem()) {
            if (e.getItem().equals(Items.ANDROID_PANEL)) {
                e.setCancelled(true);
                e.getPlayer().closeInventory();
                if (BlockStorage.getStorage(e.getClickedBlock().getWorld()).hasInventory(e.getClickedBlock().getLocation())) {
                    if ("可编程机器人".equals(BlockStorage.getInventory(e.getClickedBlock()).toInventory().getTitle())) {
                        if (Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(e.getClickedBlock().getLocation(), "owner"))).getUniqueId().equals(e.getPlayer().getUniqueId()) || e.getPlayer().isOp()) {
                            openMenu(e.getPlayer(), e.getClickedBlock());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().equals(Items.ANDROID_PANEL)) {
            e.setCancelled(true);
        }

    }


    public static void openMenu(Player player, Block block) {
        ChestMenu menu = new ChestMenu("§b机器人面板");
        menu.setEmptySlotsClickable(false);
        for (int i = 0; i <= 53; i++) {
            boolean isBorder = i <= 8 || i % 9 == 0 || 45 <= i || i % 9 == 8;
            if (isBorder) {
                menu.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "), (arg0, arg1, arg2, arg3) -> false);
            }
            menu.addItem(4, SlimefunItem.getItem(BlockStorage.getLocationInfo(block.getLocation(), "id")), (arg0, arg1, arg2, arg3) -> false);
        }
        menu.addItem(10, new SkullItem("§a主人 §b" + Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(block.getLocation(), "owner"))).getName(), Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(block.getLocation(), "owner"))).getName(), "", "§a点击以更改主人", ""),
                (p, arg1, arg2, arg3) -> {
                    openPlayerList(p, block);
                    return false;
                });

        menu.open(player);
    }

    public static void openPlayerList(Player player, Block block) {
        List<UUID> onlinePlayers = new ArrayList<>();
        ChestMenu playlists = new ChestMenu("§b选择玩家");
        playlists.setEmptySlotsClickable(false);

        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                playlists.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "), (p, arg1, arg2, arg3) -> false);
            } else {
                playlists.addItem(4, new CustomItem(new MaterialData(Material.EMERALD), "§7⇦ 返回上一界面"), (p1, arg1, arg2, arg3) -> {
                    p1.closeInventory();
                    openMenu(player, block);
                    return false;
                });
            }
        }
        Bukkit.getOnlinePlayers().forEach(p -> onlinePlayers.add(p.getUniqueId()));
        for (int i = 0; i < onlinePlayers.size(); i++) {
            playlists.addItem(i + 9, new SkullItem("§a玩家 §b" + Bukkit.getOfflinePlayer(onlinePlayers.get(i)).getName(), Bukkit.getOfflinePlayer(onlinePlayers.get(i)).getName()), (p, arg1, arg2, arg3) -> {
                if (Bukkit.getOfflinePlayer(onlinePlayers.get(arg1 - 9)).isOnline()) {
                    BlockStorage.addBlockInfo(block, "owner", onlinePlayers.get(arg1 - 9).toString(), true);
                    p.sendMessage("§a已设玩家 " + Bukkit.getOfflinePlayer(onlinePlayers.get(arg1 - 9)).getName() + "为此机器人主人");
                    p.closeInventory();
                    return false;
                }
                p.sendMessage("§c玩家 " + Bukkit.getOfflinePlayer(onlinePlayers.get(arg1 - 9)).getName() + "不在线");
                return false;
            });
        }
        player.closeInventory();
        playlists.open(player);
    }
}