package com.assignment;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.adapter.EmpViewAdapter;
import com.assignment.database.DBHelper;
import com.assignment.model.Employee;
import com.assignment.utils.CommonUtilities;
import com.assignment.utils.Constants;
import com.assignment.utils.RecyclerViewItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.toString();
    private EditText mNameEdt, mPhoneNumberEdt, mDOfBirthEdt;
    private Button mSaveBtn, mCancelBtn;
    private TextView mStatusTv;
    private RecyclerView mRecyclerView;
    private DBHelper dbHelper;
    private EmpViewAdapter empViewAdapter;
    List <Employee> emp = new ArrayList <Employee>();
    private String name, phoneNumber, dateOfBirth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout layout(.xml)
        setContentView(R.layout.activity_main);
        // init views
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_View_employee);
        mStatusTv = (TextView) findViewById(R.id.textview_no_result);
        dbHelper = CommonUtilities.getDBObject(this); // get database reference
        getSupportActionBar().setTitle("Employee Details"); //  set tittle
        empViewAdapter = new EmpViewAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(this, 0));
        mRecyclerView.setAdapter(empViewAdapter); // set adapter
    }

    @Override
    protected void onStart() {
        super.onStart();
        // get all employee records
        emp = dbHelper.getEmployeeDetails();
        if (emp.size() == 0) { // check for record count
            mStatusTv.setVisibility(View.VISIBLE); // if 0 then make 'no records' text as visible
        } else {
            mStatusTv.setVisibility(View.INVISIBLE);
            empViewAdapter.updateEmpRecords(emp); // update new records to adapter and update Ui
            empViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            // Create custom dialog object
            final Dialog dialog = new Dialog(MainActivity.this);
            // Include dialog.xml file
            dialog.setContentView(R.layout.add_dialog);
            // Set dialog title
            dialog.setTitle("Enter the Details");

            mNameEdt = (EditText) dialog.findViewById(R.id.edit_text_name);
            mPhoneNumberEdt = (EditText) dialog.findViewById(R.id.edit_text_phone_number);
            mDOfBirthEdt = (EditText) dialog.findViewById(R.id.edit_text_date_of_birth);
            mSaveBtn = (Button) dialog.findViewById(R.id.button_add);
            mCancelBtn = (Button) dialog.findViewById(R.id.button_cancle);
            dialog.show();
            // set onclick listener to save button
            mSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get all values
                    name = mNameEdt.getText().toString();
                    phoneNumber = mPhoneNumberEdt.getText().toString();
                    dateOfBirth = mDOfBirthEdt.getText().toString();

                    if (validateAndSave()) {            // if all values are current and save in DB
                        mStatusTv.setVisibility(View.INVISIBLE);
                        emp = dbHelper.getEmployeeDetails(); // then get updated values from the database
                        empViewAdapter.updateEmpRecords(emp); // update UI
                        empViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }
            });
            // set onclick listener to cancel button
            mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();    //  dismiss dialog
                }
            });
        }
        return true;
    }

    // this method will check all fields are having values or not
    private boolean validateAndSave() {
        if (validate(name, mNameEdt, "Please enter Name"))
            if (validate(phoneNumber, mPhoneNumberEdt, "Please Enter Phone Number"))
                if (validate(dateOfBirth, mDOfBirthEdt, "Please Enter Date of birth")) {
                    final ContentValues contentValues = new ContentValues();
                    contentValues.put(Constants.EMPLOYEE_NAME, name);
                    contentValues.put(Constants.EMPLOYEE_PHONE_NUMBER, phoneNumber);
                    contentValues.put(Constants.EMPLOYEE_DATA_OF_BIRTH, dateOfBirth);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long result = dbHelper.insertContentVals(Constants.EMPLOYEE_TABLE, contentValues);
                        }
                    }).start();
                    Toast.makeText(getApplicationContext(), "Records saved", Toast.LENGTH_SHORT).show();
                    return true;
                }
        return false;
    }

    // this method will check whether the fields are having values or not if not return false
    private boolean validate(String value, EditText view, String error) {
        if (value.equalsIgnoreCase("")) {
            view.setError(error);
            return false;
        } else {
            return true;
        }
    }
}
