package won983212.kpatch.toolbar;

public class ToolbarRenderer {
	public static final int TOOLBAR_KOREAN_INPUT = 0;
	private final TopViewToolbar[] toolbars = new TopViewToolbar[1];
	
	public ToolbarRenderer() {
		toolbars[TOOLBAR_KOREAN_INPUT] = new KoreanInputToolbar();
	}
	
	public void render() {
		for(TopViewToolbar tb : toolbars) {
			tb.render();
		}
	}
	
	public void requestDrawToolbar(IToolbarContainer container, int toolbarId) {
		toolbars[toolbarId].setRenderNext(container);
	}
}
