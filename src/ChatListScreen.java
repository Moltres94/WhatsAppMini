package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class ChatListScreen extends Screen{

	private int lineHeight;
	private int selected=0;
	private int hStr;

	public ChatListScreen(MidletLifecycle lifecycle, OnClientListener listener, DrawScreen ds) {
		super(lifecycle, listener, ds);
		
		this.hStr=MFont.getHeight()+2;
		this.lineHeight=drawScreen.lineHeight;
	}

	private void drawHead(Graphics g){
        int color=g.getColor();
		g.setColor(1, 130, 107);
        g.fillRect(0,0,w,32);
		g.fillRect(0,h-14,w,14);
		g.setColor(0,0,255);
		
		MFont.setColor(255,255,255,255);
		MFont.drawString(g,"WhatsApp "+drawScreen.id, 5,2);
		
		MFont.drawString(g,"Статус: "+drawScreen.status, 5,16);
		if (drawScreen.statusID>=200) 
			MFont.drawString(g,drawScreen.userCount, w-5-MFont.textWidth(drawScreen.userCount),16);
		
		MFont.drawString(g,"Выход", 5,h-14);
		if (drawScreen.statusID>=200) MFont.drawString(g,"Выбор", w-5-MFont.textWidth("Выбор"), h-14);
		else if (drawScreen.statusID!=1)MFont.drawString(g,"Подкл.", w-5-MFont.textWidth("Подкл."), h-14);

        g.setColor(color);
		//MFont.Destroy();
    }
	
    public void onDraw(Graphics g) {
		super.clearScreen(g);
		g.drawImage(splash, w/2, h/2, 3);

		int color=g.getColor();
		g.setColor(240, 242, 245);
		int num;
		int startLine;
		MFont.setColor(255,0,0,0);
		for (int i=drawScreen.getChatsSize();i>0;i--)
		{		
			num=drawScreen.getChatsSize()-i;
			startLine=32+num*lineHeight;

			g.drawLine(0,startLine+lineHeight,w,startLine+lineHeight);
			if (num==selected) g.fillRect(0,startLine,w,lineHeight);
			if (!drawScreen.smallScreen)g.drawImage(avatar, 2, startLine+4, g.TOP|g.LEFT);
			
			Chat c = drawScreen.getChat(i-1);
			if (drawScreen.smallScreen) {MFont.drawString(g, c.getName(), 5,startLine+2);}
			else {
				MFont.drawString(g, c.getName(), lineHeight,startLine+2);
				String lastMessage=c.getLastMessage();
				if (lastMessage.length()>50) 
					lastMessage=lastMessage.substring(0,45);
				MFont.drawString(g, lastMessage, lineHeight,startLine+2+hStr);				
			}
		}
		g.setColor(color);
		drawHead(g);
		//if (loading) g.drawImage(loadingimg, w/2, h/2, 3);
    }

    public void onKeyPressed(int keyCode) {
		if (drawScreen.statusID<200) drawScreen.addMessage("KeyCode: "+keyCode,2);
		if (keyCode==KEY_SOFT1) lifecycle.quit();
		if (keyCode==KEY_SOFT2) 
		{
			if (drawScreen.statusID==200) drawScreen.goToChat(drawScreen.getChatsSize()-selected-1);
			else if (drawScreen.statusID!=1) {lifecycle.startClient(); drawScreen.setStatus(1);}
		}
		if (keyCode==KEY_FIRE) 
		{
			if (drawScreen.getChatsSize()>0)
			drawScreen.goToChat(drawScreen.getChatsSize()-selected-1);
		}
		
		if ((keyCode==KEY_DOWN)&(selected<drawScreen.getChatsSize()-1)) selected++;
		else if ((keyCode==KEY_UP)&(selected>0))selected--;
		
    }
}