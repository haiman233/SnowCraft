package io.github.thehrz.snowcraft.setup;

import io.github.thehrz.snowcraft.list.Items;
import io.github.thehrz.snowcraft.object.machines.*;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.module.light.TLight;
import io.izzel.taboolib.module.nms.impl.Type;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.Lists.NarItems;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author Thehrz
 */
@TListener
public class ItemsSetup implements Listener {
    public static void setupItems() {
        new SlimefunItem(CategoriesSetup.SnowCraft_Misc, Items.LANTERN, "LANTERN", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{new ItemStack(Material.IRON_INGOT), new ItemStack(Material.TRIPWIRE_HOOK), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.TORCH), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)}, new CustomItem(Items.LANTERN, 6)).register(false, new SlimefunBlockHandler() {
            @Override
            public void onPlace(Player player, Block block, SlimefunItem slimefunItem) {
                TLight.create(block, Type.BLOCK, 15);
            }

            @Override
            public boolean onBreak(Player player, Block block, SlimefunItem slimefunItem, UnregisterReason unregisterReason) {
                TLight.delete(block.getLocation(), Type.BLOCK);
                return true;
            }
        });
        new SlimefunItem(CategoriesSetup.SnowCraft_Misc, Items.ANDROID_PANEL, "ANDROID_PANEL", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, new ItemStack(Material.THIN_GLASS), new ItemStack(Material.REDSTONE), SlimefunItems.PLASTIC_SHEET, NarItems.UU, new ItemStack(Material.THIN_GLASS), null, SlimefunItems.PLASTIC_SHEET, null}).register(false);
        new IronGolemAssembler(CategoriesSetup.SnowCraft_Technology, Items.IRON_GOLEM_ASSEMBLER, "IRON_GOLEM_ASSEMBLER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.HARDENED_GLASS, SlimefunItems.ADVANCED_CIRCUIT_BOARD, SlimefunItems.HARDENED_GLASS, SlimefunItems.GILDED_IRON, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.GILDED_IRON}).registerChargeableBlock(false, 4096);
        new MagnesiumGenerator(CategoriesSetup.SnowCraft_Technology, Items.MAGNESIUM_GENERATOR, "MAGNESIUM_GENERATOR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.HEATING_COIL, null, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.COAL_GENERATOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.COMBUSTION_REACTOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.COMBUSTION_REACTOR}).registerUnrechargeableBlock(false, 256);
        new AbstractTreeGrowthAccelerator(CategoriesSetup.SnowCraft_Technology, Items.TREE_GROWTH_ACCELERATOR, "TREE_GROWTH_ACCELERATOR", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.FERTILIZER2, new ItemStack(Material.WATER_BUCKET), SlimefunItems.FERTILIZER2, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DURALUMIN_INGOT, SlimefunItems.FOOD_COMPOSTER, SlimefunItems.DURALUMIN_INGOT}) {
            @Override
            public int getEnergyConsumption() {
                return 32;
            }

            @Override
            public int getRadius() {
                return 3;
            }
        }.registerChargeableBlock(false, 128);
        new AbstractTreeGrowthAccelerator(CategoriesSetup.SnowCraft_Technology, Items.TREE_GROWTH_ACCELERATOR_2, "TREE_GROWTH_ACCELERATOR_2", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.FERTILIZER2, new ItemStack(Material.WATER_BUCKET), SlimefunItems.FERTILIZER2, SlimefunItems.ELECTRIC_MOTOR, Items.TREE_GROWTH_ACCELERATOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.DAMASCUS_STEEL_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.DAMASCUS_STEEL_INGOT}) {
            @Override
            public int getEnergyConsumption() {
                return 64;
            }

            @Override
            public int getRadius() {
                return 7;
            }
        }.registerChargeableBlock(false, 256);
        new AbstractTreeGrowthAccelerator(CategoriesSetup.SnowCraft_Technology, Items.TREE_GROWTH_ACCELERATOR_3, "TREE_GROWTH_ACCELERATOR_3", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.FERTILIZER2, new ItemStack(Material.WATER_BUCKET), SlimefunItems.FERTILIZER2, SlimefunItems.ELECTRIC_MOTOR, Items.TREE_GROWTH_ACCELERATOR_2, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.REINFORCED_ALLOY_INGOT}) {
            @Override
            public int getEnergyConsumption() {
                return 128;
            }

            @Override
            public int getRadius() {
                return 9;
            }
        }.registerChargeableBlock(false, 512);
        new AbstractTreeGrowthDispenser(CategoriesSetup.SnowCraft_Technology, Items.TREE_GROWTH_DISPENSER, "TREE_GROWTH_DISPENSER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.CARBONADO, SlimefunItems.POWER_CRYSTAL, SlimefunItems.CARBONADO, SlimefunItems.ELECTRO_MAGNET, Items.TREE_GROWTH_ACCELERATOR, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.BLISTERING_INGOT_3}) {
            @Override
            public ItemStack getProgressBar() {
                return new ItemStack(Material.SAPLING);
            }

            @Override
            public int getEnergyConsumption() {
                return 512;
            }
        }.registerChargeableBlock(false, 2048);
        new AbstractBlockCompresMachine(CategoriesSetup.SnowCraft_Technology, Items.BLOCK_COMPRES_MACHINE, "BLOCK_COMPRES_MACHINE", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.PISTON_BASE), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.ALUMINUM_BRASS_INGOT, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ALUMINUM_BRASS_INGOT, null, new ItemStack(Material.OBSIDIAN), null}) {
            @Override
            public int getEnergyConsumption() {
                return 8;
            }

            @Override
            public int getSpeed() {
                return 1;
            }
        }.registerChargeableBlock(false, 32);
        new AbstractBlockCompresMachine(CategoriesSetup.SnowCraft_Technology, Items.BLOCK_COMPRES_MACHINE_2, "BLOCK_COMPRES_MACHINE_2", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.ELECTRIC_MOTOR, Items.BLOCK_COMPRES_MACHINE, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REDSTONE_ALLOY, SlimefunItems.LARGE_CAPACITOR, SlimefunItems.REDSTONE_ALLOY, null, new ItemStack(Material.OBSIDIAN), null}) {
            @Override
            public int getEnergyConsumption() {
                return 32;
            }

            @Override
            public int getSpeed() {
                return 2;
            }
        }.registerChargeableBlock(false, 256);
        new AbstractBlockCompresMachine(CategoriesSetup.SnowCraft_Technology, Items.BLOCK_COMPRES_MACHINE_3, "BLOCK_COMPRES_MACHINE_3", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{SlimefunItems.ELECTRIC_MOTOR, Items.BLOCK_COMPRES_MACHINE_2, SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.BLISTERING_INGOT_3, SlimefunItems.CARBONADO_EDGED_CAPACITOR, SlimefunItems.BLISTERING_INGOT_3, null, new ItemStack(Material.OBSIDIAN), null}) {
            @Override
            public int getEnergyConsumption() {
                return 64;
            }

            @Override
            public int getSpeed() {
                return 4;
            }
        }.registerChargeableBlock(false, 512);
        (new OriginalAutomatedCraftingChamber(CategoriesSetup.SnowCraft_Technology, Items.ORIGINAL_AUTOMATED_CRAFTING_CHAMBER, "ORIGINAL_AUTOMATED_CRAFTING_CHAMBER", RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{null, SlimefunItems.POWER_CRYSTAL, null, SlimefunItems.ELECTRIC_MOTOR, new ItemStack(Material.WORKBENCH), SlimefunItems.ELECTRIC_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT, SlimefunItems.CARGO_MOTOR, SlimefunItems.REINFORCED_ALLOY_INGOT})).registerChargeableBlock(true, 256);

        Slimefun.registerResearch(new Research(3000, "§bBLOCK_COMPRES_MACHINE", 32), Items.BLOCK_COMPRES_MACHINE, Items.BLOCK_COMPRES_MACHINE_2, Items.BLOCK_COMPRES_MACHINE_3);
        Slimefun.registerResearch(new Research(3001, "§bLANTERN", 16), Items.LANTERN);
        Slimefun.registerResearch(new Research(3002, "§bANDROID_PANEL", 128), Items.ANDROID_PANEL);
        Slimefun.registerResearch(new Research(3003, "§aIRON_GOLEM_PROGRAM", 16), Items.IRON_GOLEM_ASSEMBLER);
        Slimefun.registerResearch(new Research(3004, "§cMAGNESIUM_GENERATOR", 32), Items.MAGNESIUM_GENERATOR);
        Slimefun.registerResearch(new Research(3005, "§bTREE_GROWTH_ACCELERATOR", 64), Items.TREE_GROWTH_ACCELERATOR, Items.TREE_GROWTH_ACCELERATOR_2, Items.TREE_GROWTH_ACCELERATOR_3);
        Slimefun.registerResearch(new Research(3006, "§bTREE_GROWTH_DISPENSER", 64), Items.TREE_GROWTH_DISPENSER);
        Slimefun.registerResearch(new Research(3007, "§bORIGINAL_AUTOMATED_CRAFTING_CHAMBER", 32), Items.ORIGINAL_AUTOMATED_CRAFTING_CHAMBER);
    }
}