import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyConverter {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Welcome to the Currency Converter!");
            
            // Prompt user to choose base and target currencies
            System.out.print("Enter the base currency (e.g., USD, EUR): ");
            String baseCurrency = reader.readLine().toUpperCase();
            System.out.print("Enter the target currency (e.g., USD, EUR): ");
            String targetCurrency = reader.readLine().toUpperCase();
            
            // Fetch exchange rates from a reliable API
            double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
            
            if (exchangeRate > 0) {
                System.out.print("Enter the amount to convert: ");
                double amountToConvert = Double.parseDouble(reader.readLine());
                
                // Perform currency conversion
                double convertedAmount = amountToConvert * exchangeRate;
                
                // Display the result
                System.out.println("Converted amount: " + convertedAmount + " " + targetCurrency);
            } else {
                System.out.println("Invalid currency pair or unable to fetch exchange rates.");
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to fetch exchange rates from ExchangeRatesAPI
    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL("https://api.exchangeratesapi.io/latest?base=" + baseCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("rates").getDouble(targetCurrency);
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Return a negative value to indicate failure
        }
    }
}

