package com.won983212.simpleui.component;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.simpleui.Arranges;
import com.won983212.simpleui.DirWeights;
import com.won983212.simpleui.HorizontalArrange;
import com.won983212.simpleui.VerticalArrange;

/**
 * Component의 기초적인 기능을 담은 클래스 (위치, 크기, 이벤트)
 */
public abstract class UIComponent<T> {
    protected int id = 0;
    protected int x = 0;
    protected int y = 0;
    protected int width = 0;
    protected int height = 0;

    private HorizontalArrange hArrange = HorizontalArrange.STRETCH;
    private VerticalArrange vArrange = VerticalArrange.STRETCH;
    private Dimension minSize = new Dimension(10, 10);
    private DirWeights margin = new DirWeights();
    private DirWeights padding = new DirWeights();
    private boolean isEnabled = true;
    private boolean isVisible = true;

    public Object metadata = null;
    protected Object layoutData = null;
    protected UIComponent parent = null;
    private Dimension sizeCache = null;

    protected Point calculateActualLocation() {
        UIComponent obj = this;
        Point p = new Point(0, 0);
        while (obj != null) {
            p.x += obj.x;
            p.y += obj.y;
            obj = obj.parent;
        }
        return p;
    }

    /**
     * Focus를 재설정합니다. 재설정 대상 패널은 부모 컴포넌트로 한 단계씩 올라갔을 때 가장 먼저 만나는 패널입니다.
     */
    protected void setFocusdComponent(UIComponent comp) {
        UIComponent obj = this;
        while (obj != null) {
            if (obj instanceof UIPanel) {
                ((UIPanel) obj).setFocusdComponent(comp);
                break;
            }
            obj = obj.parent;
        }
    }

