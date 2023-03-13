package com.srijan.empmgmt.ui.displayEmployees;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.MainActivity;
import com.srijan.empmgmt.UserDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DisplayEmployeesViewModel extends ViewModel {
    private LiveData<List<Employee>> listLiveData;
    private AppDatabase db ;
    private UserDao userDao;
    public  DisplayEmployeesViewModel(){
        db = Room.databaseBuilder(MainActivity.context,
                AppDatabase.class, "employee")
                .build();
        userDao=db.userDao();
        listLiveData=userDao.getAll();
    }
    public LiveData<List<Employee>> getListLiveData(){
        return listLiveData;
    }

    public void delete(Employee employee) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            db.userDao().delete(employee);
        });
    }
    public void add(Employee employee) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            db.userDao().insert(employee);
        });
    }

}