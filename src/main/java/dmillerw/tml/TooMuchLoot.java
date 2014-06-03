package dmillerw.tml;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import dmillerw.tml.wrapper.ConfigWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.Configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
@Mod(modid="TML", name="Too Much Loot", version="@VERSION@")
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

	public static String getFormattedKey(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(Character.toUpperCase(key.charAt(0)));
		for (int i=1; i<key.length(); i++) {
			sb.append(key.charAt(i));
			if (i < key.length() - 1) {
				if (Character.isLowerCase(key.charAt(i)) && Character.isUpperCase(key.charAt(i + 1))) {
					sb.append(' ');
				}
			}
		}
		return sb.toString();
	}

	public static String getFormattedStackString(ItemStack stack) {
		if (stack == null) {
			return "null";
		}
		return stack.getUnlocalizedName() + ";" + stack.getItemDamage();
	}

	public static boolean isLootAllowed(Configuration configuration, String category, ItemStack stack) {
		if (stack == null) return true; // Doesn't get processed, won't throw any errors
		return configuration.get(category, getFormattedStackString(stack), true, stack.getDisplayName()).getBoolean(true);
	}

	public static void warn(String msg) {
		FMLLog.warning("[TooMuchLoot]: %s", msg);
	}

	public static void logModification(String key, String display) {
		if (log) FMLLog.info("[TooMuchLoot]: %s from %s has been modified", display, key);
	}

	public static void logRemoval(String key, String display) {
		if (log) FMLLog.info("[TooMuchLoot]: Removed %s from %s", display, key);
	}

	public static File configFolder;
	public static File configFile;
	public static File lootFolder;

	public static Field contents;

	public static boolean log = true;
	public static boolean failed = false;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		configFolder = new File(event.getModConfigurationDirectory(), CONFIG_FOLDER);
		configFile = new File(configFolder, CONFIG_FILE);
		lootFolder = new File(configFolder, LOOT_FOLDER);

		Configuration configuration = new Configuration(configFile);
		configuration.load();

		log = configuration.get("_main", "log_removal", true, "Whether loot removals/modifications should be printed to the console/logged").getBoolean(true);

		if (configuration.hasChanged()) {
			configuration.save();
		}

		try {
			contents = ChestGenHooks.class.getDeclaredField("contents");
			contents.setAccessible(true);
		} catch (NoSuchFieldException e) {
			failed = true;
			warn("Failed to obtain contents field. This mod will now cease to function.");
			e.printStackTrace();
		}
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerAboutToStartEvent event) {
		if (failed) {
			return;
		}

		Configuration legacyConfig = new Configuration(configFile);
		legacyConfig.load();

		for (String key : CHEST_GEN_KEYS) {
			File file = new File(configFolder, GEN_FOLDER + "/" + key + ".cfg");
			boolean legacy = configFile.exists() && !file.exists();
			ChestGenHooks chestInfo = ChestGenHooks.getInfo(key);

			if (legacy) {
				FMLLog.info("Restoring " + key + " data from legacy config");
			}

			Configuration configuration = new Configuration(file);
			configuration.load();

			List<ConfigWrapper> newGen = new ArrayList<ConfigWrapper>();

			try {
				List<WeightedRandomChestContent> chestContents = (List<WeightedRandomChestContent>) contents.get(chestInfo);
				for (WeightedRandomChestContent content : chestContents) {
					boolean defaultValue = true;
					if (legacy) {
						defaultValue = legacyConfig.get(key, getFormattedStackString(content.theItemId), true).getBoolean(true);
					}

					ConfigWrapper wrapper = ConfigWrapper.fromConfig(configuration, chestInfo, content, defaultValue);
					newGen.add(wrapper);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			for (ConfigWrapper wrapper : newGen) {
				chestInfo.removeItem(wrapper.item.theItemId);
				if (wrapper.enabled) {
					if (wrapper.modified ) {
						logModification(key, wrapper.item.theItemId.getDisplayName());
					}
					chestInfo.addItem(wrapper.item);
				} else {
					logRemoval(key, wrapper.item.theItemId.getDisplayName());
				}
			}

			if (configuration.hasChanged()) {
				configuration.save();
			}
		}
	}
}
