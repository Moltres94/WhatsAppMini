package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class Client implements Runnable {

    private final MidletLifecycle lifecycle;
    private final OnClientListener listener;
	private String outMessageString;
    private boolean stop;
    private InputStream is;
    private OutputStream os;
    private SocketConnection sc;
    private Sender sender;

    public Client(MidletLifecycle lifecycle, OnClientListener listener) {
        this.lifecycle = lifecycle;
        this.listener = listener;
    }

    //Start the client thread
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            sc = (SocketConnection) Connector.open("socket://localhost:27030");
            listener.onStatus("������");
            is = sc.openInputStream();
            os = sc.openOutputStream();
            // Start the thread for sending messages
            sender = new Sender(os);
            // Loop forever, receiving data
            while (true) {
                StringBuffer sb = new StringBuffer();
                int c = 0;

                while (((c = is.read()) != '\n') && (c != -1)) {
                    sb.append((char) c);
                }

                if (c == -1) {
                    break;
                }
                listener.onMessage(sb.toString());
            }
            stop();
            listener.onStatus("���������� �������");
        } catch (ConnectionNotFoundException cnfe) {
            listener.onStatus("������ ����������");
        } catch (IOException ioe) {
            listener.onStatus("something went wrong");
            if (!stop) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            listener.onStatus("something went wrong");
            e.printStackTrace();
        }
    }


    //note: do we need this function?
    //outMessageString seems undefined
    public void sendMessage() {
        if (!lifecycle.isPaused()) {
            sender.send(outMessageString);
        }
    }

    //Close all open streams
    public void stop() {
        try {
            stop = true;
            if (sender != null) {
                sender.destroy();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (sc != null) {
                sc.close();
            }
        } catch (IOException ioe) {
            System.err.println("something went wrong Client.stop");
        }
    }
}