    /**
     * <code>(x, y)</code> 좌표가 컴포넌트 영역 내에 포함되는지 확인합니다. 좌표는 상대좌표를 사용합니다.
     * 상대좌표는 이 컴포넌트 영역의 가장 오른쪽 위가 (0, 0)인 좌표입니다.
     *
     * @param x 상대 x좌표
     * @param y 상대 y좌표
     * @return <code>(x, y)</code>가 이 컴포넌트 내에 포함되는지 확인
     */
    public boolean containsRelative(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * <code>(x, y)</code> 좌표가 컴포넌트 영역 내에 포함되는지 확인합니다. 좌표는 절대좌표를 사용합니다.
     * 절대 좌표는 실제 마우스가 클릭된 위치를 기준으로 정합니다. 마인크래프트 창의 왼쪽 위가 (0, 0)입니다.
     *
     * @param x 절대 x좌표
     * @param y 절대 y좌표
     * @return <code>(x, y)</code>가 이 컴포넌트 내에 포함되는지 확인
     */
    public boolean containsAbsolute(int x, int y) {
        Point p = calculateActualLocation();
        return x >= p.x && x < p.x + width && y >= p.y && y < p.y + height;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 이 Component가 상호작용이 가능한지 여부입니다. <code>true</code>일 경우 키 입력과 마우스 입력을 받을 수 있습니다.
     */
    public boolean isInteractive() {
        return isVisible && isEnabled;
    }

    public T setRelativeLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return (T) this;
    }

    public T setRelativeBounds(int x, int y, int width, int height) {
        setRelativeLocation(x, y);
        this.width = width;
        this.height = height;
        invalidateSize();
        return (T) this;
    }

    public T setVisible(boolean visible) {
        this.isVisible = visible;
        return (T) this;
    }

    public T setEnabled(boolean enable) {
        this.isEnabled = enable;
        return (T) this;
    }

    public T setId(int id) {
        this.id = id;
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

    public T setMargin(DirWeights margin) {
        this.margin = margin;
        return (T) this;
    }

    public T setPadding(DirWeights padding) {
        this.padding = padding;
        return (T) this;
    }

    public T setMinimalSize(int width, int height) {
        this.minSize = new Dimension(width, height);
        return (T) this;
    }

    public T setArrange(int arrange) {
        setHorizontalArrange(Arranges.getHorizontalArrangeByTemplate(arrange));
        setVerticalArrange(Arranges.getVerticalArrangeByTemplate(arrange));
        return (T) this;
    }

    public int getId() {
        return id;
    }

    /**
     * 이 컴포넌트가 취급하는 데이터(텍스트필드: 입력된 텍스트, 스위치: 스위치 on/off상태, 등..)를 String타입으로 직렬화합니다.
     */
    public String serializeData() {
        return "";
    }

    /**
     * 직렬화된 데이터로부터 이 컴포넌트를 재설정합니다.
     */
    public void deserializeData(String serialized) {
    }

    /**
     * <code>getMeasuredMinSize</code>에서 사용하는 캐시를 삭제합니다. 이 메서드를 호출하고 <code>getMeasuredMinSize</code>를 호출하면
     * 다시 계산해서 캐싱합니다.
     */
    public void invalidateSize() {
        sizeCache = null;
    }

    /**
     * 최상위 panel에 다시 layout을 요청합니다.
     */
    public void requestLayout() {
        if (parent != null)
            parent.requestLayout();
    }

    /**
     * 사용 가능한 영역(내부영역)을 반환합니다.
     */
    protected Rectangle getInnerBounds() {
        return new Rectangle(0, 0, width, height);
    }

    /**
     * 컴포넌트 총 영역에서 실제 컨텐츠가 그려질 영역을 반환합니다. 실제 컨텐츠가 그려질 영역은 컴포넌트 영역에서 padding을 뺀 영역입니다.
     */
    protected Rectangle getContentBounds() {
        return padding.getContentRect(getInnerBounds());
    }

    /**
     * 레이아웃시 사용할 최소 사이즈를 구합니다. <code>getMeasuredMinSize</code>는 컴포넌트의 최소 사이즈를 구하는 반면,
     * 이 메서드는 실제 layout할 때 어느정도의 영역을 차지하는지 구합니다. 일반적으로는 minSize에 padding과 margin을 더한 값을 사용합니다.
     */
    protected Dimension getLayoutMinSize() {
        return margin.getExpandedSize(getMeasuredMinSize());
    }

    /**
     * 컴포넌트의 최소 사이즈를 가져옵니다. 컴포넌트의 최소 사이즈를 얻을 때는 이 메서드를 사용하세요. <code>measureMinSize</code>는 처음부터 다시 계산하므로
     * 다소 느려질 수 있습니다. 이 메서드는 캐싱을 지원하므로 rendering할 때 유용합니다.
     */
    protected Dimension getMeasuredMinSize() {
        if (sizeCache == null)
            sizeCache = measureMinSize();
        return sizeCache;
    }

    /**
     * 실제 총 최소 사이즈를 다시 계산합니다.
     */
    protected Dimension measureMinSize() {
        return padding.getExpandedSize(minSize);
    }

    /**
     * 컴포넌트의 위치와 사이즈를 <code>available</code>(사용 가능한 영역)을 기준으로 설정합니다.
     */
    protected void arrange(Rectangle available) {
        setSizeByArrange(available);
        setPositionByArrange(available);
    }

    private void setPositionByArrange(Rectangle available) {
        Rectangle marginCalc = margin.getContentRect(available);
        this.x = hArrange.getHorizontalArrangedLocation(marginCalc, width);
        this.y = vArrange.getVerticalArrangedLocation(marginCalc, height);
    }

    private void setSizeByArrange(Rectangle available) {
        Dimension size = getMeasuredMinSize();
        Rectangle marginCalc = margin.getContentRect(available);
        this.width = Math.min(Math.max(minSize.width, hArrange.getWidthArranged(marginCalc, size.width)), marginCalc.width);
        this.height = Math.min(Math.max(minSize.height, vArrange.getHeightArranged(marginCalc, size.height)), marginCalc.height);
    }

    public void draw(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (isVisible) {
            renderComponent(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * 키 입력 이벤트입니다. 포커스된 컴포넌트만 받을 수 있습니다.
     */
    public void onKeyTyped(char typedChar, int keyCode) {
    }

    /**
     * 마우스 클릭 이벤트입니다. 가장 위에 있는(index가 가장 높은) 컴포넌트만 우선적으로 이벤트를 받습니다.
     * 이 이벤트를 받은 컴포넌트가 포커스 처리됩니다.
     */
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        return false;
    }

    /**
     * 전역 마우스 클릭 이벤트입니다. 컴포넌트 영역이나 focus여부에 상관없이 클릭시 무조건 호출됩니다.
     */
    public void onStaticMouseDown(int mouseX, int mouseY, int mouseButton) {
    }

    /**
     * 마우스 릴리즈 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
     */
    public void onMouseReleased(int mouseX, int mouseY, int state) {
    }

    /**
     * 포커스를 잃었을 때 호출됩니다.
     */
    public void onLostFocus() {
    }

    /**
     * 포커스를 얻었을 때 호출됩니다.
     */
    public void onGotFocus() {
    }

    /**
     * 마우스 드래그 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
     */
    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    protected abstract void renderComponent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);
}
