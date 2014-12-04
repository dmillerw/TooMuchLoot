package dmillerw.tml.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import dmillerw.tml.data.NBTDeserializer;
import dmillerw.tml.data.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author dmillerw
 */
public class GsonHelper {

    public static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(NBTTagCompound.class, new NBTDeserializer());
        builder.registerTypeAdapter(NBTTagCompound.class, new NBTSerializer());
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static Number parseNumber(char type, JsonPrimitive jsonPrimitive) {
        String string = jsonPrimitive.getAsString();
        string = string.substring(0, string.length() - 1);

        // If not a number with support for decimals, trim any that occur
        if (type == 'b' || type == 'B' || type == 'i' || type == 'I' || type == 's' || type == 'S' || type == 'l' || type == 'L') {
            if (string.contains("."))
                string = string.substring(0, string.indexOf("."));
        }

        if (type == 'b' || type == 'B') {
            return Byte.parseByte(string);
        } else if (type == 's' || type == 'S') {
            return Short.parseShort(string);
        } else if (type == 'i' || type == 'I') {
            return Integer.parseInt(string);
        } else if (type == 'f' || type == 'F') {
            return Float.parseFloat(string);
        } else if (type == 'd' || type == 'D') {
            return Double.parseDouble(string);
        } else if (type == 'l' || type == 'L') {
            return Long.parseLong(string);
        } else {
            return null;
        }
    }
}
