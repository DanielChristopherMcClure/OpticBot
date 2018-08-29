package extentions;

import java.io.IOException;

import utilities.Tools;

public class GetItemIds {

	public String url = "http://2007rshelp.com/items.php?id=";

	public GetItemIds() throws IOException {
		for (int i = 1; i < 4635; i++) {
			String source = Tools.getUrlSource(url + i);
			if (source != null) {
				String[] split = source.split("<u>");
				String name = "";
				String it = split[1];
				for (char c : it.toCharArray()) {
					if (c != '<') {
						name += c;						
					} else {
						break;
					}
				}
				Tools.log(name + "~" + i);
			}
		}
	}

	public static void main(String[] arg0) {
		try {
			new GetItemIds();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
