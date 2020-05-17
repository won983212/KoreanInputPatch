package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JFrame;

import net.minecraft.client.gui.FontRenderer;

/**
 * Test Platform
 */
public class Main extends JFrame implements KeyListener {
	private TextInterface input = new TextInterface();
	private FontRendererInterface fr = new FontRendererInterface();
	private Graphics2D gCtx;

	public Main() {
		setTitle("KpDevEngine");
		setPreferredSize(new Dimension(600, 265));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setVisible(true);
		pack();
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		gCtx = (Graphics2D) g;
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
		FontMetrics met = gCtx.getFontMetrics(f);
		fr.setContext(met);
		gCtx.setFont(f);

		int y = 0;
		for (String str : fr.listFormattedStringToWidth(input.getText(), 550)) {
			gCtx.drawString(str, 10, 50 + y);
			y += fr.FONT_HEIGHT;
		}

		gCtx.setXORMode(Color.white);
		drawSelectionBox(fr, 10, 50 - met.getAscent(), 550);
	}

	public void drawSelectionBox(int startX, int startY, int endX, int endY, int color) {
		gCtx.setColor(new Color(color & 0xffffff));
		gCtx.fillRect(startX, startY, endX - startX, endY - startY);
	}

	public int getStartCursor() {
		return Math.min(input.getAnchorCursor(), input.getMovingCursor());
	}

	public int getEndCursor() {
		return Math.max(input.getAnchorCursor(), input.getMovingCursor());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		input.keyTyped(e);
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

	// TODO -------------- Implementation ---------------------
	public void drawSelectionBox(FontRendererInterface fontRenderer, int x, int y, int maxWidth) {
		int minX = 0, maxX = 0; // actual X position
		int minY = 0, maxY = 0; // line Y position

		String text = input.getText();
		List<String> wrapped = fr.listFormattedStringToWidth(input.getText(), maxWidth);
		int start = getStartCursor();
		int end = getEndCursor();

		minX = x + fontRenderer.getStringWidth(text.substring(0, start));
		maxX = x + fontRenderer.getStringWidth(text.substring(0, end));

		int len = 0;
		for(String s : wrapped) {
			if(len + s.length() > start) {
				break;
			}
			len += s.length();
			minY++;
		}

		if (minY == maxY) { // single line
			drawSelectionBox(minX, y, maxX, y + fontRenderer.FONT_HEIGHT);
		} else { // multiple lines
			drawSelectionBox(minX, y, x + fr.getStringWidth(wrapped.get(minY)), y + fr.FONT_HEIGHT);
			for (int i = minY; i < maxY - 2; i++) {
				drawSelectionBox(x, y, x + fr.getStringWidth(wrapped.get(i)), y + fr.FONT_HEIGHT);
			}
			drawSelectionBox(x, y, minX, y + fr.FONT_HEIGHT);
		}
	}

	// draw caret. color will be selected automatically.
	public void drawSelectionBox(int startX, int startY, int endX, int endY) {
		int color = 0xff0000ff;
		if (startX == endX) {
			endX += 2;
			color = 0xff000000;
		}
		drawSelectionBox(startX, startY, endX, endY, color);
	}
}