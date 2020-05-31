package won983212.simpleui.components;

import java.util.HashMap;

import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

public class UISettingList extends UIPanel {
	private static final int ITEM_HEIGHT = 20;
	private static final int ITEM_GAP = 3;
	private static final int MARGIN = 6;
	private Property[] properties; // cached properties.

	public UISettingList(Property[] properties) {
		this.properties = properties;
	}
	
	@Override
	protected void onChangedBounds() { // bounds가 바뀔 때 마다 components를 전부 다시만든다
		super.onChangedBounds();
		clearComponents();
		createComponents();
	}
	
	private void createComponents() {
		for (int i = 0; i < properties.length; i++) {
			final int px = x + MARGIN;
			final int py = (int) (y + MARGIN + (ITEM_HEIGHT + ITEM_GAP) * (i + 0.5));
			/*add(new UIRectangle().setBounds(px, py, width - MARGIN * 2, ITEM_HEIGHT)
				.setBackgroundColor(Theme.BACKGROUND));*/
			add(new UILabel(properties[i].label).setBounds(px + 6, py, width - MARGIN * 2, ITEM_HEIGHT)
					.setTextCenter(false, true).setForegroundColor(Theme.BLACK).setShadow(Theme.LIGHT_GRAY));
			switch (properties[i].type) {
				case Property.BOOLEAN:
					add(new UISwitch().setLocation(px + width - UISwitch.DEFAULT_WIDTH - MARGIN * 3,
							py + (ITEM_HEIGHT - UISwitch.DEFAULT_HEIGHT) / 2));
					break;
				case Property.SELECT:
					add(new UISwitch().setLocation(px + width - UISwitch.DEFAULT_WIDTH - MARGIN * 3,
							py + (ITEM_HEIGHT - UISwitch.DEFAULT_HEIGHT) / 2));
					break;
			}
		}
	}

	public static class Property {
		public static final int BOOLEAN = 0;
		public static final int SELECT = 1;
		public static final int KEY = 2;
		public static final int INPUT = 3;

		public final String label;
		public final String configName;
		public final int type;
		public final String[] selectLabels;

		public Property(String label, String confName, int type) {
			this(label, confName, type, null);
		}

		public Property(String label, String confName, int type, String[] selectLabels) {
			this.label = label;
			this.configName = confName;
			this.type = type;
			this.selectLabels = selectLabels;
		}
	}
}
