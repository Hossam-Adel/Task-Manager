package hossam.bs.tasks.main.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Task;
import hossam.bs.tasks.main.DAO.Users;
import hossam.bs.tasks.main.R;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addTask;
    DBMgr mgr;
    Users user;
    ListAdapter adapter;
    List<Task> tasks;
    ListView listView;
    ImageButton filter;
    boolean filterIndic;
    TextView userName;
    Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void  initialize(){
        act = this;
        mgr = DBMgr.getInstance(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        user =bundle.getParcelable("user");
        userName = findViewById(R.id.userName);
        userName.setText(user.getName()+"'s tasks");
        tasks = user.loadUserTasks(mgr);
        adapter = new ListAdapter(this,R.layout.listcell,tasks,mgr);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        addTask = (FloatingActionButton) findViewById(R.id.addTask);
        filterIndic = false;
        filter = findViewById(R.id.filter);
    }

    void setter(){
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(filterIndic){
                adapter.notifyDataSetChanged();
                filter.setImageResource(R.drawable.icons8checkallfilledwhite);
                adapter= new ListAdapter(act,R.layout.listcell,tasks,mgr);
                filterIndic = false;
            }else if(!filterIndic){
                adapter.notifyDataSetChanged();
                filter.setImageResource(R.drawable.icons8checkallfilledblue);
                adapter= new ListAdapter(act,R.layout.listcell,filter(0),mgr);
                filterIndic = true;
            }
            listView.setAdapter(adapter);
            }
        });
    }

    void createTask(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        final EditText input = new EditText(this);input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if(!input.getText().toString().equals("")) {
                    Task task = new Task(name);
                    task.setUser(user.getId());
                    task.save(mgr);
                    tasks = user.loadUserTasks(mgr);
                    user.setTasks(tasks);
                    if (filterIndic) {
                        adapter = new ListAdapter(act, R.layout.listcell, filter(0),mgr);
                        listView.setAdapter(adapter);
                    } else {
                        adapter = new ListAdapter(act, R.layout.listcell, tasks,mgr);
                        listView.setAdapter(adapter);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });

        builder.show();
    }

    List<Task> filter(int done){
        List<Task> filteredTasks = new ArrayList<>();
        for(Task task : tasks){
            if(task.getDone() ==done)
                filteredTasks.add(task);
        }
        return filteredTasks;
    }

    @Override
    protected void onPause() {
        super.onPause();
        for(Task task : tasks){
            if(!task.saveOrUpdate(mgr)){
                Toast.makeText(this,"Tasks not saved or updated",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2) { Bundle bun = data.getExtras();
            Task editedtask= editedtask = bun.getParcelable("editedTask");
            boolean deleteInd = bun.getBoolean("deleteInd");
            int position = bun.getInt("pos");
            if (!deleteInd) {
                tasks.remove(position);
                tasks.add(position, editedtask);
            }else if(deleteInd) {
                tasks.remove(position);
                Toast.makeText(this,"task deleted",Toast.LENGTH_SHORT).show();
            }
            adapter = new ListAdapter(this, R.layout.listcell, tasks,mgr);
            listView.setAdapter(adapter);
        }
    }
}
