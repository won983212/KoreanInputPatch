package won983212.simpleui.components;

public class UISettingList extends UIPanel {
	public UISettingList(Property[] properties) {
		// TODO Settinglist implement
	}
	
	public static class Property {
		public static final int BOOLEAN = 0;
		public static final int SELECT = 1;
		public static final int KEY = 2;
		
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
