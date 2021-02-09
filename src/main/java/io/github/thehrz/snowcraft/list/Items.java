package io.github.thehrz.snowcraft.list;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */
public class Items {
    public static final ItemStack BLOCK_COMPRES_MACHINE = new CustomItem(new ItemStack(Material.PISTON_BASE), "§a方块压缩机 §7(§eI§7)", "", "§7可以帮你压缩方块", "", "§6基础机器", "§8⇨ §7速度: 1x", "§8⇨ §e⚡ §732 J 缓存", "§8⇨ §e⚡ §78 J/s");
    public static final ItemStack BLOCK_COMPRES_MACHINE_2 = new CustomItem(new ItemStack(Material.PISTON_BASE), "§a方块压缩机 §7(§eII§7)", "", "§7可以帮你压缩方块", "", "§6高级机器", "§8⇨ §7速度: 1x", "§8⇨ §e⚡ §7256 J 缓存", "§8⇨ §e⚡ §732 J/s");
    public static final ItemStack BLOCK_COMPRES_MACHINE_3 = new CustomItem(new ItemStack(Material.PISTON_BASE), "§a方块压缩机 §7(§eIII§7)", "", "§7可以帮你压缩方块", "", "§6终极机器", "§8⇨ §7速度: 1x", "§8⇨ §e⚡ §7512 J 缓存", "§8⇨ §e⚡ §764 J/s");
    public static final ItemStack ANDROID_PANEL = new CustomItem(new ItemStack(Material.NAME_TAG), "§b机器人面板", "", "§7可以给你的机器人进行一些设置", "§7右键机器人以打开面板", "");
    public static final ItemStack IRON_GOLEM_ASSEMBLER = new CustomItem(new ItemStack(Material.IRON_BLOCK), "§5铁傀儡组装机", "", "§4终极机器", "§8⇨ §7冷却: §b30 秒", "§8⇨ §e⚡ §74096 J 缓存", "§8⇨ §e⚡ §72048 J/铁傀儡");
    public static final ItemStack TREE_GROWTH_ACCELERATOR = new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "§a树木生长催化机 §7(§eI§7)", "", "§r依靠 §a有机肥料 §r运作", "", "§e基础 机器", "§8⇨ §7半径: 3x3", "§8⇨ §7速度: §a1/次", "§8⇨ §e⚡ §7128 J 缓存", "§8⇨ §e⚡ §732 J/s");
    public static final ItemStack TREE_GROWTH_ACCELERATOR_2 = new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "§a树木生长催化机 §7(§eII§7)", "", "§r依靠 §a有机肥料 §r运作", "", "§e中级 机器", "§8⇨ §7半径: 7x7", "§8⇨ §7速度: §a1/次", "§8⇨ §e⚡ §7256 J 缓存", "§8⇨ §e⚡ §764 J/s");
    public static final ItemStack TREE_GROWTH_ACCELERATOR_3 = new CustomItem(new ItemStack(Material.STAINED_CLAY, 1, (short) 13), "§a树木生长催化机 §7(§eIII§7)", "", "§r依靠 §a有机肥料 §r运作", "", "§4终极 机器", "§8⇨ §7半径: 9x9", "§8⇨ §7速度: §a1/次", "§8⇨ §e⚡ §7128 J 缓存", "§8⇨ §e⚡ §7512 J/s");
    public static final ItemStack TREE_GROWTH_DISPENSER = new CustomItem(new ItemStack(Material.DISPENSER), "§a树木肥料发射器", "", "§r依靠 §a有机肥料 §r运作", "§r向面对的树苗发射有机肥料", "", "§6终极机器", "§8⇨ §7速度: §a1/次", "§8⇨ §e⚡ §72048 J 缓存", "§8⇨ §e⚡ §7512 J/s");
    public static final ItemStack ORIGINAL_AUTOMATED_CRAFTING_CHAMBER = new CustomItem(new ItemStack(Material.WORKBENCH), "&6原版自动合成机", "", "§7可以自动合成原版物品", "", "&6高级机器", "§8⇨ §e⚡ §732 J/物品");
    public static ItemStack MAGNESIUM_GENERATOR = null;
    public static ItemStack LANTERN = null;
    public static ItemStack ITEM_CHANGEOVER_TABLE = null;
    public static ItemStack MONEY_PRINTER = null;

    static {
        try {
            LANTERN = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRmNzU1NWQwNWY1MGZhMTMzYjZhMjEwMTE3NDIzNDY0MDFkZjczNDM5OWQ1YWE2YzI5ODgwYmIxZTM1YjkzZCJ9fX0="), "&a灯笼", "", "&a能亮的灯笼哦", "");
            MAGNESIUM_GENERATOR = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTM0M2NlNThkYTU0Yzc5OTI0YTJjOTMzMWNmYzQxN2ZlOGNjYmJlYTliZTQ1YTdhYzg1ODYwYTZjNzMwIn19fQ=="), "§c镁发电机", "", "§6发电机组", "§8⇨ §e⚡ §7256 J 缓存", "§8⇨ §e⚡ §764 J/s");
            MONEY_PRINTER = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxMzA5ZGFjNTU2OTExZTU1Mzk4MDM4YzQzNjdmODkyZDk2Y2Q1ZTgwMzRmYzIzMmRiOTIwNzM2ODc5OTQ0YyJ9fX0="), "§a印钞机", "", "§a这个机器可以帮你打印钱");
            ITEM_CHANGEOVER_TABLE = new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmM0ODJlM2I1YzdkNmZiNmIwMDFkZTBlM2JiZmEwOWQyODE3YTY5OGNhYTZlMzNlYjI3NmFlYTA4MTA0NDFmNiJ9fX0="), "§a物品转换台", "", "§a这个机器可以将现有物品转换成某些特定的物品");
        } catch (Exception e) {
            System.out.println("插件: SnowCraft 注册物品时发生错误");
            e.printStackTrace();
        }
    }

}

