package hossam.bs.tasks.main.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Calendar;
import java.util.List;

import hossam.bs.tasks.main.DAO.Comment;
import hossam.bs.tasks.main.R;

public class CommentListAdapter extends ArrayAdapter {

    Context cntx;
    List<Comment> values;


    public CommentListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.cntx = context;
        this.values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        final CommentListViewHolder vHolder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listcomment, parent, false);
            vHolder = new CommentListViewHolder(rowView);
            rowView.setTag(vHolder);
        } else {
            vHolder = (CommentListViewHolder) rowView.getTag();
        }
        vHolder.getComment().setText(values.get(position).getText());
        vHolder.getTime().setText(getTimeDifference(values.get(position)));
        return rowView;
    }

    String getTimeDifference(Comment comm){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long diff = currentTime - comm.getTime();
        long hours = (diff/1000/60/60);
        long days  = (diff/1000/60/60/24);
        long minutes = (diff/1000/60);
        return (days>0)?days+" days ":(hours>0)?days+" hours ":(minutes>0)?minutes+" minutes":"less than a minute";
    }


}