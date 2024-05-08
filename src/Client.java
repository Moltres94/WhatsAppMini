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
	private final static byte[] RN = "\r\n".getBytes();

    public Client(MidletLifecycle lifecycle, OnClientListener listener) {
        this.lifecycle = lifecycle;
        this.listener = listener;
    }

    //Start the client thread
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

	public void sendData(String message){
        /* Проверяем сокет. Если он не создан или закрыт, то выдаем исключение */
        if (sc == null) {
            System.out.println("Невозможно отправить данные. Сокет не создан или закрыт");
        }
        /* Отправка данных */
        else try {
            os.write(message.getBytes());
            os.write(RN);
        } catch (IOException e) {
            System.err.println("couldn't send the message");
        }
    }

    public void run() {
        try {
            sc = (SocketConnection) Connector.open("socket://localhost:27030");
			//sc = (SocketConnection) Connector.open("socket://192.168.3.104:27030");
			//sc = (SocketConnection) Connector.open("socket://192.168.1.151:27030");
            listener.onStatus(200);
            is = sc.openInputStream();
            os = sc.openOutputStream();
			stop=false;
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
            listener.onStatus(101);
        } catch (ConnectionNotFoundException cnfe) {
            listener.onStatus(102);
        } catch (IOException ioe) {
            listener.onStatus(103);
            if (!stop) {
                ioe.printStackTrace();
            }
        } catch (Exception e) {
            listener.onStatus(104);
            e.printStackTrace();
        }
    }


    //note: do we need this function?
    //outMessageString seems undefined
    public void sendMessage(String msg) {
        if (!lifecycle.isPaused()) {
            sendData(msg);
        }
    }
	public boolean getStatus() {
        return stop;
    }

    //Close all open streams
    public void stop() {
        try {
            stop = true;
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
