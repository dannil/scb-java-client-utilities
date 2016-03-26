/*
 * Copyright 2015 Daniel Nilsson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.dannil.scbjavaclient.client.SCBClient;

public class Test {

	private static Locale locale = new Locale("en");
	private static SCBClient client = new SCBClient(locale);

	public static void main(String[] args) {
		// PopulationClient populationClient = client.population();
		// PopulationDemographyClient populationDemographyClient = client.population().demography();
		// PopulationNameStatisticsClient populationNameStatisticsClient =
		// client.population().nameStatistics();
		// PopulationStatisticsClient populationStatisticsClient = client.population().statistics();
		//
		// List<String> regions = new ArrayList<String>();
		// // regions.add("00");
		// regions.add("1267");
		//
		// List<String> types = new ArrayList<String>();
		// types.add("01");
		//
		// List<Integer> years = new ArrayList<Integer>();
		// years.add(2012);
		// years.add(2013);
		//
		// List<String> ages = new ArrayList<String>();
		// ages.add("18");
		// ages.add("20");
		//
		// // Map<String, String> map = api.getRegionMappings();
		// // for (String key : map.keySet()) {
		// // System.out.println(key + " : " + map.get(key));
		// // }
		//
		// // System.out.println(ParseUtility.parseLong("221", null));
		//
		// List<LiveBirth> collection9 = populationStatisticsClient.getLiveBirths();
		// for (LiveBirth l : collection9) {
		// // System.out.println(l);
		// }
		//
		// // List<Integer> availableYears = statisticApi.getYears();
		// // for (Integer year : availableYears) {
		// // System.out.println(year);
		// // }
		// //
		// EnvironmentLandAndWaterAreaClient lawClient = client.environment().landAndWaterArea();
		//
		// List<Area> collection6 = lawClient.getArea(regions, types, years);
		// for (Area a : collection6) {
		// System.out.println(a);
		// }
		//
		// List<MeanAgeFirstChild> collection7 = client.population().demography()
		// .getMeanAgeFirstChild(regions, null, null);
		// for (MeanAgeFirstChild a : collection7) {
		// System.out.println(a);
		// }
		//
		// // api.population().demography().getFertilityRate();
		//
		// ResourceBundle bundle = ResourceBundle.getBundle("language", client.getLocale());
		// bundle.getString("hello");
		//
		// // System.out.println(client.getRegions("HE/HE0103/HE0103B/BefolkningAlder"));
		//
		// Map<String, Collection<?>> payload = new HashMap<String, Collection<?>>();
		// payload.put("ContentsCode", Arrays.asList("BE0101N1"));
		// payload.put("Region", Arrays.asList("00", "01", "0114"));
		// payload.put("Civilstand", Arrays.asList("OG", "G"));
		// payload.put("Alder", Arrays.asList(45, 50));
		// payload.put("Kon", null);
		// payload.put("Tid", Arrays.asList(2011, 2012));
		//
		// String response = client.getRawData("BE/BE0101/BE0101A/BefolkningNy", payload);
		// System.out.println(response);
		//
		// System.out.println(populationStatisticsClient.getAverageAge());
		//
		// System.out.println(populationNameStatisticsClient.getNumberOfChildrenBornWithFirstName());
		//
		// System.out.println(populationDemographyClient.getFertilityRate());

		SCBClient baseClient = new SCBClient();

		// Specifies the criterion for the information we want to retrieve, in this case:
		// The contents code (so the API know what information we want for the response)
		// The regions 00, 01 and 0114
		// The relationship statuses OG (unmarried) and G (married)
		// The ages 45 and 50
		// The genders are null; we want to retrieve information for all genders
		Map<String, Collection<?>> inputs = new HashMap<String, Collection<?>>();
		inputs.put("ContentsCode", Arrays.asList("BE0101N1"));
		inputs.put("Region", Arrays.asList("00", "01", "0114"));
		inputs.put("Civilstand", Arrays.asList("OG", "G"));
		inputs.put("Alder", Arrays.asList(45, 50));
		inputs.put("Kon", null);
		inputs.put("Tid", Arrays.asList(2011, 2012));

		// Specify the table to retrieve from and our inputs to this table. The response will be a
		// JSON
		// string containing information that matched our criterion.
		String json = baseClient.getRawData("BE/BE0101/BE0101A/BefolkningNy", inputs);
		System.out.println(json);
	}
}
