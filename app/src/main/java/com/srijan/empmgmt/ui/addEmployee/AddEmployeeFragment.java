package com.srijan.empmgmt.ui.addEmployee;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.srijan.empmgmt.AppDatabase;
import com.srijan.empmgmt.Employee;
import com.srijan.empmgmt.MainActivity;
import com.srijan.empmgmt.R;
import com.srijan.empmgmt.RepeatListener;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class AddEmployeeFragment extends Fragment {
    private AddEmployeeViewModel addEmployeeViewModel;
    private AppDatabase db ;

    LiveData<String> fakeID;
    LiveData<String> fakeFirst;
    LiveData<String>  fakeLast;
    LiveData<String>  fakeAddress;
    LiveData<String>  fakeTitle;
    LiveData<String>  fakeField;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bitmap okIcon = getBitmap(R.drawable.ic_donee_black_24dp);
        Bitmap crossIcon = getBitmap(R.drawable.ic_warning_black_48dp);
        Bitmap errorIcon = getBitmap(R.drawable.ic_error_black_24dp);
        Bitmap refreshIcon = getBitmap(R.drawable.ic_refresh_black_24dp);

        addEmployeeViewModel =
                ViewModelProviders.of(this).get(AddEmployeeViewModel.class);
        db = Room.databaseBuilder(MainActivity.context,
                AppDatabase.class, "employee")
                .build();

        View root = inflater.inflate(R.layout.fragment_addemployee, container, false);
        EditText id = root.findViewById(R.id._id);
        EditText fName = root.findViewById(R.id.fname);
        EditText lName = root.findViewById(R.id.lname);
        EditText address = root.findViewById(R.id.address);
        EditText field = root.findViewById(R.id.field);
        EditText title = root.findViewById(R.id.title);

        CircularProgressButton shuffle = root.findViewById(R.id.shuffle);
        shuffle.setOnClickListener(view -> {
            View.OnClickListener listen = v -> {
                shuffle.startAnimation();
                shuffle.doneLoadingAnimation(Color.rgb(29, 233, 182), refreshIcon);
                new Handler().postDelayed(shuffle::revertAnimation, 1250);
                shuffle.setBackgroundResource(R.drawable.icon_shuffle);
                List<MutableLiveData<String>> list = addEmployeeViewModel.randomize();
                fakeID = list.get(0);
                fakeFirst = list.get(1);
                fakeLast = list.get(2);
                fakeAddress = list.get(3);
                fakeTitle = list.get(4);
                fakeField = list.get(5);
                fakeID.observe(getViewLifecycleOwner(), id::setText);
                fakeFirst.observe(getViewLifecycleOwner(), fName::setText);
                fakeLast.observe(getViewLifecycleOwner(), lName::setText);
                fakeAddress.observe(getViewLifecycleOwner(), address::setText);
                fakeTitle.observe(getViewLifecycleOwner(), title::setText);
                fakeField.observe(getViewLifecycleOwner(), field::setText);

            };
            Snackbar.make(view, "Randomize Fields?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", listen).show();
        });

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


        CircularProgressButton addMany = root.findViewById(R.id.addMany);
        addMany.setOnTouchListener(new RepeatListener(1000, 500, new View.OnClickListener() {
            int duration=0;
            int peopleAdd=1;
            @Override
            public void onClick(View view) {
                // the code to execute repeatedly
                duration+=1;

                List<MutableLiveData<String>> list = addEmployeeViewModel.randomize();
                fakeID = list.get(0);
                fakeFirst = list.get(1);
                fakeLast = list.get(2);
                fakeAddress = list.get(3);
                fakeTitle = list.get(4);
                fakeField = list.get(5);
                fakeID.observe(getViewLifecycleOwner(), id::setText);
                fakeFirst.observe(getViewLifecycleOwner(), fName::setText);
                fakeLast.observe(getViewLifecycleOwner(), lName::setText);
                fakeAddress.observe(getViewLifecycleOwner(), address::setText);
                fakeTitle.observe(getViewLifecycleOwner(), title::setText);
                fakeField.observe(getViewLifecycleOwner(), field::setText);

                ColorStateList colorStateListRED = ColorStateList.valueOf(Color.rgb(244, 67, 54));
                ColorStateList colorStateListGREEN = ColorStateList.valueOf(Color.rgb(29, 233, 182));
                String newid, newName, newlName, newaddress, newtitle, newfield;
                newid = isnEmp(id);
                newName = isnEmp(fName);
                newlName = isnEmp(lName);
                newaddress = isnEmp(address);
                newtitle = isnEmp(field);
                newfield = isnEmp(title);
                AtomicReference<Integer> error = new AtomicReference<>(0);
                Integer intNewID = Integer.parseInt(newid);
                Employee emp = addEmployeeViewModel.newEmp(intNewID, newName, newlName, newaddress,
                        newtitle, newfield);
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    try {
                        db.userDao().insert(emp);
                        error.set(0);
                    } catch (SQLiteConstraintException | NullPointerException e) {
                        error.set(1);
                        e.printStackTrace();
                    } finally {
                        if (error.get() == 1) {
                            peopleAdd-=1;
                        } else {
                            peopleAdd+=1;
                        }
                    }
                });
                addMany.startAnimation();
                addMany.setBackgroundResource(R.drawable.icon_addmany);
                if(duration>1) {
                    new Handler().postDelayed(() -> {
                        addMany.revertAnimation();
                        addMany.setBackgroundResource(R.drawable.icon_addmany);
                    }, duration * 1000 + 200);
                }

                Toast toast=null;
                if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
                    toast=Toast.makeText(MainActivity.context,"Added "+peopleAdd+" Employees", Toast.LENGTH_SHORT);
                    toast.show();
                }

               Toast.makeText(MainActivity.context,"Added "+peopleAdd+" Employees", Toast.LENGTH_SHORT).show();

            }

        }));
        db.close();
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

