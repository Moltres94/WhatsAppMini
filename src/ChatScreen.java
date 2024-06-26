package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;
//import com.motorola.multimedia.Lighting;


public class ChatScreen extends Screen{
	
	private MultiLineText MLT;
	private Chat chat;
	private String name="null";
	private boolean isFirstRun=true;
	private Vector messages;

	public ChatScreen(MidletLifecycle lifecycle, OnClientListener listener, DrawScreen ds) {
		super(lifecycle, listener, ds);
		
		MLT=new MultiLineText(MFont);
	}
	public void setChat(Chat c)
	{
		isFirstRun=true;
		this.chat=c;
		this.name=c.getName();
	}
	public Chat getChat()
	{
		return chat;
	}
	public void loadMessages()
	{
		messages=chat.getMessages();
		if ((messages!=null)&&(messages.size()>0)){
			MLT.clearLines();
			for (int i=0;i<messages.size();i++)
			{
				MLT.addLines((String)messages.elementAt(i),0);
			}
		}
	}
	public void addMessage(String m)
	{
		MLT.addLines(m,0);
	}

	private void drawHead(Graphics g){
        int color=g.getColor();
		g.setColor(1, 130, 107);
        g.fillRect(0,0,w,32);
		g.fillRect(0,h-14,w,14);
		g.setColor(0,0,255);
		
		MFont.setColor(255,255,255,255);
		if (chat!=null)
			MFont.drawString(g,name, 5,2);
		
		MFont.drawString(g,"������: "+drawScreen.status, 5,16);
		if (drawScreen.statusID==200) MFont.drawString(g,drawScreen.userCount, w-5-MFont.textWidth(drawScreen.userCount),16);
		MFont.drawString(g,"�����", 5,h-14);
		if (drawScreen.statusID==200) MFont.drawString(g,"������.", w-5-MFont.textWidth("������."), h-14);
		//MFont.drawString(g,drawScreen.commandText, w-5-MFont.textWidth(drawScreen.commandText), h-14);

        g.setColor(color);
		//MFont.Destroy();
    }
	
	private void sendCommand()
	{
		//addMessage("Me: Send!");
		//listener.sendMessage("Me: Send!");
		listener.showTextBox();
	}

    public void onDraw(Graphics g) {
		super.clearScreen(g);
		g.drawImage(splash, w/2, h/2, 3);
		
		if (isFirstRun) {MLT.SetTextPar(5,33, w-10,h-47,5,g,"(�����)"); isFirstRun=false;loadMessages();}
		
		MLT.DrawMultStr();//������� ����� �� �����.

		drawHead(g);
		//if (loading) 
    }

    public void onKeyPressed(int keyCode) {
		super.onKeyPressed(keyCode);
		if (keyCode==KEY_SOFT1) drawScreen.goToChatList();
		if (keyCode==KEY_SOFT2) 
		{
			if (drawScreen.statusID==200) sendCommand();
		}

		if (keyCode==KEY_UP) MLT.MoveUp();
		else if (keyCode==KEY_DOWN) MLT.MoveDown();//� � - �
		if (keyCode==35) listener.sendMessage("Whatsapp{\"version\":1}");
		if (keyCode==42) listener.sendMessage("Logon|24749");
		
		//if (keyCode==35) listener.sendMessage("������� ��������, ��� ������������� ������������ ������ �� ����� ������� ��������� � ������������ �������������, ���������� �� ����� ������������ � �������������. ��� ��� ����� ������ ����������� ��������� ����������� ����������� ���������� �������� ������� ����������� ������� �������� ������, ��������������� �������� ������������.");
    }
}