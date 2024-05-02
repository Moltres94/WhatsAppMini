package socket;

import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class Sender extends Thread {

    private final static byte[] RN = "\r\n".getBytes();
    private final OutputStream os;
    private String message;

    public Sender(OutputStream os) {
        this.os = os;
        start();
    }

    public synchronized void send(String msg) {
        message = msg;
        notify();
    }

    public synchronized void run() {
        while (true) {
            // If no client to deal, wait until one connects
            if (message == null) {
                try {
                    wait();
                } catch (InterruptedException exception) {
                    System.err.println("thread was unexpectedly interrupted");
                    close();
                    break;
                }
            }
            if (message == null) {
                break;
            }
            try {
                os.write(message.getBytes());
                os.write(RN);
            } catch (IOException exception) {
                System.err.println("couldn't send the message");
                exception.printStackTrace();
            }
            // Completed client handling, return handler to pool and
            // mark for wait
            message = null;
        }
    }

    /**
     * stop is a final method of Thread class
     * so we should use another name, like destroy
     */
    public synchronized void destroy() {
        message = null;
        notify();
        close();
    }

    private void close() {
        if (this.os != null) {
            System.err.println("closing resources");
            try {
                this.os.close();
            } catch (IOException exception) {
                System.err.println("couldn't close OutputStream");
                exception.printStackTrace();
            }
        }
    }

}

