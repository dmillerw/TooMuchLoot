package dmillerw.tml.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.tml.TooMuchLoot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author dmillerw
 */
public class LootDeserielizer {

	public static class SerializedLoot {
		public String category;
		public String item;
		public int damage = 0;
		public NBTTagCompound nbt = new NBTTagCompound();
		public int weight;
		public int min_count;
		public int max_count;
	}

	public static SerializedLoot loadFile(File file) throws FileNotFoundException {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(NBTTagCompound.class, new NBTDeserializer());
		Gson gson = builder.create();
		return gson.fromJson(new FileReader(file), SerializedLoot.class);
	}

	public static void loadLoot(SerializedLoot loot) {
		ItemStack stack = new ItemStack(GameData.getItemRegistry().getObject(loot.item), 1, loot.damage);
		stack.setTagCompound(loot.nbt);
		TooMuchLoot.logAddition(loot.category, stack.getDisplayName());
		ChestGenHooks.getInfo(loot.category).addItem(new WeightedRandomChestContent(stack, loot.min_count, loot.max_count, loot.weight));
	}
}