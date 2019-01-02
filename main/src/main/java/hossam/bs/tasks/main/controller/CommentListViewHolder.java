package hossam.bs.tasks.main.controller;

import android.view.View;
import android.widget.TextView;

import hossam.bs.tasks.main.R;

public class CommentListViewHolder {
    View convertView;
    TextView comment;
    TextView time;

    public CommentListViewHolder(View view){
        this.convertView = view;
    }

    public TextView getTime() {
        if(time == null)
            time = convertView.findViewById(R.id.dt);
        return time;
    }

    public TextView getComment() {
        if(comment == null)
            comment = convertView.findViewById(R.id.comment);
        return comment;
    }

}
