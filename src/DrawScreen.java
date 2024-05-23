package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

class Message{
	private  String id;
	private  String body;
	private  String from;
	private  int timestamp;
	
	public Message (String from, String body){
		this.from=from;
		this.body=body;
	}
	public String getId()
	{
		return id;
	}
	public String getFrom()
	{
		return from;
	}
	public String getBody()
	{
		return body;
	}
}

public class DrawScreen extends Canvas{
    private int w,h;
	private int fps=0;
    private Image splash = null;
	private String message = "Test";
	private int statusID;
	private String userCount="";
	private String status="";
	private String commandText;
	private MultiLineText MLT;
	private FontClass MFont;
	private boolean isFirstRun=true;
	
	//SE+NOKIA
	private int KEY_DOWN=-2;
	private int KEY_SOFT1=-6;
	private int KEY_SOFT2=-7;
	//MOTO
	//private int KEY_DOWN=-6;
	//private int KEY_SOFT1=-21;
	//private int KEY_SOFT2=-22;

	private Vector messages = new Vector();
	private final MidletLifecycle lifecycle;
	private final OnClientListener listener;
	private boolean scroll=false;
	
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
		MLT=new MultiLineText(MFont);
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
        g.fillRect(0,0,w,32);
		g.fillRect(0,h-14,w,14);
		g.setColor(0,0,255);
		g.drawLine(fps,0,fps,36);fps++;
		
		MFont.setColor(255,255,255,255);
		MFont.drawString(g,"WhatsApp", 5,2);
		MFont.drawString(g,""+fps, w-5-MFont.textWidth(""+fps),2);
		MFont.drawString(g,"Статус: "+status, 5,16);
		if (statusID==200) MFont.drawString(g,userCount, w-5-MFont.textWidth(userCount),16);
		MFont.drawString(g,"Выход", 5,h-14);//+4 moto
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
		
		//messages.addElement(new Message(from,message));
		MLT.addLines(from,type);
		MLT.addLines(message,0);

		int index=chats.indexOf(from);
		if (index>-1) chats.removeElementAt(index);
		chats.addElement(from);
		System.out.println(chats.toString());
		
		repaint();
	}
	
	private void sendCommand()
	{
		//addMessage("Me: Send!");
		//listener.sendMessage("Me: Send!");
		listener.showTextBox();
	}
	

	public void setStatus(int id) {
		statusID=id;
		if (statusID==0) {status="Нет подключения";commandText="Подкл.";}
		else if (statusID==1) {status="Подключение...";commandText="";}
		else if (statusID==101) status="Соединение закрыто";
		else if (statusID==102) status="Сервер недоступен";
		else if (statusID==103) status="103 something went wrong";
		else if (statusID==104) status="104 something went wrong";
		else if (statusID==200) {status="Онлайн";commandText="Отправить";}
		if ((statusID>100)&(statusID<200)) commandText="Подкл.";
		repaint();
	}

    protected void paint(Graphics g) {
		clearScreen(g);
		g.drawImage(splash, w/2, h/2, 3);
		
		//if (fps==0){w = getWidth();h = getHeight();}
		if (isFirstRun){
			w = getWidth();h = getHeight();
			MLT.SetTextPar(5,33, w-10,h-47,5,g,"start");//41 56	
			isFirstRun=false;	
		}
		MLT.DrawMultStr();//Выводим текст на экран.
		
		
		//for (int i=0;i<messages.size()-startpos;i++)
		//{
		//	Message m=(Message)messages.elementAt(i+startpos-scrollpos);
		//	String from=m.getFrom();
		//		if (from.equals("APP")) g.setColor(0, 0, 255);	
		//		else if (from.equals("SERVER")) g.setColor(127, 0, 0);	
		//		else g.setColor(0, 0, 0);
		//	g.drawString(m.getFrom(), 5,41+i*28,g.LEFT|g.TOP);
			//g.drawString(m.getBody(), 15,69+i*28,g.LEFT|g.BOTTOM); 
		//}

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

		if (keyCode==-1) MLT.MoveUp();
		else if (keyCode==KEY_DOWN) MLT.MoveDown();
		if (keyCode==35) listener.sendMessage("Следует отметить, что синтетическое тестирование влечет за собой процесс внедрения и модернизации экспериментов, поражающих по своей масштабности и грандиозности. Вот вам яркий пример современных тенденций — современная методология разработки выявляет срочную потребность системы обучения кадров, соответствующей насущным потребностям.");
		repaint();
    }
}