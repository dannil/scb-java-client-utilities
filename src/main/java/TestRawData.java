import com.github.dannil.scbjavaclient.client.SCBClient;

public class TestRawData {

	public static void main(String[] args) {
		SCBClient c = new SCBClient();

		// System.out.println(c.getRawData("MI/MI0811/LandarealSmaort"));
		System.out.println(c.getRawData("BE/BE0101/BE0101B/BefolkningMedelAlder"));
	}

}
