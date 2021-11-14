package io.github.thehrz.snowcraft.`object`.misc

import io.github.thehrz.snowcraft.list.Items
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang

object IceCream {
    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        if (SlimefunManager.isItemSimiliar(e.item, Items.ICE_CREAM, true)) {
            e.let {
                it.isCancelled = true
                it.item.amount -= 1
                it.player.sendLang("ice-cream-eating")
                it.player.addPotionEffect(PotionEffect(PotionEffectType.JUMP, 3600, 5))
            }
        }
    }
}