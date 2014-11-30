package dmillerw.tml.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dmillerw.tml.helper.NBTHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author dmillerw
 */
public class NBTSerializer implements JsonSerializer<NBTTagCompound> {

    @Override
    public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        for (String key : (Set<String>)src.func_150296_c()) {
            NBTBase tag = src.getTag(key);
            if (NBTHelper.isNumber(tag)) {
                Number number = NBTHelper.getNumber(tag);
                jsonObject.addProperty(key + "^" + NBTHelper.getType(number), number);
            } else if (tag instanceof NBTTagString) {
                jsonObject.addProperty(key, ((NBTTagString) tag).func_150285_a_());
            } else if (tag instanceof NBTTagList) {
                jsonObject.add(key, context.serialize(NBTHelper.getTagList((NBTTagList) tag)));
            } else if (tag instanceof NBTTagCompound) {
                if (!((NBTTagCompound) tag).hasNoTags())
                    jsonObject.add(key, context.serialize(tag));
            }
        }
        return jsonObject;
    }
}
