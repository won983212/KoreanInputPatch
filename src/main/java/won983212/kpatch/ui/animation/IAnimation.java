package won983212.kpatch.ui.animation;

public interface IAnimation<T> {
	public void setReverse(boolean reverse);
	
	public void play();
	
	public boolean isRunning();
	
	public T update();
}
