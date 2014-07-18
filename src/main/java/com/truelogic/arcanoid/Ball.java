package com.truelogic.arcanoid;

import java.util.List;

import com.truelogic.arcanoid.ui.Board;
import com.truelogic.arcanoid.ui.Pixel;

public class Ball extends Pixel {
	
	private int vy = -1;
	private int vx = 1;
	
	public Ball(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(Integer vbar, List<Block> blocks) {
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
		x += vx;
		y += vy;
		
		for (Block block : blocks) {
			if (this.crashWith(block)) {
				blocks.remove(block);
				return;
			}
		}
	}


	private boolean crashWith(Block block) {
		Ball leftBall = this.getNewLeftBall();
		Ball rightBall = this.getNewRightBall();
		Ball topBall = this.getNewTopBall();
		Ball bottomBall = this.getNewBottomBall();
		if (block.getBody().get(0).inSameSpot(leftBall)) {
			vx = -vx;
			return true;
		}
		if (block.getBody().get(2).inSameSpot(rightBall)) {
			vx = -vx;
			return true;
		}
		for (int i = 0; i < 4; i++) {
			if (block.getBody().get(i).inSameSpot(topBall) || block.getBody().get(i).inSameSpot(bottomBall)) {
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
	
	
	
}
