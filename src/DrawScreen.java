package socket;
import javax.microedition.lcdui.*;
 
public class DrawScreen extends Canvas{
    private int w,h;
    private Image splash = null;
	private String message = "Test";
	private int statusID;
	private String status;
	private String commandText;

	private Client client;
	private final String[] messages=new String[100];
	private final int[] messageType=new int[100];
	private int messagesCount=0;
	private final MidletLifecycle lifecycle;
	private int startpos=0;
	private boolean scroll=false;

	public DrawScreen(MidletLifecycle lifecycle, Client c) {
		this.lifecycle = lifecycle;
		client=c;
		setFullScreenMode(true);
		w = getWidth();
		h = getHeight();
		setStatus(0);
		try {
			splash = Image.createImage("/logo80.png");
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
		g.drawString(commandText, w-55,h,Graphics.LEFT|Graphics.BOTTOM);
        g.setColor(color);
    }

	/**
	 * Here we could go out of screen height
	 * @param message
	 */
	public void addMessage(String message, int type) {
		messageType[messagesCount]=type;
		messages[messagesCount++]=message;
		if (messagesCount==100) {messagesCount=0;startpos=0;}
		//if ((69+(messagesCount-startpos)*14)>h-15) startpos++;
		if ((54+(messagesCount)*14)>h) startpos++;
		repaint();
	}
	
	private void sendCommand()
	{
		//addMessage("Me: Send!");
		//client.sendMessage("Me: Send!");
		lifecycle.showTextBox();
	}
	

	public void setStatus(int id) {
		statusID=id;
		if (statusID==0) {status="��� �����������";commandText="�����.";}
		else if (statusID==1) {status="�����������...";commandText="";}
		else if (statusID==101) status="���������� �������";
		else if (statusID==102) status="������ ����������";
		else if (statusID==103) status="103 something went wrong";
		else if (statusID==104) status="104 something went wrong";
		else if (statusID==200) {status="������";commandText="���������";}
		if ((statusID>100)&(statusID<200)) commandText="�����.";
		repaint();
	}

    protected void paint(Graphics g) {
		clearScreen(g);
		g.drawImage(splash, w/2, h/2, 3);
		
		for (int i=0;i<messagesCount-startpos;i++)
		{
			switch (messageType[i+startpos]) {
				case 2:		g.setColor(0, 0, 255);	break;
				case 3:		g.setColor(127, 0, 0);	break;
				case 4:		g.setColor(0, 127, 0);	break;
				default:	g.setColor(0, 0, 0);	break;
			}
			g.drawString(messages[i+startpos], 15,55+i*14,Graphics.LEFT|Graphics.BOTTOM); 
		}

		drawHead(g);
    }

    protected void keyPressed(int keyCode) {
        if (statusID<200) addMessage("KeyCode: "+keyCode,2);
		//if (statusID==200) client.sendMessage("Me: WhatsApp! "+keyCode);
		if ((keyCode==-6)|(keyCode==-21)) lifecycle.quit();
		if ((keyCode==-7)|(keyCode==-22)) 
		{
			if (statusID<200) {lifecycle.startClient(); setStatus(1);}
			else if (statusID==200) sendCommand();
		}
    }
}