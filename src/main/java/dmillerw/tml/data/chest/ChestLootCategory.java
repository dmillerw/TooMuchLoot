package dmillerw.tml.data.chest;

import com.google.common.collect.Lists;
import dmillerw.tml.TooMuchLoot;
import dmillerw.tml.data.LootLoadingMode;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.util.List;

/**
 * @author dmillerw
 */
public class ChestLootCategory {

    public static ChestLootCategory fromChestGenHooks(String category) {
        ChestGenHooks chestGenHooks = ChestGenHooks.getInfo(category);
        ChestLootCategory lootCategory = new ChestLootCategory();
        lootCategory.category = category;
        lootCategory.count_min = chestGenHooks.getMin();
        lootCategory.count_max = chestGenHooks.getMax();

        List<ChestLootItem> lootList = Lists.newArrayList();
        try {
            for (WeightedRandomChestContent item : (List<WeightedRandomChestContent>)TooMuchLoot.contents.get(chestGenHooks)) {
                lootList.add(ChestLootItem.fromChestContent(item));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        lootCategory.loot = lootList.toArray(new ChestLootItem[lootList.size()]);
        return lootCategory;
    }

    public String category;
    public LootLoadingMode loading_mode = LootLoadingMode.OVERRIDE;
    public int count_min;
    public int count_max;
    public ChestLootItem[] loot;

    public ChestLootCategory checkCountValues() {
        if (count_min == -1 && count_max == -1) {
            count_min = 1;
            count_max = 1;
        } else if (count_min == -1) {
            count_min = count_max;
        } else if (count_max == -1) {
            count_max = count_min;
        }

        return this;
    }

    public ChestGenHooks toChestGenHooks() {
        WeightedRandomChestContent[] chestContents = new WeightedRandomChestContent[loot.length];
        for (int i=0; i<loot.length; i++) {
            chestContents[i] = loot[i].toChestContent();
        }
        return new ChestGenHooks(category, chestContents, count_min, count_max);
    }
}
