import java.util.List;
import java.util.logging.Logger;

import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclient.model.population.statistic.Population;

public class TestPopulationStatisticsClient {

	private static final Logger LOGGER = Logger.getLogger(TestPopulationStatisticsClient.class.getName());

	public static void main(String[] args) {
		SCBClient c = new SCBClient();
		// String json = c.getRawData("BE/BE0101/BE0101A/BefolkningNy");
		//
		// System.out.println("JSON");
		// System.out.println(json);
		//
		// JsonNode converted = JsonUtility.toConventionalJson(json);
		// System.out.println("CONVERTED");
		// System.out.println(converted);

		List<Population> populations = c.population().statistics().getPopulation();
		LOGGER.info(populations.toString());
	}

}
