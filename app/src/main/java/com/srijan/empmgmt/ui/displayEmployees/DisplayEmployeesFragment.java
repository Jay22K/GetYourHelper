package com.srijan.empmgmt.ui.displayEmployees;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.EmployeeListAdapter;
import com.srijan.empmgmt.MainActivity;
import com.srijan.empmgmt.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DisplayEmployeesFragment extends Fragment implements EmployeeListAdapter.OnDeleteClickListener {
    public EmployeeListAdapter employeeListAdapter;
    private DisplayEmployeesViewModel displayEmployeesViewModel;
    private AppDatabase db;
    private ConstraintLayout coordinatorLayout;
    private RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db=Room.databaseBuilder(MainActivity.context,
                AppDatabase.class, "employee").build();
        displayEmployeesViewModel =
                ViewModelProviders.of(this).get(DisplayEmployeesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_display, container, false);
        coordinatorLayout=root.findViewById(R.id.coordinatorLayout);
        recyclerView=root.findViewById(R.id.recView);
        employeeListAdapter=new EmployeeListAdapter(MainActivity.context,this);
        recyclerView.setAdapter(employeeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        displayEmployeesViewModel.getListLiveData().observe(getViewLifecycleOwner(), employees -> employeeListAdapter.setNotes(employees));
        enableSwipeToDeleteAndUndo();
        MainActivity.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                employeeListAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                employeeListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        MainActivity.deleteAll.setOnMenuItemClickListener(item -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            List<Employee> refEmp=employeeListAdapter.getData();
            for(Employee emp:refEmp){
                myExecutor.execute(() -> {
                    db.userDao().delete(emp);
                });
            }
            employeeListAdapter.notifyDataSetChanged();
            MainActivity.allAccessNav.navigate(R.id.loadingPage);
            new Handler().postDelayed(() -> {
                MainActivity.allAccessNav.navigate(R.id.displayEmp);
            }, 750);
            Toast.makeText(MainActivity.context,"Successfully cleared all employees",Toast.LENGTH_SHORT).show();
            return true;
        });
        return root;
    }

    @Override
    public void OnDeleteClickListener(Employee employee) {
        displayEmployeesViewModel.delete(employee);
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(MainActivity.context) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Employee employee = employeeListAdapter.getData().get(position);
                employeeListAdapter.removeItem(position);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Employee was removed", Snackbar.LENGTH_SHORT);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        employeeListAdapter.restoreItem(employee, position);
                        displayEmployeesViewModel.add(employee);
                        recyclerView.scrollToPosition(position);
                        employeeListAdapter.setNotes(employeeListAdapter.getData());
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
