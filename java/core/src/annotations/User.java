package src.annotations;

import java.util.List;

public class User {
    private String name;
    protected List<PermissionAction> permissions;

    public User(String name) {
        this.name = name;
    }

    public List<PermissionAction> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionAction> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                '}';
    }
}
