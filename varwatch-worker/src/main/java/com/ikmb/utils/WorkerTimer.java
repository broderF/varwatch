/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikmb.utils;

/**
 *
 * @author broder
 */
public class WorkerTimer {

    private long runningTime;
    private long startTime;

    private long jobStartTime;

    public void startJob() {
        jobStartTime = System.currentTimeMillis();
    }

    public Long stopJob() {
        return System.currentTimeMillis() - jobStartTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public boolean isFinish() {
        long currentTime = System.currentTimeMillis();
        long dif = currentTime - startTime;
        if (dif > runningTime) { //5 min erstmal
            return true;
        } else {
            return false;
        }
    }

    public void reset() {

    }
}
