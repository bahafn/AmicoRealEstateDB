package com.github.lamico.entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.github.lamico.db.DBConnection;
import com.github.lamico.gui.utils.AlertUtil;

import javafx.scene.control.Alert.AlertType;

public class Person {
    private String ssn;
    private String pName;
    private String address;
    private Date dateOfBirth;
    private String bankInfo;

    private String phone;
    private String email;

    public Person(String ssn, String pName, String address, Date dateOfBirth, String bankInfo, String phone,
            String email) {
        this.ssn = ssn;
        this.pName = pName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.bankInfo = bankInfo;
        this.phone = phone;
        this.email = email;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /** @return Whether an Person has this ssn. */
    public static boolean searchPerson(String ssn) {
        String query = String.format("SELECT * from person WHERE ssn = %s", ssn);
        try (Connection connection = DBConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet queryResult = statement.executeQuery(query)) {
            return queryResult.next();
        } catch (SQLException sql_e) {
            AlertUtil.showAlert(AlertType.ERROR, "Error reading database", sql_e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "ssn=" + ssn +
                ", pName='" + pName + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bankInfo='" + bankInfo + '\'' +
                '}';
    }
}
