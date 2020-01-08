package server.model;


public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isAdmin;
    private int x;
    private int y;
    private String password;
    private String biometricData;

    public User(String firstName, String lastName, String email, boolean isAdmin, int x, int y, String password, String biometricData) {
        this(0, firstName, lastName, email, isAdmin, x, y, password, biometricData);
    }

    public User(int id, String firstName, String lastName, String email, boolean isAdmin, int x, int y, String password, String biometricData) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.x = x;
        this.y = y;
        this.password = password;
        this.biometricData = biometricData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiometricData() {
        return biometricData;
    }

    public void setBiometricData(String biometricData) {
        this.biometricData = biometricData;
    }
}
