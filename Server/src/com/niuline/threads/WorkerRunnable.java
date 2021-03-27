package com.niuline.threads;

import com.niuline.models.Worker;
import com.niuline.service.WorkerService;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerRunnable implements Runnable {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    private final WorkerService workerService;
    private final Worker clientWorker;
    boolean isStopped;

    public WorkerRunnable(Integer uid, Socket clientSocket, WorkerService workerService) throws IOException {
        clientWorker = new Worker(uid, clientSocket);
        this.workerService = workerService;
    }

    public Worker getClientWorker() {
        return clientWorker;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped() {
        isStopped = true;
    }

    @Override
    public void run() {

        while (!isStopped()) {

            clientWorker.getOut().println("let me know something");
            String next = clientWorker.getIn().nextLine();
            logger.log(Level.INFO, next);

            if (next.equalsIgnoreCase("close"))
                setStopped();
        }

        clientWorker.getOut().print("close");
        this.close();
    }

    public void close() {
        try {
            this.workerService.stopWorker(this.getClientWorker());
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }

}
