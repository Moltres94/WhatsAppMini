package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;


public class Client implements Runnable {
    private HelloWorldMidlet parent;
    private Display disp;
	public static DrawScreen splash_screen;
	private String receivedString;
	private String statusString;
	private String outMessageString;
    private boolean stop;
    InputStream is;
    OutputStream os;
    SocketConnection sc;
    Sender sender;

    public Client(HelloWorldMidlet m) {
        parent = m;
		splash_screen = new DrawScreen();
        disp = Display.getDisplay(parent);
        statusString = "Status: ";
        disp.setCurrent(splash_screen);
    }

    //Start the client thread
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            sc = (SocketConnection) Connector.open("socket://localhost:27030");
            statusString = "Status: Connected to server";
			splash_screen.setMessage("Status: Connected to server");
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

                statusString = "Status: Message received - " + sb.toString();
				splash_screen.setMessage("Status: Message received - " + sb.toString());
            }
            stop();
            statusString = "Status: Connection closed";
			splash_screen.setMessage("Status: Connection closed");
        } catch (ConnectionNotFoundException cnfe) {
			splash_screen.setMessage("Server not found");          
        } catch (IOException ioe) {
            if (!stop) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        if (!parent.isPaused()) {
            sender.send(outMessageString);
        }
    }

    //Close all open streams
    public void stop() {
        try {
            stop = true;

            if (sender != null) {
                sender.stop();
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
        } catch (IOException ioe) {}
    }
}
