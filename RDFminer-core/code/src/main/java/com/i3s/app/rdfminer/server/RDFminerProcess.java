package com.i3s.app.rdfminer.server;

import com.i3s.app.rdfminer.Global;

import java.util.HashMap;
import java.util.Map;

/**
 * RDFminer-server activity management
 * This is a singleton
 * @author Rémi Felin
 */
public class RDFminerProcess {

    private static RDFminerProcess instance;

    private Map<String, Thread> runningProcesses;

    private RDFminerProcess() {
        this.runningProcesses = new HashMap<>();
    }

    public boolean setProcess(String projectName, Thread thread) {
        if (this.hasConflict(projectName) || this.runningProcesses.keySet().size() >= Global.MAX_SIMULTANEOUS_EXEC) {
            return false;
        }
        this.runningProcesses.put(projectName, thread);
        return true;
    }

    public Thread getThread(String projectName) {
        return this.runningProcesses.get(projectName);
    }

    public void startThread(String userID) {
        Thread toExec = this.getThread(userID);
        MyLogger.info("Starting task (id: " + toExec.getId() + ") ...");
        toExec.start();
        try {
            toExec.join();
        } catch (InterruptedException e) {
            MyLogger.error("error during process starting: (id: " + toExec.getId() + ") ...");
            MyLogger.error(e.getMessage());
        } finally {
            MyLogger.info("clean processes ...");
            this.runningProcesses.remove(userID);
        }
    }

    public boolean killProcess(String projectName) {
        Thread tokill = this.runningProcesses.get(projectName);
        if (tokill != null) {
            tokill.interrupt();
            try {
                // Wait for the thread to complete
                tokill.join();
            } catch (InterruptedException e) {
                MyLogger.error("error during process shutdown: (id: " + tokill.getId() + ") ...");
                MyLogger.error(e.getMessage());
                return false;
            }
            // clean runningProcesses ...
            this.runningProcesses.remove(projectName);
            return true;
        } else {
            MyLogger.warn("project: " + projectName + " is not running ...");
            return true;
        }
    }

    private boolean hasConflict(String projectName) {
        if (this.runningProcesses.containsKey(projectName)) {
            MyLogger.warn("project: " + projectName + " -> conflict (multi-exec)");
            return true;
        }
        return false;
    }

    public static synchronized RDFminerProcess getInstance() {
        if (instance == null) {
            instance = new RDFminerProcess();
        }
        return instance;
    }

}
