package com.jisu.tenantmanager;

public class Dictionary {

    private String usernumber;
    private String name;
    private String age;
    private String phonenumber;
    private String address;

    public Dictionary(String usernumber, String name, String age, String phonenumber, String address) {
        this.usernumber = usernumber;
        this.name = name;
        this.age = age;
        this.phonenumber = phonenumber;
        this.address = address;
    }


    public String getUsernumber() {
        return usernumber;
    }
    public String getName() {
        return name;
    }
    public String getAge() {
        return age;
    }
    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setData(String usernumber, String name) {
        this.name = name;
        this.usernumber = usernumber;
    }
}
