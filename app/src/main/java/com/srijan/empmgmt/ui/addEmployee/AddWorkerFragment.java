package com.srijan.empmgmt.ui.addEmployee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteConstraintException;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.MainActivity;
import com.srijan.empmgmt.R;
import com.srijan.empmgmt.soonActivity;
import com.srijan.empmgmt.ui.SelectActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class AddWorkerFragment extends Fragment {
    private AddEmployeeViewModel addEmployeeViewModel;
    private AppDatabase db ;




    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        Bitmap okIcon = getBitmap(R.drawable.ic_donee_black_24dp);
        Bitmap crossIcon = getBitmap(R.drawable.ic_warning_black_48dp);
        Bitmap errorIcon = getBitmap(R.drawable.ic_error_black_24dp);
        Bitmap refreshIcon = getBitmap(R.drawable.ic_refresh_black_24dp);


        addEmployeeViewModel =
                ViewModelProviders.of(this).get(AddEmployeeViewModel.class);
        db = Room.databaseBuilder(AddWorkerActivity.context,
                AppDatabase.class, "employee")
                .build();

        View root= inflater.inflate(R.layout.fragment_add_worker, container, false);
        EditText id = root.findViewById(R.id._id);
        EditText fName = root.findViewById(R.id.fname);
        EditText lName = root.findViewById(R.id.lname);
        EditText address = root.findViewById(R.id.address);
        EditText field = root.findViewById(R.id.field);
        EditText title = root.findViewById(R.id.title);







        CircularProgressButton add = root.findViewById(R.id.add);
        add.setOnClickListener(view -> {
            add.startAnimation();
            ColorStateList colorStateListRED = ColorStateList.valueOf(Color.rgb(244, 67, 54));
            ColorStateList colorStateListGREEN = ColorStateList.valueOf(Color.rgb(29, 233, 182));
            String newid, newName, newlName, newaddress, newtitle, newfield;
            newid = isnEmp(id);
            newName = isnEmp(fName);
            newlName = isnEmp(lName);
            newaddress = isnEmp(address);
            newtitle = isnEmp(field);
            newfield = isnEmp(title);
            if (newid.isEmpty() || newName.isEmpty() || newaddress.isEmpty() || newlName.isEmpty()
                    || newtitle.isEmpty() || newfield.isEmpty()) {
                add.doneLoadingAnimation(Color.rgb(244, 67, 54), errorIcon);
                Handler chandler = new Handler();
                chandler.postDelayed(() -> {
                    ViewCompat.setBackgroundTintList(id, colorStateListGREEN);
                    id.setError(null);
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
                }, 2000);
                Snackbar.make(view, "Empty Fields!", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(Color.RED)
                        .setAction("Okay", v -> {
                        })
                        .show();
            } else {
                AtomicReference<Integer> error = new AtomicReference<>(0);
                Integer intNewID = Integer.parseInt(newid);
                Employee emp = addEmployeeViewModel.newEmp(intNewID, newName, newlName, newaddress,
                        newtitle, newfield);
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    try {
                        db.userDao().insert(emp);
                    } catch (SQLiteConstraintException | NullPointerException e) {
                        error.set(1);
                    } finally {
                        if (error.get() == 0) {
                            add.doneLoadingAnimation(Color.rgb(29, 233, 182), okIcon);
                            Snackbar.make(view, "Employee Added!", Snackbar.LENGTH_SHORT)
                                    .setAction("Okay", v -> {
                                    })
                                    .show();

                            Intent intent = new Intent(getContext(), soonActivity.class);
                            startActivity(intent);



                            new Thread(() -> {
                                requireActivity().runOnUiThread(() -> {
                                    id.getText().clear();
                                    fName.getText().clear();
                                    lName.getText().clear();
                                    address.getText().clear();
                                    field.getText().clear();
                                    title.getText().clear();
                                });
                            }).start();

                        } else {
                            ViewCompat.setBackgroundTintList(id, colorStateListRED);
                            add.doneLoadingAnimation(Color.rgb(244, 67, 54), crossIcon);
                            View.OnClickListener add2;
                            add2 = v -> {
                                addEmployeeViewModel.fakeIDMake();
                            };
                            Snackbar.make(view, "Duplicate ID detected", Snackbar.LENGTH_SHORT)
                                    .setActionTextColor(Color.RED)
                                    .setAction("Change ID", add2).show();
                        }
                    }
                });
            }
            new Handler().postDelayed(add::revertAnimation, 1250);
            add.setBackgroundResource(R.drawable.icon_add);

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
    public  Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}

