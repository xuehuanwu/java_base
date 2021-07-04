package com.java.features;

/**
 * 传值
 */
public class TransferValueDemo {

    public static void main(String[] args) {
        // 基本数据类型传值相当于复制一份
        int age = 20;
        changeInt(age);
        System.out.println("age: " + age);
        // 对象传值相当于引用地址(指针)
        Person person = new Person("abc");
        changePerson(person);
        System.out.println("name: " + person.getName());
        // string特殊性：没有则新建，存在则复用
        String str = "abc";
        changeString(str);
        System.out.println("str: " + str);
    }

    public static void changeInt(int age) {
        age = 30;
    }

    public static void changePerson(Person person) {
        person.setName("xxx");
    }

    public static void changeString(String str) {
        str = "xxx";
    }
}

class Person {
    private Integer age;
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(String name) {
        this.name = name;
    }
}
