package idm.idm.model;

/**
 * Created by Lily
 * Used to store user data from JWT.
 */

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer isAdmin;
    private Integer isLocked;

    public User(String firstName, String lastName, String username, String email, Integer isAdmin, Integer isLocked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.isLocked = isLocked;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Integer getIsAdmin()  { return isAdmin; }

    public void setIsAdmin(Integer isAdmin) { this.isAdmin = isAdmin; }

    public Integer getIsLocked()  { return isLocked; }

    public void setIsLocked(Integer isLocked) { this.isLocked = isLocked; }



}

