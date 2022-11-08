package com.github.peacetrue.lang.reflect;

/**
 * @author peace
 **/
public class EmployeeUser extends User {
    private String job;
    public Integer pay;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }
}
