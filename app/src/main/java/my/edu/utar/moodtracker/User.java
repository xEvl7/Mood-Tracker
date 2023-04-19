package my.edu.utar.moodtracker;

//Done by Khor Jia Jun 2101593
public class User {
    private String username,password,email, userId,pinLock;

    public User(){
    }

    public User(String username, String password, String email, String userId,String pinLock) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userId = userId;
        this.pinLock = pinLock;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPinLock() {
        return pinLock;
    }

    public void setPinLock(String pinLock) {
        this.pinLock = pinLock;
    }
}
