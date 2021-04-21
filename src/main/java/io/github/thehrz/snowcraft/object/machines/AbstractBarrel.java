package io.github.thehrz.snowcraft.object.machines;

import io.github.thehrz.snowcraft.SnowCraft;
import io.izzel.taboolib.util.item.ItemBuilder;
import io.izzel.taboolib.util.item.Items;
import io.izzel.taboolib.util.item.inventory.MenuBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Misc.compatibles.ProtectionUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */
public abstract class AbstractBarrel extends SlimefunItem {
    private static final int[] BORDER = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 13, 14, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    public AbstractBarrel(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);

        new BlockMenuPreset(id, getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu blockMenu, Block block) {
                if (BlockStorage.getLocationInfo(block.getLocation(), "amount") == null) {
                    BlockStorage.addBlockInfo(block, "amount", "0");
                }
            }

            @Override
            public boolean canOpen(Block block, Player player) {
                boolean perm = (player.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(player.getUniqueId(), block, true));
                return (perm && ProtectionUtils.canAccessItem(player, block));
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                if (itemTransportFlow.equals(ItemTransportFlow.INSERT)) {
                    return getInputSlots();
                }
                return getOutputSlots();
            }
        };

        registerBlockHandler(id, new SlimefunBlockHandler() {
            @Override
            public void onPlace(Player player, Block block, SlimefunItem slimefunItem) {

            }

            @Override
            public boolean onBreak(Player player, Block block, SlimefunItem slimefunItem, UnregisterReason unregisterReason) {
                int amount = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount"));

                for (int slot : getInputSlots()) {
                    if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                        amount += BlockStorage.getInventory(block).getItemInSlot(slot).getAmount();
                    }
                }

                for (int slot : getOutputSlots()) {
                    if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                        amount += BlockStorage.getInventory(block).getItemInSlot(slot).getAmount();
                    }
                }

                if (amount == 0) {
                    return true;
                } else {
                    int finalAmount = amount;
                    new MenuBuilder(SnowCraft.getInstance().getPlugin()).title("§4确认破坏此方块?").rows(3).buildAsync(inventory -> inventory.setItem(13, new ItemBuilder(Material.REDSTONE).name("§4确认?").lore("", "§7该方块内还有 §4" + finalAmount + " §7个 §b" + Items.getName(Items.fromJson(BlockStorage.getLocationInfo(block.getLocation(), "item-type"))), "", "§7点击清空方块内物品并破坏方块").build())).event(clickEvent -> {
                        if (clickEvent.getRawSlot() == 13) {
                            block.getWorld().dropItemNaturally(block.getLocation(), BlockStorage.check(block).getItem());
                            BlockStorage.clearBlockInfo(block);
                            block.setType(Material.AIR);
                            player.closeInventory();
                        }
                    }).lockHand(true).open(player);
                }
                return false;
            }
        });
    }

    public int[] getInputSlots() {
        return new int[]{10, 11};
    }

    public int[] getOutputSlots() {
        return new int[]{15, 16};
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7), " "), (player, slot, itemStack, clickAction) -> false);
        }

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {
                @Override
                public boolean onClick(InventoryClickEvent inventoryClickEvent, Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR);
                }

                @Override
                public boolean onClick(Player player, int i, ItemStack itemStack, ClickAction clickAction) {
                    return false;
                }
            });
        }

        preset.addMenuClickHandler(13, (player, i, itemStack, clickAction) -> false);
    }

    @Override
    public void register(boolean slimefun) {
        addItemHandler(new BlockTicker() {
            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void uniqueTick() {

            }

            @Override
            public void tick(Block block, SlimefunItem slimefunItem, Config config) {
                AbstractBarrel.this.tick(block);
            }
        });

        super.register(slimefun);
    }

    protected void tick(Block block) {

        ItemStack material = null;

        if (BlockStorage.getLocationInfo(block.getLocation(), "item-type") != null && Items.fromJson(BlockStorage.getLocationInfo(block.getLocation(), "item-type")) != null) {
            material = Items.fromJson(BlockStorage.getLocationInfo(block.getLocation(), "item-type"));
        }

        if (material != null) {
            BlockStorage.getInventory(block).replaceExistingItem(13, new CustomItem(material, "§a目标方块: §e" + Items.getName(material), "", "§e容量: §7" + BlockStorage.getLocationInfo(block.getLocation(), "amount") + "/" + getBarrelCapacity(), ""));
        }

        for (int slot : getInputSlots()) {
            if (BlockStorage.getInventory(block).getItemInSlot(slot) != null) {
                ItemStack itemStack = BlockStorage.getInventory(block).getItemInSlot(slot).clone();
                ItemStack singleItemStack = new ItemBuilder(itemStack.clone()).amount(1).build();

                if (BlockStorage.getLocationInfo(block.getLocation(), "item-type") == null) {
                    material = itemStack;
                    BlockStorage.getInventory(block).replaceExistingItem(13, new CustomItem(material, "§a目标方块: §e" + Items.getName(material), "", "§e容量: §7" + BlockStorage.getLocationInfo(block.getLocation(), "amount") + "/" + getBarrelCapacity(), ""));
                    BlockStorage.addBlockInfo(block, "item-type", Items.toJson(singleItemStack));
                }

                if (singleItemStack.equals(Items.fromJson(BlockStorage.getLocationInfo(block.getLocation(), "item-type")))) {
                    if (Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount")) + itemStack.getAmount() > getBarrelCapacity()) {
                        BlockStorage.getInventory(block).getItemInSlot(slot).setAmount(itemStack.getAmount() - (getBarrelCapacity() - Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount"))));
                        BlockStorage.addBlockInfo(block, "amount", String.valueOf(getBarrelCapacity()));
                    } else if (Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount")) + itemStack.getAmount() <= getBarrelCapacity()) {
                        BlockStorage.getInventory(block).replaceExistingItem(slot, null);
                        BlockStorage.addBlockInfo(block, "amount", (Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount")) + itemStack.getAmount() == getBarrelCapacity()) ? String.valueOf(getBarrelCapacity()) : String.valueOf(Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount")) + itemStack.getAmount()));
                    }

                }
            }
        }

        if (material != null && !"0".equals(BlockStorage.getLocationInfo(block.getLocation(), "amount"))) {
            for (int slot : getOutputSlots()) {

                int barrelItemAmount = Integer.parseInt(BlockStorage.getLocationInfo(block.getLocation(), "amount"));

                if (BlockStorage.getInventory(block).getItemInSlot(slot) == null) {
                    if (barrelItemAmount >= material.getMaxStackSize()) {
                        BlockStorage.getInventory(block).replaceExistingItem(slot, new ItemBuilder(material.clone()).amount(material.getMaxStackSize()).build());
                        BlockStorage.addBlockInfo(block, "amount", String.valueOf(barrelItemAmount - material.getMaxStackSize()));
                    } else {
                        BlockStorage.getInventory(block).replaceExistingItem(slot, new ItemBuilder(material.clone()).amount(barrelItemAmount).build());
                        BlockStorage.addBlockInfo(block, "amount", String.valueOf(0));
                    }
                } else if (new ItemBuilder(BlockStorage.getInventory(block).getItemInSlot(slot).clone()).amount(1).build().equals(material) &&
                        BlockStorage.getInventory(block).getItemInSlot(slot).getAmount() != BlockStorage.getInventory(block).getItemInSlot(slot).getMaxStackSize()) {
                    ItemStack itemStack = BlockStorage.getInventory(block).getItemInSlot(slot);
                    int amount = material.getMaxStackSize() - itemStack.getAmount();

                    BlockStorage.getInventory(block).replaceExistingItem(slot, new ItemBuilder(material.clone()).amount(material.getMaxStackSize()).build());
                    BlockStorage.addBlockInfo(block, "amount", String.valueOf(barrelItemAmount - amount));
                }
            }
        }
    }

    public abstract String getInventoryTitle();

    public abstract int getBarrelCapacity();
}
