package data;

public class Hook {

	private String name;
	private String clazz;
	private String field;

	public Hook(String name, String clazz, String field) {
		this.name = name;
		this.clazz = clazz;
		this.field = field;
	}

	public String getName() {
		return name;
	}

	public String getClazz() {
		return clazz;
	}

	public String getField() {
		return field;
	}

}
