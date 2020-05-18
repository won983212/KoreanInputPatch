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
import java.util.Timer;
import java.util.TimerTask;

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
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1000);
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
		
		long s = System.nanoTime();
		drawSelectionBox(fr, 10, 50 - met.getAscent(), 550);
		long e = System.nanoTime();
		System.out.println((e-s) / 1000.0 + "ms");
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
	private int prevStartCaret = -1;
	private int prevEndCaret = -1;
	private String prevText = null;

	private CaretPosition min = new CaretPosition();
	private CaretPosition max = new CaretPosition();
	private List<String> cachedWrappingText = null;

	private int[] cachedCaretOffset = new int[0];

	public void drawSelectionBox(FontRendererInterface fontRenderer, int x, int y, int maxWidth) {
		boolean update = false;

		String text = input.getText();
		if (text != prevText) {
			cachedWrappingText = fr.listFormattedStringToWidth(text + "_", maxWidth);
			if (!cachedWrappingText.isEmpty()) {
				int lastIdx = cachedWrappingText.size() - 1;
				String last = cachedWrappingText.get(lastIdx);
				cachedWrappingText.set(lastIdx, last.substring(0, last.length() - 1));
			}
			prevText = text;
			update = true;
		}

		int start = getStartCursor();
		if (start != prevStartCaret) {
			min = calculateCaretPosition(text, fontRenderer, x, start);
			prevStartCaret = start;
			update = true;
		}

		int end = getEndCursor();
		if (end != prevEndCaret) {
			max = calculateCaretPosition(text, fontRenderer, x, end);
			prevEndCaret = end;
			update = true;
		}

		if (update) {
			int len = max.y - min.y;
			cachedCaretOffset = new int[len];
			for (int i = 0; i < len; i++) {
				cachedCaretOffset[i] = fr.getStringWidth(cachedWrappingText.get(min.y + i));
			}
		}

		y += min.y * fr.FONT_HEIGHT;
		if (min.y == max.y) { // for single line
			drawSelectionBox(min.x, y, max.x, y + fontRenderer.FONT_HEIGHT);
		} else { // for multiple lines
			// first & center lines
			for (int i = 0; i < cachedCaretOffset.length; i++) {
				int endY = y + (i + 1) * fr.FONT_HEIGHT;
				drawSelectionBox(i == 0 ? min.x : x, y + i * fr.FONT_HEIGHT, x + cachedCaretOffset[i], endY);
			}
			// end line
			y += (max.y - min.y) * fr.FONT_HEIGHT;
			drawSelectionBox(x, y, max.x, y + fr.FONT_HEIGHT);
		}
	}

	private CaretPosition calculateCaretPosition(String text, FontRendererInterface fr, int offsetX, int caret) {
		CaretPosition pos = new CaretPosition();
		int len = 0, tempLen = 0;
		for (String s : cachedWrappingText) {
			tempLen = len + s.length();
			if (tempLen >= text.length() || text.charAt(tempLen) == '\n') {
				tempLen = s.length() + 1; // \n by user
			} else {
				tempLen = s.length(); // inserted \n by listFormattedStringToWidth
			}
			if (len + tempLen > caret) {
				break;
			}
			len += tempLen;
			pos.y++;
		}
		pos.x = offsetX + fr.getStringWidth(text.substring(len, caret));
		return pos;
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

	private class CaretPosition {
		public int x = 0; // actual x location
		public int y = 0; // y'th line location
	}
}