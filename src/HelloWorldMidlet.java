package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class HelloWorldMidlet extends MIDlet implements MidletLifecycle, OnClientListener {
	
	private boolean isPaused;
	private Client client;
	private DrawScreen splashScreen;

	protected void startApp() {
		isPaused = false;
		client = new Client(this, this);
		splashScreen = new DrawScreen(this,client);
		disp = Display.getDisplay(this);
		disp.setCurrent(splashScreen);
		client.start();
	}

	public boolean isPaused() {
        return isPaused;
    }


	public void pauseApp() {
        isPaused = true;
    }

	public void quit(){
		destroyApp(true);
		notifyDestroyed();
	}
	
	protected void destroyApp(boolean force) {

	}

	public void onStatus(String status) {
		splashScreen.setStatus(status);
	}

	public void onMessage(String message) {
		splashScreen.printMessage(message);
	}
}