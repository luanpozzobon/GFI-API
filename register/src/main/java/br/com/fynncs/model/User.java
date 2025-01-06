package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;
import br.com.fynncs.core.annotation.RegisterAttributeModified;

import java.time.LocalDate;
import java.util.UUID;

public class User extends ModelState<User> {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private String profession;
    private String academicBackground;
    private String username;
    private String password;

    public UUID getId() {
        return id;
    }

    @RegisterAttributeModified("id")
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @RegisterAttributeModified("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    @RegisterAttributeModified("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    @RegisterAttributeModified("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @RegisterAttributeModified("birth_date")
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    @RegisterAttributeModified("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    @RegisterAttributeModified("nationality")
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    @RegisterAttributeModified("marital_status")
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProfession() {
        return profession;
    }

    @RegisterAttributeModified("profession")
    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAcademicBackground() {
        return academicBackground;
    }

    @RegisterAttributeModified("academic_background")
    public void setAcademicBackground(String academicBackground) {
        this.academicBackground = academicBackground;
    }

    public String getUsername() {
        return username;
    }

    @RegisterAttributeModified("username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @RegisterAttributeModified("password")
    public void setPassword(String password) {
        this.password = password;
    }
}