package io.github.thehrz.snowcraft;

import io.github.thehrz.snowcraft.setup.ItemsSetup;
import org.bukkit.Bukkit;
import io.izzel.taboolib.loader.Plugin;

/**
 * @author Thehrz
 */
public final class SnowCraft extends Plugin {

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this.getPlugin());
    }

    @Override
    public void onEnable() {
        ItemsSetup.setupItems();
    }
}
