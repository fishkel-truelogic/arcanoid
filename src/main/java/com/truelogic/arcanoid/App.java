package com.truelogic.arcanoid;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.truelogic.arcanoid.ui.Board;

/**
 * @author lfishkel
 */
public class App extends JFrame { 

	private static final long serialVersionUID = 8850137530866277211L;

	public App() {
		add(Board.getInstance());
		setTitle("Arcanoid");
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new App();
				frame.setVisible(true);
			}

		});
	}

}
