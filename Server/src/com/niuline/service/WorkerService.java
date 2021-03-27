package com.niuline.service;

import com.niuline.models.Worker;
import com.niuline.threads.WorkerRunnable;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerService {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final UIDService uidService = new UIDService();
    private final Map<Worker, WorkerRunnable> workerRunnableMap = new HashMap<>();

    public void addWorker(Socket socket) {
        WorkerRunnable workerRunnable = null;
        Thread workerThread = null;
        try {
            workerRunnable = new WorkerRunnable(uidService.generateIndex(), socket, this);
            workerThread = new Thread(workerRunnable);

            workerThread.start();
            this.workerRunnableMap.put(workerRunnable.getClientWorker(), workerRunnable);
            this.logger.log(Level.INFO, "New client connected. UID = {0}.", workerRunnable.getClientWorker().getUid());
        } catch (IOException e) {
            this.logger.log(Level.WARNING, e.getMessage());
        }
    }

    public void stopWorker(Worker worker) throws IOException {
        worker.getInputStream().close();
        worker.getOutputStream().close();
        workerRunnableMap.get(worker).setStopped();
        this.logger.log(Level.INFO, "Stopped client uid = {0}", worker.getUid());
    }

    public void stopAllWorkers() throws IOException {
        for (Map.Entry<Worker, WorkerRunnable> worker : this.workerRunnableMap.entrySet()) {
            this.stopWorker(worker.getKey());
        }
    }
}
