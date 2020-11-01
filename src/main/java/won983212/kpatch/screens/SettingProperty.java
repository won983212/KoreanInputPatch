package won983212.kpatch.screens;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.component.UIComponent;
import won983212.simpleui.component.ui.UICombobox;
import won983212.simpleui.component.ui.UIKeyBox;
import won983212.simpleui.component.ui.UISwitch;
import won983212.simpleui.component.ui.UITextField;

public class SettingProperty {
	public static final int SELECT = 1;
	public static final int BOOLEAN = 2;
	public static final int KEY = 3;
	public static final int INPUT = 4;
	
	private String label;
	private String confId;
	private int type;
	private String[] selectLabels = null;
	private UIComponent component = null;
	
	public SettingProperty(String label, String configId, int type) {
		this(label, configId, type, null);
	}
	
	public SettingProperty(String label, String configId, int type, String[] selectLabels) {
		this.label = label;
		this.confId = configId;
		this.type = type;
		this.selectLabels = selectLabels;
		this.component = createComponent();
	}
	
	public String getLabel() {
		return label;
	}
	
	public void save() {
		Configs.set(confId, component.serializeData());
	}
	
	public UIComponent getComponent() {
		return component;
	}
	
	private UIComponent createComponent() {
		switch (type) {
		case BOOLEAN:
			return new UISwitch(Configs.getBoolean(confId));
		case INPUT:
			return new UITextField(Configs.get(confId)).setBorder(Theme.GRAY);
		case KEY:
			return new UIKeyBox(Configs.getInt(confId)).setMinimalSize(60, 16).setBorder(Theme.adjColor(Theme.PRIMARY, -20));
		case SELECT:
			return new UICombobox(Configs.getInt(confId), selectLabels).setMinimalSize(80, 14);
		}
		return null;
	}
}