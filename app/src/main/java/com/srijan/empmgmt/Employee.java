package com.srijan.empmgmt;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity
public class  Employee {
    @PrimaryKey
    public int uid;
    @ColumnInfo(name = "first_name")
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "field")
    public String field;
    @ColumnInfo(name = "title")
    public String title;

    public int getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getField() {
        return field;
    }

    public String getTitle() {
        return title;
    }

    public Employee(Integer uid, String firstName, String lastName, String address, String field, String title){
        this.uid=uid;
        this.firstName=firstName;
        this.lastName=lastName;
        this.address=address;
        this.field=field;
        this.title=title;
    }



}
