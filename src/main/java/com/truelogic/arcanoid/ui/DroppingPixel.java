package com.truelogic.arcanoid.ui;

import com.truelogic.arcanoid.Bar;
import com.truelogic.arcanoid.attributes.LargerBar;
import com.truelogic.arcanoid.attributes.MultiBall;
import com.truelogic.arcanoid.attributes.Weapon;

public class DroppingPixel extends Pixel {

	public void move() {
		y += 1;
	}

	public boolean outOfScreen() {
		return y > Board.M_HEIGHT;
	}

	public boolean crash(Bar bar) {
		DroppingPixel dp = new DroppingPixel();
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
		}
		
	}

}
