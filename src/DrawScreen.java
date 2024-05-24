package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class DrawScreen extends Canvas{
    private int w,h;
	private int fps=0;
    private Image splash = null;
	private Image avatar = null;
	private String message = "Test";
	private int statusID;
	private String userCount="";
	private String status="";
	private String commandText;
	private MultiLineText MLT;
	private FontClass MFont;
	private boolean isFirstRun=true;
	private boolean smallScreen=false;
	private int hStr;
	private int lineHeight;
	
	//SE+NOKIA
	private int KEY_UP=-1;
	private int KEY_DOWN=-2;
	private int KEY_SOFT1=-6;
	private int KEY_SOFT2=-7;
	//MOTO
	//private int KEY_UP=-1;
	//private int KEY_DOWN=-6;
	//private int KEY_SOFT1=-21;
	//private int KEY_SOFT2=-22;
	//SIEMENS
	//private int KEY_DOWN=-60;
	//private int KEY_UP=-59;
	//private int KEY_SOFT1=-1;
	//private int KEY_SOFT2=-4;

	private final MidletLifecycle lifecycle;
	private final OnClientListener listener;
	private int selected=0;
	//private boolean scroll=false;
	
	private Vector chats = new Vector();

	public DrawScreen(MidletLifecycle lifecycle, OnClientListener listener) {
		this.lifecycle = lifecycle;
		this.listener = listener;
		setFullScreenMode(true);
		w = getWidth();
		h = getHeight();
		setStatus(0);
		MFont=new FontClass();
        MFont.Init("Tahoma8");
		hStr=MFont.getHeight()+2;
		
		try {
			splash = Image.createImage("/logo80.png");
			avatar = Image.createImage("/user.png");
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
        g.fillRect(0,0,w,32);
		g.fillRect(0,h-14,w,14);
		g.setColor(0,0,255);
		//g.drawLine(fps,0,fps,36);
		fps++;
		
		MFont.setColor(255,255,255,255);
		MFont.drawString(g,"WhatsApp", 5,2);
		MFont.drawString(g,""+fps, w-5-MFont.textWidth(""+fps),2);
		MFont.drawString(g,"������: "+status, 5,16);
		if (statusID==200) MFont.drawString(g,userCount, w-5-MFont.textWidth(userCount),16);
		MFont.drawString(g,"�����", 5,h-14);//+4 moto
		MFont.drawString(g,commandText, w-5-MFont.textWidth(commandText), h-14);

        g.setColor(color);
		//MFont.Destroy();
    }

	/**
	 * Here we could go out of screen height
	 * @param message
	 */
	public void addMessage(String message, int type) {
		String from="none";
		System.out.println(message);
		if (message.indexOf("MSG|")==0) {
			int split=message.indexOf('|',4);
			int split2=message.indexOf('@');
			if (split>4) {
				if (split2>4) {from=message.substring(4,split2); message=message.substring(split+1);}
				else {from=message.substring(4,split); message=message.substring(split+1);}
			} 
			else {type=2; message="parse error";} 
		}
		else if (message.indexOf("SYS|UCOUNT|")==0) {message=message.substring(11);userCount=message;repaint();return; }
		else if (message.indexOf("INF|")==0) {message=message.substring(4);type=3;}
		
		if (type==2) from="APP";
		if (type==3) from="SERVER";

		Chat c=new Chat(from,message);
		int index=chats.indexOf(c);
		
		if (index>-1) {
			Chat chat=(Chat)chats.elementAt(index);
			chat.addMessage(message);
			chats.removeElementAt(index);
			chats.addElement(chat);
		}
		else chats.addElement(c);
		System.out.println(chats.toString());
		
		repaint();
	}
	
	private void sendCommand()
	{
		listener.showTextBox();
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
		
		if (isFirstRun){
			w = getWidth();h = getHeight();if (w<176) {smallScreen=true; lineHeight=16;}else{smallScreen=false; lineHeight=32;}
			isFirstRun=false;	
		}
		
		int color=g.getColor();
		g.setColor(240, 242, 245);
		int num;
		int startLine;
		MFont.setColor(255,0,0,0);
		for (int i=chats.size();i>0;i--)
		{		
			num=chats.size()-i;
			startLine=32+num*lineHeight;

			g.drawLine(0,startLine+lineHeight,w,startLine+lineHeight);
			if (num==selected) g.fillRect(0,startLine,w,lineHeight);
			if (!smallScreen)g.drawImage(avatar, 2, startLine+4, g.TOP|g.LEFT);
			
			Chat c = (Chat)chats.elementAt(i-1);
			if (smallScreen) {MFont.drawString(g, c.getName(), 5,startLine+2);}
			else {
				MFont.drawString(g, c.getName(), lineHeight,startLine+2);
				if (!smallScreen) MFont.drawString(g, c.getLastMessage(), lineHeight,startLine+2+hStr);
			}
		}
		g.setColor(color);
		drawHead(g);
    }

    protected void keyPressed(int keyCode) {
        if (statusID<200) addMessage("KeyCode: "+keyCode,2);
		if (keyCode==KEY_SOFT1) lifecycle.quit();
		if (keyCode==KEY_SOFT2) 
		{
			if (statusID<200) {lifecycle.startClient(); setStatus(1);}
			else if (statusID==200) sendCommand();
		}

		if ((keyCode==KEY_DOWN)&(selected<chats.size()-1)) selected++;
		else if ((keyCode==KEY_UP)&(selected>0))selected--;
		if (keyCode==35) listener.sendMessage("������� ��������, ��� ������������� ������������ ������ �� ����� ������� ��������� � ������������ �������������, ���������� �� ����� ������������ � �������������. ��� ��� ����� ������ ����������� ��������� � ����������� ����������� ���������� �������� ������� ����������� ������� �������� ������, ��������������� �������� ������������.");
		repaint();
    }
}