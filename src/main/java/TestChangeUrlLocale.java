import com.github.dannil.scbjavaclient.utility.URLUtility;

public class TestChangeUrlLocale {

	public static void main(String[] args) {
		String url = URLUtility
				.changeLanguageForUrl("http://api.scb.se/OV0104/v1/doris/en/ssd/BE/BE0101/BE0101A/BefolkningNy");

		String expected = "http://api.scb.se/OV0104/v1/doris/sv/ssd/BE/BE0101/BE0101A/BefolkningNy";

		System.out.println(url);
		System.out.println(expected);

		if (!url.equals(expected)) {
			throw new AssertionError(url + " != " + expected);
		}
	}
}
