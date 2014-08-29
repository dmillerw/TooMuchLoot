package dmillerw.tml.client.config.entry;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import dmillerw.tml.config.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class EntryLootList extends GuiConfigEntries.CategoryEntry {

    public static List<IConfigElement> getConfigElements(String key) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        for (String category : ConfigHandler.configMapping.get(key).getCategoryNames()) {
            list.add(new DummyConfigElement.DummyCategoryElement(category.replace("_", "."), (key + "-" + category), EntryLootProperty.class));
        }
        return list;
    }

    public EntryLootList(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    protected GuiScreen buildChildScreen() {
        return new GuiConfig(
                this.owningScreen,
                getConfigElements(this.configElement.getName()),
                this.owningScreen.modID,
                this.configElement.getName(),
                this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                GuiConfig.getAbridgedConfigPath(ConfigHandler.configMapping.get(this.configElement.getName()).toString())
        );
    }
}
