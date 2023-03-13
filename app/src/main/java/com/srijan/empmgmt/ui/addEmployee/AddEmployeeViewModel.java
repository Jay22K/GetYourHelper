package com.srijan.empmgmt.ui.addEmployee;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.javafaker.Faker;
import com.srijan.empmgmt.Employee;

import java.util.ArrayList;
import java.util.List;

public class AddEmployeeViewModel extends ViewModel {
    Faker faker = new Faker();
    private MutableLiveData<String> fakeID=new MutableLiveData<>();
    private MutableLiveData<String> fakeFirst=new MutableLiveData<>() ;
    private MutableLiveData<String>  fakeLast=new MutableLiveData<>() ;
    private MutableLiveData<String>  fakeAddress=new MutableLiveData<>();
    private MutableLiveData<String>  fakeTitle=new MutableLiveData<>();
    private MutableLiveData<String>  fakeField=new MutableLiveData<>();
    List<MutableLiveData<String>> list = new ArrayList<>();
    public List<MutableLiveData<String>> randomize() {
        fakeID.setValue(faker.phoneNumber().subscriberNumber());
        list.add(0,fakeID);
        fakeFirst.setValue(faker.name().firstName());
        list.add(1,fakeFirst);
        fakeLast.setValue(faker.funnyName().name());
        list.add(2,fakeLast);
        fakeAddress.setValue(faker.address().streetAddress());
        list.add(3,fakeAddress);
        fakeTitle.setValue(faker.job().title());
        list.add(4,fakeTitle);
        fakeField.setValue(faker.job().field());
        list.add(5,fakeField);
        return list;
    }
    public void fakeIDMake(){
        list.remove(0);
        fakeID.setValue(faker.phoneNumber().subscriberNumber());
        list.add(0,fakeID);
    }
    public Employee newEmp(Integer id, String fname, String lname,
                    String address, String field, String title) {
        Employee emp = new Employee(id, fname, lname, address, field, title);
        return emp;
    }




}