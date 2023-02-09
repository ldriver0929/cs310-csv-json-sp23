package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    
 /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
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
                        if (j == 2 || j == 3) {
                            dataRow.add(Integer.parseInt(row[j].trim()));
                        } else {
                            dataRow.add(row[j].trim());
                        }
                    }
                    data.add(dataRow);
                    productNumbers.add(row[0]);
                }
                i++;
            }
            jsonObject.put("ColHeadings", colHeadings);
            jsonObject.put("Data", data);
            jsonObject.put("ProdNums", productNumbers);
            result = Jsoner.serialize(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        String result = "";
        try {
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");

            JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray colHeadings = (JsonArray) jsonObject.get("ColHeadings");
            JsonArray data = (JsonArray) jsonObject.get("Data");
            JsonArray productNumbers = (JsonArray) jsonObject.get("ProdNums");

            String[] header = new String[colHeadings.size()];
            for (int i = 0; i < header.length; i++) {
                header[i] = (String) colHeadings.get(i);
            }

            csvWriter.writeNext(header);

            for (int i = 0; i < data.size(); i++) {
                JsonArray row = (JsonArray) data.get(i);
                String[] rowData = new String[row.size() + 1];
                if (i == 79) {
                    System.out.println(data.get(i));
                }
                rowData[0] = productNumbers.get(i).toString();

                for (int j = 0; j < row.size(); j++) {

                    rowData[j + 1] = row.get(j).toString();
                    if (j == 2) {

                        String paddedzeros = String.format("%02d", Integer.parseInt(row.get(j).toString()));
                        rowData[j + 1] = paddedzeros;

                    }

                }

                csvWriter.writeNext(rowData);
            }

            csvWriter.close();
            result = writer.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
