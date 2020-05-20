package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.swing.JFrame;

import net.minecraft.client.gui.FontRenderer;
import scala.actors.threadpool.Arrays;

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

	/*private static class Hanja {
		public char hanja;
		public String meaning;
		
		public Hanja(char hanja, String meaning) {
			this.hanja = hanja;
			this.meaning = meaning;
		}
		
		@Override
		public String toString() {
			return isSpecial() ? String.valueOf(hanja) : (hanja + " " + meaning);
		}
		
		public boolean isSpecial() {
			return meaning.length() == 0;
		}
	}
	
	private static HashMap<Character, Hanja[]> load1() {
		HashMap<Character, Hanja[]> data = new HashMap<>();
		InputStream is = Main.class.getResourceAsStream("/assets/openkoreanime/hanja");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		char snd = 0;
		ArrayList<Hanja> hanjaBuffer = new ArrayList<>();
		
		try {
			String buf = null;
			while((buf = br.readLine()) != null) {
				if(buf.startsWith("#")) {
					if(!hanjaBuffer.isEmpty()) {
						data.put(snd, hanjaBuffer.toArray(new Hanja[hanjaBuffer.size()]));
						hanjaBuffer.clear();
					}
					snd = buf.charAt(1);
				} else {
					hanjaBuffer.add(new Hanja(buf.charAt(0), buf.substring(1)));
				}
			}
			
			if(!hanjaBuffer.isEmpty()) {
				data.put(snd, hanjaBuffer.toArray(new Hanja[hanjaBuffer.size()]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static void convert() {
		try {
			HashMap<Character, Hanja[]> data = load1();
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File("C:\\Users\\Cyber\\Desktop\\hanja2")));
			dos.writeShort(data.size());
			for (Entry<Character, Hanja[]> ent : data.entrySet()) {
				Hanja[] values = ent.getValue();
				dos.writeChar(ent.getKey());
				dos.writeShort(values.length);
				for (Hanja val : values) {
					dos.writeChar(val.hanja);
					dos.writeUTF(val.meaning);
				}
			}
			dos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static HashMap<Character, Hanja[]> load2() {
		try {
			HashMap<Character, Hanja[]> data = new HashMap<>();
			DataInputStream dis = new DataInputStream(Main.class.getResourceAsStream("/assets/openkoreanime/hanja2"));
			int len = dis.readShort();
			for (int i = 0; i < len; i++) {
				char key = dis.readChar();
				Hanja[] value = new Hanja[dis.readShort()];
				for (int j = 0; j < value.length; j++) {
					value[j] = new Hanja(dis.readChar(), dis.readUTF());
				}
				data.put(key, value);
			}
			dis.close();
			return data;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public static void main(String[] args) {
		//convert();
		/*double time = 0;
		for(int i=0;i<300;i++) {
			long s = System.nanoTime();
			load2();
			long e = System.nanoTime();
			time += (e - s) / 1000000.0;
		}
		System.out.println(time / 300 + "ms");*/
		
		/*s = System.nanoTime();
		load1();
		e = System.nanoTime();
		System.out.println((e - s) / 1000000.0 + "ms");*/
		new Main();
	}

	// -------------- Implementation ---------------------
	private String prevText = null;
	private CaretPosition min = new CaretPosition();
	private CaretPosition max = new CaretPosition();
	private int[] cachedCaretOffset = new int[0];

	public void drawSelectionBox(FontRendererInterface fontRenderer, int x, int y, int maxWidth) {
		String text = input.getText();
		int start = getStartCursor();
		int end = getEndCursor();

		String inText = text.substring(0, start) + '\u200B' + text.substring(start, end) + '\u200B' + text.substring(end);
		if (inText != prevText) {
			List<String> cachedWrappingText = fr.listFormattedStringToWidth(inText, maxWidth);
			
			// get full wrapped text (it contains \n)
			StringBuilder sb = new StringBuilder();
			for(String str : cachedWrappingText) {
				sb.append(str);
				sb.append('\n');
			}
			
			// find caret indicator character
			int idx1 = -1, idx2 = -1;
			String wrappedFullText = sb.toString().substring(0, sb.length() - 1);
			idx1 = wrappedFullText.indexOf('\u200B');
			idx2 = wrappedFullText.indexOf('\u200B', idx1 + 1);
			
			// calculate caret X,Y location in textarea(book, etc...)
			calculateCaretPosition(min, wrappedFullText, x, idx1);
			calculateCaretPosition(max, wrappedFullText, x, idx2);
			
			// generate actual caret location cache
			int len = max.y - min.y;
			cachedCaretOffset = new int[len];
			for (int i = 0; i < len; i++) {
				cachedCaretOffset[i] = fr.getStringWidth(cachedWrappingText.get(min.y + i));
			}
			prevText = inText;
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

	private void calculateCaretPosition(CaretPosition target, String wrappedFullText, int x, int idx) {
		target.x = target.y = 0;
		for (int i = 0; i < idx; i++) {
			char c = wrappedFullText.charAt(i);
			if(c == '\n') {
				target.x = i;
				target.y++;
			}
		}
		target.x = x + fr.getStringWidth(wrappedFullText.substring(target.x, idx));
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