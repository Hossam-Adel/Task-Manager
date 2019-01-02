package hossam.bs.tasks.main.DAO;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Users implements DAO,Parcelable {

    protected Users(Parcel in) {
        tableName = in.readString();
        id = in.readInt();
        name = in.readString();
        fields = in.createStringArray();
        tasks = in.createTypedArrayList(Task.CREATOR);
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getTableName() {
        return tableName;
    }

    String tableName = "users";

    int id;

    String name;

    String[] fields ={"id","name"};

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    List<Task> tasks;

    public Users(){}

    public String[] getFields() {
        return fields;
    }

    public Users(int id, String name) {
        this.name = name;
        this.id = id;
    }
    public Users(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String[] getValues(){
        String[] values = {String.valueOf(getId()),getName()};
        return values;
    }

    public Users getUserByName(String name,DBMgr mgr){
        Users user ;
        String query = "Select * from users where name = ?";
        String[] args = {name};
        Cursor c = mgr.getEntity(query,args);
        if(c!=null){
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    user = new Users(c.getInt(0), c.getString(1));
                    return user;
                }
            }
        }
        return null;
    }

    public void save(DBMgr mgr){
        mgr.save(this);
    }

    @Override
    public DAO loadEntity(DBMgr mgr) {
        return null;
    }

    public List<Task> loadUserTasks(DBMgr mgr){
        List<Task> tasks = new ArrayList<Task>();
        String query = "Select * from task where user ="+getId();
        Cursor c = mgr.getEntity(query,null);
        if(c!=null){
            if(c.getCount()>0){
                while(c.moveToNext()){
                    Task task = new Task(c.getInt(0)
                            ,c.getString(1)
                            ,c.getString(2)
                            ,c.getString(3)
                            ,c.getInt(4)
                            ,c.getInt(5)
                            ,c.getInt(6));
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tableName);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeStringArray(fields);
        dest.writeTypedList(tasks);
    }

}
