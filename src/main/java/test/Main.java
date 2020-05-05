package test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import won983212.kpatch.inputengine.KoreanInputEngine;

/**
 * Test Platform Environment
 */
public class Main extends JFrame implements KeyListener {
	private TextInterface textObj = new TextInterface();
	private KoreanInputEngine input = new KoreanInputEngine(textObj);

	public Main() {
		setTitle("KpDevEngine");
		setPreferredSize(new Dimension(600, 65));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setVisible(true);
		pack();
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
		Graphics2D g2 = (Graphics2D) g;
		FontMetrics met = g2.getFontMetrics(f);
		g2.setFont(f);
		g2.drawString(textObj.getText(), 35, 50);
		g2.setXORMode(Color.white);

		char[] chars = textObj.getText().toCharArray();
		int minX = met.charsWidth(chars, 0, Math.min(textObj.getAnchorCursor(), textObj.getMovingCursor()));
		int maxX = met.charsWidth(chars, 0, Math.max(textObj.getAnchorCursor(), textObj.getMovingCursor()));
		if (maxX == minX)
			maxX += 2;
		g2.fillRect(minX + 35, 50 - met.getAscent(), maxX - minX, met.getAscent() + met.getDescent());

		g2.setColor(Color.BLACK);
		g2.drawString(KoreanInputEngine.isKorMode() ? "한" : "영", 10, 50);
	}

	long sum = 0;
	int n = 0;
	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();
		if (KoreanInputEngine.isKorMode()) {
			if (i == KeyEvent.VK_LEFT || i == KeyEvent.VK_RIGHT || i == KeyEvent.VK_ENTER)
				input.cancelAssemble();
		}
		
		long start = System.nanoTime();
		if (!input.handleKeyTyped(e.getKeyChar(), i)) {
			textObj.keyTyped(e);
		}
		long end = System.nanoTime();
		sum += end - start;
		System.out.println((end - start) / 1000000.0 + "ms (AVG: " + (sum / 1000000.0 / n++) + "ms)");
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		new Main();
	}
}