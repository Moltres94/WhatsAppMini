package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class HelloWorldMidlet extends MIDlet {
	
    public Display disp;
	private boolean isPaused;
	private Client client;
	
	public HelloWorldMidlet(){
	}
	protected void startApp() {
		isPaused = false;
		client = new Client(this);
                client.start();
	}
	public boolean isPaused() {
        return isPaused;
    }

    public void pauseApp() {
        isPaused = true;
    }
	protected void destroyApp(boolean force) {}
}