package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
//        writeString(json);
    }

    private static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String listToJson(List<Employee> list) {
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
                .create();
//        String json = gson.toJson(list);
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        String json = gson.toJson(list, listType);
        System.out.println(json);
        return json;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        CsvToBean<Employee> csv = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            csv.parse().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assert csv != null;
        return csv.parse();
    }
}