package io.github.thehrz.snowcraft.`object`.misc

import io.github.thehrz.snowcraft.list.Items
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import org.bukkit.Material
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent

object SnowGolemSuit {
    @SubscribeEvent
    fun e(e: PlayerMoveEvent) {
        if (e.player.inventory.helmet != null && e.player.inventory.chestplate != null && e.player.inventory.leggings != null && e.player.inventory.boots != null &&
            SlimefunManager.isItemSimiliar(e.player.inventory.helmet, Items.SNOW_GOLEM_HELMET, true) &&
            SlimefunManager.isItemSimiliar(e.player.inventory.chestplate, Items.SNOW_GOLEM_CHESTPLATE, true) &&
            SlimefunManager.isItemSimiliar(e.player.inventory.leggings, Items.SNOW_GOLEM_LEGGINGS, true) &&
            SlimefunManager.isItemSimiliar(e.player.inventory.boots, Items.SNOW_GOLEM_BOOTS, true) &&
            (e.player.location.block.type == Material.SNOW || e.player.location.block.type == Material.SNOW_BLOCK)
        ) {
            e.player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 2))
        }
    }
}