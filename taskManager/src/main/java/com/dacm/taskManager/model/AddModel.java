package com.dacm.taskManager.model;

import java.util.ArrayList;


public class AddModel {
    private boolean success;
    private int total;
    private int num_added;
    private int num_failed;
    private ArrayList added;
    private ArrayList failed;
    private String reason;


    public AddModel(boolean success, int total, int num_added, int num_failed, ArrayList added, ArrayList failed, String reason) {
        this.success = success;
        this.total = total;
        this.num_added = num_added;
        this.num_failed = num_failed;
        this.added = added;
        this.failed = failed;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNum_added() {
        return num_added;
    }

    public void setNum_added(int num_added) {
        this.num_added = num_added;
    }

    public int getNum_failed() {
        return num_failed;
    }

    public void setNum_failed(int num_failed) {
        this.num_failed = num_failed;
    }

    public ArrayList getAdded() {
        return added;
    }

    public void setAdded(ArrayList added) {
        this.added = added;
    }

    public ArrayList getFailed() {
        return failed;
    }

    public void setFailed(ArrayList failed) {
        this.failed = failed;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
