package com.truelogic.arcanoid.model;

import com.truelogic.arcanoid.ui.Board;
import com.truelogic.arcanoid.ui.pixel.BarPixel;
import com.truelogic.arcanoid.ui.pixel.Pixel;
import com.truelogic.arcanoid.ui.pixel.attributes.FireBall;
import com.truelogic.arcanoid.ui.pixel.attributes.LargerBar;
import com.truelogic.arcanoid.ui.pixel.attributes.MultiBall;
import com.truelogic.arcanoid.ui.pixel.attributes.Weapon;

public class DroppingItem extends Pixel {

	public void move() {
		y += 1;
	}

	public boolean outOfScreen() {
		return y > Board.M_HEIGHT;
	}

	public boolean crash(Bar bar) {
		DroppingItem dp = new DroppingItem();
		dp.setX(x);
		dp.setY(y + 1);
		for (BarPixel bp : bar.getBody()) {
			if (bp.inSameSpot(dp)) {
				this.apply(bar);
				return true;
			}
		}
		return false;
	}

	private void apply(Bar bar) {
		if (this instanceof MultiBall) {
			Board.applyMultiBall();
		} else if (this instanceof LargerBar) {
			bar.applyLargerBar();
		} else if (this instanceof Weapon) {
			bar.setWeapon(true);
		} else if (this instanceof FireBall) {
			Board.applyFireball();
		}
		
	}

}
