package com.niuline.threads;

import com.niuline.service.WorkerService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRunnable implements Runnable {

    WorkerService workerService;
    ServerSocket server;
    boolean isStopped;
    int serverPort;

    public ServerRunnable(int serverPort) {
        this.isStopped = false;
        this.serverPort = serverPort;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped() {
        isStopped = true;
    }

    @Override
    public void run() {

        try {
            this.server = new ServerSocket(this.serverPort);
            this.workerService = new WorkerService();

            System.out.println("Server started\nWaiting for clients");
            this.waitForClients();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.setStopped();
        }

    }

    public void waitForClients() {
        while (!isStopped()) {
            try {
                Socket socket = this.server.accept();
                this.workerService.addWorker(socket);
            } catch (IOException e) {
                this.killAll();

                if (isStopped()) {
                    System.out.println("Served Stopped.");
                    return;
                }

                throw new RuntimeException("Error accepting client connection", e);
            }
        }
        this.killAll();
    }

    public void killAll() {
        try {
            this.workerService.stopAllWorkers();
            this.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
