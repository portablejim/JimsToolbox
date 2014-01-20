package portablejim.jimstoolbox;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import portablejim.jimstoolbox.block.BlockDrain;
import portablejim.jimstoolbox.block.tileentity.TEDrain;
import portablejim.jimstoolbox.network.PacketHandler;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 19/01/14
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Mod(modid = JimsToolbox.MOD_ID, name = JimsToolbox.MOD_NAME)
@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = {JimsToolbox.MOD_ID}, packetHandler = PacketHandler.class)
public class JimsToolbox {
    public static final String MOD_ID = "jimstoolbox";
    public static final String MOD_NAME = "Jim's Toolbox";

    public BlockDrain drain;

    @Mod.Instance
    public JimsToolbox instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        drain = new BlockDrain(2345, Material.iron);
        GameRegistry.registerBlock(drain, "jimstoolbox:Drain");
        TileEntity.addMapping(TEDrain.class, MOD_ID + ":Drain");
    }

}
