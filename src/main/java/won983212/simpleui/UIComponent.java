package won983212.simpleui;

import java.awt.Dimension;
import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

// TODO Combobox, Keybox, Textfield
/**
 * Component의 기초적인 기능을 담은 클래스 (위치, 크기, 이벤트)
 */
public abstract class UIComponent<T> {
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;

	private HorizontalArrange hArrange = HorizontalArrange.STRECTCH;
	private VerticalArrange vArrange = VerticalArrange.STRECTCH;
	private Dimension minSize = new Dimension(10, 10);
	private DirWeights margin = new DirWeights();
	private DirWeights padding = new DirWeights();
	private boolean isEnabled = true;
	private boolean isVisible = true;
	
	public Object metadata = null;
	private Dimension sizeCache = null;
	
	
	public boolean isIn(int px, int py) {
		return px >= x && px <= x + width && py >= y && py <= y + height;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public T setEnabled(boolean enable) {
		this.isEnabled = enable;
		return (T) this;
	}
	
	public T setMetadata(Object data) {
		this.metadata = data;
		return (T) this;
	}
	
	public T setHorizontalArrange(HorizontalArrange arr) {
		this.hArrange = arr;
		return (T) this;
	}

	public T setVerticalArrange(VerticalArrange arr) {
		this.vArrange = arr;
		return (T) this;
	}
	
	/**
	 * 레이아웃시 사용할 최종 최소 사이즈를 구합니다. <code>getMeasuredMinSize</code>는 컴포넌트의 최소 사이즈를 구하는 반면,
	 * 이 메서드는 실제 layout될 때 어느정도의 영역을 차지하는지 구합니다. 일반적으로는 minSize에 padding과 margin을 더한 값을 사용합니다.
	 */
	protected Dimension getLayoutMinSize() {
		return margin.getExpandedSize(getMeasuredMinSize());
	}
	
	/** 
	 * 컴포넌트의 최소 사이즈를 가져옵니다. 컴포넌트의 최소 사이즈를 얻을 때는 이 메서드를 사용하세요. <code>measureMinSize</code>는 처음부터 다시 계산하므로
	 * 다소 느려질 수 있습니다. 이 메서드는 캐싱을 지원하므로 rendering할 때 유용합니다.
	 */
	protected Dimension getMeasuredMinSize() {
		if(sizeCache == null)
			sizeCache = measureMinSize();
		return sizeCache;
	}
	
	/** 
	 * 실제 총 최소 사이즈를 다시 계산합니다.
	 */
	protected Dimension measureMinSize() {
		return padding.getExpandedSize(minSize);
	}
	
	private void setPositionByArrange(Rectangle available) {
		Rectangle marginCalc = margin.getContentRect(available);
		this.x = hArrange.getHorizontalArrangedLocation(marginCalc, width);
		this.y = vArrange.getVerticalArrangedLocation(marginCalc, height);
	}

	private void setSizeByArrange(Rectangle available) {
		Dimension size = getMeasuredMinSize();
		Rectangle marginCalc = margin.getContentRect(available);
		this.width = Math.max(minSize.width, hArrange.getWidthArranged(marginCalc, size.width));
		this.height = Math.max(minSize.height, vArrange.getHeightArranged(marginCalc, size.height));
	}

	public void draw(int mouseX, int mouseY, float partialTicks) {
		if (isVisible) {
			renderComponent(mouseX, mouseY, partialTicks);
		}
	}

	/**
	 * 키 입력 이벤트입니다. 활성화(Enabled)되어있는 모든 component에서 받을 수 있습니다.
	 */
	public void onKeyTyped(char typedChar, int keyCode) {
	}

	/**
	 * 마우스 클릭 이벤트입니다. 가장 위에 있는(index가 가장 높은) 컴포넌트만 우선적으로 이벤트를 받습니다.
	 */
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	/**
	 * 마우스 릴리즈 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
	 */
	public void onMouseReleased(int mouseX, int mouseY, int state) {
	}

	/**
	 * 마우스 드래그 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
	 */
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	}
	
	protected abstract void renderComponent(int mouseX, int mouseY, float partialTicks);
}
