package com.truelogic.arcanoid.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.truelogic.arcanoid.model.Ball;
import com.truelogic.arcanoid.model.Bar;
import com.truelogic.arcanoid.model.Block;
import com.truelogic.arcanoid.model.DroppingItem;
import com.truelogic.arcanoid.ui.pixel.Background;
import com.truelogic.arcanoid.ui.pixel.BulletPixel;
import com.truelogic.arcanoid.ui.pixel.Pixel;

public class Board extends JPanel implements ActionListener {

	private static final long serialVersionUID = 592910003380869323L;

	private static final int DELAY = 40;

	public static final int M_WIDTH = 50;

	public static final int M_HEIGHT = 50;

	private static final int B_WIDTH = M_WIDTH * Pixel.SIZE;

	private static final int B_HEIGHT = M_HEIGHT * Pixel.SIZE;

	private static final int MARGIN_LEFT = 0;

	private static final int MARGIN_TOP = 0;

	private static Board instance;

	private Timer timer;

	private Bar bar;

	private List<Ball> balls;

	private List<Block> blocks;

	private List<DroppingItem> specialAttrs;

	private BulletPixel bullet;

	private BulletPixel bullet2;

	private Background background;

	private int lives;
	
	private int points;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBackground(g);
		paintPoints(g);
		if (paintLives(g)) {
			paintBar(g);
			paintBlocks(g);
			paintBall(g);
			paintSpecialAttrs(g);
			paintBullets(g);
		}

	}

	private void paintPoints(Graphics g) {
		String msg = "Points: " + points;
		Font small = new Font("Helvetica", Font.BOLD, 22);
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, 5 * Pixel.SIZE, 2 * Pixel.SIZE);
	}

	private boolean paintLives(Graphics g) {
		String msg = "Lives: " + lives;
		Font small = new Font("Helvetica", Font.BOLD, 22);
		FontMetrics metr = getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		if (lives >= 0) {
			g.drawString(msg, M_WIDTH / 2 * Pixel.SIZE, 2 * Pixel.SIZE);
			return true;
		} else {
			msg = "Game Over";
			g.drawString(msg,
					(M_WIDTH / 2) * Pixel.SIZE - metr.stringWidth(msg),
					M_HEIGHT / 2 * Pixel.SIZE);
			return false;
		}
	}

	private void paintBackground(Graphics g) {
		g.drawImage(background.getImage(), -20, -20, this);

	}

	private void paintBullets(Graphics g) {
		if (bullet != null) {
			int x = MARGIN_LEFT + bullet.getX() * Pixel.SIZE;
			int y = MARGIN_TOP + bullet.getY() * Pixel.SIZE;
			g.drawImage(bullet.getImage(), x, y, this);

		}
		if (bullet2 != null) {
			int x = MARGIN_LEFT + bullet2.getX() * Pixel.SIZE;
			int y = MARGIN_TOP + bullet2.getY() * Pixel.SIZE;
			g.drawImage(bullet2.getImage(), x, y, this);
		}

	}

	private void paintSpecialAttrs(Graphics g) {
		for (DroppingItem sa : specialAttrs) {
			int x = MARGIN_LEFT + sa.getX() * Pixel.SIZE;
			int y = MARGIN_TOP + sa.getY() * Pixel.SIZE;
			g.drawImage(sa.getImage(), x, y, this);
		}

	}

	private void paintBlocks(Graphics g) {
		for (Block blck : this.blocks) {
			for (Pixel bp : blck.getBody()) {
				int x = MARGIN_LEFT + bp.getX() * Pixel.SIZE;
				int y = MARGIN_TOP + bp.getY() * Pixel.SIZE;
				g.drawImage(bp.getImage(), x, y, this);
			}
		}

	}

	private void paintBall(Graphics g) {
		for (Ball ball : balls) {
			int x = MARGIN_LEFT + ball.getX() * Pixel.SIZE;
			int y = MARGIN_TOP + ball.getY() * Pixel.SIZE;
			g.drawImage(ball.getImage(), x, y, this);
		}
	}

	private void paintBar(Graphics g) {
		for (Pixel bp : bar.getBody()) {
			int x = MARGIN_LEFT + bp.getX() * Pixel.SIZE;
			int y = MARGIN_TOP + bp.getY() * Pixel.SIZE;
			g.drawImage(bp.getImage(), x, y, this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		checkNextLevel();
		moveBalls();
		checkLives();
		moveDroppingItems();
		moveBullets();
		repaint();
	}

	private void checkLives() {
		if (lives < 0) {
			timer.stop();
		}

	}

	public Board() {
		super();
		lives = 5;
		background = new Background();
		this.bar = new Bar();
		this.balls = new ArrayList<Ball>();
		this.balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
		this.specialAttrs = new ArrayList<DroppingItem>();
		setUpBlocks();
		setBackground(Color.BLACK);
		timer = new Timer(DELAY, this);
		timer.start();
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		setFocusable(true);
		BufferedImage cursorImg = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");
		setCursor(blankCursor);
		addMouseMotionListener(new MouseArkanoidListener());
		addMouseListener(new MouseArkanoidListe());
		addKeyListener(new KeyPressListener(this));
	}

	private void checkNextLevel() {
		if (blocks.isEmpty()) {
			this.setUpBlocks();
			balls = new ArrayList<Ball>();
			balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
			bar = new Bar();
		}
	}

	private void moveBullets() {
		if (bullet != null) {
			if (bullet.move(blocks, specialAttrs)) {
				bullet = null;
			}
		}

		if (bullet2 != null) {
			if (bullet2.move(blocks, specialAttrs)) {
				bullet2 = null;
			}
		}
	}

	private void moveDroppingItems() {
		List<DroppingItem> dpToRemove = new ArrayList<DroppingItem>();
		for (DroppingItem dp : specialAttrs) {
			dp.move();
			if (dp.crash(bar) || dp.outOfScreen()) {
				dpToRemove.add(dp);
			}
		}
		for (DroppingItem dp : dpToRemove)
			specialAttrs.remove(dp);

	}

	private void moveBalls() {
		List<Ball> diedBalls = new ArrayList<Ball>();
		bar.move(balls);
		for (Ball ball : balls) {
			ball.move(crash(ball, bar), blocks, specialAttrs);
			if (ball.died()) {
				diedBalls.add(ball);
			}
		}
		for (Ball ball : diedBalls) {
			balls.remove(ball);
			if (balls.isEmpty()) {
				lives--;
				balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
			}
		}
	}

	private Integer crash(Ball ball, Bar bar) {
		if (bar.crash(ball)) {
			return bar.getVelocity();
		}
		return null;
	}

	private void setUpBlocks() {
		this.blocks = new ArrayList<Block>();
		for (int i = 5; i < 20; i++) {
			for (int j = 0; j <= M_WIDTH - 3; j += 4) {
				if (Math.random() < 0.5) {
					blocks.add(new Block(j, i));
				}
			}
		}

	}

	private class KeyPressListener extends KeyAdapter {

		private Board board;

		public KeyPressListener(Board board) {
			this.board = board;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			super.keyReleased(e);
			board.getBar().setDirection(0);
		}

		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				board.getBar().setDirection(Bar.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				board.getBar().setDirection(Bar.RIGHT);
				break;
			case KeyEvent.VK_SPACE:
				for (Ball ball : board.getBalls()) {
					if (ball.standStill()) {
						ball.activate();
						break;
					}
				}
				if (board.getBar().isWeapon()) {
					board.shoot();
				}
			}
		}

	}

	private class MouseArkanoidListe implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Board board = Board.getInstance();
			for (Ball ball : board.getBalls()) {
				if (ball.standStill()) {
					ball.activate();
					break;
				}
			}
			if (board.getBar().isWeapon()) {
				board.shoot();
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

	}

	private class MouseArkanoidListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent arg0) {

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			Board board = Board.getInstance();
			Bar bar = board.getBar();
			List<Ball> balls = board.getBalls();
			boolean left = bar.move(arg0.getX());
			List<Ball> diedBalls = new ArrayList<Ball>();
			for (Ball ball : balls) {
				if (bar.crash(ball)) {
					if (left) {
						ball.move(-1, board.getBlocks(),
								board.getSpecialAttrs());
					} else {
						ball.move(1, board.getBlocks(), board.getSpecialAttrs());
					}

				}
				if (ball.died()) {
					diedBalls.add(ball);
				}
			}
			for (Ball ball : diedBalls) {
				balls.remove(ball);
				if (balls.isEmpty()) {
					lives--;
					balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
					
				}
			}
		}

	}

	public Bar getBar() {
		return bar;
	}

	public void shoot() {
		if (bullet == null && bullet2 == null) {
			this.bullet = new BulletPixel();
			this.bullet2 = new BulletPixel();
			bullet.setX(bar.getBody().get(0).getX());
			bullet.setY(bar.getBody().get(0).getY());
			bullet2.setX(bar.getBody().get(bar.getBody().size() - 1).getX());
			bullet2.setY(bar.getBody().get(bar.getBody().size() - 1).getY());
		}

	}

	public void setBar(Bar bar) {
		this.bar = bar;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public static void applyMultiBall() {
		Board board = getInstance();
		Ball firstBall = null;
		for (Ball ball : board.getBalls()) {
			if (ball != null) {
				firstBall = ball;
			}
		}
		getInstance().getBalls().add(
				new Ball(firstBall.getX(), firstBall.getY(), 1, firstBall
						.isFireBall()));
		getInstance().getBalls().add(
				new Ball(firstBall.getX(), firstBall.getY(), -1, firstBall
						.isFireBall()));
		getInstance().getBalls().add(
				new Ball(firstBall.getX(), firstBall.getY(), 0, firstBall
						.isFireBall()));

	}

	public static Board getInstance() {
		if (instance == null) {
			instance = new Board();
		}
		return instance;
	}

	public static void applyFireball() {
		Board board = Board.getInstance();
		for (Ball ball : board.getBalls()) {
			if (ball != null) {
				ball.setFireBall(true);
				ball.setImage(null);
			}
		}
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<DroppingItem> getSpecialAttrs() {
		return specialAttrs;
	}

	public void setSpecialAttrs(List<DroppingItem> specialAttrs) {
		this.specialAttrs = specialAttrs;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	

}
