package com.assignment.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.assignment.R;
import com.assignment.model.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 25/1/18.
 */

public class EmpViewAdapter extends RecyclerView.Adapter <EmpViewAdapter.MyViewAdapter> {
    private final String TAG = EmpViewAdapter.class.toString();
    private List <Employee> empRecords = new ArrayList <Employee>();

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(final MyViewAdapter holder, int position) {
        holder.mNameTv.setText(empRecords.get(position).getName());    // set name
        holder.mPhoneNumberTv.setText(empRecords.get(position).getphoneNumber());  // set phoneNumber
        holder.mDoBTv.setText(empRecords.get(position).getDob());  // set doteofbirth
    }

    // return total record count
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + empRecords.size());
        return empRecords.size();
    }

    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mNameTv, mPhoneNumberTv, mDoBTv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mNameTv = (TextView) itemView.findViewById(R.id.text_view_name);
            mPhoneNumberTv = (TextView) itemView.findViewById(R.id.text_view_phone_number);
            mDoBTv = (TextView) itemView.findViewById(R.id.text_view_date_of_birth);
        }
    }

    public void updateEmpRecords(List <Employee> empRecords) {
        this.empRecords = empRecords;
    }
}
