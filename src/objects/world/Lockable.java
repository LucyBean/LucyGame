package objects.world;

public interface Lockable {
	public abstract void lock();
	public abstract void unlock();
	public abstract int getLockID();
}
