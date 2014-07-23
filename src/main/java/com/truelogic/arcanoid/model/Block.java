package com.truelogic.arcanoid.model;

import java.util.ArrayList;
import java.util.List;

import com.truelogic.arcanoid.ui.pixel.Pixel;
import com.truelogic.arcanoid.ui.pixel.attributes.FireBall;
import com.truelogic.arcanoid.ui.pixel.attributes.LargerBar;
import com.truelogic.arcanoid.ui.pixel.attributes.MultiBall;
import com.truelogic.arcanoid.ui.pixel.attributes.Weapon;
import com.truelogic.arcanoid.ui.pixel.block.GreenBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.GreenLeftBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.GreenRightBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.OrangeBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.OrangeLeftBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.OrangeRightBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.PurpleBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.PurpleLeftBlockPixel;
import com.truelogic.arcanoid.ui.pixel.block.PurpleRightBlockPixel;

public class Block {

	private List<Pixel> body;

	private DroppingItem specialAttribute;

	public Block(int x, int y) {
		this.body = new ArrayList<Pixel>();
		for (int i = 0; i < 4; i++) {
			body.add(selectBlockPixel(x, y, i));
		}
		if (Math.random() >= 0.8) {
			addSpecialAttribute();
		}

	}

	private void addSpecialAttribute() {
		double random = Math.random();
		if (random < 0.2) {
			specialAttribute = new LargerBar();
		} else if (random < 0.5) {
			specialAttribute = new MultiBall();
		} else if (random < 0.7) {
			specialAttribute = new FireBall();
		} else {
			specialAttribute = new Weapon();
		}
		specialAttribute.setX(body.get(2).getX());
		specialAttribute.setY(body.get(2).getY());

	}

	private Pixel selectBlockPixel(int x, int y, int i) {
		if (y < 10) {
			if (i == 0) {
				return new GreenLeftBlockPixel(x + i, y);
			} else if (i == 3) {
				return new GreenRightBlockPixel(x + i, y);
			} else {
				return new GreenBlockPixel(x + i, y);
			}
		} else if (y <= 15) {
			if (i == 0) {
				return new PurpleLeftBlockPixel(x + i, y);
			} else if (i == 3) {
				return new PurpleRightBlockPixel(x + i, y);
			} else {
				return new PurpleBlockPixel(x + i, y);
			}
		} else {
			if (i == 0) {
				return new OrangeLeftBlockPixel(x + i, y);
			} else if (i == 3) {
				return new OrangeRightBlockPixel(x + i, y);
			} else {
				return new OrangeBlockPixel(x + i, y);
			}
		}
	}

	public List<Pixel> getBody() {
		return body;
	}

	public void setBody(List<Pixel> body) {
		this.body = body;
	}

	public boolean constraints() {
		if (body.get(0) instanceof GreenBlockPixel) {
			int x = body.get(0).getX();
			int y = body.get(0).getY();
			for (int i = 0; i < 4; i++) {
				if (i == 0) {
					body.set(i, new PurpleLeftBlockPixel(x + i, y));
				} else if (i == 3) {
					body.set(i, new PurpleRightBlockPixel(x + i, y));
				} else {
					body.set(i, new PurpleBlockPixel(x + i, y));

				}
			}
			return false;
		}
		if (body.get(0) instanceof PurpleBlockPixel) {
			int x = body.get(0).getX();
			int y = body.get(0).getY();
			for (int i = 0; i < 4; i++) {
				if (i == 0) {
					body.set(i, new OrangeLeftBlockPixel(x + i, y));
				} else if (i == 3) {
					body.set(i, new OrangeRightBlockPixel(x + i, y));
				} else {
					body.set(i, new OrangeBlockPixel(x + i, y));

				}
			}
			return false;
		}
		return true;
	}

	public DroppingItem getSpecialAttribute() {
		return specialAttribute;
	}

	public void setSpecialAttribute(DroppingItem specialAttribute) {
		this.specialAttribute = specialAttribute;
	}

}
