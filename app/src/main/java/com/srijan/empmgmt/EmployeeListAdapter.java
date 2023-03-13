package com.srijan.empmgmt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.srijan.empmgmt.ui.editEmployee.EditEmployee;

import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder> implements Filterable {
    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Employee> employeeList;
    List<Employee> filteredList;
    private CustomFilter filter;
    private OnDeleteClickListener onDeleteClickListener;

    public List<Employee> getData() {
        return employeeList;
    }

    public void removeItem(int position) {
        if (onDeleteClickListener != null) {
            onDeleteClickListener.OnDeleteClickListener(employeeList.get(position));
        }
    }

    public void restoreItem(Employee employee, int position) {
        employeeList.add(position,employee);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new CustomFilter(filteredList,this);
        }
        return filter;
    }

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Employee employee);
    }

    public EmployeeListAdapter(Context context,OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
        this.filteredList=employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
        EmployeeViewHolder viewHolder = new EmployeeViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListAdapter.EmployeeViewHolder holder, int position) {
        if (employeeList != null) {
            Employee employee = employeeList.get(position);
            holder.setData(employee, position);
        } else {
            holder.employeeNameView.setText(R.string.no_emp);
        }
    }

    public void setNotes(List<Employee> emps) {
        employeeList = emps;
        this.filteredList=emps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (employeeList != null) {
            return employeeList.size();
        } else return 0;
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView employeeNameView;
        private TextView employeeAddressView;
        private TextView employeeJobView;
        private int mPosition;
        private CircularProgressButton imgEdit,imgDelete;
        private Bitmap deleteIcon = getBitmap(R.drawable.ic_delete_forever_black_24dp);
        private Bitmap editIcon = getBitmap(R.drawable.ic_mode_edit_black_24dp);
        public EmployeeViewHolder(View itemView) {
            super(itemView);
            employeeNameView = itemView.findViewById(R.id.txvNote);
            employeeAddressView=itemView.findViewById(R.id.addressText);
            employeeJobView=itemView.findViewById(R.id.posText);
            imgDelete = itemView.findViewById(R.id.ivRowDelete);
            imgEdit = itemView.findViewById(R.id.ivRowEdit);
            setListeners();
        }

        public void setData(Employee employee, int position) {
            String fullName="Full Name: "+employee.getFirstName()+"\nAadhar No: "+employee.getLastName();
            String address="Address: "+employee.getAddress();
            String fullJob="Field: "+employee.getField()+"\n\nMobile No: "+employee.getTitle();
            employeeNameView.setText(fullName);
            employeeAddressView.setText(address);
            employeeJobView.setText(fullJob);
            mPosition = position;
        }

        public void setListeners() {
            final Handler handler = new Handler();
            imgEdit.setOnClickListener(v -> {
                imgEdit.startAnimation();
                imgEdit.doneLoadingAnimation(Color.rgb(207,104,123), deleteIcon);
                handler.postDelayed(() -> {
                    imgEdit.revertAnimation();
                    imgEdit.setBackgroundResource(R.drawable.icon_edit);
                }, 250);
//                Intent intent = new Intent(mContext, EditEmployee.class);
////                intent.putExtra("emp_id", employeeList.get(mPosition).getUid());
////                ((Activity)mContext).startActivityForResult(intent,1);
                FragmentTransaction ft = MainActivity.ft.beginTransaction();
                Fragment prev = MainActivity.ft.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                DialogFragment newFragment = EditEmployee.newInstance(employeeList.get(mPosition).getUid());
                newFragment.show(ft, "dialog");
                notifyItemChanged(mPosition);

            });
            imgDelete.setOnClickListener(v -> {
                imgDelete.startAnimation();
                imgDelete.doneLoadingAnimation(Color.rgb(207,104,123), deleteIcon);
                handler.postDelayed(() -> {
                    imgDelete.revertAnimation();
                    imgDelete.setBackgroundResource(R.drawable.icon_delete);
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.OnDeleteClickListener(employeeList.get(mPosition));
                    }

                }, 650);


            });

        }


    }
    public  Bitmap getBitmap(int drawableRes) {
        Drawable drawable = mContext.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}


