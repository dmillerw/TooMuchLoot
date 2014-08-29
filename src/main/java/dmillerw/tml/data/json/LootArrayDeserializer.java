package dmillerw.tml.data.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dmillerw.tml.data.LootLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class LootArrayDeserializer implements JsonDeserializer<LootLoader.LootArrayWrapper> {

    @Override
    public LootLoader.LootArrayWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LootLoader.LootArrayWrapper wrapper = new LootLoader.LootArrayWrapper();
        List<LootLoader.SerializedLoot> lootList = new ArrayList<LootLoader.SerializedLoot>();

        if (!json.isJsonArray()) {
            return null; // Figure out how to fire a proper exception here...
        }

        for (JsonElement element : json.getAsJsonArray()) {
            lootList.add(context.<LootLoader.SerializedLoot>deserialize(element, LootLoader.SerializedLoot.class));
        }

        wrapper.loot = lootList.toArray(new LootLoader.SerializedLoot[lootList.size()]);
        return wrapper;
    }
}
