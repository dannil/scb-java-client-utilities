package com.github.dannil.scbjavaclientutil;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dannil.scbjavaclient.client.SCBClient;
import com.github.dannil.scbjavaclient.client.agriculture.livestock.AgricultureLivestockClient;
import com.github.dannil.scbjavaclient.client.population.statistics.PopulationStatisticsClient;
import com.github.dannil.scbjavaclient.client.publicfinances.governmentdebt.PublicFinancesGovernmentDebtClient;
import com.github.dannil.scbjavaclient.model.GenericModel;
import com.github.dannil.scbjavaclient.model.ResponseModel;

public class Test {

    public static void main(String[] args) {
        // Create the client
        SCBClient client = new SCBClient();

        // Retrieve some client(s) matching the table(s) you want to fetch information from
        AgricultureLivestockClient livestockClient = client.agriculture().livestock();
        PublicFinancesGovernmentDebtClient debtClient = client.publicFinances().governmentDebt();

        // Retrieve all livestock by county data
        List<ResponseModel> livestockData = livestockClient.getLivestockByCounty();

        // Retrieve all government debt data
        List<ResponseModel> debtData = debtClient.getGovernmentDebt();

        // You may also want to skip the explicit creation of the client and fetch data
        // directly from the method call.
        List<ResponseModel> densityData = client.population().statistics().density().getDensity();

        // Specify the criterion for the information we want to retrieve
        List<String> regions = Arrays.asList("00", "01", "0114");
        List<String> types = Arrays.asList("01", "02");

        // Defining an input as null means that we want to retrieve all information
        // for this input. 
        // 
        // Example: if the information from the API specifies that it has information 
        // for the years 2011 through 2015 (for a specific table), the inputs underneath 
        // this row would result in an equivalent response from the API.
        //
        // List<Integer> years = Arrays.asList(2011, 2012, 2013, 2014, 2015);
        // List<Integer> years = null;
        // 
        // This makes it easy to be selective when we have a case where we 
        // want to retrieve all information for one input and at the same time 
        // restrict the input for another (for example the years 2012 and 2014).
        List<Integer> years = null;

        // Retrieve all area statistics using the selected values
        List<ResponseModel> areas = client.environment().landAndWaterArea().getArea(regions, types, years);

        // Specify the table to retrieve from. The response will be a JSON string containing all the
        // available data from our specified table.
        String json = client.getRawData("BE/BE0101/BE0101A/BefolkningNy");

        // Fetch all available inputs for a table. Every key in the map corresponds to an available input
        // parameter and the values for a specific key are all the available values for a specific input.
        // The contents of availableInputs can then be analyzed and used for fetching more specific data.
        Map<String, Collection<String>> availableInputs = client.getInputs("BE/BE0101/BE0101A/BefolkningNy");

        // Specifies the criterion for the information we want to retrieve, in this case:
//         		The contents code BE0101N1 (the total population, so the API knows what information we want)
//        		The regions 00 (whole country), 01 (Stockholm County) and 0114 (Upplands Väsby)
//        		The relationship statuses OG (unmarried) and G (married)
//        		The ages 45 and 50
//        		The genders are null; we want to retrieve information for all genders
//        		The years 2011 and 2012
        Map<String, Collection<?>> inputs = new HashMap<>();
        inputs.put("ContentsCode", Arrays.asList("BE0101N1"));
        inputs.put("Region", Arrays.asList("00", "01", "0114"));
        inputs.put("Civilstand", Arrays.asList("OG", "G"));
        inputs.put("Alder", Arrays.asList(45, 50));
        inputs.put("Kon", null);
        inputs.put("Tid", Arrays.asList(2011, 2012));

        // Specify the table to retrieve from and our inputs to this table. The response will be a JSON
        // string containing the data that matched our criterion.
        json = client.getRawData("BE/BE0101/BE0101A/BefolkningNy", inputs);

        // Fetch all data from a table and construct a GenericModel using this data.
        json = client.getRawData("BE/BE0101/BE0101A/BefolkningNy");
        GenericModel m = new GenericModel(json);

        // Retrieve all the entries into a collection.
        Collection<Map<String, Object>> entries = m.getEntries();

        // Retrieve all the entries having the code "alder" with either value 45 or 50 and the code 
        // "region" with value 01.
        Map<String, Collection<String>> inputs2 = new HashMap<>();
        inputs.put("alder", Arrays.asList("45", "50"));
        inputs.put("region", Arrays.asList("01"));

        entries = m.getEntries(inputs2);

        // Retrieve all the entries having the code "alder" with value 45.
        entries = m.getEntries("alder", "45");

    }
}
