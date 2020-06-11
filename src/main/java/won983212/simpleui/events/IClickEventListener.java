package won983212.simpleui.events;

import won983212.simpleui.component.UIComponent;

public interface IClickEventListener extends IEventListener {
	public void onClick(UIComponent comp, int mouseX, int mouseY, int mouseButton);
}
