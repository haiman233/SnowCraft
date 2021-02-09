package io.github.thehrz.snowcraft.object.machines;

import io.github.thehrz.snowcraft.list.Items;
import io.izzel.taboolib.internal.apache.lang3.RandomUtils;
import io.izzel.taboolib.module.inject.TListener;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Math.DoubleHandler;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SnowGolemAssembler extends SlimefunItem {
    private static final int[] BORDER = new int[]{0, 2, 3, 4, 5, 6, 8, 12, 14, 21, 23, 30, 32, 39, 40, 41};
    private static final int[] BORDER_1 = new int[]{9, 10, 11, 18, 20, 27, 29, 36, 37, 38};
    private static final int[] BORDER_2 = new int[]{15, 16, 17, 24, 26, 33, 35, 42, 43, 44};
    private static int lifetime = 0;

    public SnowGolemAssembler(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);

        new BlockMenuPreset(name, getInventoryTitle()) {
            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(final BlockMenu menu, final Block b) {
                try {
                    if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "enabled") == null || "false".equals(BlockStorage.getLocationInfo(b.getLocation(), "enabled"))) {
                        menu.replaceExistingItem(22, new CustomItem(new MaterialData(Material.SULPHUR), "§7激活状态: §4✘", "", "§e> 点击激活这个机器"));
                        menu.addMenuClickHandler(22, (p, arg1, arg2, arg3) -> {
                            BlockStorage.addBlockInfo(b, "enabled", "true");
                            newInstance(menu, b);
                            return false;
                        });
                    } else {

                        menu.replaceExistingItem(22, new CustomItem(new MaterialData(Material.REDSTONE), "§7激活状态: §2✔", "", "§e> 点击停止这个机器"));
                        menu.addMenuClickHandler(22, (p, arg1, arg2, arg3) -> {
                            BlockStorage.addBlockInfo(b, "enabled", "false");
                            newInstance(menu, b);
                            return false;
                        });
                    }

                    double offset = (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "offset") == null) ? 3.0D : Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "offset"));

                    menu.replaceExistingItem(31, new CustomItem(new MaterialData(Material.PISTON_BASE), "§7偏移: §3" + offset + " 方块", "", "§r左键点击: §7+0.1", "§r右键点击: §7-0.1"));
                    menu.addMenuClickHandler(31, (p, arg1, arg2, arg3) -> {
                        double offset1 = DoubleHandler.fixDouble(Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "offset")) + (arg3.isRightClicked() ? -0.1F : 0.1F));
                        BlockStorage.addBlockInfo(b, "offset", String.valueOf(offset1));
                        newInstance(menu, b);
                        return false;
                    });
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return (p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) {
                    return getInputSlots();
                }
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(BlockMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow.equals(ItemTransportFlow.INSERT)) {
                    if (SlimefunManager.isItemSimiliar(item, new ItemStack(Material.SOUL_SAND), true)) {
                        return getIronBlockSlots();
                    }
                    return getSnowGolemPumpkinSlots();
                }
                return new int[0];
            }
        };

        registerBlockHandler(name, new SlimefunBlockHandler() {
            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
                BlockStorage.addBlockInfo(b, "offset", "3.0");
                BlockStorage.addBlockInfo(b, "enabled", "false");
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                if (reason.equals(UnregisterReason.EXPLODE)) {
                    return false;
                }
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    for (int slot : getIronBlockSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                            inv.replaceExistingItem(slot, null);
                        }
                    }
                    for (int slot : getSnowGolemPumpkinSlots()) {
                        if (inv.getItemInSlot(slot) != null) {
                            b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                            inv.replaceExistingItem(slot, null);
                        }
                    }
                }
                return true;
            }
        });
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "), (arg0, arg1, arg2, arg3) -> false);
        }

        for (int i : BORDER_1) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 12), " "), (arg0, arg1, arg2, arg3) -> false);
        }

        for (int i : BORDER_2) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0), " "), (arg0, arg1, arg2, arg3) -> false);
        }

        preset.addItem(1, new CustomItem(new ItemStack(Material.PUMPKIN, 1, (short) 1), "§7南瓜槽", "", "§r这个槽位用于放置南瓜"), (arg0, arg1, arg2, arg3) -> false);

        preset.addItem(7, new CustomItem(new MaterialData(Material.IRON_BLOCK), "§7雪槽", "", "§r这个槽位用于放置雪"), (arg0, arg1, arg2, arg3) -> false);

        preset.addItem(13, new CustomItem(new MaterialData(Material.WATCH), "§7冷却: §b30 秒", "", "§r这个机器需要半分钟的时间来作运转准备", "§r请耐心等待!"), (arg0, arg1, arg2, arg3) -> false);
    }

    public String getInventoryTitle() {
        return "§a雪傀儡组装机";
    }

    public int[] getInputSlots() {
        return new int[]{19, 28, 25, 34};
    }

    public int[] getSnowGolemPumpkinSlots() {
        return new int[]{19, 28};
    }

    public int[] getIronBlockSlots() {
        return new int[]{25, 34};
    }

    @Override
    public void register(boolean slimefun) {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(final Block b, SlimefunItem sf, Config data) {
                if ("false".equals(BlockStorage.getLocationInfo(b.getLocation(), "enabled"))) {
                    return;
                }
                if (lifetime % 60 == 0) {
                    if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                        return;
                    }
                    int ironblock = 0;
                    int pumpkin = 0;

                    for (int slot : getIronBlockSlots()) {
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.SNOW_BLOCK), true, SlimefunManager.DataType.ALWAYS)) {
                            ironblock += BlockStorage.getInventory(b).getItemInSlot(slot).getAmount();
                            if (ironblock > 1) {
                                ironblock = 2;
                                break;
                            }
                        }
                    }
                    for (int slot : getSnowGolemPumpkinSlots()) {
                        if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.PUMPKIN), true, SlimefunManager.DataType.ALWAYS)) {
                            pumpkin += BlockStorage.getInventory(b).getItemInSlot(slot).getAmount();
                            if (pumpkin > 0) {
                                pumpkin = 1;
                                break;
                            }
                        }
                    }
                    if (ironblock > 1 && pumpkin > 0) {
                        for (int slot : getIronBlockSlots()) {
                            if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.SNOW_BLOCK), true, SlimefunManager.DataType.ALWAYS)) {
                                int amount = BlockStorage.getInventory(b).getItemInSlot(slot).getAmount();
                                if (amount >= ironblock) {
                                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), ironblock));
                                    break;
                                }
                                ironblock -= amount;
                                BlockStorage.getInventory(b).replaceExistingItem(slot, null);
                            }
                        }


                        for (int slot : getSnowGolemPumpkinSlots()) {
                            if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), new ItemStack(Material.PUMPKIN), true, SlimefunManager.DataType.ALWAYS)) {
                                int amount = BlockStorage.getInventory(b).getItemInSlot(slot).getAmount();
                                if (amount >= pumpkin) {
                                    BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), pumpkin));
                                    break;
                                }
                                pumpkin -= amount;
                                BlockStorage.getInventory(b).replaceExistingItem(slot, null);
                            }
                        }


                        ChargableBlock.addCharge(b, -getEnergyConsumption());

                        final double offset = Double.parseDouble(BlockStorage.getLocationInfo(b.getLocation(), "offset"));

                        Bukkit.getScheduler().runTask(SlimefunStartup.instance, () -> {
                            Snowman snowman = (Snowman) b.getWorld().spawnEntity(new Location(b.getWorld(), b.getX() + 0.5D, b.getY() + offset, b.getZ() + 0.5D), EntityType.SNOWMAN);
                            AttributeInstance snowmanmaxhealth = snowman.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                            snowmanmaxhealth.addModifier(new AttributeModifier("MAX_HEALTH", 20D, AttributeModifier.Operation.ADD_NUMBER));
                            snowman.setHealth(24);
                        });
                    }
                }
            }


            @Override
            public void uniqueTick() {
                lifetime++;
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });

        super.register(slimefun);
    }

    public int getEnergyConsumption() {
        return 1024;
    }

}

@TListener
class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType().equals(EntityType.SNOWMAN)) {
            Snowman snowman = (Snowman) event.getEntity();
            AttributeInstance snowmanmaxhealth = snowman.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (snowmanmaxhealth.getValue() == 24D) {
                int randomInt = RandomUtils.nextInt(1, 101);
                if (randomInt <= 5) {
                    event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), Items.SNOW_GOLEM_HEART);
                }
            }
        }
    }
}
