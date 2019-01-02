package hossam.bs.tasks.main.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.sql.Date;

import hossam.bs.tasks.main.Helper;


public class DBMgr {
    private volatile static DBMgr mgr;
    Helper help;
    SQLiteDatabase db;

    private DBMgr(Context context){
        this.help = new Helper(context);;
        db = help.getWritableDatabase();
    }
    public static DBMgr getInstance(Context context) {
        synchronized (DBMgr.class){
            if(mgr == null){
                mgr = new DBMgr(context);
            }
        }
        return mgr;
    }

    public <T extends DAO> void save(T dao){
        Object[] values = dao.getValues();
        String[] fields = dao.getFields();
        if(values != null && fields != null){
            if(values.length == fields.length){
                String queryFields = queryInjector(fields,1);
                String queryvalues = queryInjector(values,1);
                String query = new String("insert into "+dao.getTableName()+queryFields+" values "+queryvalues+";");
                Log.i("query",query);
                try {
                    db.execSQL(query);
                }catch (Exception e){
                    Log.i("dboperation","object not saved");
                    e.printStackTrace();
                }
            }
        }
    }

    public Cursor getEntity(String query,String[] args){
        Cursor c = db.rawQuery(query,args);
        if(c!=null){
            if(c.getCount()>0){
                return c;
            }
        }
        return null;
    }

    String queryInjector(Object[] fieldsOrValues,int idIndicator){
        String fields="(";
        for(int i = idIndicator;i<fieldsOrValues.length;i++){
            if(i!=1){
                fields = fields.concat(",");
            }
            if(fieldsOrValues[i] instanceof String || fieldsOrValues[i] instanceof Date){
                fields = fields.concat("'"+fieldsOrValues[i]+"'");
            }else if(fieldsOrValues[i] instanceof Number) {
                fields = fields.concat(String.valueOf(fieldsOrValues[i]));
            }else{
                fields = fields.concat((String)fieldsOrValues[i]);
            }
        }
        fields = fields.concat(")");
        return fields;
    }

    public <T extends DAO> boolean saveOrUpdate(T entity){
        DAO record = entity.loadEntity(this);
        if(record!=null){
                if(update(entity)){
                    return true;
                }else{
                    return false;
                }
        }else{
            mgr.save(entity);
        }
        return true;
    }

    public <T extends DAO> boolean update(T entity){
        String filter = "id=?";
        ContentValues args = new ContentValues();
        String[] columns = entity.getFields();
        String[] values = entity.getValues();
        String[] whereArgs = {String.valueOf(entity.getId())};
        for(int i = 1;i<columns.length;i++){
            args.put(columns[i],values[i]);
        }
        int rowsAfftected = db.update(entity.getTableName(),args,filter,whereArgs);
        if(rowsAfftected<1){
            return false;
        }
        return true;
    }

    <T extends DAO> void deleteEntity(T entity){
        String[] args = {String.valueOf(entity.getId())};
        db.delete(entity.getTableName(),"id = ?",args);
    }


}
