package com.won983212.simpleui.events;

import com.won983212.simpleui.component.UIComponent;

public interface IClickEventListener<T> extends IEventListener {
	void onClick(UIComponent<T> comp, int mouseX, int mouseY, int mouseButton);
}
