package swing;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class TronicList extends JList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultListModel<Object> model = new DefaultListModel<Object>();

	public TronicList() {

	}

	public TronicList(Object[] objects) {
		this.setModel(objects);
	}

	public void remove(Object obj) {
		for (int i = 0; i < model.size(); i++) {
			if (model.get(i).equals(obj)) {
				model.remove(i);
				break;
			}
		}
	}

	public void remove(int index) {
		model.remove(index);
		this.setModel(model);
	}

	public void addelement(Object obj) {
		model.addElement(obj);
		this.setModel(model);
	}

	public void setModel(Object[] objs) {
		model.clear();
		for (Object o : objs)
			model.addElement(o);
		this.setModel(model);
	}

	public void clearModel() {
		model.clear();
	}

}
