package dmillerw.tml.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dmillerw.tml.TooMuchLoot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

/**
 * @author dmillerw
 */
public class LootDeserielizer {

	public static class SerializedLoot {
		public static final String[] MESSAGES = new String[] {
			"Everything's shiny cap'n!",
			"Bad category!",
			"Invalid item! %s does not exist!"
		};

		public String category;
		public int item;
		public int damage = 0;
		public NBTTagCompound nbt = new NBTTagCompound();
		public int weight;
		public int min_count;
		public int max_count;

		public byte verify() {
			if (!Arrays.asList(TooMuchLoot.CHEST_GEN_KEYS).contains(category)) {
				return 1;
			}

			if (Item.itemsList[item] == null) {
				return 2;
			}

			return 0;
		}
	}

	public static SerializedLoot loadFile(File file) throws FileNotFoundException {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(NBTTagCompound.class, new NBTDeserializer());
		Gson gson = builder.create();
		SerializedLoot loot = gson.fromJson(new FileReader(file), SerializedLoot.class);
		byte result = loot.verify();
		if (result != 0) {
			String error = SerializedLoot.MESSAGES[result];
			if (result == 2) {
				error = String.format(error, loot.item);
			}
			TooMuchLoot.warn("Failed to parse " + file.getName() + ". Reason: " + error, false);
			return null;
		} else {
			return loot;
		}
	}

	public static void loadLoot(SerializedLoot loot) {
		ItemStack stack = new ItemStack(loot.item, 1, loot.damage);
		stack.setTagCompound(loot.nbt);
		TooMuchLoot.logAddition(loot.category, stack.getDisplayName());
		ChestGenHooks.getInfo(loot.category).addItem(new WeightedRandomChestContent(stack, loot.min_count, loot.max_count, loot.weight));
	}
}