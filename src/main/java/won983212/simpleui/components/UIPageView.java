package won983212.simpleui.components;

public class UIPageView extends UIComponent<UIPageView> {
	private int currentPage = 0;
	private UIComponent[] components = null;
	
	public UIPageView setPages(UIComponent[] components) {
		this.components = components;
		return this;
	}
	
	public UIPageView setPageIndex(int idx) {
		if(idx < 0) {
			idx = 0;
		}
		
		this.currentPage = idx;
		return this;
	}
	
	@Override
	protected void onRenderAreaUpdate() {
		if(components != null && currentPage < components.length) {
			components[currentPage].setBounds(x, y, width, height);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(components != null && currentPage < components.length) {
			components[currentPage].draw(mouseX, mouseY, partialTicks);
		}
	}
}
