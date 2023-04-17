package com.won983212.simpleui.events;

import com.won983212.simpleui.component.UIComponent;

public interface IStateChangedEventListener<T> extends IEventListener {
	T onChanged(UIComponent comp, T newState);
}
