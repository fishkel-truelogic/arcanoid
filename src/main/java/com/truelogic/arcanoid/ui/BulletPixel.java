package com.truelogic.arcanoid.ui;

import java.util.List;

import com.truelogic.arcanoid.Block;

public class BulletPixel extends Pixel {

	public boolean move(List<Block> blocks, List<DroppingPixel> specialAttrs) {
		y -= 1;
		for (Block block : blocks) {
			 for (Pixel p : block.getBody()) {
				 if (p.inSameSpot(this)) {
					 if (block.constraints()) {
						 blocks.remove(block);
					 }
					 if (block.getSpecialAttribute() != null) {
						 specialAttrs.add(block.getSpecialAttribute());
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
