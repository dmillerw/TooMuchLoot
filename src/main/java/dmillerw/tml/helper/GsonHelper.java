package dmillerw.tml.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import dmillerw.tml.data.NBTDeserializer;
import dmillerw.tml.data.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FilenameFilter;

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

    public static Number parseNumber(String key, JsonPrimitive jsonPrimitive) {
        if (key.contains("^")) {
            String type = key.substring(key.lastIndexOf("^") + 1, key.length());
            if (type.equalsIgnoreCase("byte")) {
                return jsonPrimitive.getAsByte();
            } else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
                return jsonPrimitive.getAsBoolean() ? 1 : 0;
            } else if (type.equalsIgnoreCase("short")) {
                return jsonPrimitive.getAsShort();
            } else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
                return jsonPrimitive.getAsInt();
            } else if (type.equalsIgnoreCase("float")) {
                return jsonPrimitive.getAsFloat();
            } else if (type.equalsIgnoreCase("double")) {
                return jsonPrimitive.getAsDouble();
            } else if (type.equalsIgnoreCase("long")) {
                return jsonPrimitive.getAsLong();
            } else {
                return null;
            }
        } else {
            return jsonPrimitive.getAsInt();
        }
    }
}
