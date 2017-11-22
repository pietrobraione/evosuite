package phandler;

public class Process {
	public static final int HIGH_PRIORITY = 10;
	private int priority = HIGH_PRIORITY;
	private int ttl = 1;
	public Process(int ttl) { if (ttl > 1) this.ttl = ttl; }
	public int priority() { return priority; }
	public void setPriority(int p) { priority = p; }		
	public int updTTL() { return --ttl; }
}
