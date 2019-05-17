package au.com.addstar.serversigns.utils;

import java.util.HashMap;

import org.bukkit.Material;

public class MaterialConvertor {
    private static final HashMap<Integer, String> idMap = new HashMap();

    static {
        idMap.put(Integer.valueOf(0), "AIR");
        idMap.put(Integer.valueOf(1), "STONE");
        idMap.put(Integer.valueOf(2), "GRASS");
        idMap.put(Integer.valueOf(3), "DIRT");
        idMap.put(Integer.valueOf(4), "COBBLESTONE");
        idMap.put(Integer.valueOf(5), "WOOD");
        idMap.put(Integer.valueOf(6), "SAPLING");
        idMap.put(Integer.valueOf(7), "BEDROCK");
        idMap.put(Integer.valueOf(8), "WATER");
        idMap.put(Integer.valueOf(9), "STATIONARY_WATER");
        idMap.put(Integer.valueOf(10), "LAVA");
        idMap.put(Integer.valueOf(11), "STATIONARY_LAVA");
        idMap.put(Integer.valueOf(12), "SAND");
        idMap.put(Integer.valueOf(13), "GRAVEL");
        idMap.put(Integer.valueOf(14), "GOLD_ORE");
        idMap.put(Integer.valueOf(15), "IRON_ORE");
        idMap.put(Integer.valueOf(16), "COAL_ORE");
        idMap.put(Integer.valueOf(17), "LOG");
        idMap.put(Integer.valueOf(18), "LEAVES");
        idMap.put(Integer.valueOf(19), "SPONGE");
        idMap.put(Integer.valueOf(20), "GLASS");
        idMap.put(Integer.valueOf(21), "LAPIS_ORE");
        idMap.put(Integer.valueOf(22), "LAPIS_BLOCK");
        idMap.put(Integer.valueOf(23), "DISPENSER");
        idMap.put(Integer.valueOf(24), "SANDSTONE");
        idMap.put(Integer.valueOf(25), "NOTE_BLOCK");
        idMap.put(Integer.valueOf(26), "BED_BLOCK");
        idMap.put(Integer.valueOf(27), "POWERED_RAIL");
        idMap.put(Integer.valueOf(28), "DETECTOR_RAIL");
        idMap.put(Integer.valueOf(29), "PISTON_STICKY_BASE");
        idMap.put(Integer.valueOf(30), "WEB");
        idMap.put(Integer.valueOf(31), "LONG_GRASS");
        idMap.put(Integer.valueOf(32), "DEAD_BUSH");
        idMap.put(Integer.valueOf(33), "PISTON_BASE");
        idMap.put(Integer.valueOf(34), "PISTON_EXTENSION");
        idMap.put(Integer.valueOf(35), "WOOL");
        idMap.put(Integer.valueOf(36), "PISTON_MOVING_PIECE");
        idMap.put(Integer.valueOf(37), "YELLOW_FLOWER");
        idMap.put(Integer.valueOf(38), "RED_ROSE");
        idMap.put(Integer.valueOf(39), "BROWN_MUSHROOM");
        idMap.put(Integer.valueOf(40), "RED_MUSHROOM");
        idMap.put(Integer.valueOf(41), "GOLD_BLOCK");
        idMap.put(Integer.valueOf(42), "IRON_BLOCK");
        idMap.put(Integer.valueOf(43), "DOUBLE_STEP");
        idMap.put(Integer.valueOf(44), "STEP");
        idMap.put(Integer.valueOf(45), "BRICK");
        idMap.put(Integer.valueOf(46), "TNT");
        idMap.put(Integer.valueOf(47), "BOOKSHELF");
        idMap.put(Integer.valueOf(48), "MOSSY_COBBLESTONE");
        idMap.put(Integer.valueOf(49), "OBSIDIAN");
        idMap.put(Integer.valueOf(50), "TORCH");
        idMap.put(Integer.valueOf(51), "FIRE");
        idMap.put(Integer.valueOf(52), "MOB_SPAWNER");
        idMap.put(Integer.valueOf(53), "WOOD_STAIRS");
        idMap.put(Integer.valueOf(54), "CHEST");
        idMap.put(Integer.valueOf(55), "REDSTONE_WIRE");
        idMap.put(Integer.valueOf(56), "DIAMOND_ORE");
        idMap.put(Integer.valueOf(57), "DIAMOND_BLOCK");
        idMap.put(Integer.valueOf(58), "WORKBENCH");
        idMap.put(Integer.valueOf(59), "CROPS");
        idMap.put(Integer.valueOf(60), "SOIL");
        idMap.put(Integer.valueOf(61), "FURNACE");
        idMap.put(Integer.valueOf(62), "BURNING_FURNACE");
        idMap.put(Integer.valueOf(63), "SIGN_POST");
        idMap.put(Integer.valueOf(64), "WOODEN_DOOR");
        idMap.put(Integer.valueOf(65), "LADDER");
        idMap.put(Integer.valueOf(66), "RAILS");
        idMap.put(Integer.valueOf(67), "COBBLESTONE_STAIRS");
        idMap.put(Integer.valueOf(68), "WALL_SIGN");
        idMap.put(Integer.valueOf(69), "LEVER");
        idMap.put(Integer.valueOf(70), "STONE_PLATE");
        idMap.put(Integer.valueOf(71), "IRON_DOOR_BLOCK");
        idMap.put(Integer.valueOf(72), "WOOD_PLATE");
        idMap.put(Integer.valueOf(73), "REDSTONE_ORE");
        idMap.put(Integer.valueOf(74), "GLOWING_REDSTONE_ORE");
        idMap.put(Integer.valueOf(75), "REDSTONE_TORCH_OFF");
        idMap.put(Integer.valueOf(76), "REDSTONE_TORCH_ON");
        idMap.put(Integer.valueOf(77), "STONE_BUTTON");
        idMap.put(Integer.valueOf(78), "SNOW");
        idMap.put(Integer.valueOf(79), "ICE");
        idMap.put(Integer.valueOf(80), "SNOW_BLOCK");
        idMap.put(Integer.valueOf(81), "CACTUS");
        idMap.put(Integer.valueOf(82), "CLAY");
        idMap.put(Integer.valueOf(83), "SUGAR_CANE_BLOCK");
        idMap.put(Integer.valueOf(84), "JUKEBOX");
        idMap.put(Integer.valueOf(85), "FENCE");
        idMap.put(Integer.valueOf(86), "PUMPKIN");
        idMap.put(Integer.valueOf(87), "NETHERRACK");
        idMap.put(Integer.valueOf(88), "SOUL_SAND");
        idMap.put(Integer.valueOf(89), "GLOWSTONE");
        idMap.put(Integer.valueOf(90), "PORTAL");
        idMap.put(Integer.valueOf(91), "JACK_O_LANTERN");
        idMap.put(Integer.valueOf(92), "CAKE_BLOCK");
        idMap.put(Integer.valueOf(93), "DIODE_BLOCK_OFF");
        idMap.put(Integer.valueOf(94), "DIODE_BLOCK_ON");
        idMap.put(Integer.valueOf(95), "STAINED_GLASS");
        idMap.put(Integer.valueOf(96), "TRAP_DOOR");
        idMap.put(Integer.valueOf(97), "MONSTER_EGGS");
        idMap.put(Integer.valueOf(98), "SMOOTH_BRICK");
        idMap.put(Integer.valueOf(99), "HUGE_MUSHROOM_1");
        idMap.put(Integer.valueOf(100), "HUGE_MUSHROOM_2");
        idMap.put(Integer.valueOf(101), "IRON_FENCE");
        idMap.put(Integer.valueOf(102), "THIN_GLASS");
        idMap.put(Integer.valueOf(103), "MELON_BLOCK");
        idMap.put(Integer.valueOf(104), "PUMPKIN_STEM");
        idMap.put(Integer.valueOf(105), "MELON_STEM");
        idMap.put(Integer.valueOf(106), "VINE");
        idMap.put(Integer.valueOf(107), "FENCE_GATE");
        idMap.put(Integer.valueOf(108), "BRICK_STAIRS");
        idMap.put(Integer.valueOf(109), "SMOOTH_STAIRS");
        idMap.put(Integer.valueOf(110), "MYCEL");
        idMap.put(Integer.valueOf(111), "WATER_LILY");
        idMap.put(Integer.valueOf(112), "NETHER_BRICK");
        idMap.put(Integer.valueOf(113), "NETHER_FENCE");
        idMap.put(Integer.valueOf(114), "NETHER_BRICK_STAIRS");
        idMap.put(Integer.valueOf(115), "NETHER_WARTS");
        idMap.put(Integer.valueOf(116), "ENCHANTMENT_TABLE");
        idMap.put(Integer.valueOf(117), "BREWING_STAND");
        idMap.put(Integer.valueOf(118), "CAULDRON");
        idMap.put(Integer.valueOf(119), "ENDER_PORTAL");
        idMap.put(Integer.valueOf(120), "ENDER_PORTAL_FRAME");
        idMap.put(Integer.valueOf(121), "ENDER_STONE");
        idMap.put(Integer.valueOf(122), "DRAGON_EGG");
        idMap.put(Integer.valueOf(123), "REDSTONE_LAMP_OFF");
        idMap.put(Integer.valueOf(124), "REDSTONE_LAMP_ON");
        idMap.put(Integer.valueOf(125), "WOOD_DOUBLE_STEP");
        idMap.put(Integer.valueOf(126), "WOOD_STEP");
        idMap.put(Integer.valueOf(127), "COCOA");
        idMap.put(Integer.valueOf(128), "SANDSTONE_STAIRS");
        idMap.put(Integer.valueOf(129), "EMERALD_ORE");
        idMap.put(Integer.valueOf(130), "ENDER_CHEST");
        idMap.put(Integer.valueOf(131), "TRIPWIRE_HOOK");
        idMap.put(Integer.valueOf(132), "TRIPWIRE");
        idMap.put(Integer.valueOf(133), "EMERALD_BLOCK");
        idMap.put(Integer.valueOf(134), "SPRUCE_WOOD_STAIRS");
        idMap.put(Integer.valueOf(135), "BIRCH_WOOD_STAIRS");
        idMap.put(Integer.valueOf(136), "JUNGLE_WOOD_STAIRS");
        idMap.put(Integer.valueOf(137), "COMMAND");
        idMap.put(Integer.valueOf(138), "BEACON");
        idMap.put(Integer.valueOf(139), "COBBLE_WALL");
        idMap.put(Integer.valueOf(140), "FLOWER_POT");
        idMap.put(Integer.valueOf(141), "CARROT");
        idMap.put(Integer.valueOf(142), "POTATO");
        idMap.put(Integer.valueOf(143), "WOOD_BUTTON");
        idMap.put(Integer.valueOf(144), "SKULL");
        idMap.put(Integer.valueOf(145), "ANVIL");
        idMap.put(Integer.valueOf(146), "TRAPPED_CHEST");
        idMap.put(Integer.valueOf(147), "GOLD_PLATE");
        idMap.put(Integer.valueOf(148), "IRON_PLATE");
        idMap.put(Integer.valueOf(149), "REDSTONE_COMPARATOR_OFF");
        idMap.put(Integer.valueOf(150), "REDSTONE_COMPARATOR_ON");
        idMap.put(Integer.valueOf(151), "DAYLIGHT_DETECTOR");
        idMap.put(Integer.valueOf(152), "REDSTONE_BLOCK");
        idMap.put(Integer.valueOf(153), "QUARTZ_ORE");
        idMap.put(Integer.valueOf(154), "HOPPER");
        idMap.put(Integer.valueOf(155), "QUARTZ_BLOCK");
        idMap.put(Integer.valueOf(156), "QUARTZ_STAIRS");
        idMap.put(Integer.valueOf(157), "ACTIVATOR_RAIL");
        idMap.put(Integer.valueOf(158), "DROPPER");
        idMap.put(Integer.valueOf(159), "STAINED_CLAY");
        idMap.put(Integer.valueOf(160), "STAINED_GLASS_PANE");
        idMap.put(Integer.valueOf(161), "LEAVES_2");
        idMap.put(Integer.valueOf(162), "LOG_2");
        idMap.put(Integer.valueOf(163), "ACACIA_STAIRS");
        idMap.put(Integer.valueOf(164), "DARK_OAK_STAIRS");
        idMap.put(Integer.valueOf(165), "SLIME_BLOCK");
        idMap.put(Integer.valueOf(166), "BARRIER");
        idMap.put(Integer.valueOf(167), "IRON_TRAPDOOR");
        idMap.put(Integer.valueOf(168), "PRISMARINE");
        idMap.put(Integer.valueOf(169), "SEA_LANTERN");
        idMap.put(Integer.valueOf(170), "HAY_BLOCK");
        idMap.put(Integer.valueOf(171), "CARPET");
        idMap.put(Integer.valueOf(172), "HARD_CLAY");
        idMap.put(Integer.valueOf(173), "COAL_BLOCK");
        idMap.put(Integer.valueOf(174), "PACKED_ICE");
        idMap.put(Integer.valueOf(175), "DOUBLE_PLANT");
        idMap.put(Integer.valueOf(176), "STANDING_BANNER");
        idMap.put(Integer.valueOf(177), "WALL_BANNER");
        idMap.put(Integer.valueOf(178), "DAYLIGHT_DETECTOR_INVERTED");
        idMap.put(Integer.valueOf(179), "RED_SANDSTONE");
        idMap.put(Integer.valueOf(180), "RED_SANDSTONE_STAIRS");
        idMap.put(Integer.valueOf(181), "DOUBLE_STONE_SLAB2");
        idMap.put(Integer.valueOf(182), "STONE_SLAB2");
        idMap.put(Integer.valueOf(183), "SPRUCE_FENCE_GATE");
        idMap.put(Integer.valueOf(184), "BIRCH_FENCE_GATE");
        idMap.put(Integer.valueOf(185), "JUNGLE_FENCE_GATE");
        idMap.put(Integer.valueOf(186), "DARK_OAK_FENCE_GATE");
        idMap.put(Integer.valueOf(187), "ACACIA_FENCE_GATE");
        idMap.put(Integer.valueOf(188), "SPRUCE_FENCE");
        idMap.put(Integer.valueOf(189), "BIRCH_FENCE");
        idMap.put(Integer.valueOf(190), "JUNGLE_FENCE");
        idMap.put(Integer.valueOf(191), "DARK_OAK_FENCE");
        idMap.put(Integer.valueOf(192), "ACACIA_FENCE");
        idMap.put(Integer.valueOf(193), "SPRUCE_DOOR");
        idMap.put(Integer.valueOf(194), "BIRCH_DOOR");
        idMap.put(Integer.valueOf(195), "JUNGLE_DOOR");
        idMap.put(Integer.valueOf(196), "ACACIA_DOOR");
        idMap.put(Integer.valueOf(197), "DARK_OAK_DOOR");
        idMap.put(Integer.valueOf(256), "IRON_SPADE");
        idMap.put(Integer.valueOf(257), "IRON_PICKAXE");
        idMap.put(Integer.valueOf(258), "IRON_AXE");
        idMap.put(Integer.valueOf(259), "FLINT_AND_STEEL");
        idMap.put(Integer.valueOf(260), "APPLE");
        idMap.put(Integer.valueOf(261), "BOW");
        idMap.put(Integer.valueOf(262), "ARROW");
        idMap.put(Integer.valueOf(263), "COAL");
        idMap.put(Integer.valueOf(264), "DIAMOND");
        idMap.put(Integer.valueOf(265), "IRON_INGOT");
        idMap.put(Integer.valueOf(266), "GOLD_INGOT");
        idMap.put(Integer.valueOf(267), "IRON_SWORD");
        idMap.put(Integer.valueOf(268), "WOOD_SWORD");
        idMap.put(Integer.valueOf(269), "WOOD_SPADE");
        idMap.put(Integer.valueOf(270), "WOOD_PICKAXE");
        idMap.put(Integer.valueOf(271), "WOOD_AXE");
        idMap.put(Integer.valueOf(272), "STONE_SWORD");
        idMap.put(Integer.valueOf(273), "STONE_SPADE");
        idMap.put(Integer.valueOf(274), "STONE_PICKAXE");
        idMap.put(Integer.valueOf(275), "STONE_AXE");
        idMap.put(Integer.valueOf(276), "DIAMOND_SWORD");
        idMap.put(Integer.valueOf(277), "DIAMOND_SPADE");
        idMap.put(Integer.valueOf(278), "DIAMOND_PICKAXE");
        idMap.put(Integer.valueOf(279), "DIAMOND_AXE");
        idMap.put(Integer.valueOf(280), "STICK");
        idMap.put(Integer.valueOf(281), "BOWL");
        idMap.put(Integer.valueOf(282), "MUSHROOM_SOUP");
        idMap.put(Integer.valueOf(283), "GOLD_SWORD");
        idMap.put(Integer.valueOf(284), "GOLD_SPADE");
        idMap.put(Integer.valueOf(285), "GOLD_PICKAXE");
        idMap.put(Integer.valueOf(286), "GOLD_AXE");
        idMap.put(Integer.valueOf(287), "STRING");
        idMap.put(Integer.valueOf(288), "FEATHER");
        idMap.put(Integer.valueOf(289), "SULPHUR");
        idMap.put(Integer.valueOf(290), "WOOD_HOE");
        idMap.put(Integer.valueOf(291), "STONE_HOE");
        idMap.put(Integer.valueOf(292), "IRON_HOE");
        idMap.put(Integer.valueOf(293), "DIAMOND_HOE");
        idMap.put(Integer.valueOf(294), "GOLD_HOE");
        idMap.put(Integer.valueOf(295), "SEEDS");
        idMap.put(Integer.valueOf(296), "WHEAT");
        idMap.put(Integer.valueOf(297), "BREAD");
        idMap.put(Integer.valueOf(298), "LEATHER_HELMET");
        idMap.put(Integer.valueOf(299), "LEATHER_CHESTPLATE");
        idMap.put(Integer.valueOf(300), "LEATHER_LEGGINGS");
        idMap.put(Integer.valueOf(301), "LEATHER_BOOTS");
        idMap.put(Integer.valueOf(302), "CHAINMAIL_HELMET");
        idMap.put(Integer.valueOf(303), "CHAINMAIL_CHESTPLATE");
        idMap.put(Integer.valueOf(304), "CHAINMAIL_LEGGINGS");
        idMap.put(Integer.valueOf(305), "CHAINMAIL_BOOTS");
        idMap.put(Integer.valueOf(306), "IRON_HELMET");
        idMap.put(Integer.valueOf(307), "IRON_CHESTPLATE");
        idMap.put(Integer.valueOf(308), "IRON_LEGGINGS");
        idMap.put(Integer.valueOf(309), "IRON_BOOTS");
        idMap.put(Integer.valueOf(310), "DIAMOND_HELMET");
        idMap.put(Integer.valueOf(311), "DIAMOND_CHESTPLATE");
        idMap.put(Integer.valueOf(312), "DIAMOND_LEGGINGS");
        idMap.put(Integer.valueOf(313), "DIAMOND_BOOTS");
        idMap.put(Integer.valueOf(314), "GOLD_HELMET");
        idMap.put(Integer.valueOf(315), "GOLD_CHESTPLATE");
        idMap.put(Integer.valueOf(316), "GOLD_LEGGINGS");
        idMap.put(Integer.valueOf(317), "GOLD_BOOTS");
        idMap.put(Integer.valueOf(318), "FLINT");
        idMap.put(Integer.valueOf(319), "PORK");
        idMap.put(Integer.valueOf(320), "GRILLED_PORK");
        idMap.put(Integer.valueOf(321), "PAINTING");
        idMap.put(Integer.valueOf(322), "GOLDEN_APPLE");
        idMap.put(Integer.valueOf(323), "SIGN");
        idMap.put(Integer.valueOf(324), "WOOD_DOOR");
        idMap.put(Integer.valueOf(325), "BUCKET");
        idMap.put(Integer.valueOf(326), "WATER_BUCKET");
        idMap.put(Integer.valueOf(327), "LAVA_BUCKET");
        idMap.put(Integer.valueOf(328), "MINECART");
        idMap.put(Integer.valueOf(329), "SADDLE");
        idMap.put(Integer.valueOf(330), "IRON_DOOR");
        idMap.put(Integer.valueOf(331), "REDSTONE");
        idMap.put(Integer.valueOf(332), "SNOW_BALL");
        idMap.put(Integer.valueOf(333), "BOAT");
        idMap.put(Integer.valueOf(334), "LEATHER");
        idMap.put(Integer.valueOf(335), "MILK_BUCKET");
        idMap.put(Integer.valueOf(336), "CLAY_BRICK");
        idMap.put(Integer.valueOf(337), "CLAY_BALL");
        idMap.put(Integer.valueOf(338), "SUGAR_CANE");
        idMap.put(Integer.valueOf(339), "PAPER");
        idMap.put(Integer.valueOf(340), "BOOK");
        idMap.put(Integer.valueOf(341), "SLIME_BALL");
        idMap.put(Integer.valueOf(342), "STORAGE_MINECART");
        idMap.put(Integer.valueOf(343), "POWERED_MINECART");
        idMap.put(Integer.valueOf(344), "EGG");
        idMap.put(Integer.valueOf(345), "COMPASS");
        idMap.put(Integer.valueOf(346), "FISHING_ROD");
        idMap.put(Integer.valueOf(347), "WATCH");
        idMap.put(Integer.valueOf(348), "GLOWSTONE_DUST");
        idMap.put(Integer.valueOf(349), "RAW_FISH");
        idMap.put(Integer.valueOf(350), "COOKED_FISH");
        idMap.put(Integer.valueOf(351), "INK_SACK");
        idMap.put(Integer.valueOf(352), "BONE");
        idMap.put(Integer.valueOf(353), "SUGAR");
        idMap.put(Integer.valueOf(354), "CAKE");
        idMap.put(Integer.valueOf(355), "BED");
        idMap.put(Integer.valueOf(356), "DIODE");
        idMap.put(Integer.valueOf(357), "COOKIE");
        idMap.put(Integer.valueOf(358), "MAP");
        idMap.put(Integer.valueOf(359), "SHEARS");
        idMap.put(Integer.valueOf(360), "MELON");
        idMap.put(Integer.valueOf(361), "PUMPKIN_SEEDS");
        idMap.put(Integer.valueOf(362), "MELON_SEEDS");
        idMap.put(Integer.valueOf(363), "RAW_BEEF");
        idMap.put(Integer.valueOf(364), "COOKED_BEEF");
        idMap.put(Integer.valueOf(365), "RAW_CHICKEN");
        idMap.put(Integer.valueOf(366), "COOKED_CHICKEN");
        idMap.put(Integer.valueOf(367), "ROTTEN_FLESH");
        idMap.put(Integer.valueOf(368), "ENDER_PEARL");
        idMap.put(Integer.valueOf(369), "BLAZE_ROD");
        idMap.put(Integer.valueOf(370), "GHAST_TEAR");
        idMap.put(Integer.valueOf(371), "GOLD_NUGGET");
        idMap.put(Integer.valueOf(372), "NETHER_STALK");
        idMap.put(Integer.valueOf(373), "POTION");
        idMap.put(Integer.valueOf(374), "GLASS_BOTTLE");
        idMap.put(Integer.valueOf(375), "SPIDER_EYE");
        idMap.put(Integer.valueOf(376), "FERMENTED_SPIDER_EYE");
        idMap.put(Integer.valueOf(377), "BLAZE_POWDER");
        idMap.put(Integer.valueOf(378), "MAGMA_CREAM");
        idMap.put(Integer.valueOf(379), "BREWING_STAND_ITEM");
        idMap.put(Integer.valueOf(380), "CAULDRON_ITEM");
        idMap.put(Integer.valueOf(381), "EYE_OF_ENDER");
        idMap.put(Integer.valueOf(382), "SPECKLED_MELON");
        idMap.put(Integer.valueOf(383), "MONSTER_EGG");
        idMap.put(Integer.valueOf(384), "EXP_BOTTLE");
        idMap.put(Integer.valueOf(385), "FIREBALL");
        idMap.put(Integer.valueOf(386), "BOOK_AND_QUILL");
        idMap.put(Integer.valueOf(387), "WRITTEN_BOOK");
        idMap.put(Integer.valueOf(388), "EMERALD");
        idMap.put(Integer.valueOf(389), "ITEM_FRAME");
        idMap.put(Integer.valueOf(390), "FLOWER_POT_ITEM");
        idMap.put(Integer.valueOf(391), "CARROT_ITEM");
        idMap.put(Integer.valueOf(392), "POTATO_ITEM");
        idMap.put(Integer.valueOf(393), "BAKED_POTATO");
        idMap.put(Integer.valueOf(394), "POISONOUS_POTATO");
        idMap.put(Integer.valueOf(395), "EMPTY_MAP");
        idMap.put(Integer.valueOf(396), "GOLDEN_CARROT");
        idMap.put(Integer.valueOf(397), "SKULL_ITEM");
        idMap.put(Integer.valueOf(398), "CARROT_STICK");
        idMap.put(Integer.valueOf(399), "NETHER_STAR");
        idMap.put(Integer.valueOf(400), "PUMPKIN_PIE");
        idMap.put(Integer.valueOf(401), "FIREWORK");
        idMap.put(Integer.valueOf(402), "FIREWORK_CHARGE");
        idMap.put(Integer.valueOf(403), "ENCHANTED_BOOK");
        idMap.put(Integer.valueOf(404), "REDSTONE_COMPARATOR");
        idMap.put(Integer.valueOf(405), "NETHER_BRICK_ITEM");
        idMap.put(Integer.valueOf(406), "QUARTZ");
        idMap.put(Integer.valueOf(407), "EXPLOSIVE_MINECART");
        idMap.put(Integer.valueOf(408), "HOPPER_MINECART");
        idMap.put(Integer.valueOf(409), "PRISMARINE_SHARD");
        idMap.put(Integer.valueOf(410), "PRISMARINE_CRYSTALS");
        idMap.put(Integer.valueOf(411), "RABBIT");
        idMap.put(Integer.valueOf(412), "COOKED_RABBIT");
        idMap.put(Integer.valueOf(413), "RABBIT_STEW");
        idMap.put(Integer.valueOf(414), "RABBIT_FOOT");
        idMap.put(Integer.valueOf(415), "RABBIT_HIDE");
        idMap.put(Integer.valueOf(416), "ARMOR_STAND");
        idMap.put(Integer.valueOf(417), "IRON_BARDING");
        idMap.put(Integer.valueOf(418), "GOLD_BARDING");
        idMap.put(Integer.valueOf(419), "DIAMOND_BARDING");
        idMap.put(Integer.valueOf(420), "LEASH");
        idMap.put(Integer.valueOf(421), "NAME_TAG");
        idMap.put(Integer.valueOf(422), "COMMAND_MINECART");
        idMap.put(Integer.valueOf(423), "MUTTON");
        idMap.put(Integer.valueOf(424), "COOKED_MUTTON");
        idMap.put(Integer.valueOf(425), "BANNER");
        idMap.put(Integer.valueOf(427), "SPRUCE_DOOR_ITEM");
        idMap.put(Integer.valueOf(428), "BIRCH_DOOR_ITEM");
        idMap.put(Integer.valueOf(429), "JUNGLE_DOOR_ITEM");
        idMap.put(Integer.valueOf(430), "ACACIA_DOOR_ITEM");
        idMap.put(Integer.valueOf(431), "DARK_OAK_DOOR_ITEM");
        idMap.put(Integer.valueOf(2256), "GOLD_RECORD");
        idMap.put(Integer.valueOf(2257), "GREEN_RECORD");
        idMap.put(Integer.valueOf(2258), "RECORD_3");
        idMap.put(Integer.valueOf(2259), "RECORD_4");
        idMap.put(Integer.valueOf(2260), "RECORD_5");
        idMap.put(Integer.valueOf(2261), "RECORD_6");
        idMap.put(Integer.valueOf(2262), "RECORD_7");
        idMap.put(Integer.valueOf(2263), "RECORD_8");
        idMap.put(Integer.valueOf(2264), "RECORD_9");
        idMap.put(Integer.valueOf(2265), "RECORD_10");
        idMap.put(Integer.valueOf(2266), "RECORD_11");
        idMap.put(Integer.valueOf(2267), "RECORD_12");
    }

    public static Material getMaterialById(int id) {
        try {
            return Material.valueOf(idMap.get(Integer.valueOf(id)));
        } catch (Exception ex) {
        }
        return null;
    }
}

