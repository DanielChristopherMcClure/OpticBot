package script;

import interfaces.Paintable;
import methods.Methods;

public abstract class OpticScript extends TronicAPI implements Runnable,
		Paintable {

	public OpticScript(Methods methods) {
		super(methods);
		// TODO Auto-generated constructor stub
	}

	public abstract String getDescription();

	public abstract String getName();

	public abstract void onStart();

	public abstract void onEnd();

}
