package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileName2 = "data.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
        json = String.valueOf(readToString(fileName2));
        jsonToList(json);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        CsvToBean<Employee> csv = null;
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assert csv != null;
        return staff;
    }

    private static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(list);
//        или так
//        Type listType = new TypeToken<List<Employee>>(){}.getType();
//        String json = gson.toJson(list, listType);
        System.out.println(json);
        return json;
    }

    private static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static JSONArray readToString(String fileName2) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        JSONArray array = null;
        try {
            array = (JSONArray) parser.parse(new FileReader(fileName2));
            for (Object item : array) {
                jsonObject = (JSONObject) item;
                System.out.println(jsonObject);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return array;
    }

    private static void jsonToList(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        List<Employee> list = gson.fromJson(json, listType);
        list.forEach(System.out::println);
    }
}