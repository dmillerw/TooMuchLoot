package dmillerw.tml.client.config.entry;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import dmillerw.tml.TooMuchLoot;
import net.minecraft.client.gui.GuiScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class EntryGeneration extends GuiConfigEntries.CategoryEntry {

	public static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		for (String key : TooMuchLoot.CHEST_GEN_KEYS) {
			list.add(new DummyConfigElement.DummyCategoryElement(key, key, EntryLootList.class));
		}
		return list;
	}

	public EntryGeneration(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}

	@Override
	protected GuiScreen buildChildScreen() {
		return new GuiConfig(
			this.owningScreen,
			getConfigElements(),
			this.owningScreen.modID,
			"generation",
			this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
			this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
			GuiConfig.getAbridgedConfigPath(new File(TooMuchLoot.configFolder, TooMuchLoot.GEN_FOLDER).getPath())
		);
	}
}
