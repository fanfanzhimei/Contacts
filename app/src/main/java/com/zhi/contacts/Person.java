package com.zhi.contacts;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Person {
    private String name;
    private String phone;

    public Person(){

    }

    public Person(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person----:"+"name:"+name+";phone:"+phone;
    }
}
