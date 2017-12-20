package com.example.lyz.asyncTasks;

/**
 * Created by Lyz on 20.12.2017.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onPreExecuting();
    public void onTaskComplete(T result);
}
