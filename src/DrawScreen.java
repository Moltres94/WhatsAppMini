package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class DrawScreen extends Canvas{
    public static int w,h;
	private int fps=0;
    public static Image splash = null;
	public static Image avatar = null;
	public static int statusID;
	public static String status="";
	public static String userCount="";
	public static String commandText;
	public static FontClass MFont;
	public static boolean isFirstRun=true;
	public static boolean smallScreen=false;
	public static int lineHeight;
	public static int hStr;

	private Vector screens = new Vector();
	public static int ACTIVE_SCREEN;
	public static ChatListScreen chatListScreen;
    public static ChatScreen chatScreen;
	public static Screen currentScreen;
	
	public static  MidletLifecycle lifecycle;
	public static OnClientListener listener;
	
	public static Vector chats = new Vector();
	
	private boolean loadComplete=false;

	public DrawScreen(MidletLifecycle lifecycle, OnClientListener listener) {
		setFullScreenMode(true);
		w = getWidth();
		h = getHeight();if (w<176) {smallScreen=true; lineHeight=16;}else{smallScreen=false; lineHeight=32;}
		this.lifecycle = lifecycle;
		this.listener = listener;
		
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
		
		ACTIVE_SCREEN =0;
        chatScreen=new ChatScreen(lifecycle, listener, this);
		chatListScreen=new ChatListScreen(lifecycle, listener, this);
		screens.addElement(chatListScreen);
		screens.addElement(chatScreen);    
		currentScreen=(Screen)screens.elementAt(ACTIVE_SCREEN);
		
		loadComplete=true;
	}
	
	public void goToChatList(){
		ACTIVE_SCREEN =0;
		currentScreen=(Screen)screens.elementAt(ACTIVE_SCREEN);
	}
	public void goToChat(int selected){
		ACTIVE_SCREEN =1;
		currentScreen=(Screen)screens.elementAt(ACTIVE_SCREEN);
		chatScreen.setChat((Chat)chats.elementAt(selected));
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
	
	private  void clearScreen(Graphics g){
        int color=g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(0,0,w,h);
        g.setColor(color);
    }
	
    public void paint(Graphics g) {
		if (isFirstRun){
			clearScreen(g);
			w = getWidth();h = getHeight();if (w<176) {smallScreen=true; lineHeight=16;}else{smallScreen=false; lineHeight=32;}
			isFirstRun=false;	
		}
		
		if (loadComplete)
			currentScreen.onDraw(g);
		
		fps++;
		MFont.drawString(g,""+fps, w-5-MFont.textWidth(""+fps),2);
		
    }

    public void keyPressed(int keyCode) {
		currentScreen.onKeyPressed(keyCode);
		repaint();
    }
}