package com.niuline.communication;

import com.niuline.models.Worker;
import com.niuline.threads.WorkerRunnable;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WorkerCommunication {

    private final WorkerRunnable workerRunnable;
    private final Worker clientWorker;
    private final ScheduledExecutorService scheduler  = Executors.newScheduledThreadPool(1);

    public WorkerCommunication(WorkerRunnable workerRunnable) {
        this.workerRunnable = workerRunnable;
        this.clientWorker = workerRunnable.getClientWorker();
    }

    public void start() {

        while (!workerRunnable.isStopped()) {

            clientWorker.getOut().println("waiting");

            String msg = clientWorker.getIn().hasNextLine() ? clientWorker.getIn().nextLine() : "";
            Scanner msgScanner = new Scanner(msg);

            String command = msgScanner.hasNext() ? msgScanner.next() : "";
            String body = msgScanner.hasNextLine() ? msgScanner.nextLine() : "";

            if (command.equalsIgnoreCase("set")) { // notify


                msgScanner = new Scanner(body);

                int ms = msgScanner.hasNextInt() ? msgScanner.nextInt() : -1;
                String finalBody = msgScanner.hasNextLine() ? msgScanner.nextLine() : "";

                if (ms < 0) {
                    clientWorker.getOut().println("error");
                    continue;
                }

                System.out.println("[" + clientWorker.getUid() + "] notification => " + body);

                this.scheduler.schedule(() -> clientWorker.getOut().println("notification " + finalBody), ms, TimeUnit.SECONDS);

                clientWorker.getOut().println("ok");
                continue;
            }

            if (command.equalsIgnoreCase("close")) {
                workerRunnable.setStopped();
                break;
            }

            // Message not recognized

            clientWorker.getOut().println("notFound");
        }

        clientWorker.getOut().println("close");

    }
}
