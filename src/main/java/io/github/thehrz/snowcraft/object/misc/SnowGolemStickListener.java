package io.github.thehrz.snowcraft.object.misc;

import io.github.thehrz.snowcraft.list.Items;
import io.izzel.taboolib.internal.apache.lang3.RandomUtils;
import io.izzel.taboolib.module.inject.TListener;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.izzel.taboolib.module.locale.TLocale.Display.sendActionBar;

/**
 * @author Thehrz
 */
@TListener
public class SnowGolemStickListener implements Listener {
    private final List<UUID> uuids = new ArrayList<>();

    @EventHandler
    public boolean onPlayerInteract(PlayerInteractEvent event) {
        if (SlimefunManager.isItemSimiliar(event.getItem(), Items.SNOW_GOLEM_STICK, true) &&
                event.getAction() == Action.RIGHT_CLICK_AIR &&
                !event.getPlayer().hasCooldown(Material.STICK) &&
                event.getPlayer().getInventory().getHelmet() != null &&
                event.getPlayer().getInventory().getChestplate() != null &&
                event.getPlayer().getInventory().getLeggings() != null &&
                event.getPlayer().getInventory().getBoots() != null &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getHelmet(), Items.SNOW_GOLEM_HELMET, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getChestplate(), Items.SNOW_GOLEM_CHESTPLATE, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getLeggings(), Items.SNOW_GOLEM_LEGGINGS, true) &&
                SlimefunManager.isItemSimiliar(event.getPlayer().getInventory().getBoots(), Items.SNOW_GOLEM_BOOTS, true)) {
            Vector vector = event.getPlayer().getLocation().getDirection();
            uuids.add(event.getPlayer().launchProjectile(Snowball.class, vector.multiply(1.6D)).getUniqueId());
            event.getPlayer().setCooldown(Material.STICK, 60);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                if (uuids.contains(event.getDamager().getUniqueId())) {
                    int randomInt = RandomUtils.nextInt(1, 101);
                    event.setCancelled(true);
                    if (randomInt <= 5) {
                        event.getEntity().setVelocity(new Vector(0, 2.25D, 0));
                        ((Damageable) event.getEntity()).damage(10D, ((Player) ((Projectile) event.getDamager()).getShooter()));
                        sendActionBar(((Player) ((Projectile) event.getDamager()).getShooter()), "§a您对" + event.getEntity().getName() + "造成了10点伤害并击飞了");
                    } else if (randomInt <= 8) {
                        event.getEntity().getWorld().strikeLightning(event.getEntity().getLocation());
                        sendActionBar(((Player) ((Projectile) event.getDamager()).getShooter()), "§a您为" + event.getEntity().getName() + "召唤了一道闪电");
                    } else if (randomInt <= 9) {
                        event.getEntity().getWorld().strikeLightningEffect(event.getEntity().getLocation());
                        sendActionBar(((Player) ((Projectile) event.getDamager()).getShooter()), "§a您为" + event.getEntity().getName() + "召唤了一道没有伤害的闪电");
                    } else if (randomInt <= 29) {
                        ((Damageable) event.getEntity()).damage(20D);
                        sendActionBar(((Player) ((Projectile) event.getDamager()).getShooter()), "§a您对" + event.getEntity().getName() + "造成了20点伤害");
                    } else {
                        event.setCancelled(false);
                    }
                }
            }
        }
    }
}
