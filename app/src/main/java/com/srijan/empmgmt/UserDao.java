package com.srijan.empmgmt;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface UserDao {
    @Query("SELECT * FROM employee")
    LiveData<List<Employee>> getAll();

    @Query("SELECT * FROM employee")
    List<Employee> getAllList();

    @Query("SELECT * FROM employee WHERE uid IN (:userIds)")
    List<Employee> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM employee WHERE uid=:userIds")
    Employee loadByID(int userIds);
    @Query("SELECT * FROM employee WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    Employee findByName(String first, String last);

    @Query("SELECT * FROM employee WHERE address LIKE :add LIMIT 1")
    Employee findByAddress(String add);


    @Query("SELECT * FROM employee WHERE field LIKE :field LIMIT 1")
    Employee findByField(String field);

    @Query("SELECT * FROM employee WHERE title LIKE :tit LIMIT 1")
    Employee findByTit(String tit);

    @Insert
    void insertAll(Employee... employees);
    @Insert
    void insert(Employee employee);
    @Delete
    int delete(Employee employee);
    @Update
    void update(Employee employee);

}