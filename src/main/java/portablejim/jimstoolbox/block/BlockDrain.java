package portablejim.jimstoolbox.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import portablejim.jimstoolbox.block.tileentity.TEDrain;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 19/01/14
 * Time: 4:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class BlockDrain extends BlockContainer {

    public BlockDrain(int par1, Material par2Material) {
        super(par1, par2Material);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("jimstoolbox:drain");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TEDrain(world);
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
        if(te instanceof TEDrain) {
            TEDrain drain = (TEDrain) te;

            drain.startDrain();
        }

        return false;
    }
}
