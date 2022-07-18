package src.csv_reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFileReader {

    List<List<String>> readCats() {

        List<List<String>> catsList = new ArrayList<>();
        String line = null;

        try (BufferedReader br = Files.newBufferedReader(Paths.get("cats.csv"))) {

            br.readLine();
            while ((line = br.readLine()) != null) {
                catsList.add(Arrays.asList(line.split("\n")));
            }

            System.out.println("Success");
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        return catsList;
    }
}
