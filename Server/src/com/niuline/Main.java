package com.niuline;

import com.niuline.threads.ServerRunnable;

public class Main {

    public static void main(String[] args) {

        ServerRunnable serverRunnable = new ServerRunnable( 9000);
        Thread serverThread = new Thread(serverRunnable);

        serverThread.start();

    }

}
