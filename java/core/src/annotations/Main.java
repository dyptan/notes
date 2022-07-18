package src.annotations;


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        User user1 = new User("Ivan");
        User user2 = new User("Vova");


        List prm1 = new ArrayList<PermissionAction>();
                prm1.add(PermissionAction.USER_CHANGE);
                prm1.add(PermissionAction.USER_READ);

        List prm2 = new ArrayList<PermissionAction>();
                prm2.add(PermissionAction.USER_READ);
                prm2.add(PermissionAction.USER_CHANGE);

        user1.setPermissions(prm1);
        user2.setPermissions(prm2);

        new Action().writeToFile(user2);

        new Action().readFromFile(user1);

    }
}
