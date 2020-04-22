package src.csv_reader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MyCSVWrite {
    static void writeCats(Cat cat){
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("cats_output.csv"),StandardOpenOption.APPEND)) {
            bw.write(cat.getId()+","+cat.getType()+","+cat.getName()+","+cat.getAge()+","+cat.getColor()+"\n");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
