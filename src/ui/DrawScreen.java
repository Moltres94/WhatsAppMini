package socket.ui;
import socket.io.Client;
import socket.MidletLifecycle;

import javax.microedition.lcdui.*;
 
public class DrawScreen extends Canvas implements CommandListener{
    private int w,h;
    private Image splash = null;
	private String message = "Test";
	private String status = "��� �����������";
	private Command CMND_Send = new Command("Send",Command.ITEM, 1);
	private Command CMND_Exit = new Command("�����",Command.EXIT, 1);
	
	private Client client;
	private final String[] messages=new String[100];
	private int messagesCount=0;
	private final MidletLifecycle lifecycle;

	public DrawScreen(MidletLifecycle lifecycle, Client c) {
		this.lifecycle = lifecycle;
		client=c;
		setFullScreenMode(true);
		w = getWidth();
		h = getHeight();
		try {
			splash = Image.createImage("/logo80.png");
			addCommand(CMND_Exit);
			addCommand(CMND_Send);
			setCommandListener(this);
		} catch(Exception e) {
			System.err.println("something went wrong on DrawScreen.init()");
			e.printStackTrace();
		}
	}

	private  void clearScreen(Graphics g){
        int color=g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(0,0,w,h);
        g.setColor(color);
    }
	private void drawHead(Graphics g){
        int color=g.getColor();
		g.setColor(1, 130, 107);
        g.fillRect(0,0,w,36);
		g.fillRect(0,h-15,w,15);
		
		g.setColor(255, 255, 255);
		g.drawString("WhatsApp", 15,18,Graphics.LEFT|Graphics.BOTTOM);
		g.drawString("������: "+status, 15,32,Graphics.LEFT|Graphics.BOTTOM);
		g.drawString("�����", 15,h,Graphics.LEFT|Graphics.BOTTOM);
        g.setColor(color);
    }

	/**
	 * Here we could go out of screen height
	 * @param message
	 */
	private void addMessage(String message) {
		messages[messagesCount++]=message;
		repaint();
	}
	
	public void commandAction(Command command, Displayable displayable) {
        if (command==CMND_Exit){
			lifecycle.quit();
        }
		if (command==CMND_Send){
            addMessage("Me: Send!");
			client.sendMessage("Me: Send!");
        }
    }
	public void printMessage(String mes) {
		addMessage(mes);
	}

	public void setStatus(String mes) {
		status=mes;
		repaint();
	}

    protected void paint(Graphics g) {
		clearScreen(g);
		for (int i=0;i<messagesCount;i++)
			g.drawString(messages[i], 15,55+i*14,Graphics.LEFT|Graphics.BOTTOM);
        g.drawImage(splash, w/2, h/2, 3);
		drawHead(g);
    }

    protected void keyPressed(int keyCode) {
        addMessage("Me: WhatsApp!");
		client.sendMessage("Me: WhatsApp!");
    }
}