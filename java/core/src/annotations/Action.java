package src.annotations;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Action {

    @MyPermission(PermissionAction.USER_CHANGE)
    public void writeToFile(User in) {
        Method actionMethod = null;
        try {
            actionMethod = this.getClass().getMethod("writeToFile", User.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MyPermission userPermissions = actionMethod.getAnnotation(MyPermission.class);

        for (PermissionAction permission:in.getPermissions()){
                if (permission==userPermissions.value()){
                    try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("file.dat"))){
                        bw.write(in.toString());
                        System.out.println("Write Success");
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("write error");
                    }
                }
        }
    }

    @MyPermission(PermissionAction.USER_READ)
    public void readFromFile(User in) {

        Method actionMethod = null;
        try {
            actionMethod = this.getClass().getMethod("readFromFile", User.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MyPermission userPermissions = actionMethod.getAnnotation(MyPermission.class);

        for (PermissionAction permission:in.getPermissions()){
            if (permission==userPermissions.value()){
                try (BufferedReader br = Files.newBufferedReader(Paths.get("file.dat"))) {
                    String inValue;
                    while ((inValue = br.readLine())!=null){
                        System.out.println(inValue);
                    }
                    System.out.println("Read success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
