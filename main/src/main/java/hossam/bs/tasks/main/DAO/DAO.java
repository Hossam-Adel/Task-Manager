package hossam.bs.tasks.main.DAO;

public interface DAO {
     String[] getValues();
     String[] getFields();
     String getTableName();
     int getId();
     void save(DBMgr mgr);
     DAO loadEntity(DBMgr mgr);
}
