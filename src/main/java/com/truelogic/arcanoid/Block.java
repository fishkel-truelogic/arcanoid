package com.truelogic.arcanoid;

import java.util.ArrayList;
import java.util.List;

import com.truelogic.arcanoid.ui.GreenBlockPixel;
import com.truelogic.arcanoid.ui.OrangeBlockPixel;
import com.truelogic.arcanoid.ui.Pixel;
import com.truelogic.arcanoid.ui.PurpleBlockPixel;

public class Block {

	private List<Pixel> body;
	
	public Block(int x, int y) {
		double random = Math.random();
		this.body = new ArrayList<Pixel>();
		for (int i = 0; i < 4; i++) {
			body.add(randomBlockPixel(x + i, y, random));
		}
	}

	private Pixel randomBlockPixel(int x, int y, double random) {
		if (random < 0.3) {
			return new GreenBlockPixel(x, y);
		} else if (random <= 0.6) {
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

	
	
}
