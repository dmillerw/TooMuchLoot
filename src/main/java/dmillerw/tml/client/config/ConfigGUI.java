package dmillerw.tml.client.config;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import dmillerw.tml.client.config.entry.EntryGeneration;
import dmillerw.tml.client.config.entry.EntryMain;
import dmillerw.tml.lib.ModInfo;
import net.minecraft.client.gui.GuiScreen;

import java.util.Arrays;
import java.util.List;

/**
 * @author dmillerw
 */
public class ConfigGUI extends GuiConfig {

    private static List<IConfigElement> getConfigElements() {
        return Arrays.asList(
                (IConfigElement) new DummyConfigElement.DummyCategoryElement("Main", "Main", EntryMain.class),
                (IConfigElement) new DummyConfigElement.DummyCategoryElement("Generation", "Generation", EntryGeneration.class)
        );
    }

    public ConfigGUI(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), ModInfo.ID, true, false, ModInfo.NAME);
    }
}
