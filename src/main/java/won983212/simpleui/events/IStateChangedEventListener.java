package won983212.simpleui.events;

import won983212.simpleui.components.panels.UIComponent;

public interface IStateChangedEventListener<T> extends IEventListener {
	public T onChanged(UIComponent comp, T newState);
}
