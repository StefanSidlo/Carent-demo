package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Stefan Sidlovsky
 */
public class User {

    private String username;
    private String password;
    private String realName;
    private String email;
    private String phoneNumber;
    private final LocalDate dateOfBirth;
    private Integer balance;
    private LocalDate since;

    public User(String username, String password, String realName, String email, String phoneNumber, LocalDate dateOfBirth){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.balance = 0;
        this.since = LocalDate.now();
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String dateOfBirthToString(){
        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public LocalDate getSince() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public String sinceDateToString(){
        return since.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
