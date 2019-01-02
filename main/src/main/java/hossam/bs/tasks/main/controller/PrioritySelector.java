package hossam.bs.tasks.main.controller;

import android.view.View;
import android.widget.Button;

import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Task;

public class PrioritySelector {
    Task task;
    Button p1;
    Button p2;
    Button p3;
    DBMgr mgr;
     PrioritySelector(Button p1, Button p2, Button p3, Task task, DBMgr mgr){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.task = task;
        this.mgr = mgr;
    }

     void adjustPriority(){
        if(task.getPriority() == 1){
            p1.setBackgroundColor(0xffff8800);
        }else if(task.getPriority() == 2){
            p2.setBackgroundColor(0xffff8800);
        }else if(task.getPriority() == 3){
            p3.setBackgroundColor(0xffff8800);
        }
    }
     void configureClickListener(final Button target,final Button b2,final Button b3,final int priority){
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target.setBackgroundColor(0xffff8800);
                b2.setBackgroundColor(0xFFFFFFFF);
                b3.setBackgroundColor(0xFFFFFFFF);
                task.setPriority(priority);
                task.saveOrUpdate(mgr);
            }
        });
    }
}
