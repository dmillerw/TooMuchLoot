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
        if (log) FMLLog.info("[TooMuchLoot]: Parsing %s", file);
    }

    public static void logAddition(String key, String display) {
        if (log) FMLLog.info("[TooMuchLoot]: Adding %s to %s", display, key);
    }

    public static void logModification(String key, String display) {
        if (log) FMLLog.info("[TooMuchLoot]: %s from %s has been modified", display, key);
    }

    public static void logRemoval(String key, String display) {
        if (log) FMLLog.info("[TooMuchLoot]: Removed %s from %s", display, key);
    }

    public static String interpretReason(String reason) {
        if (reason.equalsIgnoreCase("java.lang.IllegalStateException: Expected BEGIN_ARRAY but was STRING")) {
            return "You probably are still defining the category with a string. Switch to an array!";
        } else {
            return reason;
        }
    }
}
