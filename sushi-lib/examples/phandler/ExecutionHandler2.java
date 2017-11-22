package phandler;

import java.util.HashSet;
import java.util.Set;
import common.LinkedList;

public class ExecutionHandler2 {
	public static final  int NUM_PHYSICAL_UNITS = 8;
	public static final  int MAX_POSTPONE = 2;
	public Set<Process> executePool(LinkedList<Process> primaryP, LinkedList<Process> secondaryP) {
		for (Process p : primaryP) p.setPriority(Process.HIGH_PRIORITY);
		LinkedList<Process> toExec = new LinkedList<>(primaryP);
		if (primaryP.size() > NUM_PHYSICAL_UNITS) {
			switchToVirtualUnits();
			toExec.addAll(secondaryP);
		}
		Set<Process> postpone = new HashSet<>();
		for (Process p : toExec) {
			if (p.priority() >= Process.HIGH_PRIORITY) {
				exec(p);
			} else {
				int ttl = p.updTTL();
				if (postpone.size() >= MAX_POSTPONE && ttl <= 0) {
					logUndone(p);
				} else {
					postpone.add(p);
				}
			}
		}	
		if (postpone.size() > MAX_POSTPONE) {
			handle_postponed(postpone);
		}
		return postpone;
	}
	private void switchToVirtualUnits() { /* ... */ }
	private void exec(Process p) { /* ... */ }
	private void logUndone(Process p) { /* ... */ }
	private void handle_postponed(Set<Process> postponed) { /* ... */ }
}
