package io.github.thehrz.snowcraft.object.misc;

import io.github.thehrz.snowcraft.list.Items;
import io.izzel.taboolib.module.inject.TListener;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static io.izzel.taboolib.module.locale.TLocale.Display.sendActionBar;

@TListener
public class IceCream implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (SlimefunManager.isItemSimiliar(event.getItem(), Items.ICE_CREAM, true)) {
            event.setCancelled(true);
            int amount = event.getPlayer().getInventory().getItemInMainHand().getAmount();
            event.getPlayer().getInventory().getItemInMainHand().setAmount(--amount);
            sendActionBar(event.getPlayer(), "§a你食用了冰淇淋 获得了冰淇淋给予你的跳跃加成");
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3600, 5));
        }
    }
}
