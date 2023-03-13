package com.srijan.empmgmt;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {
    private EmployeeListAdapter employeeListAdapter;
    private List<Employee> filterList;
    public CustomFilter(List<Employee> list,EmployeeListAdapter employeeListAdapter){
        this.employeeListAdapter=employeeListAdapter;
        this.filterList=list;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint!=null&&constraint.length()>0){
            constraint=constraint.toString().toUpperCase();
            ArrayList<Employee> filterEmp=new ArrayList<>();
            for(int i=0;i<filterList.size();i++){
                Employee candidate=filterList.get(i);
                if(
                           candidate.getFirstName().toUpperCase()
                                    .contains(constraint)
                        || candidate.getLastName().toUpperCase()
                                    .contains(constraint)
                        || candidate.getTitle().toUpperCase()
                                    .contains(constraint)
                        ||candidate.getAddress().toUpperCase()
                                   .contains(constraint)
                        ||candidate.getField().toUpperCase()
                                   .contains(constraint)
                    )
                {
                    filterEmp.add(filterList.get(i));
                }
            }
            results.count=filterEmp.size();
            results.values=filterEmp;
        } else{
             results.count=filterList.size();
             results.values=filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        employeeListAdapter.setNotes( (List<Employee>) results.values );
    }
}
