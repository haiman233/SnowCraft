package io.github.thehrz.snowcraft.object.machines;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.MC_1_8.ParticleEffect;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ItemDye;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import org.bukkit.material.Sapling;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Thehrz
 */
public abstract class AbstractTreeGrowthDispenser extends SlimefunItem {
    private static final int[] BORDER = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    public static Map<Block, MachineRecipe> processing = new HashMap<>();

    public static Map<Block, Integer> progress = new HashMap<>();

    public AbstractTreeGrowthDispenser(Category category, ItemStack item, String id, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, id, recipeType, recipe);
        new BlockMenuPreset(id, "§a树木肥料发射器") {
            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu blockMenu, Block block) {
            }

            @Override
            public boolean canOpen(Block block, Player player) {
                return player.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(player.getUniqueId(), block, true);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow itemTransportFlow) {
                if (itemTransportFlow.equals(ItemTransportFlow.INSERT)) {
                    return getInputSlots();
                }
                return new int[0];
            }
        };

        AbstractTreeGrowthDispenser.registerBlockHandler(id, new SlimefunBlockHandler() {
            @Override
            public void onPlace(Player player, Block block, SlimefunItem slimefunItem) {

            }

            @Override
            public boolean onBreak(Player player, Block block, SlimefunItem slimefunItem, UnregisterReason unregisterReason) {
                BlockMenu inv = BlockStorage.getInventory(block);
                if (inv != null) {
                    for (int slot : getInputSlots()) {
                        if (inv.getItemInSlot(slot) == null) {
                            continue;
                        }
                        block.getWorld().dropItemNaturally(block.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }

                AbstractTreeGrowthDispenser.progress.remove(block);
                AbstractTreeGrowthDispenser.processing.remove(block);
                return true;
            }
        });
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9), " "), (arg0, arg1, arg2, arg3) -> false);
        }
    }

    public abstract ItemStack getProgressBar();

    public abstract int getEnergyConsumption();

    public int[] getInputSlots() {
        return new int[]{10, 11, 12, 13, 14, 15, 16};
    }

    public boolean isProcessing(Block b) {
        return (getProcessing(b) != null);
    }

    public MachineRecipe getProcessing(Block b) {
        return processing.get(b);
    }


    @Override
    public void register(boolean slimefun) {
        this.addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                try {
                    AbstractTreeGrowthDispenser.this.tick(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void uniqueTick() {
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });

        super.register(slimefun);
    }

    protected void tick(Block block) throws Exception {
        BlockMenu blockMenu = BlockStorage.getInventory(block);
        if (ChargableBlock.getCharge(block) >= ChargableBlock.getMaxCharge(block)) {
            Directional directional = (Directional) block.getState().getData();
            Block b = block.getRelative(directional.getFacing());
            if (!b.getType().equals(Material.SAPLING)) {
                return;
            }
            Sapling sapling = (Sapling) b.getState().getData();
            grow(block, b, blockMenu, sapling);

        }
    }

    private void grow(Block machine, Block block, BlockMenu blockMenu, Sapling sapling) throws Exception {
        for (int slot : getInputSlots()) {
            if (SlimefunManager.isItemSimiliar(blockMenu.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
                ChargableBlock.addCharge(machine, -getEnergyConsumption());
                block.getState().setData(sapling);
                Class<?> aClass = Class.forName("net.minecraft.server.v1_12_R1.ItemDye");
                ItemDye itemDye = (ItemDye) aClass.getDeclaredConstructor().newInstance();
                aClass.getDeclaredMethod("a", net.minecraft.server.v1_12_R1.ItemStack.class, World.class, BlockPosition.class)
                        .invoke(itemDye, new net.minecraft.server.v1_12_R1.ItemStack(CraftMagicNumbers.getBlock(block)), ((CraftWorld) block.getWorld()).getHandle(), new BlockPosition(block.getX(), block.getY(), block.getZ()));
                blockMenu.replaceExistingItem(slot, InvUtils.decreaseItem(blockMenu.getItemInSlot(slot), 1));
                ParticleEffect.VILLAGER_HAPPY.display(block.getLocation().add(0.5D, 0.5D, 0.5D), 0.1F, 0.1F, 0.1F, 0.0F, 4);
                break;
            }
        }
    }
}
