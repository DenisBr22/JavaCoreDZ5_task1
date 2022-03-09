import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Задача: CSV - JSON парсер

public class Main {

    public static void main(String[] args) {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        String[] employee = "1,John,Smith,USA,25".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String[] employee2 = "2,Inav,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName, true))) {
            writer.writeNext(employee2);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json);
    }


    public static List<Employee> parseCSV(String[] arr, String filename) {
        List<Employee> data = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(arr);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            data = csv.parse();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() { }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String textJsont) {
        try (FileWriter file = new FileWriter("new data.jason")) {
            file.write(textJsont);
            file.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

