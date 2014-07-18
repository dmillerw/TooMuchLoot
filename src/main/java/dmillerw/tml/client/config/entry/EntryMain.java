package dmillerw.tml.client.config.entry;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import dmillerw.tml.config.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

/**
 * @author dmillerw
 */
public class EntryMain extends GuiConfigEntries.CategoryEntry {

	public EntryMain(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}

	@Override
	protected GuiScreen buildChildScreen() {
		return new GuiConfig(
			this.owningScreen,
			(new ConfigElement(ConfigHandler.main.getCategory("main"))).getChildElements(),
			this.owningScreen.modID,
			"main",
			this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
			this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
			GuiConfig.getAbridgedConfigPath(ConfigHandler.main.toString())
		);
	}
}
