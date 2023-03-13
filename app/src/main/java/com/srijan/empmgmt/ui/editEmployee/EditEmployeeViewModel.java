package com.srijan.empmgmt.ui.editEmployee;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.MainActivity;

public class EditEmployeeViewModel extends ViewModel {
    AppDatabase db = Room.databaseBuilder(MainActivity.context,
                    AppDatabase .class, "employee")
                    .allowMainThreadQueries().build();
    Employee getEmp(Integer UID){
//        AtomicReference<Employee> employeeAtomicReference= new AtomicReference<>();
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(() -> {
//            employeeAtomicReference.set(db.userDao().loadByID(UID));
//
//            list.add(0,employeeAtomicReference.get().getFirstName());
//            return employeeAtomicReference.get();
//        });
        return db.userDao().loadByID(UID);
    }
    public void saveEmp(Employee employee){
//        Executor myExecutor = Executors.newSingleThreadExecutor();
//        myExecutor.execute(() -> {
//            db.userDao().update(employee);
//        });
        db.userDao().update(employee);

    }
}
