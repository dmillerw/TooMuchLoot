package dmillerw.tml.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author dmillerw
 */
public class LootDeserielizer {

	public static NBTTagCompound nbt;

	public static class Test {
		public NBTTagCompound nbt;
	}

	public static void loadFile(File file) throws FileNotFoundException {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(NBTTagCompound.class, new NBTDeserializer());
		Gson gson = builder.create();
		Test test = gson.fromJson(new FileReader(file), Test.class);
		LootDeserielizer.nbt = test.nbt;
		System.out.println(nbt);
	}

}
