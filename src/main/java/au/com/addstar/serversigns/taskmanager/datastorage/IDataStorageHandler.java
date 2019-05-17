package au.com.addstar.serversigns.taskmanager.datastorage;

public interface IDataStorageHandler {
    void init()
            throws Exception;

    IDataStorageAccessor newDataStorageAccessor()
            throws Exception;

    void addTask(PersistTask paramPersistTask);

    void close();
}
