package hossam.bs.tasks.main.controller;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import hossam.bs.tasks.main.R;

public class ViewHolder {
    View convertView;
    TextView title;
    ImageButton done;
    Button priority1;
    Button priority2;
    Button priority3;
    TextView dt;
    LinearLayout container;

    public ViewHolder(View view){
        this.convertView = view;
    }

    public TextView getTitle() {
        if(title == null)
            title = convertView.findViewById(R.id.title);
        return title;
    }

    public ImageButton getDone() {
        if(done == null)
            done = convertView.findViewById(R.id.done);
        return done;
    }

    public Button getPriority1() {
        if(priority1 == null)
            priority1= convertView.findViewById(R.id.priority1);
        return priority1;
    }

    public Button getPriority2() {
        if(priority2 == null)
            priority2= convertView.findViewById(R.id.priority2);
        return priority2;
    }

    public Button getPriority3() {
        if(priority3 == null)
            priority3= convertView.findViewById(R.id.priority3);
        return priority3;
    }

    public TextView getDate() {
        if(dt == null)
            dt = convertView.findViewById(R.id.dt);
        return dt;
    }

    public LinearLayout getLayout(){
        if(container == null)
            container = convertView.findViewById(R.id.container);
        return container;
    }
}
