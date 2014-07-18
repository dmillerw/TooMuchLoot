package dmillerw.tml.data.json;

import com.google.gson.*;
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
			parseJSONElement(nbt, key, element);
		}

		return nbt;
	}

	public void parseJSONElement(NBTTagCompound nbt, String key, JsonElement element) {
		if (element.isJsonPrimitive()) {
			// Type is implied or defined
			if (key.contains(":")) {
				String type = key.substring(0, key.indexOf(":"));
				String name = key.substring(key.indexOf(":") + 1, key.length());
				nbt.setTag(name, getForcedTagFromElement(type, element));
			} else {
				nbt.setTag(key, getTagFromPrimitive(element.getAsJsonPrimitive()));
			}
		} else if (element.isJsonArray()) {
			// Type is NBT array (int or string?)
			if (key.contains(":")) {
				String type = key.substring(0, key.indexOf(":"));
				String name = key.substring(key.indexOf(":") + 1, key.length());
				nbt.setTag(name, getArrayFromElement(type, element.getAsJsonArray()));
			} else {
				// Type isn't defined, and because I'm lazy, we fail
				//TODO Fail
			}
		} else if (element.isJsonObject()) {
			// Type is NBT compound
			NBTTagCompound tag = new NBTTagCompound();

			for (Map.Entry<String, JsonElement> elementEntry : element.getAsJsonObject().entrySet()) {
				parseJSONElement(tag, elementEntry.getKey(), elementEntry.getValue());
			}

			nbt.setTag(key, tag);
		}
	}

	public NBTBase getTagFromPrimitive(JsonPrimitive primitive) {
		if (primitive.isBoolean()) {
			return new NBTTagByte((byte) (primitive.getAsBoolean() ? 1 : 0));
		} else if (primitive.isNumber()) {
			return new NBTTagInt(primitive.getAsNumber().intValue());
		} else if (primitive.isString()) {
			return new NBTTagString(primitive.getAsString());
		} else {
			throw new JsonParseException("Failed to retrieve NBT tag from non-primitive element!");
		}
	}

	public NBTBase getArrayFromElement(String type, JsonArray array) {
		NBTTagList list = new NBTTagList();
		for (int i=0; i<array.size(); i++) {
			list.appendTag(getForcedTagFromElement(type, array.get(i)));
		}
		return list;
	}

	public NBTBase getForcedTagFromElement(String type, JsonElement element) {
		if (type.equalsIgnoreCase("string")) {
			return new NBTTagString(element.getAsString());
		} else if (type.equalsIgnoreCase("byte")) {
			return new NBTTagByte(element.getAsByte());
		} else if (type.equalsIgnoreCase("bool") || type.equalsIgnoreCase("boolean")) {
			return new NBTTagByte((byte) (element.getAsBoolean() ? 1 : 0));
		} else if (type.equalsIgnoreCase("short")) {
			return new NBTTagShort(element.getAsShort());
		} else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
			return new NBTTagInt(element.getAsInt());
		} else if (type.equalsIgnoreCase("float")) {
			return new NBTTagFloat(element.getAsFloat());
		} else if (type.equalsIgnoreCase("double")) {
			return new NBTTagDouble(element.getAsDouble());
		} else if (type.equalsIgnoreCase("long")) {
			return new NBTTagLong(element.getAsLong());
		} else {
			return null;
		}
	}

}
