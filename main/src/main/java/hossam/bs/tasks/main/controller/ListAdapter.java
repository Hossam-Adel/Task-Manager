package hossam.bs.tasks.main.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Task;
import hossam.bs.tasks.main.R;

public class ListAdapter extends ArrayAdapter {
    Activity cntx;
    List<Task> values;
    PrioritySelector config;
    DBMgr mgr;

    public ListAdapter(@NonNull Context context, int resource, @NonNull List objects,DBMgr mgr) {
        super(context, resource, objects);
        this.cntx = (MainActivity) context;
        this.values = objects;
        this.mgr = mgr;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        final ViewHolder vHolder;
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listcell,parent,false);
            vHolder = new ViewHolder(rowView);
            rowView.setTag(vHolder);
        }else{
            vHolder = (ViewHolder) rowView.getTag();
        }

        final Task task = values.get(position);
        vHolder.getTitle().setText(task.getName());
        vHolder.getDate().setText(task.getDt());
        final ImageButton done= vHolder.getDone();
        if(task.getDone() == 0){
            done.setImageResource(R.drawable.icons8uncheckedcheckbox);
        }else{
            done.setImageResource(R.drawable.icons8checkedcheckboxfilled);
        }
        vHolder.getDone().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.getDone()==0){
                    done.setImageResource(R.drawable.icons8checkedcheckboxfilled);
                    task.setDone(1);
                    notifyDataSetChanged();
                }else{
                    done.setImageResource(R.drawable.icons8uncheckedcheckbox);
                    task.setDone(0);
                    notifyDataSetChanged();
                }
                task.saveOrUpdate(mgr);
            }
        });
        Button priority1 = vHolder.getPriority1();
        Button priority2 = vHolder.getPriority2();
        Button priority3 = vHolder.getPriority3();
        config = new PrioritySelector(priority1,priority2,priority3,task,mgr);
        config.adjustPriority();
        config.configureClickListener(priority1,priority2,priority3,1);
        config.configureClickListener(priority2,priority1,priority3,2);
        config.configureClickListener(priority3,priority2,priority1,3);
        final ArrayList<Task> taskList= (ArrayList<Task>) values;
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(cntx,TaskActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("task",values.get(position));
                b.putParcelableArrayList("taskList",taskList);
                b.putInt("pos",position);
                in.putExtras(b);
                cntx.startActivityForResult(in, 1);
            }
        });
        return rowView;
    }
}
