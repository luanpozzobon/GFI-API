package br.com.fynncs.model;

import br.com.fynncs.core.ModelState;
import br.com.fynncs.core.annotation.RegisterAttributeModified;

import java.time.LocalDate;
import java.util.UUID;

public class Person extends ModelState<Person> {
    private UUID userId;
    private String nickname;
    private String name;
    private LocalDate birthday;
    private String email;
    private String gender;
    private String nationality;
    private String maritalStatus;
    private String profession;
    private String education;

    public UUID getUserId() {
        return userId;
    }

    @RegisterAttributeModified("user_id")
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    @RegisterAttributeModified("nickname")
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    @RegisterAttributeModified("name")
    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    @RegisterAttributeModified("birthday")
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    @RegisterAttributeModified("email")
    public void setEmail(String email) {
        this.email = email;
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

    public String getEducation() {
        return education;
    }

    @RegisterAttributeModified("education")
    public void setEducation(String education) {
        this.education = education;
    }

    public br.com.fynncs.record.Person toRecord() {
        return new br.com.fynncs.record.Person(
                this.getUserId(),
                this.getNickname(),
                this.getName(),
                this.getBirthday(),
                this.getGender(),
                this.getNationality(),
                this.getMaritalStatus(),
                this.getProfession(),
                this.getEducation(),
                this.getEmail()
        );
    }
}
