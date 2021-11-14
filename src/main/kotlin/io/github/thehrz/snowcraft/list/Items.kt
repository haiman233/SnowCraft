package io.github.thehrz.snowcraft.list

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomArmor
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author Thehrz
 */
object Items {
    val BLOCK_COMPRES_MACHINE: ItemStack = CustomItem(
        ItemStack(Material.PISTON_BASE),
        "§a方块压缩机 §7(§eI§7)",
        "",
        "§7可以帮你压缩方块",
        "",
        "§6基础机器",
        "§8⇨ §7速度: 1x",
        "§8⇨ §e⚡ §732 J 缓存",
        "§8⇨ §e⚡ §78 J/s"
    )
    val BLOCK_COMPRES_MACHINE_2: ItemStack = CustomItem(
        ItemStack(Material.PISTON_BASE),
        "§a方块压缩机 §7(§eII§7)",
        "",
        "§7可以帮你压缩方块",
        "",
        "§6高级机器",
        "§8⇨ §7速度: 1x",
        "§8⇨ §e⚡ §7256 J 缓存",
        "§8⇨ §e⚡ §732 J/s"
    )
    val BLOCK_COMPRES_MACHINE_3: ItemStack = CustomItem(
        ItemStack(Material.PISTON_BASE),
        "§a方块压缩机 §7(§eIII§7)",
        "",
        "§7可以帮你压缩方块",
        "",
        "§6终极机器",
        "§8⇨ §7速度: 1x",
        "§8⇨ §e⚡ §7512 J 缓存",
        "§8⇨ §e⚡ §764 J/s"
    )
    val ANDROID_PANEL: ItemStack =
        CustomItem(ItemStack(Material.NAME_TAG), "§b机器人面板", "", "§7可以给你的机器人进行一些设置", "§7右键机器人以打开面板", "")
    val IRON_GOLEM_ASSEMBLER: ItemStack = CustomItem(
        ItemStack(Material.IRON_BLOCK),
        "§5铁傀儡组装机",
        "",
        "§4终极机器",
        "§8⇨ §7冷却: §b30 秒",
        "§8⇨ §e⚡ §74096 J 缓存",
        "§8⇨ §e⚡ §72048 J/铁傀儡"
    )
    val TREE_GROWTH_ACCELERATOR: ItemStack = CustomItem(
        ItemStack(Material.STAINED_CLAY, 1, 13.toShort()),
        "§a树木生长催化机 §7(§eI§7)",
        "",
        "§r依靠 §a有机肥料 §r运作",
        "",
        "§e基础 机器",
        "§8⇨ §7半径: 3x3",
        "§8⇨ §7速度: §a1/次",
        "§8⇨ §e⚡ §7128 J 缓存",
        "§8⇨ §e⚡ §732 J/s"
    )
    val TREE_GROWTH_ACCELERATOR_2: ItemStack = CustomItem(
        ItemStack(Material.STAINED_CLAY, 1, 13.toShort()),
        "§a树木生长催化机 §7(§eII§7)",
        "",
        "§r依靠 §a有机肥料 §r运作",
        "",
        "§e中级 机器",
        "§8⇨ §7半径: 7x7",
        "§8⇨ §7速度: §a1/次",
        "§8⇨ §e⚡ §7256 J 缓存",
        "§8⇨ §e⚡ §764 J/s"
    )
    val TREE_GROWTH_ACCELERATOR_3: ItemStack = CustomItem(
        ItemStack(Material.STAINED_CLAY, 1, 13.toShort()),
        "§a树木生长催化机 §7(§eIII§7)",
        "",
        "§r依靠 §a有机肥料 §r运作",
        "",
        "§4终极 机器",
        "§8⇨ §7半径: 9x9",
        "§8⇨ §7速度: §a1/次",
        "§8⇨ §e⚡ §7128 J 缓存",
        "§8⇨ §e⚡ §7512 J/s"
    )
    val TREE_GROWTH_DISPENSER: ItemStack = CustomItem(
        ItemStack(Material.DISPENSER),
        "§a树木肥料发射器",
        "",
        "§r依靠 §a有机肥料 §r运作",
        "§r向面对的树苗发射有机肥料",
        "",
        "§6终极机器",
        "§8⇨ §7速度: §a1/次",
        "§8⇨ §e⚡ §72048 J 缓存",
        "§8⇨ §e⚡ §7512 J/s"
    )
    val ORIGINAL_AUTOMATED_CRAFTING_CHAMBER: ItemStack =
        CustomItem(ItemStack(Material.WORKBENCH), "&6原版自动合成机", "", "§7可以自动合成原版物品", "", "&6高级机器", "§8⇨ §e⚡ §732 J/物品")
    val SNOW_GOLEM_ASSEMBLER: ItemStack = CustomItem(
        ItemStack(Material.SNOW_BLOCK),
        "§a雪傀儡组装机",
        "",
        "§r只有此机器组装的雪傀儡才可能掉落雪傀儡之心",
        "",
        "§4终极机器",
        "§8⇨ §7冷却: §b30 秒"
    )
    val SNOW_GOLEM_HEART: ItemStack = CustomItem(ItemStack(Material.SNOW_BALL), "§6雪傀儡之心", "")
    val SNOW_GOLEM_HELMET: ItemStack = CustomArmor(
        CustomItem(ItemStack(Material.LEATHER_HELMET), "§r雪傀儡头盔", "", "§7使用整套装备后即可使用雪傀儡手杖", ""),
        Color.WHITE
    )
    val SNOW_GOLEM_CHESTPLATE: ItemStack = CustomArmor(
        CustomItem(ItemStack(Material.LEATHER_CHESTPLATE), "§r雪傀儡胸甲", "", "§7使用整套装备后即可使用雪傀儡手杖", ""),
        Color.WHITE
    )
    val SNOW_GOLEM_LEGGINGS: ItemStack = CustomArmor(
        CustomItem(ItemStack(Material.LEATHER_LEGGINGS), "§r雪傀儡护腿", "", "§7使用整套装备后即可使用雪傀儡手杖", ""),
        Color.WHITE
    )
    val SNOW_GOLEM_BOOTS: ItemStack =
        CustomArmor(CustomItem(ItemStack(Material.LEATHER_BOOTS), "§r雪傀儡靴子", "", "§7使用整套装备后即可使用雪傀儡手杖", ""), Color.WHITE)
    val SNOW_GOLEM_STICK: ItemStack = CustomItem(ItemStack(Material.STICK), "§a雪傀儡手杖", "", "§7右键掷出一个神奇雪球", "")
    val ICE_CREAM: ItemStack = CustomItem(ItemStack(Material.FLOWER_POT_ITEM), "§a冰淇淋", "", "§7这不是花盆吗?", "")
    val MAGNESIUM_GENERATOR: ItemStack = CustomItem(
        CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="),
        "§c镁发电机",
        "",
        "§6发电机组",
        "§8⇨ §e⚡ §7256 J 缓存",
        "§8⇨ §e⚡ §764 J/s"
    )
    val LANTERN: ItemStack = CustomItem(
        CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRmNzU1NWQwNWY1MGZhMTMzYjZhMjEwMTE3NDIzNDY0MDFkZjczNDM5OWQ1YWE2YzI5ODgwYmIxZTM1YjkzZCJ9fX0="),
        "&a灯笼",
        "",
        "&a能亮的灯笼哦",
        ""
    )
}