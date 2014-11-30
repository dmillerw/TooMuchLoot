package dmillerw.tml.helper;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.nbt.*;

import java.util.List;

/**
 * @author dmillerw
 */
public class NBTHelper {

    private static final String[] TAG_LIST = new String[] {"c", "field_74747_a", "tagList"};

    public static boolean isNumber(NBTBase nbtBase) {
        return nbtBase instanceof NBTTagByte || nbtBase instanceof NBTTagShort || nbtBase instanceof NBTTagInt || nbtBase instanceof NBTTagFloat || nbtBase instanceof NBTTagDouble || nbtBase instanceof NBTTagLong;
    }

    public static Number getNumber(NBTBase nbtBase) {
        if (!isNumber(nbtBase))
            return -1;

        if (nbtBase instanceof NBTTagByte)
            return ((NBTTagByte) nbtBase).func_150290_f();
        else if (nbtBase instanceof NBTTagShort)
            return ((NBTTagShort) nbtBase).func_150289_e();
        else if (nbtBase instanceof NBTTagInt)
            return ((NBTTagInt) nbtBase).func_150287_d();
        else if (nbtBase instanceof NBTTagFloat)
            return ((NBTTagFloat) nbtBase).func_150288_h();
        else if (nbtBase instanceof NBTTagDouble)
            return ((NBTTagDouble) nbtBase).func_150286_g();
        else if (nbtBase instanceof NBTTagLong)
            return ((NBTTagLong) nbtBase).func_150291_c();
        else
            return -1;
    }

    public static String getType(Number number) {
        if (number instanceof Byte)
            return "byte";
        else if (number instanceof Short)
            return "short";
        else if (number instanceof Integer)
            return "integer";
        else if (number instanceof Float)
            return "float";
        else if (number instanceof Double)
            return "double";
        else if (number instanceof Long)
            return "long";
        else
            return "";
    }

    public static List<NBTBase> getTagList(NBTTagList nbtTagList) {
        return ObfuscationReflectionHelper.getPrivateValue(NBTTagList.class, nbtTagList, TAG_LIST);
    }
}
