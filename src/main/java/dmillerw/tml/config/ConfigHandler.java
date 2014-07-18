package dmillerw.tml.config;

import com.google.common.collect.Maps;
import dmillerw.tml.TooMuchLoot;
import dmillerw.tml.helper.LogHelper;
import dmillerw.tml.data.ConfigWrapper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dmillerw
 */
public class ConfigHandler {

	public static File configFile;

	public static Configuration main;

	public static Map<String, Configuration> configMapping;

	public static List<String> getItems(String key) {
		List<String> list = new ArrayList<String>();
		list.addAll(configMapping.get(key).getCategoryNames());
		return list;
	}

	public static List<Property> getProperties(String key, String item) {
		List<Property> list = new ArrayList<Property>();
		list.addAll(configMapping.get(key).getCategory(item).getOrderedValues());
		return list;
	}

	public static void initializeMain() {
		main = new Configuration(configFile);
		main.load();
	}

	public static void syncMain() {
		LogHelper.log = main.get("main", "log", true, "Whether loot removals/modifications/additions should be printed to the console/logged").getBoolean(true);

		if (main.hasChanged()) {
			main.save();
		}
	}

	public static void initializeLoot() {
		configMapping = Maps.newHashMap();

		for (String key : TooMuchLoot.CHEST_GEN_KEYS) {
			File file = new File(TooMuchLoot.configFolder, TooMuchLoot.GEN_FOLDER + "/" + key + ".cfg");

			Configuration configuration = new Configuration(file);
			configuration.load();

			configMapping.put(key, configuration);
		}
	}

	public static void syncLoot() {
		for (String key : TooMuchLoot.CHEST_GEN_KEYS) {
			ChestGenHooks chestInfo = ChestGenHooks.getInfo(key);

			Configuration configuration = configMapping.get(key);

			List<ConfigWrapper> newGen = new ArrayList<ConfigWrapper>();

			try {
				List<WeightedRandomChestContent> chestContents = (List<WeightedRandomChestContent>) TooMuchLoot.contents.get(chestInfo);
				for (WeightedRandomChestContent content : chestContents) {
					ConfigWrapper wrapper = ConfigWrapper.fromConfig(configuration, chestInfo, content, true);
					newGen.add(wrapper);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			for (ConfigWrapper wrapper : newGen) {
				chestInfo.removeItem(wrapper.item.theItemId);
				if (wrapper.enabled) {
					if (wrapper.modified ) {
						LogHelper.logModification(key, wrapper.item.theItemId.getDisplayName());
					}
					chestInfo.addItem(wrapper.item);
				} else {
					LogHelper.logRemoval(key, wrapper.item.theItemId.getDisplayName());
				}
			}

			if (configuration.hasChanged()) {
				configuration.save();
			}
		}
	}
}
