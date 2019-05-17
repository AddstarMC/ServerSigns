package au.com.addstar.serversigns.taskmanager;

import java.util.List;

public interface ITaskExecutor<E> {
    void runTasks(List<? extends E> paramList);

    void runTask(E paramE);
}
