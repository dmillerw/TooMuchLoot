package dmillerw.tml.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.tml.data.drop.DropLootLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.lang.reflect.InvocationTargetException;

/**
 * @author dmillerw
 */
public class WorldHandler {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new WorldHandler());
    }

    @SubscribeEvent
    public void onWorldLoaded(WorldEvent.Load event) {
        try {
            DropLootLoader.getDropCounts(event.world);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
