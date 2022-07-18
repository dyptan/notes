import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Random;

public class Main {
    public static boolean isFileExist(String pathToFile){
        return Files.exists(Paths.get(pathToFile));
    }

    public static void main(String[] args) {
        Path path = Paths.get(args[0]);

        if (!isFileExist(args[0])){
            System.out.println("No such file: "+args[0]);
            try {
                Path newFile = Files.createFile(path);
                int randomInt = new Random().nextInt(10);
                Files.writeString(newFile, String.valueOf(randomInt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String fileContent = Files.readString(path);
                System.out.println("Existing file contains: "+fileContent);
                int increamentedValue = 3+Integer.valueOf(fileContent);
                System.out.println("New value: "+increamentedValue);
                Files.writeString(path, String.valueOf(increamentedValue));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
