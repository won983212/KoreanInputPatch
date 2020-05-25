package won983212.simpleui.events;

import won983212.simpleui.components.UIComponent;

public interface IStateChangedEventListener extends IEventListener {
	public void onChanged(UIComponent comp, Object newState);
}
