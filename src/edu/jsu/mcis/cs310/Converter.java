package edu.jsu.mcis.cs310;
import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Converter {

 @SuppressWarnings("unchecked")
public static String csvToJson(String csvString) {
    String result = "{}";
    try { 
        List<String[]> csvRows = new ArrayList<>();
        CSVReader csvReader = new CSVReader(new StringReader(csvString));
        csvRows = csvReader.readAll();

        JsonObject jsonObject = new JsonObject();
        JsonArray colHeadings = new JsonArray();
        JsonArray data = new JsonArray();
        JsonArray productNumbers = new JsonArray();
        int i = 0;
        for (String[] row : csvRows) {
            if (i == 0) {
                for (String colHeading : row) {
                    colHeadings.add(colHeading);
                }
            } else {
                JsonArray dataRow = new JsonArray();
                for (int j = 1; j < row.length; j++) {
                    dataRow.add(row[j]);
                }
                data.add(dataRow);
                productNumbers.add(row[0]);
            }
            i++;
        }
        jsonObject.put("ColHeadings", colHeadings);
        jsonObject.put("Data", data);
        jsonObject.put("ProdNums", productNumbers);
        result = jsonObject.toString();
   } catch (Exception e) {
        e.printStackTrace();
    }
    return result.trim();
}


 @SuppressWarnings("unchecked")
public static String jsonToCsv(String jsonString) {
String result = ""; // default return value; replace later!
try {
JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);
JsonArray colHeadings = (JsonArray) jsonObject.get("ColHeadings");
JsonArray data = (JsonArray) jsonObject.get("Data");
JsonArray productNumbers = (JsonArray) jsonObject.get("ProdNums");
//System.out.println("productNumbers: " + productNumbers);  // Debug statement
StringBuilder csvString = new StringBuilder();
for (Object colHeading : colHeadings) {
csvString.append(colHeading).append(",");
}
csvString.setLength(csvString.length() - 1);
csvString.append("\n");
for (int i = 0; i < data.size(); i++) {
JsonArray dataRow = (JsonArray) data.get(i);
csvString.append(productNumbers.get(i)).append(",");
for (int j = 0; j < dataRow.size(); j++) {
csvString.append(dataRow.get(j)).append(",");
}
csvString.setLength(csvString.length() - 1);
csvString.append("\n");
}
result = csvString.toString();
} catch (Exception e) {
e.printStackTrace();
}
return result;
}
}



    