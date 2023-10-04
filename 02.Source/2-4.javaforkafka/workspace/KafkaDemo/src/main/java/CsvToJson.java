import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvToJson {

    public static void main(String[] args) throws IOException {
        // Read the CSV file
        String csvFile = "/usr/local/workspace/creditcard.csv";
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));

        // Get the header of the CSV file
        String header = reader.readLine();
        String[] headerValues = header.split(",");

        // Create a JSON object from the header
        Map<String, String> jsonHeader = new HashMap<>();
        for (String headerValue : headerValues) {
            jsonHeader.put(headerValue, "");
        }

        // Create a JSON file writer
        FileWriter writer = new FileWriter("data.json");

        // Write the JSON header to the file
        //writer.write(jsonHeader.toString());

        // Read the remaining lines of the CSV file
        String line;
        List<Map<String, String>> jsonData = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            String[] lineValues = line.split(",");

            // Create a JSON object from the line
            Map<String, String> jsonObject = new HashMap<>();
            for (int i = 0; i < lineValues.length; i++) {
                //System.out.println(i);

                int check = 0;

                if (i == 30 && check == 0) {
                    jsonObject.put(headerValues[i], lineValues[i]);
                    //System.out.println(headerValues[i]);
                } else {
                    //jsonObject.put(headerValues[i], "\"" + lineValues[i] + "\"");
                    jsonObject.put(headerValues[i], lineValues[i]);
                }
            }

            // Add the JSON object to the list
            jsonData.add(jsonObject);
        }

        // Write the JSON data to the file
        writer.write(jsonData.toString().replaceAll("=", ":"));

        // Close the reader and writer
        reader.close();
        writer.close();

        // Print each line of the CSV file to Stdout
        // System.out.println(header);
        // while ((line = reader.readLine()) != null) {
        //    System.out.println(line);
        //}
    }
}
