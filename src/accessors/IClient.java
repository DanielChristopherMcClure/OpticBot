package accessors;

public interface IClient {

	public String[] getMenuActions();

	public String[] getMenuOptions();

	public int getMenuOptionsCount();

	public int getMenuX();

	public int getMenuY();

	public int getMenuHeight();

	public int getMenuWidth();

	public boolean isMenuUp();
	
	public int getLoginState();

}
