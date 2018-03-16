package model;

import org.springframework.stereotype.Component;

@Component
public class UserProfile {
    private String name;
    private String surname;
    private String age;
    private String sex;
    private String language;
    public UserProfile() {
    }

    public UserProfile(String name, String surname, String age, String sex, String language) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.language = language;
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
        return String.format("Name: %s\nSurname: %s\nAge: %s\nSex: %s\n Language: %s\n", name, surname, age, sex, language);
    }
}
