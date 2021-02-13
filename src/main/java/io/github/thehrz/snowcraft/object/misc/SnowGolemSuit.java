package io.github.thehrz.snowcraft.object.misc;

import io.github.thehrz.snowcraft.list.Items;
import io.izzel.taboolib.module.inject.TListener;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@TListener
public class SnowGolemSuit implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getInventory().getHelmet() != null &&
                event.getPlayer().getInventory().getChestplate() != null &&
                event.getPlayer().getInventory().getLeggings() != null &&
                event.getPlayer().getInventory().getBoots() != null &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getHelmet(), Items.SNOW_GOLEM_HELMET, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getChestplate(), Items.SNOW_GOLEM_CHESTPLATE, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getLeggings(), Items.SNOW_GOLEM_LEGGINGS, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getBoots(), Items.SNOW_GOLEM_BOOTS, true) &&
                event.getPlayer().getLocation().getBlock().getType() == Material.SNOW ||
                event.getPlayer().getLocation().getBlock().getType() == Material.SNOW_BLOCK) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
        }
    }
}
