package src.csv_reader;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MyFileReader fileReader = new MyFileReader();
        List<List<String>> cats = fileReader.readCats();
        List<Cat> catsList = new ArrayList<>();

        for (List<String> cat:cats
             ) {
            String[] aCat = null;
            aCat = cat.get(0).split(",");
            catsList.add(new Cat(Integer.parseInt(aCat[0]), aCat[1], aCat[2], Integer.parseInt(aCat[3]), aCat[4]));
        }

        catsList.stream()
                .sorted((x,y)->x.getAge()-y.getAge())
                .forEach(MyCSVWrite::writeCats);

    }
}