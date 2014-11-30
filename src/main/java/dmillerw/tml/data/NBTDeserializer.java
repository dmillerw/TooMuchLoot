package dmillerw.tml.data;

import com.google.gson.*;
import dmillerw.tml.helper.GsonHelper;
import net.minecraft.nbt.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author dmillerw
 */
public class NBTDeserializer implements JsonDeserializer<NBTTagCompound> {

    @Override
    public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Map.Entry<String, JsonElement> elementEntry : json.getAsJsonObject().entrySet()) {
            String key = elementEntry.getKey();
            JsonElement element = elementEntry.getValue();
            parseJsonElement(nbt, key, element);
        }
        return nbt;
    }

    public void parseJsonElement(NBTTagCompound nbt, String key, JsonElement element) {
        NBTKeyAndTag nbtKeyAndTag = getTagFromElement(key, element);
        nbt.setTag(nbtKeyAndTag.key, nbtKeyAndTag.tag);
    }

    public NBTKeyAndTag getTagFromElement(String key, JsonElement element) {
        if (element.isJsonPrimitive()) {
            String substring = key;
            if (!element.getAsJsonPrimitive().isString())
                substring = key.substring(0, key.lastIndexOf("^"));

            return new NBTKeyAndTag(substring, getTagFromPrimitive(key, element.getAsJsonPrimitive()));
        } else if (element.isJsonArray()) {
            NBTTagList nbtTagList = new NBTTagList();
            for (JsonElement jsonElement : element.getAsJsonArray()) {
                nbtTagList.appendTag(getTagFromElement(key, jsonElement).tag);
            }
            return new NBTKeyAndTag(key, nbtTagList);
        } else if (element.isJsonObject()) {
            NBTTagCompound tag = new NBTTagCompound();
            for (Map.Entry<String, JsonElement> elementEntry : element.getAsJsonObject().entrySet()) {
                parseJsonElement(tag, elementEntry.getKey(), elementEntry.getValue());
            }
            return new NBTKeyAndTag(key, tag);
        } else {
            return new NBTKeyAndTag(key, new NBTTagString(element.getAsString()));
        }
    }

    public NBTBase getTagFromPrimitive(String key, JsonPrimitive primitive) {
        if (primitive.isBoolean()) {
            return new NBTTagByte((byte) (primitive.getAsBoolean() ? 1 : 0));
        } else if (primitive.isNumber()) {
            Number number = GsonHelper.parseNumber(key, primitive);
            if (number instanceof Byte)
                return new NBTTagByte(number.byteValue());
            else if (number instanceof Short)
                return new NBTTagShort(number.shortValue());
            else if (number instanceof Integer)
                return new NBTTagInt(number.intValue());
            else if (number instanceof Float)
                return new NBTTagFloat(number.floatValue());
            else if (number instanceof Double)
                return new NBTTagDouble(number.doubleValue());
            else if (number instanceof Long)
                return new NBTTagLong(number.longValue());
        } else if (primitive.isString()) {
            return new NBTTagString(primitive.getAsString());
        } else {
            throw new JsonParseException("Failed to retrieve NBT tag from non-primitive element!");
        }

        return null;
    }
}
