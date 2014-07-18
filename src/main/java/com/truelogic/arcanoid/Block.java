package com.truelogic.arcanoid;

import java.util.ArrayList;
import java.util.List;

import com.truelogic.arcanoid.attributes.LargerBar;
import com.truelogic.arcanoid.attributes.MultiBall;
import com.truelogic.arcanoid.attributes.Weapon;
import com.truelogic.arcanoid.ui.DroppingPixel;
import com.truelogic.arcanoid.ui.GreenBlockPixel;
import com.truelogic.arcanoid.ui.OrangeBlockPixel;
import com.truelogic.arcanoid.ui.Pixel;
import com.truelogic.arcanoid.ui.PurpleBlockPixel;

public class Block {

	private List<Pixel> body;
	
	private DroppingPixel specialAttribute;
	
	public Block(int x, int y) {
		this.body = new ArrayList<Pixel>();
		for (int i = 0; i < 4; i++) {
			body.add(randomBlockPixel(x + i, y));
		}
		if (Math.random() >= 0.8) {
			addSpecialAttribute();
		}
		
	}

	private void addSpecialAttribute() {
		double random = Math.random();
		if (random < 0.3) {
			specialAttribute = new LargerBar();
		} else if (random < 0.7) {
			specialAttribute = new MultiBall();
		} else {
			specialAttribute = new Weapon();
		}
		specialAttribute.setX(body.get(2).getX());
		specialAttribute.setY(body.get(2).getY());
		
	}

	private Pixel randomBlockPixel(int x, int y) {
		if (y < 10) {
			return new GreenBlockPixel(x, y);
		} else if (y <= 15) {
			return new PurpleBlockPixel(x, y);
		} else {
			return new OrangeBlockPixel(x, y);
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
				body.set(i, new PurpleBlockPixel(x + i, y));
			}
			return false;
		}
		if (body.get(0) instanceof PurpleBlockPixel) {
			int x = body.get(0).getX();
			int y = body.get(0).getY();
			for (int i = 0; i < 4; i++) {
				body.set(i, new OrangeBlockPixel(x + i, y));
			}
			return false;
		}
		return true;
	}

	public DroppingPixel getSpecialAttribute() {
		return specialAttribute;
	}

	public void setSpecialAttribute(DroppingPixel specialAttribute) {
		this.specialAttribute = specialAttribute;
	}

	
	

	
	
}
