package dmillerw.tml.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.registry.GameData;
import dmillerw.tml.data.json.LootArrayDeserializer;
import dmillerw.tml.data.json.NBTDeserializer;
import dmillerw.tml.helper.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;
import java.io.FileReader;

/**
 * @author dmillerw
 */
public class LootLoader {

	public static class LootArrayWrapper {
		public SerializedLoot[] loot;
	}

	public static class SerializedLoot {
		public String[] category;
		public String item;
		public int damage = 0;
		public NBTTagCompound nbt = new NBTTagCompound();
		public int weight;
		public int min_count;
		public int max_count;
	}

	private static Gson gson;

	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(LootArrayWrapper.class, new LootArrayDeserializer());
		builder.registerTypeAdapter(NBTTagCompound.class, new NBTDeserializer());
		gson = builder.create();
	}

	public static LootArrayWrapper loadFile(File file) {
		try {
			return gson.fromJson(new FileReader(file), LootArrayWrapper.class);
		} catch (Exception ex) {
			LogHelper.warn(String.format("Failed to parse %s! Reason: %s", file.getName(), LogHelper.interpretReason(ex.getMessage())), false);
			return null;
		}
	}

	public static void loadLoot(SerializedLoot loot) {
		if (loot == null) {
			return;
		}

		ItemStack stack = new ItemStack(GameData.getItemRegistry().getObject(loot.item), 1, loot.damage);

		if (stack.getItem() == null) {
			LogHelper.warn(String.format("Tried to add %s to the %s category, but couldn't find the item!", loot.item, loot.category), false);
			return;
		}

		if (!loot.nbt.hasNoTags()) {
			stack.setTagCompound(loot.nbt);
		}

		for (String category : loot.category) {
			LogHelper.logAddition(category, stack.getDisplayName());
			ChestGenHooks.getInfo(category).addItem(new WeightedRandomChestContent(stack, loot.min_count, loot.max_count, loot.weight));
		}
	}
}