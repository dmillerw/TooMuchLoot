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
        if (type == 'b' || type == 'B') {
            return jsonPrimitive.getAsByte();
        } else if (type == 's' || type == 'S') {
            return jsonPrimitive.getAsShort();
        } else if (type == 'i' || type == 'I') {
            return jsonPrimitive.getAsInt();
        } else if (type == 'f' || type == 'F') {
            return jsonPrimitive.getAsFloat();
        } else if (type == 'd' || type == 'D') {
            return jsonPrimitive.getAsDouble();
        } else if (type == 'l' || type == 'L') {
            return jsonPrimitive.getAsLong();
        } else {
            return null;
        }
    }
}
