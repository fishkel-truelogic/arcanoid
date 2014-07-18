package com.truelogic.arcanoid.ui;

import java.util.List;

import com.truelogic.arcanoid.Block;

public class BulletPixel extends Pixel {

	public boolean move(List<Block> blocks) {
		y -= 1;
		for (Block block : blocks) {
			 for (Pixel p : block.getBody()) {
				 if (p.inSameSpot(this)) {
					 if (block.constraints()) {
						 blocks.remove(block);
					 }
					 return true;
				 }
			 }
		}
		if (y <= 0) {
			return true;
		}
		return false;
	}

}
