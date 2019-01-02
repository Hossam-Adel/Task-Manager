package hossam.bs.tasks.main.DAO;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements DAO,Parcelable {

    String tableName = "COMMENTS";
    String[] fields = {"id","text","time","task"};
    String text;
    long time;
    int id;
    int task;

    public Comment(String text, long time){
        this.text = text ;
        this.time = time;
    }

    public Comment(int id,String text, long time){
        this.text = text ;
        this.time = time;
        this.id = id;
    }

    protected Comment(Parcel in) {
        tableName = in.readString();
        fields = in.createStringArray();
        text = in.readString();
        time = in.readLong();
        id = in.readInt();
        task = in.readInt();
    }


    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    public int getId() {
        return id;
    }

    @Override
    public void save(DBMgr mgr) {
        mgr.save(this);
    }

    @Override
    public DAO loadEntity(DBMgr mgr) {
        Comment comm = null;
        String query = "Select * from comments where id ="+getId();
        Cursor c = mgr.getEntity(query,null);
        if(c!=null){
            if(c.getCount()>0){
                if(c.moveToNext()){
                    comm = new Comment(c.getInt(0),c.getString(1),c.getLong(2));
                }
            }
        }
        return comm;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    @Override
    public String[] getValues() {
        String[] values = {String.valueOf(getId()),getText(),String.valueOf(getTime()),String.valueOf(task)};
        return values;
    }

    @Override
    public String[] getFields() {
        return fields;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tableName);
        dest.writeStringArray(fields);
        dest.writeString(text);
        dest.writeLong(time);
        dest.writeInt(id);
        dest.writeInt(task);
    }
}
