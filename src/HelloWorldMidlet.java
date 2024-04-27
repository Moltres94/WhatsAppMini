package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class HelloWorldMidlet extends MIDlet {
	
    public Display disp;
	private boolean isPaused;
	private Client client;
	private static HelloWorldMidlet midlet;
	
	public HelloWorldMidlet(){
		midlet=this;
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
	
	public static HelloWorldMidlet getMidlet(){
        return midlet;
    }
	  public void quit(){
        destroyApp(true);
        notifyDestroyed();
    }
	
	protected void destroyApp(boolean force) {}
}