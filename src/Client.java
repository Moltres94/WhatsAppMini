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
	private String response;
	private final static byte[] RN = "\n".getBytes();

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
		System.out.println("Отправка "+message);
        /* Проверяем сокет. Если он не создан или закрыт, то выдаем исключение */
        if (sc == null) {
            System.out.println("Невозможно отправить данные. Сокет не создан или закрыт");
        }
        /* Отправка данных */
        else try {
            os.write(message.getBytes("UTF-8"));
        } catch (Exception e) {
            System.err.println("couldn't send the message");
        }
    }

    public void run() {
        try {
			sc = (SocketConnection) Connector.open("socket://77.34.226.23:27030");
			sc.setSocketOption((byte) 2, 1);

            listener.onStatus(200);
            is = sc.openInputStream();
            os = sc.openOutputStream();        
			stop=false;
            // Loop forever, receiving data
			int actual;
			int sizeInt;
            do{
                

				byte[] size = new byte[2]; size[0]=0;size[1]=0;
				
				actual = is.read(size,0,2);if (actual==0) break;
				sizeInt=(int)(((size[1] & 0xFF) << 8) + ((size[0] & 0xFF) << 0));
				System.out.println("Длина сообщения "+sizeInt);
				
				long startTime = System.currentTimeMillis();
				byte[] readData = new byte[sizeInt]; 
				actual = is.read(readData,0,sizeInt);

				long endTime = System.currentTimeMillis();
				long elapsedTime = endTime - startTime;

				response = new String(readData,0,actual,"UTF-8");
                listener.onMessage(response+" "+elapsedTime+" ms");
				
				
			} while (actual!=0);
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
