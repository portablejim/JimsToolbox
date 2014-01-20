package portablejim.jimstoolbox.block.tileentity;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import portablejim.jimstoolbox.util.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 19/01/14
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TEDrain extends TileEntity {
    HashSet<Point> blocksSeen = new HashSet<Point>();
    HashMap<Integer, Set<Point>> fluidToRemove;
    ConcurrentLinkedQueue<Point> queuedBlocks;
    int maxHeightBlock;
    int startY;
    boolean enabled;

    public TEDrain(World world) {
        super();

        blocksSeen = new HashSet<Point>();
        fluidToRemove = new HashMap<Integer, Set<Point>>();
        queuedBlocks = new ConcurrentLinkedQueue<Point>();
        maxHeightBlock = 0;
        startY = 0;

        enabled = false;
    }

    private void testBlock(int x, int y, int z) {
        Block targetBlock = Block.blocksList[worldObj.getBlockId(x, y, z)];
        Fluid targetFluid = FluidRegistry.lookupFluidForBlock(targetBlock);

        if(targetFluid != null && !targetFluid.isGaseous(worldObj, x, y, z)) {

            if(!fluidToRemove.containsKey(y)) {
                fluidToRemove.put(y, new HashSet<Point>());
                if(maxHeightBlock < y) {
                    maxHeightBlock = y;
                }
            }

            Point thisPos = new Point(x, y, z);
            if(!blocksSeen.contains(thisPos))
            {
                fluidToRemove.get(y).add(thisPos);
                blocksSeen.add(thisPos);

                queuedBlocks.add(new Point(x, y + 1, z));
                queuedBlocks.add(new Point(x + 1, y, z));
                queuedBlocks.add(new Point(x - 1, y, z));
                queuedBlocks.add(new Point(x, y, z + 1));
                queuedBlocks.add(new Point(x, y, z - 1));
            }
        }
    }

    private void processBlock() {
        if(!queuedBlocks.isEmpty()) {
            Point target = queuedBlocks.remove();
            testBlock(target.getX(), target.getY(), target.getZ());
        }
    }

    private void removeBlock() {
        while(maxHeightBlock > startY && fluidToRemove.get(maxHeightBlock).size() <= 0) {
            maxHeightBlock--;
        }

        if(maxHeightBlock >= startY && fluidToRemove.get(maxHeightBlock) != null) {
            FMLLog.info("REMOVE BLOCK: Size: %d", fluidToRemove.get(maxHeightBlock).size());
            Point targetPoint = fluidToRemove.get(maxHeightBlock).iterator().next();
            fluidToRemove.get(maxHeightBlock).remove(targetPoint);

            worldObj.setBlock(targetPoint.getX(), targetPoint.getY(), targetPoint.getZ(), 0, 0, 6);

            if(fluidToRemove.get(maxHeightBlock).size() == 0) {
                maxHeightBlock--;
            }
        }
    }

    public void startDrain() {
        FMLLog.info("START DRAIN");
        queuedBlocks.clear();
        fluidToRemove.clear();
        blocksSeen.clear();

        enabled = true;

        startY = this.yCoord;

        queuedBlocks.add(new Point(this.xCoord, this.yCoord + 1, this.zCoord));
        queuedBlocks.add(new Point(this.xCoord + 1, this.yCoord, this.zCoord));
        queuedBlocks.add(new Point(this.xCoord - 1, this.yCoord, this.zCoord));
        queuedBlocks.add(new Point(this.xCoord, this.yCoord, this.zCoord + 1));
        queuedBlocks.add(new Point(this.xCoord, this.yCoord, this.zCoord - 1));

    }

    public void stopDrain() {
        FMLLog.info("STOP DRAIN");
        this.enabled = false;
    }

    static int x = 0;

    @Override
    public void updateEntity() {
        for(int i = 1; i < 500; i++) {
            doUpdateEntity();
        }
    }

    public void doUpdateEntity() {
        if(!enabled) return;

        if(!queuedBlocks.isEmpty()) {
            processBlock();
        }
        else if(maxHeightBlock >= startY){
            removeBlock();
        }
        else {
            stopDrain();
        }
    }
}
