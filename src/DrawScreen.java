package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;


public class DrawScreen extends Canvas{
    public static int w,h;
	private int fps=0;
	
    public static Image splash = null;
	public static Image avatar = null;
	public static Image loading = null;
	public static int statusID;
	public static String status="";
	public static String userCount="";
	public static String id="";
	public static String commandText;
	public static FontClass MFont;
	public static boolean isFirstRun=true;
	public static boolean smallScreen=false;
	public static int lineHeight;

	private Vector screens = new Vector();
	private static ChatListScreen chatListScreen;
    private static ChatScreen chatScreen;
	private static Screen currentScreen;
	
	private static MidletLifecycle lifecycle;
	private static OnClientListener listener;
	
	private static Vector chats = new Vector();
	
	//private boolean loadb=false;
	private boolean loadComplete=false;
	private String errorMessage="";

	public DrawScreen(MidletLifecycle lifecycle, OnClientListener listener) {
		setFullScreenMode(true);
		w = getWidth();
		h = getHeight();if (w<176) {smallScreen=true; lineHeight=16;}else{smallScreen=false; lineHeight=32;}
		this.lifecycle = lifecycle;
		this.listener = listener;
		
		setStatus(0);
		MFont=new FontClass();
        MFont.Init("Tahoma8");
		
		try {
			splash = Image.createImage("/logo80.png");
			avatar = Image.createImage("/user.png");
			//loading = Image.createImage("/loading.png");
		} catch(Exception e) {
			System.err.println("something went wrong on DrawScreen.init()");
			e.printStackTrace();
		}
		
        chatScreen=new ChatScreen(lifecycle, listener, this);
		chatListScreen=new ChatListScreen(lifecycle, listener, this);
		goToChatList();
		
		loadComplete=true;
	}
	
	public void goToChatList(){
		currentScreen=chatListScreen;
	}
	public void goToChat(int selected){
		currentScreen=chatScreen;
		chatScreen.setChat((Chat)chats.elementAt(selected));
		//chatScreen.loadMessages();
		//chatListScreen.setLoading(false);
	}
	public Chat getChat(int x){
		return (Chat)chats.elementAt(x);
	}
	public int getChatsSize(){
		return chats.size();
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
		else if (message.indexOf("Welcome|")==0) {setStatus(200);this.id=message.substring(message.length()-5);return; }
		else if (message.indexOf("Error|")==0) {errorMessage=message.substring(6);setStatus(105);return; }
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
			if (chatScreen.getChat()==chat) chatScreen.addMessage(message);
		}
		else chats.addElement(c);
		
		repaint();
	}

	public void setStatus(int id) {
		statusID=id;
		if (statusID==0) {status="��� �����������";commandText="�����.";}
		else if (statusID==1) {status="�����������...";commandText="";}
		else if (statusID==101) status="���������� �������";
		else if (statusID==102) status="������ ����������";
		else if (statusID==103) status="103 something went wrong";
		else if (statusID==104) status="104 something went wrong";
		else if (statusID==105) status="105 "+errorMessage;
		else if (statusID==200) {status="������";commandText="���������";}
		else if (statusID==201) {status="�����������";commandText="���������";}
		else if (statusID==202) {status="��������...";commandText="���������";}
		if ((statusID>100)&(statusID<200)) commandText="�����.";
		repaint();
	}
	//public void setLoading(boolean l){
	//	this.loadb=l;
	//	repaint();
	//}
	//public boolean getLoading(){
	//	return loadb;
	//}
	private  void clearScreen(Graphics g){
        int color=g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(0,0,w,h);
        g.setColor(color);
    }
	
    public void paint(Graphics g) {
		if (isFirstRun){
			clearScreen(g);
			w = getWidth();h = getHeight();
			chatScreen.setScreenSize(w,h);
			chatListScreen.setScreenSize(w,h);
			isFirstRun=false;	
		}
		
		if (loadComplete)
			currentScreen.onDraw(g);
		
		fps++;
		MFont.drawString(g,""+fps, w-5-MFont.textWidth(""+fps),2);
		//if (loadb) g.drawImage(loading, w/2, h/2, 3);
    }

    public void keyPressed(int keyCode) {
		currentScreen.onKeyPressed(keyCode);
		repaint();
    }
}