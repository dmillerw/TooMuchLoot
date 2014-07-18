package dmillerw.tml.data;

import cpw.mods.fml.common.FMLLog;
import dmillerw.tml.TooMuchLoot;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;

/**
 * @author dmillerw
 */
public class ConfigWrapper {

	public static ConfigWrapper fromConfig(Configuration configuration, ChestGenHooks hook, WeightedRandomChestContent content, boolean legacy) {
		ConfigWrapper wrapper = new ConfigWrapper();
		String key = TooMuchLoot.getFormattedStackString(content.theItemId);
		key = key.replace(".", "_");

		configuration.addCustomCategoryComment(key, content.theItemId.getDisplayName());
		wrapper.enabled = configuration.get(key, "enabled", legacy).getBoolean(legacy);
		int weight = configuration.get(key, "spawn_weight", content.itemWeight).getInt(content.itemWeight);
		int minChance = configuration.get(key, "min_spawn_count", content.theMinimumChanceToGenerateItem).getInt(content.theMinimumChanceToGenerateItem);
		int maxChance = configuration.get(key, "max_spawn_count", content.theMaximumChanceToGenerateItem).getInt(content.theMaximumChanceToGenerateItem);
		wrapper.item = new WeightedRandomChestContent(content.theItemId, minChance, maxChance, weight);

		if (maxChance - minChance + 1 < 0) {
			FMLLog.bigWarning(wrapper.item.theItemId.getDisplayName() + " has illegal min/max spawn values!");

			wrapper.enabled = false;
			wrapper.item.theMinimumChanceToGenerateItem = 0;
			wrapper.item.theMaximumChanceToGenerateItem = 0;
		} else {
			wrapper.modified = weight != content.itemWeight || minChance != content.theMinimumChanceToGenerateItem || maxChance != content.theMaximumChanceToGenerateItem;
		}

		return wrapper;
	}

	public boolean enabled;
	public boolean modified = false;

	public WeightedRandomChestContent item;

}
