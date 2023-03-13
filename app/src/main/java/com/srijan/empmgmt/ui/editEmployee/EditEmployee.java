package com.srijan.empmgmt.ui.editEmployee;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.MainActivity;
import com.srijan.empmgmt.R;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class EditEmployee extends DialogFragment {
    public static EditEmployee newInstance(Integer num) {
        EditEmployee f = new EditEmployee();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
    EditEmployeeViewModel EditEmployeeViewModel;
    private Integer UID;
    //private TextView id;
    private EditText fName;
    private EditText lName;
    private EditText address;
    private EditText field;
    private EditText title;
    private CircularProgressButton save,cancel;
    private ColorStateList colorStateListGREEN = ColorStateList.valueOf(Color.rgb(29, 233, 182));
    private Bitmap errorIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EditEmployeeViewModel =
                ViewModelProviders.of(this).get(EditEmployeeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_editemployee, container, false);
        errorIcon= getBitmap(R.drawable.ic_error_black_24dp);
        //id = root.findViewById(R.id._id);
        fName = root.findViewById(R.id.fname);
        lName = root.findViewById(R.id.lname);
        address = root.findViewById(R.id.address);
        field = root.findViewById(R.id.field);
        title = root.findViewById(R.id.title);
        save=root.findViewById(R.id.save);
        cancel=root.findViewById(R.id.cancel);
        assert getArguments() != null;
        UID=getArguments().getInt("num");
        //id.setText(UID.toString());
        Employee employee=EditEmployeeViewModel.getEmp(UID);
        fName.setText(employee.getFirstName());
        lName.setText(employee.getLastName());
        address.setText(employee.getAddress());
        field.setText(employee.getField());
        title.setText(employee.getTitle());

        save.setOnClickListener(v -> {

            String newName, newlName, newaddress, newtitle, newfield;
            newName = isnEmp(fName);
            newlName = isnEmp(lName);
            newaddress = isnEmp(address);
            newtitle = isnEmp(field);
            newfield = isnEmp(title);
            if (newName.isEmpty() || newaddress.isEmpty() || newlName.isEmpty()
                    || newtitle.isEmpty() || newfield.isEmpty()) {
                save.startAnimation();
                save.setBackgroundResource(R.drawable.icon_save);
                save.doneLoadingAnimation(Color.rgb(244, 67, 54), errorIcon);
                Handler chandler = new Handler();
                chandler.postDelayed(() -> {
                    ViewCompat.setBackgroundTintList(fName, colorStateListGREEN);
                    fName.setError(null);
                    ViewCompat.setBackgroundTintList(lName, colorStateListGREEN);
                    lName.setError(null);
                    ViewCompat.setBackgroundTintList(address, colorStateListGREEN);
                    address.setError(null);
                    ViewCompat.setBackgroundTintList(field, colorStateListGREEN);
                    field.setError(null);
                    ViewCompat.setBackgroundTintList(title, colorStateListGREEN);
                    title.setError(null);
                    save.revertAnimation();
                    save.setBackgroundResource(R.drawable.icon_save);
                }, 2000);
                Snackbar.make(v, "Empty Fields!", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.RED)
                        .setAction("Okay", view -> {
                        })
                        .show();
            }
            else{
                Employee newEmp= new Employee(UID,newName,newlName
                        ,newaddress,newfield,
                        newtitle);
                EditEmployeeViewModel.saveEmp(newEmp);
                MainActivity.allAccessNav.navigate(R.id.loadingPage);
                new Handler().postDelayed(() -> {
                    MainActivity.allAccessNav.navigate(R.id.displayEmp);
                }, 750);
                this.dismiss();
            }

        });
        cancel.setOnClickListener(v -> {
            dismiss();
        });
        return root;
    }
    private String isnEmp(EditText text)  {
        String ans=text.getText().toString();
        ColorStateList colorStateListRED = ColorStateList.valueOf(Color.rgb(244, 67, 54));
        if(ans.isEmpty()){
            ViewCompat.setBackgroundTintList(text, colorStateListRED);
            text.setError("Field Blank!");
        }
        else{
            ColorStateList colorStateListGREEN = ColorStateList.valueOf(Color.rgb(29, 233, 182));
            ViewCompat.setBackgroundTintList(text, colorStateListGREEN);
        }
        return ans;
    }
    public Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
