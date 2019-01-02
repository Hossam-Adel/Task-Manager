package hossam.bs.tasks.main.DAO;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class Task implements DAO,Parcelable{

    String tableName = "task";
    int id;
    String name;
    String dt;
    String description;
    int priority;
    int done;
    int user;
    String[] fields ={"id","name","description","dt","priority","done","user"};
    List<Comment> comments;

    protected Task(Parcel in) {
        tableName = in.readString();
        id = in.readInt();
        name = in.readString();
        dt = in.readString();
        description = in.readString();
        priority = in.readInt();
        done = in.readInt();
        user = in.readInt();
        fields = in.createStringArray();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    int getUser() {
        return user;
    }

    public void save(DBMgr mgr){
        mgr.save(this);
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getTableName() {
        return tableName;
    }
    void setId(int id) {
        this.id = id;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Task(){}

    public String[] getFields() {
        return fields;
    }

    public Task(int id, String name, String dt, String description, int priority, int done,int user) {
        this.id = id;
        this.name = name;
        this.dt = dt;
        this.description = description;
        this.priority = priority;
        this.done = done;
        this.user = user;
    }

    public Task(String name){
        this.name = name;
        this.dt = "";
        this.description = "";
        this.priority = 0;
        this.done = 0;
    }

     public int getId() {
        return id;
    }


    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String[] getValues() {
        String[] values = {String.valueOf(getId()),getName(),getDt(),getDescription(),String.valueOf(getPriority()),String.valueOf(getDone()),String.valueOf(getUser())};
        return values;
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
        dest.writeString(dt);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeInt(done);
        dest.writeInt(user);
        dest.writeStringArray(fields);
    }
    public Task loadEntity(DBMgr mgr){
        Task task = new Task();
        String query = "Select * from task where id ="+getId();
        Cursor c = mgr.getEntity(query,null);
        if(c!=null){
            if(c.getCount()>0){
                if(c.moveToNext()){
                    task = new Task(c.getInt(0)
                            ,c.getString(1)
                            ,c.getString(2)
                            ,c.getString(3)
                            ,c.getInt(4)
                            ,c.getInt(5)
                            ,c.getInt(6));
                }
            }
        }
        return task;
    }

    public List<Comment> loadTaskComments(DBMgr mgr){
        List<Comment> comments = new ArrayList<Comment>();
        String query = "Select * from COMMENTS where task = "+getId();
        Cursor c = mgr.getEntity(query,null);
        if(c!=null){
            if(c.getCount()>0){
                while(c.moveToNext()){
                    Comment comm = new Comment(c.getInt(0),c.getString(1),c.getLong(2));
                    comments.add(comm);
                }
            }
        }
        return comments;
    }
    public void delete(DBMgr mgr){
        mgr.deleteEntity(this);
    }

    public boolean saveOrUpdate(DBMgr mgr){
        boolean done = mgr.saveOrUpdate(this);
        return done;
    }


}
