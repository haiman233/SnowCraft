package io.github.thehrz.snowcraft;

import io.github.thehrz.snowcraft.object.machines.OriginalAutomatedCraftingChamber;
import io.github.thehrz.snowcraft.setup.ItemsSetup;
import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.metrics.BMetrics;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import static io.izzel.taboolib.module.locale.chatcolor.TColor.translate;

/**
 * @author Thehrz
 */
public final class SnowCraft extends Plugin {
    private static String prefix;

    @TInject("config.yml")
    private static TConfig conf;

    @NotNull
    public static TConfig getConf() {
        return conf;
    }

    @NotNull
    public static String getPrefix() {
        return translate(prefix);
    }

    public static void setPrefix(String prefix) {
        SnowCraft.prefix = prefix;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this.getPlugin());
    }

    @Override
    public void onEnable() {
        OriginalAutomatedCraftingChamber.updateBlackList(getConf().getStringList("OriginalAutomatedCraftingChamber.blacklist"));

        conf.listener(() -> {
            OriginalAutomatedCraftingChamber.updateBlackList(getConf().getStringList("OriginalAutomatedCraftingChamber.blacklist"));
            setPrefix(getConf().getString("Prefix"));
            System.out.println(getPrefix() + " §a配置文件加载成功");
        });

        setPrefix(getConf().getString("Prefix"));

        new BMetrics(this.getPlugin(), 10284);

        ItemsSetup.setupItems();
    }
}
