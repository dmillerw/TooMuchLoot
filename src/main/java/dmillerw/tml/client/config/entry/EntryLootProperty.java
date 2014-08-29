package dmillerw.tml.client.config.entry;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import dmillerw.tml.config.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.List;

/**
 * @author dmillerw
 */
public class EntryLootProperty extends GuiConfigEntries.CategoryEntry {

    public EntryLootProperty(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement);
    }

    @Override
    protected GuiScreen buildChildScreen() {
        String key = this.configElement.getLanguageKey().split("-")[0];
        String item = this.configElement.getLanguageKey().split("-")[1];

        List<IConfigElement> list = (new ConfigElement(ConfigHandler.configMapping.get(key).getCategory(item)).getChildElements());

        return new GuiConfig(
                this.owningScreen,
                list,
                this.owningScreen.modID,
                this.configElement.getName(),
                this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                this.configElement.getName()
        );
    }
}
