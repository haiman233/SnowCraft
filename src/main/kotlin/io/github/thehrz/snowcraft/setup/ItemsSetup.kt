package io.github.thehrz.snowcraft.setup

import io.github.thehrz.snowcraft.`object`.machines.*
import io.github.thehrz.snowcraft.list.Items
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem
import me.mrCookieSlime.Slimefun.Lists.RecipeType
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems
import me.mrCookieSlime.Slimefun.Objects.Research
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason
import me.mrCookieSlime.Slimefun.api.Slimefun
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.createLight
import taboolib.module.nms.deleteLight
import taboolib.module.nms.type.LightType

/**
 * @author Thehrz
 */
object ItemsSetup {
    fun setupItems() {
        MagnesiumGenerator(
            CategoriesSetup.SnowCraft_Technology,
            Items.MAGNESIUM_GENERATOR,
            "MAGNESIUM_GENERATOR",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null,
                SlimefunItems.HEATING_COIL,
                null,
                SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.COAL_GENERATOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.COMBUSTION_REACTOR,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.COMBUSTION_REACTOR
            )
        ).registerUnrechargeableBlock(false, 256)
        object : AbstractTreeGrowthAccelerator(
            CategoriesSetup.SnowCraft_Technology,
            Items.TREE_GROWTH_ACCELERATOR,
            "TREE_GROWTH_ACCELERATOR",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.FERTILIZER2,
                ItemStack(
                    Material.WATER_BUCKET
                ),
                SlimefunItems.FERTILIZER2,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.PROGRAMMABLE_ANDROID_WOODCUTTER,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.DURALUMIN_INGOT,
                SlimefunItems.FOOD_COMPOSTER,
                SlimefunItems.DURALUMIN_INGOT
            )
        ) {
            override val energyConsumption: Int
                get() = 32

            override val radius: Int
                get() = 3
        }.registerChargeableBlock(false, 128)
        object : AbstractTreeGrowthAccelerator(
            CategoriesSetup.SnowCraft_Technology,
            Items.TREE_GROWTH_ACCELERATOR_2,
            "TREE_GROWTH_ACCELERATOR_2",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.FERTILIZER2,
                ItemStack(
                    Material.WATER_BUCKET
                ),
                SlimefunItems.FERTILIZER2,
                SlimefunItems.ELECTRIC_MOTOR,
                Items.TREE_GROWTH_ACCELERATOR,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.DAMASCUS_STEEL_INGOT,
                SlimefunItems.ELECTRO_MAGNET,
                SlimefunItems.DAMASCUS_STEEL_INGOT
            )
        ) {
            override val energyConsumption: Int
                get() = 64

            override val radius: Int
                get() = 7
        }.registerChargeableBlock(false, 256)
        object : AbstractTreeGrowthAccelerator(
            CategoriesSetup.SnowCraft_Technology,
            Items.TREE_GROWTH_ACCELERATOR_3,
            "TREE_GROWTH_ACCELERATOR_3",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.FERTILIZER2,
                ItemStack(
                    Material.WATER_BUCKET
                ),
                SlimefunItems.FERTILIZER2,
                SlimefunItems.ELECTRIC_MOTOR,
                Items.TREE_GROWTH_ACCELERATOR_2,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.REINFORCED_ALLOY_INGOT
            )
        ) {
            override val energyConsumption: Int
                get() = 128

            override val radius: Int
                get() = 9
        }.registerChargeableBlock(false, 512)
        object : AbstractTreeGrowthDispenser(
            CategoriesSetup.SnowCraft_Technology,
            Items.TREE_GROWTH_DISPENSER,
            "TREE_GROWTH_DISPENSER",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.CARBONADO,
                SlimefunItems.POWER_CRYSTAL,
                SlimefunItems.CARBONADO,
                SlimefunItems.ELECTRO_MAGNET,
                Items.TREE_GROWTH_ACCELERATOR,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.ELECTRO_MAGNET,
                SlimefunItems.BLISTERING_INGOT_3
            )
        ) {
            override val progressBar: ItemStack
                get() = ItemStack(Material.SAPLING)

            override val energyConsumption: Int
                get() = 512
        }.registerChargeableBlock(false, 2048)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.LANTERN,
            "LANTERN",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.TRIPWIRE_HOOK),
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.TORCH),
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.IRON_INGOT),
                ItemStack(Material.IRON_INGOT)
            ),
            CustomItem(Items.LANTERN, 6)
        ).register(false, object : SlimefunBlockHandler {
            override fun onPlace(p0: Player?, block: Block?, p2: SlimefunItem?) {
                block?.createLight(15, LightType.BLOCK, true)
            }

            override fun onBreak(p0: Player?, block: Block?, p2: SlimefunItem?, p3: UnregisterReason?): Boolean {
                block?.deleteLight(LightType.BLOCK, true)
                return true
            }
        })
        object : AbstractBlockCompresMachine(
            CategoriesSetup.SnowCraft_Technology,
            Items.BLOCK_COMPRES_MACHINE,
            "BLOCK_COMPRES_MACHINE",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.ELECTRIC_MOTOR,
                ItemStack(
                    Material.PISTON_BASE
                ),
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.ALUMINUM_BRASS_INGOT,
                SlimefunItems.HARDENED_METAL_INGOT,
                SlimefunItems.ALUMINUM_BRASS_INGOT,
                null,
                ItemStack(
                    Material.OBSIDIAN
                ),
                null
            )
        ) {
            override fun getEnergyConsumption() = 8

            override fun getSpeed() = 1
        }.registerChargeableBlock(false, 32)
        object : AbstractBlockCompresMachine(
            CategoriesSetup.SnowCraft_Technology,
            Items.BLOCK_COMPRES_MACHINE_2,
            "BLOCK_COMPRES_MACHINE_2",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.ELECTRIC_MOTOR,
                Items.BLOCK_COMPRES_MACHINE,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.REDSTONE_ALLOY,
                SlimefunItems.LARGE_CAPACITOR,
                SlimefunItems.REDSTONE_ALLOY,
                null,
                ItemStack(
                    Material.OBSIDIAN
                ),
                null
            )
        ) {
            override fun getEnergyConsumption() = 32

            override fun getSpeed() = 2
        }.registerChargeableBlock(false, 256)
        object : AbstractBlockCompresMachine(
            CategoriesSetup.SnowCraft_Technology,
            Items.BLOCK_COMPRES_MACHINE_3,
            "BLOCK_COMPRES_MACHINE_3",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.ELECTRIC_MOTOR,
                Items.BLOCK_COMPRES_MACHINE_2,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.CARBONADO_EDGED_CAPACITOR,
                SlimefunItems.BLISTERING_INGOT_3,
                null,
                ItemStack(
                    Material.OBSIDIAN
                ),
                null
            )
        ) {
            override fun getEnergyConsumption() = 64

            override fun getSpeed() = 4
        }.registerChargeableBlock(false, 512)
        OriginalAutomatedCraftingChamber(
            CategoriesSetup.SnowCraft_Technology,
            Items.ORIGINAL_AUTOMATED_CRAFTING_CHAMBER,
            "ORIGINAL_AUTOMATED_CRAFTING_CHAMBER",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null,
                SlimefunItems.POWER_CRYSTAL,
                null,
                SlimefunItems.ELECTRIC_MOTOR,
                ItemStack(
                    Material.WORKBENCH
                ),
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.CARGO_MOTOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT
            )
        ).registerChargeableBlock(true, 256)
        IronGolemAssembler(
            CategoriesSetup.SnowCraft_Technology,
            Items.IRON_GOLEM_ASSEMBLER,
            "IRON_GOLEM_ASSEMBLER",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                SlimefunItems.BLISTERING_INGOT_3,
                SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                SlimefunItems.HARDENED_GLASS,
                SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                SlimefunItems.HARDENED_GLASS,
                SlimefunItems.GILDED_IRON,
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.GILDED_IRON
            )
        ).registerChargeableBlock(false, 4096)
        SnowGolemAssembler(
            CategoriesSetup.SnowCraft_Technology,
            Items.SNOW_GOLEM_ASSEMBLER,
            "SNOW_GOLEM_ASSEMBLER",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null,
                SlimefunItems.POWER_CRYSTAL,
                null,
                SlimefunItems.ELECTRIC_MOTOR,
                ItemStack(
                    Material.WORKBENCH
                ),
                SlimefunItems.ELECTRIC_MOTOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT,
                SlimefunItems.CARGO_MOTOR,
                SlimefunItems.REINFORCED_ALLOY_INGOT
            )
        ).registerChargeableBlock(true, 2048)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.ICE_CREAM,
            "SNOWCRAFT_ICE_CREAM",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf<ItemStack>()
        ).register(false)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.ANDROID_PANEL,
            "ANDROID_PANEL",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null,
                ItemStack(
                    Material.THIN_GLASS
                ),
                ItemStack(Material.REDSTONE),
                SlimefunItems.PLASTIC_SHEET,
                SlimefunItems.ADVANCED_CIRCUIT_BOARD,
                ItemStack(
                    Material.THIN_GLASS
                ),
                null,
                SlimefunItems.PLASTIC_SHEET,
                null
            )
        ).register(false)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_HEART,
            "SNOW_GOLEM_HEART",
            RecipeType.MOB_DROP,
            arrayOf<ItemStack?>(
                null, null, null, null, CustomItem(
                    ItemStack(Material.SNOW_BALL), "§4击杀由雪傀儡组装机组装的雪傀儡获得", ""
                ), null, null, null, null
            )
        ).register(true)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_HELMET,
            "SNOW_GOLEM_HELMET",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART,
                null,
                null,
                null
            )
        ).register(true)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_CHESTPLATE,
            "SNOW_GOLEM_CHESTPLATE",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART
            )
        ).register(true)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_LEGGINGS,
            "SNOW_GOLEM_LEGGINGS",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART
            )
        ).register(true)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_BOOTS,
            "SNOW_GOLEM_BOOTS",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                null,
                null,
                null,
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART,
                Items.SNOW_GOLEM_HEART,
                null,
                Items.SNOW_GOLEM_HEART
            )
        ).register(true)
        SlimefunItem(
            CategoriesSetup.SnowCraft_Misc,
            Items.SNOW_GOLEM_STICK,
            "SNOW_GOLEM_STICK",
            RecipeType.ENHANCED_CRAFTING_TABLE,
            arrayOf(
                SlimefunItems.MAGIC_LUMP_3, SlimefunItems.BATTERY, SlimefunItems.MAGIC_LUMP_3, ItemStack(
                    Material.SNOW_BLOCK
                ), SlimefunItems.STAFF_WATER, ItemStack(Material.SNOW_BLOCK), null, SlimefunItems.STAFF_ELEMENTAL, null
            )
        ).register(false)
        Slimefun.registerResearch(
            Research(3000, "§bBLOCK_COMPRES_MACHINE", 32),
            Items.BLOCK_COMPRES_MACHINE,
            Items.BLOCK_COMPRES_MACHINE_2,
            Items.BLOCK_COMPRES_MACHINE_3
        )
        Slimefun.registerResearch(Research(3001, "§bLANTERN", 16), Items.LANTERN)
        Slimefun.registerResearch(Research(3002, "§bANDROID_PANEL", 128), Items.ANDROID_PANEL)
        Slimefun.registerResearch(Research(3003, "§aIRON_GOLEM_PROGRAM", 16), Items.IRON_GOLEM_ASSEMBLER)
        Slimefun.registerResearch(Research(3004, "§cMAGNESIUM_GENERATOR", 32), Items.MAGNESIUM_GENERATOR)
        Slimefun.registerResearch(
            Research(3005, "§bTREE_GROWTH_ACCELERATOR", 64),
            Items.TREE_GROWTH_ACCELERATOR,
            Items.TREE_GROWTH_ACCELERATOR_2,
            Items.TREE_GROWTH_ACCELERATOR_3
        )
        Slimefun.registerResearch(Research(3006, "§bTREE_GROWTH_DISPENSER", 64), Items.TREE_GROWTH_DISPENSER)
        Slimefun.registerResearch(
            Research(3007, "§bORIGINAL_AUTOMATED_CRAFTING_CHAMBER", 32),
            Items.ORIGINAL_AUTOMATED_CRAFTING_CHAMBER
        )
        Slimefun.registerResearch(Research(3008, "§aSNOW_GOLEM_ASSEMBLER", 32), Items.SNOW_GOLEM_ASSEMBLER)
        Slimefun.registerResearch(Research(3009, "§aSNOW_GOLEM_HEART", 8), Items.SNOW_GOLEM_HEART)
        Slimefun.registerResearch(
            Research(3010, "§aSNOW_GOLEM_SUIT", 16),
            Items.SNOW_GOLEM_HELMET,
            Items.SNOW_GOLEM_CHESTPLATE,
            Items.SNOW_GOLEM_LEGGINGS,
            Items.SNOW_GOLEM_BOOTS
        )
        Slimefun.registerResearch(Research(3011, "§aSNOW_GOLEM_STICK", 16), Items.SNOW_GOLEM_STICK)
        Slimefun.registerResearch(Research(3012, "§aSNOWCRAFT_ICE_CREAM", 4), Items.ICE_CREAM)
    }
}