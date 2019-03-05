package com.example.nitantsood.tvssampleapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.oneItemViewHolder> {

    Context mContext;
    ArrayList<OneCustomerDetail> employeeList;
    ItemClickListener mListener;

    public EmployeeListAdapter(Context mContext, ArrayList<OneCustomerDetail> employeeList, ItemClickListener mListener) {
        this.mContext = mContext;
        this.employeeList = employeeList;
        this.mListener = mListener;
    }

    public interface ItemClickListener{
        void onEmployeeClicked(View view,int position);
    }
    @NonNull
    @Override
    public oneItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.one_employee_detail,parent,false);
        return  new oneItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull oneItemViewHolder holder, int position) {

        holder.name.setText(employeeList.get(position).getName());
        holder.occupation.setText(employeeList.get(position).getOccupation());
        holder.location.setText(employeeList.get(position).getLocation());
        holder.id.setText("ID:"+employeeList.get(position).getId());
        holder.imageView.setImageResource(R.drawable.ic_launcher_background);

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class oneItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView name,location,occupation,id;
        ImageView imageView;
        public oneItemViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.empName);
            occupation=itemView.findViewById(R.id.empOccupation);
            location=itemView.findViewById(R.id.empLoc);
            id=itemView.findViewById(R.id.empID);
            imageView=itemView.findViewById(R.id.empImg);
            cardView=itemView.findViewById(R.id.main_item_card);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onEmployeeClicked(v,getAdapterPosition());
        }
    }
}
