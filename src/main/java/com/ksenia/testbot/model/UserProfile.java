package com.ksenia.testbot.model;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserProfile {
    private String id;
    private String name;
    private String surname;
    private String age;
    private String sex;
    private String language;
    private int registrationMarker;

    public int getRegistrationMarker() {
        return registrationMarker;
    }

    public void setRegistrationMarker(int registrationMarker) {
        this.registrationMarker = registrationMarker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserProfile() {

    }

    public UserProfile(String name, String surname, String age, String sex, String language) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.language = language;
    }

    public UserProfile(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nSurname: %s\nAge: %s\nSex: %s\nLanguage: %s\n", name, surname, age, sex, language);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
