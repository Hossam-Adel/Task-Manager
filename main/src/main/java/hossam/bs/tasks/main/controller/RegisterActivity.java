package hossam.bs.tasks.main.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hossam.bs.tasks.main.DAO.DBMgr;
import hossam.bs.tasks.main.DAO.Users;
import hossam.bs.tasks.main.R;

public class RegisterActivity extends AppCompatActivity {
    Button register;
    EditText name;
    DBMgr mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intialize();

        register.setOnClickListener(new View.OnClickListener() {
            Users user;
            @Override
            public void onClick(View view) {
                user = new Users(name.getText().toString());
                if(!(name.getText().toString()).equals("")){
                    makeTransaction(user);
                }
            }
        });

    }
    void intialize(){
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        mgr = DBMgr.getInstance(getApplicationContext());
    }

    void makeTransaction(Users user){
        if(user.getUserByName(user.getName(),mgr) == null){
            user.save(mgr);
            Toast.makeText(getApplicationContext(),"user saved",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"user not saved, name used",Toast.LENGTH_SHORT).show();
        }
    }
}
