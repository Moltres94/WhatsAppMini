package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class HelloWorldMidlet extends MIDlet implements MidletLifecycle, OnClientListener, CommandListener {
	
    private Display disp;
	private boolean isPaused;
	private Client client;
	private DrawScreen splashScreen;
	private TextBox textBox;
	private final static Command CMD_BACK = new Command("OK", Command.BACK, 1);

	protected void startApp() {
		isPaused = false;
		client = new Client(this, this);
		splashScreen = new DrawScreen(this,this);
		disp = Display.getDisplay(this);
		disp.setCurrent(splashScreen);
		
	}
	public void startClient(){
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

	public void onStatus(int status) {
		splashScreen.setStatus(status);
	}

	public void onMessage(String message) {
		splashScreen.addMessage(message,1);
	}
	public void sendMessage(String message) {
		if (message.length() > 0)
			client.sendMessage(message);
	}
	public void showTextBox() {
		textBox = new TextBox("Message", "", 70, TextField.ANY);
		textBox.addCommand(CMD_BACK);
        textBox.setCommandListener(this);
		disp.setCurrent(textBox);
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == CMD_BACK) {
			client.sendMessage(textBox.getString());
            disp.setCurrent(splashScreen);
        }
    }
}