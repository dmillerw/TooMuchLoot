package dmillerw.tml;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import dmillerw.tml.command.CommandChestLoot;
import dmillerw.tml.data.chest.ChestLootLoader;
import dmillerw.tml.handler.WorldHandler;
import dmillerw.tml.helper.LogHelper;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

/**
 * @author dmillerw
 */
@Mod(modid = "TML", name = "TooMuchLoot", version = "%MOD_VERSION%")
public class TooMuchLoot {

    public static final String CONFIG_FOLDER = "TooMuchLoot/";
    public static final String LOOT_FOLDER = "loot/";

    @Mod.Instance("TML")
    public static TooMuchLoot instance;

    public static HashMap<String, ChestGenHooks> lootTableCache;

    public static String[] chestGenCategories = new String[0];

    public static File configFolder;
    public static File lootFolder;
    public static File generatedFolder;

    public static Field chestInfo;
    public static Field contents;

    public static boolean failed = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configFolder = new File(event.getModConfigurationDirectory(), CONFIG_FOLDER);
        lootFolder = new File(configFolder, LOOT_FOLDER);
        generatedFolder = new File(lootFolder, "generated");

        try {
            chestInfo = ChestGenHooks.class.getDeclaredField("chestInfo");
            chestInfo.setAccessible(true);
        } catch (NoSuchFieldException e) {
            failed = true;
            LogHelper.warn("Failed to obtain chestInfo field. This mod will now cease to function.", true);
            e.printStackTrace();
        }

        try {
            contents = ChestGenHooks.class.getDeclaredField("contents");
            contents.setAccessible(true);
        } catch (NoSuchFieldException e) {
            failed = true;
            LogHelper.warn("Failed to obtain contents field. This mod will now cease to function.", true);
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        WorldHandler.register();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        if (failed)
            return;

        event.registerServerCommand(new CommandChestLoot());


        try {
            lootTableCache = ChestLootLoader.copyLootTable(((HashMap<String, ChestGenHooks>) chestInfo.get(ChestGenHooks.class)));

            // Load chest-gen keys array
            Set<String> keys = lootTableCache.keySet();
            chestGenCategories = keys.toArray(new String[keys.size()]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!lootFolder.exists()) {
            lootFolder.mkdirs();
        }

        // Load loot
        ChestLootLoader.loadFiles(lootFolder);
    }
}
