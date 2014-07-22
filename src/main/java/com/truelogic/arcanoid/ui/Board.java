package com.truelogic.arcanoid.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.truelogic.arcanoid.Ball;
import com.truelogic.arcanoid.Bar;
import com.truelogic.arcanoid.Block;

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

	private List<DroppingPixel> specialAttrs;
	
	private BulletPixel bullet;
	
	private BulletPixel bullet2;

	private Background background;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBackground(g);
		paintBar(g);
		paintBlocks(g);
		paintBall(g);
		paintSpecialAttrs(g);
		paintBullets(g);
		
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
		for (DroppingPixel sa : specialAttrs) {
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
		if (blocks.isEmpty()) {
			this.setUpBlocks();
			balls = new ArrayList<Ball>();
			balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
			bar = new Bar();
		}
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
			if (balls.isEmpty())
				balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
		}
		List<DroppingPixel> dpToRemove = new ArrayList<DroppingPixel>();
		for (DroppingPixel dp : specialAttrs) {
			dp.move();
			if (dp.crash(bar) || dp.outOfScreen()) {
				dpToRemove.add(dp);
			}
		}
		for (DroppingPixel dp : dpToRemove)
			specialAttrs.remove(dp);
		
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
		repaint();
	}

	private Integer crash(Ball ball, Bar bar) {
		if (bar.crash(ball)) {
			return bar.getVelocity();
		}
		return null;
	}

	public Board() {
		super();
		background = new Background();
		this.bar = new Bar();
		this.balls = new ArrayList<Ball>();
		this.balls.add(new Ball(M_WIDTH / 2, M_HEIGHT - 4));
		this.specialAttrs = new ArrayList<DroppingPixel>();
		setUpBlocks();
		setBackground(Color.BLACK);
		timer = new Timer(DELAY, this);
		timer.start();
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		setFocusable(true);
		addMouseMotionListener(new MouseArkanoidListener());
		addMouseListener(new MouseArkanoidListe());
		addKeyListener(new KeyPressListener(this));
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
	
	private class MouseArkanoidListener implements MouseMotionListener {

		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			Board.getInstance().getBar().move(arg0.getX());
			
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
		getInstance().getBalls().add(new Ball(firstBall.getX() , firstBall.getY(), 1, firstBall.isFireBall()));
		getInstance().getBalls().add(new Ball(firstBall.getX() , firstBall.getY(), -1, firstBall.isFireBall()));
		getInstance().getBalls().add(new Ball(firstBall.getX() , firstBall.getY(), 0, firstBall.isFireBall()));
		
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

}
