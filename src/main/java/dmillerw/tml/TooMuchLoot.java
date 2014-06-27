package dmillerw.tml;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.tml.config.ConfigHandler;
import dmillerw.tml.json.LootDeserielizer;
import dmillerw.tml.lib.ModInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;

/**
 * @author dmillerw
 */
@Mod(modid= ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION, dependencies = ModInfo.DEPENDENCIES, guiFactory = "dmillerw.tml.client.config.ConfigGUIFactory")
public class TooMuchLoot {

	public static final String CONFIG_FOLDER = "TooMuchLoot";
	public static final String CONFIG_FILE = "MainConfig.cfg";
	public static final String GEN_FOLDER = "gen";
	public static final String LOOT_FOLDER = "loot";

	public static String[] CHEST_GEN_KEYS = new String[] {
		ChestGenHooks.MINESHAFT_CORRIDOR,
		ChestGenHooks.PYRAMID_DESERT_CHEST,
		ChestGenHooks.PYRAMID_JUNGLE_CHEST,
		ChestGenHooks.PYRAMID_JUNGLE_DISPENSER,
		ChestGenHooks.STRONGHOLD_CORRIDOR,
		ChestGenHooks.STRONGHOLD_LIBRARY,
		ChestGenHooks.STRONGHOLD_CROSSING,
		ChestGenHooks.VILLAGE_BLACKSMITH,
		ChestGenHooks.BONUS_CHEST,
		ChestGenHooks.DUNGEON_CHEST
	};

	public static String getFormattedStackString(ItemStack stack) {
		if (stack == null) {
			return "null";
		}
		return stack.getUnlocalizedName() + ";" + stack.getItemDamage();
	}

	public static void warn(String msg, boolean big) {
		if (big) FMLLog.bigWarning("[TooMuchLoot]: %s", msg); else FMLLog.warning("[TooMuchLoot]: %s", msg);
	}

	public static void logParse(String file) {
		if (log) FMLLog.info("[TooMuchLoot]: Parsing %s", file);
	}

	public static void logAddition(String key, String display) {
		if (log) FMLLog.info("[TooMuchLoot]: Adding %s to %s", display, key);
	}

	public static void logModification(String key, String display) {
		if (log) FMLLog.info("[TooMuchLoot]: %s from %s has been modified", display, key);
	}

	public static void logRemoval(String key, String display) {
		if (log) FMLLog.info("[TooMuchLoot]: Removed %s from %s", display, key);
	}

	@Mod.Instance(ModInfo.ID)
	public static TooMuchLoot instance;

	public static File configFolder;
	public static File lootFolder;

	public static Field contents;

	public static boolean log = true;
	public static boolean failed = false;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// MOD METADATA ADJUSTMENT
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.version = ModInfo.VERSION;

		configFolder = new File(event.getModConfigurationDirectory(), CONFIG_FOLDER);
		lootFolder = new File(configFolder, LOOT_FOLDER);

		ConfigHandler.configFile = new File(configFolder, CONFIG_FILE);

		ConfigHandler.initializeMain();
		ConfigHandler.syncMain();

		try {
			contents = ChestGenHooks.class.getDeclaredField("contents");
			contents.setAccessible(true);
		} catch (NoSuchFieldException e) {
			failed = true;
			warn("Failed to obtain contents field. This mod will now cease to function.", true);
			e.printStackTrace();
		}

		FMLCommonHandler.instance().bus().register(instance);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		File loot = new File(configFolder, LOOT_FOLDER);

		if (!loot.exists()) {
			loot.mkdir();
		}

		for (File file : loot.listFiles()) {
			String name = file.getName();
			if (name.substring(name.lastIndexOf(".") + 1, name.length()).equalsIgnoreCase("json")) {
				try {
					logParse(name);
					LootDeserielizer.loadLoot(LootDeserielizer.loadFile(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		ConfigHandler.initializeLoot();
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerAboutToStartEvent event) {
		if (failed) {
			return;
		}

		ConfigHandler.syncLoot();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(ModInfo.ID)) {
			ConfigHandler.syncMain();
			ConfigHandler.syncLoot();
		}
	}
}
