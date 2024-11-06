package gov.healthit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MeaningfulUseStats {

	// URL of the API endpoint providing meaningful use data
    private static final String API_URL = "https://www.healthit.gov/data/open-api?source=Meaningful-Use-Acceleration-Scorecard.csv";

	// The year we are interested in: 2014
    private static final String TARGET_YEAR = "2014";

	// Fields from the API response that we will use
    private static final String FIELD_PERIOD = "period";
    private static final String FIELD_STATE = "region_code";
	private static final String FIELD_STATE_FULL_NAME = "region"; // Full state name from the API
    private static final String FIELD_PERCENTAGE = "pct_hospitals_mu";

	// Inner class to represent hospital data with state and the percentage of
	// hospitals demonstrating MU in 2014
    static class HospitalData {
		private final String stateCode;
		private final String fullStateName;
        private final double pctCriticalAccessHospitalsMU;

		// Constructor to initialize the hospital data
		public HospitalData(String stateCode, String fullStateName, double pctCriticalAccessHospitalsMU) {
			this.stateCode = stateCode;
			this.fullStateName = fullStateName;
            this.pctCriticalAccessHospitalsMU = pctCriticalAccessHospitalsMU;
        }

		// Override toString method for easy printing of the hospital data
        @Override
        public String toString() {
			return String.format("State: %s (%s), %% Critical Access Hospitals MU (%s): %.2f", fullStateName, stateCode,
					TARGET_YEAR, pctCriticalAccessHospitalsMU);
        }

		// Getter for the percentage of critical access hospitals demonstrating MU
        public double getPctCriticalAccessHospitalsMU() {
            return pctCriticalAccessHospitalsMU;
        }
    }

    public static void main(String[] args) {
		// Fetch data from the API and store it in a list
        List<HospitalData> hospitalDataList = fetchData(API_URL);

		// Sort the data by percentage of hospitals demonstrating MU in descending order
        hospitalDataList.sort(Comparator.comparingDouble(HospitalData::getPctCriticalAccessHospitalsMU).reversed());

		// Print the sorted data to the console
        hospitalDataList.forEach(System.out::println);
    }

	// Fetch data from the given API URL and parse the JSON response
    private static List<HospitalData> fetchData(String url) {
        List<HospitalData> hospitalDataList = new ArrayList<>();

		// Set up HTTP client to send the request to the API
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// Create an HTTP GET request for the given URL
            HttpGet request = new HttpGet(url);

			// Execute the request and get the response
            try (CloseableHttpResponse response = httpClient.execute(request)) {

				// Check if the response status is successful (HTTP 200)
                if (response.getStatusLine().getStatusCode() != 200) {
					// If the response is not successful, log an error (for debugging)
					System.err
							.println("Failed to fetch data. HTTP Status: " + response.getStatusLine().getStatusCode());
					return hospitalDataList; // Return an empty list if the request failed
                }

				// Convert the response body to a string
				String jsonResponse = EntityUtils.toString(response.getEntity());

				// Use Jackson to parse the JSON response
                ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(jsonResponse);

				// Iterate through the JSON data and process each item
                for (JsonNode node : rootNode) {
                    JsonNode periodNode = node.get(FIELD_PERIOD);
					JsonNode stateCodeNode = node.get(FIELD_STATE);
					JsonNode fullStateNameNode = node.get(FIELD_STATE_FULL_NAME); // Full state name directly from the
																					// API
                    JsonNode pctNode = node.get(FIELD_PERCENTAGE);

					// Only process data for the year 2014 and ensure necessary fields are present
					if (periodNode != null && TARGET_YEAR.equals(periodNode.asText()) && stateCodeNode != null
							&& fullStateNameNode != null && pctNode != null) {
						String stateCode = stateCodeNode.asText();
						String fullStateName = fullStateNameNode.asText(); // Directly get full state name from the API
                        double pctCriticalAccessHospitalsMU = pctNode.asDouble();

						// Add the parsed data to the list
						hospitalDataList.add(new HospitalData(stateCode, fullStateName, pctCriticalAccessHospitalsMU));
                    }
                }

            } catch (IOException e) {
				// Handle potential IO errors when processing the response
				System.err.println("Error processing data from the endpoint: " + e.getMessage());
            }
        } catch (IOException e) {
			// Handle errors when setting up the HTTP client
			System.err.println("Error closing HTTP client: " + e.getMessage());
        }

		// If no data was found, log a warning
        if (hospitalDataList.isEmpty()) {
			System.out.println("No data was found for the specified criteria.");
        }

		// Return the list of hospital data
        return hospitalDataList;
    }
}
