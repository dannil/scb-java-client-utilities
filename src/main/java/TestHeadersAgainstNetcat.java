import java.util.Locale;

import com.github.dannil.scbjavaclient.client.AbstractClient;

public class TestHeadersAgainstNetcat {

	private static class DummyClient extends AbstractClient {

		public DummyClient() {
			super();
		}

		public DummyClient(Locale locale) {
			super(locale);
		}

		public String get(String url) {
			return super.get(url);
		}

		public String post(String url, String query) {
			return super.post(url, query);
		}

	}

	public static void main(String[] args) {
		DummyClient c = new DummyClient();
		c.get("http://192.168.0.113:6543");
	}

}
