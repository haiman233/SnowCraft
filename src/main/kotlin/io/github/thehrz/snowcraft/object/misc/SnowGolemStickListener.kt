package io.github.thehrz.snowcraft.`object`.misc

import io.github.thehrz.snowcraft.list.Items
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import org.apache.commons.lang3.RandomUtils
import org.bukkit.Material
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.entity.Snowball
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang
import java.util.*

/**
 * @author Thehrz
 */
object SnowGolemStickListener {
    private val uuids: MutableList<UUID> = ArrayList<UUID>()
    @SubscribeEvent
    fun e(event: PlayerInteractEvent): Boolean {
        if (SlimefunManager.isItemSimiliar(
                event.item,
                Items.SNOW_GOLEM_STICK,
                true
            ) && event.action == Action.RIGHT_CLICK_AIR &&
            !event.player.hasCooldown(Material.STICK) && event.player.inventory.helmet != null && event.player.inventory.chestplate != null && event.player.inventory.leggings != null && event.player.inventory.boots != null &&
            SlimefunManager.isItemSimiliar(event.player.inventory.helmet, Items.SNOW_GOLEM_HELMET, true) &&
            SlimefunManager.isItemSimiliar(event.player.inventory.chestplate, Items.SNOW_GOLEM_CHESTPLATE, true) &&
            SlimefunManager.isItemSimiliar(event.player.inventory.leggings, Items.SNOW_GOLEM_LEGGINGS, true) &&
            SlimefunManager.isItemSimiliar(event.player.inventory.boots, Items.SNOW_GOLEM_BOOTS, true)
        ) {
            val vector = event.player.location.direction
            uuids.add(event.player.launchProjectile(Snowball::class.java, vector.multiply(1.6)).uniqueId)
            event.player.setCooldown(Material.STICK, 60)
            return true
        }
        return false
    }

    @SubscribeEvent
    fun e(event: EntityDamageByEntityEvent) {
        if (event.damager is Snowball) {
            if ((event.damager as Projectile).shooter is Player) {
                if (uuids.contains(event.damager.uniqueId)) {
                    val shooter = (event.damager as Projectile).shooter as Player
                    val name = event.entity.name
                    val randomInt = RandomUtils.nextInt(1, 101)
                    event.isCancelled = true
                    if (randomInt <= 5) {
                        event.entity.velocity = Vector(0.toDouble(), 2.25, 0.toDouble())
                        (event.entity as Damageable).damage(10.0, shooter)
                        shooter.sendLang("snow-golem-stick-10damage", name)
                    } else if (randomInt <= 8) {
                        event.entity.world.strikeLightning(event.entity.location)
                        shooter.sendLang("snow-golem-stick-strike-lightning", name)
                    } else if (randomInt <= 9) {
                        event.entity.world.strikeLightningEffect(event.entity.location)
                        shooter.sendLang("snow-golem-stick-strike-lightning-effect", name)
                    } else if (randomInt <= 29) {
                        (event.entity as Damageable).damage(20.0)
                        shooter.sendLang("snow-golem-stick-20damger", name)
                    } else {
                        event.isCancelled = false
                    }
                }
            }
        }
    }
}