package hossam.bs.tasks.main.controller;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hossam.bs.tasks.main.DAO.Comment;
import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Task;
import hossam.bs.tasks.main.R;

public class TaskActivity extends AppCompatActivity {
    ImageButton done;
    TextView title;
    TextView dt;
    Button priority1;
    Button priority2;
    Button priority3;
    FloatingActionButton submit;
    EditText comment;
    ListView comments;
    List<Comment> values;
    CommentListAdapter adapter;
    Task task;
    PrioritySelector config;
    int position;
    DBMgr mgr;
    ImageButton delete;
    Calendar dateTime;
    SimpleDateFormat formatDateTime ;
    DatePickerDialog.OnDateSetListener d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initalize();
        setter();
    }

    void initalize(){
        mgr = DBMgr.getInstance(getApplicationContext());
        dateTime = Calendar.getInstance();
        formatDateTime= new SimpleDateFormat("MMMM dd yyyy", Locale.US);
        Bundle bundle = getIntent().getExtras();
        task =bundle.getParcelable("task");
        position = bundle.getInt("pos");
        values =task.loadTaskComments(mgr);
        done = findViewById(R.id.done);
        title = findViewById(R.id.title);
        dt = findViewById(R.id.dt);
        delete = findViewById(R.id.delete);
        priority1 = findViewById(R.id.priority1);
        priority2 = findViewById(R.id.priority2);
        priority3 = findViewById(R.id.priority3);
        config = new PrioritySelector(priority1,priority2,priority3,task,mgr);
        comment = findViewById(R.id.comment);
        submit = (FloatingActionButton) findViewById(R.id.submit);
        comments = findViewById(R.id.comments);
        adapter = new CommentListAdapter(getApplicationContext(),R.layout.listcomment,values);
    }

    void setter(){
        title.setText(task.getName());
        if(task.getDone() == 0){
            done.setImageResource(R.drawable.icons8uncheckedcheckbox);
        }else{
            done.setImageResource(R.drawable.icons8checkedcheckboxfilled);
        }
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task.getDone()==0){
                    done.setImageResource(R.drawable.icons8checkedcheckboxfilled);
                    task.setDone(1);
                }else{
                    done.setImageResource(R.drawable.icons8uncheckedcheckbox);
                    task.setDone(0);
                }
            }
        });
        config.adjustPriority();
        config.configureClickListener(priority1,priority2,priority3,1);
        config.configureClickListener(priority2,priority1,priority3,2);
        config.configureClickListener(priority3,priority2,priority1,3);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(comment.getText().toString());
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDelete();
            }
        });
        comments.setAdapter(adapter);
        if(!task.getDt().equals(""))
            dt.setText(task.getDt());
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
        d = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTime.set(Calendar.YEAR, year);
                dateTime.set(Calendar.MONTH, monthOfYear);
                dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTextLabel();
            }
        };
    }

    void submitComment(String text){
        if(!comment.getText().toString().equals("")) {
            Time t = new Time();
            t.setToNow();
            Comment comm = new Comment(comment.getText().toString(),dateTime.getTimeInMillis());
            comm.setTask(task.getId());
            comm.save(mgr);
            values.add(comm);
            task.setComments(values);
            adapter.notifyDataSetChanged();
            comment.setText("");
        }
    }

    void requestDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this task?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                configureIntent(true,task);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    void configureIntent(boolean deleteInd,Task editedTask){
        Intent returnIntent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("deleteInd",deleteInd);
        bundle.putParcelable("editedTask",editedTask);
        bundle.putInt("pos",position);
        returnIntent.putExtras(bundle);
        setResult(2,returnIntent);
        if(deleteInd) {
            task.delete(mgr);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        task.setComments(values);
        mgr.saveOrUpdate(task);
        if(event.getAction() != KeyEvent.KEYCODE_BACK){
            configureIntent(false,task);
            return true;
        }
        return false;
    }


    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTextLabel(){
        dt.setText(formatDateTime.format(dateTime.getTime()));
        task.setDt(formatDateTime.format(dateTime.getTime()));
        if(!task.saveOrUpdate(mgr))
            Toast.makeText(this,"Task date not saved or updated",Toast.LENGTH_SHORT).show();


    }



}
