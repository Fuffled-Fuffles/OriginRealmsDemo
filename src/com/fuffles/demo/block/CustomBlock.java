package com.fuffles.demo.block;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import com.fuffles.demo.block.CustomBlockMetaBuilder.CustomBlockMeta;
import com.fuffles.demo.item.CustomBlockItem;

import net.minecraft.server.v1_16_R3.MathHelper;

public class CustomBlock
{	
	protected final CustomBlockItem placer;
	protected final CustomBlockMeta meta;
	private BlockData fabricatedBlock;
	
	public CustomBlock(CustomBlockItem placer, CustomBlockMeta meta) 
	{
		this.placer = placer;
		this.meta = meta;
	}
	
	public CustomBlockItem getPlacer()
	{
		return this.placer;
	}
	
	public CustomBlockMeta getBlockMeta()
	{
		return this.meta;
	}
	
	public BlockData getPlaceBlock()
	{
		if (this.fabricatedBlock == null)
		{
			this.fabricatedBlock = Bukkit.createBlockData(this.meta.blockState);
		}
		return this.fabricatedBlock;
	}
	
	public void doParticlePop(World world, Block block)
	{
        int cube = Math.max(2, MathHelper.f(1D / 0.25D));

        for(int i = 0; i < cube; i++) 
        {
           for(int j = 0; j < cube; j++) 
           {
              for(int k = 0; k < cube; k++) 
              {
            	  double x = ((double)i + 0.5D) / (double)cube;
            	  double y = ((double)j + 0.5D) / (double)cube;
            	  double z = ((double)k + 0.5D) / (double)cube;

            	  world.spawnParticle(Particle.BLOCK_DUST, block.getX() + x, block.getY() + y, block.getZ() + z, 1, x - 0.5D, y - 0.5D, z - 0.5D, block.getBlockData());
              }
           }
        }
	}
	
	public boolean canBreakWith(ToolType type, ToolTier tier)
	{
		if (this.meta.tool.equals(ToolType.NONE))
		{
			return true;
		}
		else if (type.equals(this.meta.tool))
		{
			if (this.meta.tier.equals(ToolTier.ANY))
			{
				return true;
			}
			else if (tier.order >= this.meta.tier.order)
			{
				return true;
			}
		}
		return false;
	}
}
