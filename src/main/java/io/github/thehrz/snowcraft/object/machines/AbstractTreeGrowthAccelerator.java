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
import org.bukkit.material.Sapling;

/**
 * @author Thehrz
 */
public abstract class AbstractTreeGrowthAccelerator extends SlimefunItem {
    private static final int[] BORDER = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    public AbstractTreeGrowthAccelerator(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        new BlockMenuPreset(name, "§b树木生长加速器") {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b, true);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow.equals(ItemTransportFlow.INSERT)) {
                    return getInputSlots();
                }
                return new int[0];
            }
        };
        AbstractTreeGrowthAccelerator.registerBlockHandler(name, new SlimefunBlockHandler() {

            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                BlockMenu inv = BlockStorage.getInventory(b);
                if (inv != null) {
                    for (int slot : getInputSlots()) {
                        if (inv.getItemInSlot(slot) == null) {
                            continue;
                        }
                        b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }
                return true;
            }
        });
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 9), " "), (arg0, arg1, arg2, arg3) -> false);
        }
    }

    public abstract int getEnergyConsumption();

    public abstract int getRadius();

    public int[] getInputSlots() {
        return new int[]{10, 11, 12, 13, 14, 15, 16};
    }

    @Override
    public void register(boolean slimefun) {
        this.addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                try {
                    AbstractTreeGrowthAccelerator.this.tick(b);
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
            for (int x = -getRadius(); x <= getRadius(); x++) {
                for (int z = -getRadius(); z <= getRadius(); z++) {
                    Block b = block.getRelative(x, 0, z);
                    if (b.getType().equals(Material.SAPLING)) {
                        Sapling sapling = (Sapling) b.getState().getData();
                        grow(block, b, blockMenu, sapling);
                    }
                }
            }
        }
    }

    public static void growSapling(Block block) throws ClassNotFoundException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Class<?> aClass = Class.forName("net.minecraft.server.v1_12_R1.ItemDye");
        ItemDye itemDye = (ItemDye) aClass.getDeclaredConstructor().newInstance();
        aClass.getDeclaredMethod("a", net.minecraft.server.v1_12_R1.ItemStack.class, World.class, BlockPosition.class)
                .invoke(itemDye, new net.minecraft.server.v1_12_R1.ItemStack(CraftMagicNumbers.getBlock(block)), ((CraftWorld) block.getWorld()).getHandle(), new BlockPosition(block.getX(), block.getY(), block.getZ()));
    }

    private void grow(Block machine, Block block, BlockMenu blockMenu, Sapling sapling) throws Exception {
        for (int slot : getInputSlots()) {
            if (SlimefunManager.isItemSimiliar(blockMenu.getItemInSlot(slot), SlimefunItems.FERTILIZER, false)) {
                ChargableBlock.addCharge(machine, -getEnergyConsumption());
                block.getState().setData(sapling);
                growSapling(block);
                blockMenu.replaceExistingItem(slot, InvUtils.decreaseItem(blockMenu.getItemInSlot(slot), 1));
                ParticleEffect.VILLAGER_HAPPY.display(block.getLocation().add(0.5D, 0.5D, 0.5D), 0.1F, 0.1F, 0.1F, 0.0F, 4);
                break;
            }
        }
    }
}