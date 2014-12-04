package dmillerw.tml.data.chest;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;

public class ChestLootItem {

    public static ChestLootItem fromChestContent(WeightedRandomChestContent weightedRandomChestContent) {
        ChestLootItem lootItem = new ChestLootItem();
        lootItem.item = GameData.getItemRegistry().getNameForObject(weightedRandomChestContent.theItemId.getItem());
        lootItem.damage = weightedRandomChestContent.theItemId.getItemDamage();
        if (weightedRandomChestContent.theItemId.hasTagCompound())
            lootItem.nbt = weightedRandomChestContent.theItemId.getTagCompound();
        lootItem.weight = weightedRandomChestContent.itemWeight;
        lootItem.count_min = weightedRandomChestContent.theMinimumChanceToGenerateItem;
        lootItem.count_max = weightedRandomChestContent.theMaximumChanceToGenerateItem;
        return lootItem;
    }

    public String item;
    public int damage = 0;
    public NBTTagCompound nbt = new NBTTagCompound();
    public int weight;
    public int count_min = -1;
    public int count_max = -1;

    public ChestLootItem checkCountValues() {
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

    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(GameData.getItemRegistry().getObject(item), 1, damage);
        itemStack.setTagCompound(nbt);
        return itemStack;
    }

    public WeightedRandomChestContent toChestContent() {
        return new WeightedRandomChestContent(toItemStack(), count_min, count_max, weight);
    }
}