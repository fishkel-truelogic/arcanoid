package com.truelogic.arcanoid.model;

import java.util.ArrayList;
import java.util.List;

import com.truelogic.arcanoid.ui.Board;
import com.truelogic.arcanoid.ui.pixel.BarPixel;
import com.truelogic.arcanoid.ui.pixel.Pixel;

public class Bar {

	public static final int LEFT = 4;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	private static int BODY_LENGTH = 6;
	private List<BarPixel> body;
	private int direction;
	private boolean weapon;
	
	public Bar() {
		BODY_LENGTH = 6;
		int y = Board.M_HEIGHT - 2;
		int x = Board.M_WIDTH / 2 - BODY_LENGTH / 2; 
		this.body = new ArrayList<BarPixel>();
		for (int i = 0; i < BODY_LENGTH + 1; i++) {
			body.add(new BarPixel(x, y));
			x++;
		}
	}
	
	public void move(List<Ball> balls) {
		switch (this.direction) {
		case LEFT:
			if (!(this.body.get(0).getX() - 1 == 0)) {

				BarPixel newTop = new BarPixel(body.get(0).getX() - 1, body.get(0)
						.getY());
				List<BarPixel> newBody = new ArrayList<BarPixel>();
				newBody.add(newTop);
				newBody.addAll(body);
				newBody.remove(newBody.size() - 1);
				body = newBody;
			} else {
				this.direction = 0;
			}
			break;
		case RIGHT:
			if (!(this.body.get(body.size() - 1).getX() + 1 == Board.M_WIDTH)) {
				BarPixel newBottom = new BarPixel(body.get(body.size() - 1)
						.getX() + 1, body.get(body.size() - 1).getY());
				List<BarPixel> newBody2 = new ArrayList<BarPixel>();
				body.remove(0);
				newBody2.addAll(body);
				newBody2.add(newBottom);
				body = newBody2;
			} else {
				this.direction = 0;
			}
			break;
		default:
			break;
		}
		for (Ball ball : balls) {
			if (ball.standStill()) {
				ball.setX(body.get(3).getX());
			}
		}

	}

	public List<BarPixel> getBody() {
		return body;
	}

	public void setBody(List<BarPixel> body) {
		this.body = body;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean crash(Ball ball) {
		for (BarPixel p : body) {
			Ball newBall = new Ball(ball.getX(), ball.getY() + 1);
			if (p.inSameSpot(newBall)) {
				return true;
			}
		}
		return false;
	}

	public Integer getVelocity() {
		switch (this.direction) {
		case RIGHT:
			return 1;
		case LEFT:
			return -1;
		case 0:
			return 0;
		}
		return null;
	}

	public void applyLargerBar() {
		BODY_LENGTH = BODY_LENGTH + 3;
		body.add(new BarPixel(body.get(body.size() -1).getX() + 1, body.get(0).getY()));
		body.add(new BarPixel(body.get(body.size() -1).getX() + 1, body.get(0).getY()));
		body.add(new BarPixel(body.get(body.size() -1).getX() + 1, body.get(0).getY()));
		
	}

	public boolean isWeapon() {
		return weapon;
	}

	public void setWeapon(boolean weapon) {
		this.weapon = weapon;
	}

	public boolean move(int x) {
		boolean	result = body.get(0).getX() > x / Pixel.SIZE;
		this.body = new ArrayList<BarPixel>();
		for (int i = 0; i < BODY_LENGTH + 1; i++) {
			body.add(new BarPixel(x / Pixel.SIZE, Board.M_HEIGHT - 2));
			x += Pixel.SIZE;
		}
		return result;
	}
	
	

	
}
