package wrappers;

import java.awt.Point;

public class RecordedAction {

	public String action = "null";
	public int sleep = 0;
	public Point loc = new Point(0, 0);

	public RecordedAction(String action, int sleep, Point loc) {
		this.action = action;
		this.sleep = sleep;
		this.loc = loc;
	}
}
