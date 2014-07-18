package com.truelogic.arcanoid.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

	public static final int M_HEIGHT = 60;

	private static final int B_WIDTH = M_WIDTH * Pixel.SIZE;

	private static final int B_HEIGHT = M_HEIGHT * Pixel.SIZE;

	private static final int MARGIN_LEFT = 0;

	private static final int MARGIN_TOP = 0;

	private Timer timer;
	
	private Bar bar;

	private Ball ball;

	private List<Block> blocks;

	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBar(g);
		paintBlocks(g);
		paintBall(g);
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
		int x = MARGIN_LEFT + ball.getX() * Pixel.SIZE;
		int y = MARGIN_TOP + ball.getY() * Pixel.SIZE;
		g.drawImage(ball.getImage(), x, y, this);
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
		bar.move();
		ball.move(crash(ball, bar), blocks);
		if (ball.died()) {
			this.ball = new Ball(M_WIDTH / 2, M_HEIGHT - 4);
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
		this.bar = new Bar();
		this.ball = new Ball(M_WIDTH / 2, M_HEIGHT - 4);
		setUpBlocks();
		setBackground(Color.BLACK);
		timer = new Timer(DELAY, this);
		timer.start();
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		setFocusable(true);
		addKeyListener(new KeyPressListener(this));
	}
	
	private void setUpBlocks() {
		this.blocks = new ArrayList<Block>();
		for (int i = 5; i < 20; i ++) {
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

			switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: board.getBar().setDirection(Bar.LEFT); break;
				case KeyEvent.VK_RIGHT: board.getBar().setDirection(Bar.RIGHT); break;
			}
		}

	}

	public Bar getBar() {
		return bar;
	}




	public void setBar(Bar bar) {
		this.bar = bar;
	}
	
	

	
	

}
