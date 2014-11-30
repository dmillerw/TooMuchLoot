package dmillerw.tml.helper;

import cpw.mods.fml.common.FMLLog;

/**
 * @author dmillerw
 */
public class LogHelper {

    public static boolean log = true;

    public static void warn(String msg, boolean big) {
        if (big) FMLLog.bigWarning("[TooMuchLoot]: %s", msg);
        else FMLLog.warning("[TooMuchLoot]: %s", msg);
    }

    public static void logParse(String file) {
        if (log) FMLLog.info("[TooMuchLoot]: Parsing [%s]", file);
    }

    public static void logOverride(String key) {
        if (log) FMLLog.info("[TooMuchLoot]: Overriding loot-table [%s]", key);
    }

    public static void logOverrideError(String key, String file) {
        if (log) FMLLog.warning("[TooMuchLoot]: Failed to override loot-table [%s] as defined in [%s]. It has already been overridden!", key, file);
    }

    public static void logAddition(String key, String display) {
        if (log) FMLLog.info("[TooMuchLoot]: Adding [%s] to [%s]", display, key);
    }

    public static void logRemoval(String key, String display) {
        if (log) FMLLog.info("[TooMuchLoot]: Removed [%s] from [%s]", display, key);
    }
}
