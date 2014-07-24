package com.truelogic.arcanoid.model;

import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import com.truelogic.arcanoid.ui.Board;
import com.truelogic.arcanoid.ui.pixel.Pixel;

public class Ball extends Pixel {

	private int vy = 0;
	private int vx = 0;
	private boolean fireBall;

	public Ball(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Ball(int x, int y, int vx, boolean fireball) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = -1;
		this.fireBall = fireball;
	}

	public void move(Integer vbar, List<Block> blocks, List<DroppingItem> specialAttrs) {
		if (vbar != null) {
			vy = -vy;
			vx += vbar;
		}
		if (crashRoof()) {
			vy = -vy;
		}
		if (crashWall()) {
			vx = -vx;
		}

		for (Block block : blocks) {
			if (this.crashWith(block)) {
				if (!fireBall) {
					if (block.constraints()) {
						blocks.remove(block);
					}
				} else {
					blocks.remove(block);
					Board.getInstance().setPoints(Board.getInstance().getPoints() + 500);
				}
				if (block.getSpecialAttribute() != null) {
					specialAttrs.add(block.getSpecialAttribute());
				}
				break;
			}
		}
		x += vx;
		y += vy;
	}

	private boolean crashWith(Block block) {
		Ball leftBall = this.getNewLeftBall();
		Ball rightBall = this.getNewRightBall();
		Ball topBall = this.getNewTopBall();
		Ball bottomBall = this.getNewBottomBall();
		if (block.getBody().get(0).inSameSpot(rightBall)) {
			if (!fireBall)
				vx = -vx;
			return true;
		}
		if (block.getBody().get(3).inSameSpot(leftBall)) {
			if (!fireBall)
				vx = -vx;
			return true;
		}
		for (int i = 0; i < 4; i++) {
			if (block.getBody().get(i).inSameSpot(topBall) || block.getBody().get(i).inSameSpot(bottomBall)) {
				if (!fireBall)
					vy = -vy;
				return true;
			}
		}
		return false;
	}

	public int getVy() {
		return vy;
	}

	public void setVy(int vy) {
		this.vy = vy;
	}

	public boolean crashRoof() {
		return y - 1 <= 0;
	}

	public boolean crashWall() {
		return x - 1 <= 0 || x + 1 >= Board.M_WIDTH;
	}

	public int getVx() {
		return vx;
	}

	public void setVx(int vx) {
		this.vx = vx;
	}

	public boolean died() {
		return y > Board.M_HEIGHT;
	}

	public Ball getNewLeftBall() {
		return new Ball(this.x - 1, this.y);
	}

	public Ball getNewRightBall() {
		return new Ball(this.x + 1, this.y);
	}

	public Ball getNewTopBall() {
		return new Ball(this.x, this.y - 1);
	}

	public Ball getNewBottomBall() {
		return new Ball(this.x, this.y + 1);
	}

	public boolean standStill() {
		return vx == 0 && vy == 0;
	}

	public void activate() {
		vx = 1;
		vy = -1;

	}

	public boolean isFireBall() {
		return fireBall;
	}

	public void setFireBall(boolean fireBall) {
		this.fireBall = fireBall;
	}

	@Override
	public Image getImage() {
		if (!fireBall) {
			return super.getImage();
		} else {
			if (image == null) {
				URL imgURL = getClass().getResource(Pixel.IMAGE_PATH + "FBall.png");
				ImageIcon i = new ImageIcon(imgURL);
				image = i.getImage();
			}
			return image;
		}
	}
}
