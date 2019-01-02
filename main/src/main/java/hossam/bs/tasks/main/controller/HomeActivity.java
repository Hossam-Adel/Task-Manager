package hossam.bs.tasks.main.controller;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Task;
import hossam.bs.tasks.main.DAO.Users;
import hossam.bs.tasks.main.R;

public class HomeActivity extends AppCompatActivity {
    Button login;
    Button register;
    EditText name;
    DBMgr mgr ;
    List<Task> tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intialize();

        login.setOnClickListener(new View.OnClickListener() {
            Users user;
            @Override
            public void onClick(View view) {

                if(!(name.getText().toString()).equals("")){
                    user = new Users(name.getText().toString());
                    makeTransaction(user);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(register);
            }
        });
    }
    void intialize(){
        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        mgr = DBMgr.getInstance(getApplicationContext());
    }

    void makeTransaction(Users user){
        Users returnedUser = user.getUserByName(user.getName(),mgr);
        if(returnedUser != null){
            Users comleteUser = user.getUserByName(user.getName(),mgr);
            if(comleteUser.loadUserTasks(mgr)!=null){
                comleteUser.setTasks(user.loadUserTasks(mgr));

            }else{
                Toast.makeText(getApplicationContext(),"no tasks found",Toast.LENGTH_SHORT).show();
            }
            Intent tasks = new Intent(getApplicationContext(),MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", (Parcelable) comleteUser);
            tasks.putExtras(bundle);
            startActivity(tasks);

        }else{
            Toast.makeText(getApplicationContext(),"user not found",Toast.LENGTH_SHORT).show();
        }
    }
}
