package dmillerw.tml.data;

import net.minecraft.nbt.NBTBase;

/**
 * @author dmillerw
 */
public class NBTKeyAndTag {

    public String key;
    public NBTBase tag;

    public NBTKeyAndTag(String key, NBTBase tag) {
        this.key = key;
        this.tag = tag;
    }
}
