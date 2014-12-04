package dmillerw.tml.command;

import dmillerw.tml.TooMuchLoot;
import dmillerw.tml.data.chest.ChestLootLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import java.util.Random;

/**
 * @author dmillerw
 */
public class CommandChestLoot extends CommandBase {

    @Override
    public String getCommandName() {
        return "loot";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return "/loot <reload|generate|reset|spawnDebugChest [tag, x, y, z]>";
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                ChestLootLoader.restoreCachedLootTable();
                ChestLootLoader.loadFiles(TooMuchLoot.lootFolder);
            } else if (args[0].equalsIgnoreCase("generate")) {
                ChestLootLoader.generateFiles(TooMuchLoot.generatedFolder);
            } else if (args[0].equalsIgnoreCase("reset")) {
                ChestLootLoader.restoreCachedLootTable();
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("generate")) {
                ChestLootLoader.generateFiles(TooMuchLoot.generatedFolder, args[1]);
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("spawnDebugChest")) {
                String tag = args[1];
                EntityPlayerMP entityPlayerMP = getCommandSenderAsPlayer(commandSender);
                Random random = new Random();
                WeightedRandomChestContent[] contents = ChestGenHooks.getItems(tag, random);
                int count = ChestGenHooks.getCount(tag, random);
                int x = (int)Math.floor(func_110666_a(commandSender, entityPlayerMP.posX, args[2]));
                int y = (int)Math.floor(func_110665_a(commandSender, entityPlayerMP.posY, args[3], 0, 0));
                int z = (int)Math.floor(func_110666_a(commandSender, entityPlayerMP.posZ, args[4]));

                if (entityPlayerMP.worldObj.isAirBlock(x, y, z) && World.doesBlockHaveSolidTopSurface(entityPlayerMP.worldObj, x, y - 1, z)) {
                    entityPlayerMP.worldObj.setBlock(x, y, z, Blocks.chest, 0, 2);
                    TileEntityChest tileEntity = (TileEntityChest) entityPlayerMP.worldObj.getTileEntity(x, y, z);
                    if (tileEntity != null) {
                        WeightedRandomChestContent.generateChestContents(random, contents, tileEntity, count);
                    }
                }
            }
        }
    }
}
