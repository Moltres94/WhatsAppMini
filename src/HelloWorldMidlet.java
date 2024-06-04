package socket;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class HelloWorldMidlet extends MIDlet implements MidletLifecycle, OnClientListener, CommandListener {
	
    private Display disp;
	private boolean isPaused;
	private static Client client;
	private static DrawScreen mainScreen;
	private TextBox textBox;
	private final static Command CMD_SEND = new Command("OK", Command.OK, 1);
	private final static Command CMD_SETID = new Command("OK", Command.OK, 1);

	protected void startApp() {
		System.out.println("startApp");
		isPaused = false;
		if (client==null)
			client = new Client(this, this);
		if (mainScreen==null)
			mainScreen = new DrawScreen(this,this);
		disp = Display.getDisplay(this);
		disp.setCurrent(mainScreen);
		
	}
	public void startClient(){
		client.start();
	}
	
	public boolean isPaused() {
        return isPaused;
    }


	public void pauseApp(){
		System.out.println("pauseApp");
        isPaused = true;
    }

	public void quit(){
		destroyApp(true);
		notifyDestroyed();
	}
	public void authorization(){
		if (mainScreen.id.equals("")) sendMessage("Whatsapp{\"version\":1}");
		else sendMessage("Logon|"+mainScreen.id);
	}
	
	protected void destroyApp(boolean force) {

	}

	public void onStatus(int status) {
		mainScreen.setStatus(status);
	}
	public int getStatus() {
		return mainScreen.statusID;
	}

	public void onMessage(String message) {
		mainScreen.addMessage(message,1);
	}
	public void sendMessage(String message) {
		if (message.length() > 0)
			client.sendMessage(message);
	}
	public void showTextBox() {
		textBox = new TextBox("Message", "", 70, TextField.ANY);
		textBox.addCommand(CMD_SEND);
        textBox.setCommandListener(this);
		disp.setCurrent(textBox);
	}
	
	public void commandAction(Command c, Displayable d) {
        if (c == CMD_SEND) {
			client.sendMessage(textBox.getString());
            disp.setCurrent(mainScreen);
        }
    }
}