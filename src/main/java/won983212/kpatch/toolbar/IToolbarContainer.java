package won983212.kpatch.toolbar;

/**
 * Input마다 지원하는 Toolbar가 있다. 
 * Toolbar를 그리려면, 부모 컨테이너 ui에 이 인터페이스를 상속하고 toolbar rendering을 request해야한다.
 */
public interface IToolbarContainer {
	public int getX();
	
	public int getY();
	
	public int getWidth();
	
	public int getHeight();
}
