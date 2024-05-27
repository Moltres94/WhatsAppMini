package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class ChatListScreen extends Screen{

	private int lineHeight;
	private int selected=0;
	private Vector chats = new Vector();

	public ChatListScreen(MidletLifecycle lifecycle, OnClientListener listener, DrawScreen ds) {
		super(lifecycle, listener, ds);
		
		this.lineHeight=drawScreen.lineHeight;
		this.chats=drawScreen.chats;
	}

	private void drawHead(Graphics g){
        int color=g.getColor();
		g.setColor(1, 130, 107);
        g.fillRect(0,0,w,32);
		g.fillRect(0,h-14,w,14);
		g.setColor(0,0,255);
		
		MFont.setColor(255,255,255,255);
		MFont.drawString(g,"WhatsApp", 5,2);
		MFont.drawString(g,"������: "+drawScreen.status, 5,16);
		if (drawScreen.statusID==200) MFont.drawString(g,drawScreen.userCount, w-5-MFont.textWidth(drawScreen.userCount),16);
		MFont.drawString(g,"�����", 5,h-14);//+4 moto
		if (drawScreen.statusID==200) MFont.drawString(g,"�����", w-5-MFont.textWidth(drawScreen.commandText), h-14);
		if (drawScreen.statusID==0)MFont.drawString(g,"�����.", w-5-MFont.textWidth(drawScreen.commandText), h-14);

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
		for (int i=chats.size();i>0;i--)
		{		
			num=chats.size()-i;
			startLine=32+num*lineHeight;

			g.drawLine(0,startLine+lineHeight,w,startLine+lineHeight);
			if (num==selected) g.fillRect(0,startLine,w,lineHeight);
			if (!drawScreen.smallScreen)g.drawImage(avatar, 2, startLine+4, g.TOP|g.LEFT);
			
			Chat c = (Chat)chats.elementAt(i-1);
			if (drawScreen.smallScreen) {MFont.drawString(g, c.getName(), 5,startLine+2);}
			else {
				MFont.drawString(g, c.getName(), lineHeight,startLine+2);
				if (!drawScreen.smallScreen) MFont.drawString(g, c.getLastMessage(), lineHeight,startLine+2+drawScreen.hStr);
			}
		}
		g.setColor(color);
		drawHead(g);
    }

    public void onKeyPressed(int keyCode) {
		if (keyCode==KEY_SOFT1) lifecycle.quit();
		if (keyCode==KEY_SOFT2) 
		{
			if (drawScreen.statusID<200) {lifecycle.startClient(); drawScreen.setStatus(1);}
			else if (drawScreen.statusID==200) drawScreen.goToChat(chats.size()-selected-1);
		}
		
		if ((keyCode==KEY_DOWN)&(selected<chats.size()-1)) selected++;
		else if ((keyCode==KEY_UP)&(selected>0))selected--;
		
    }
}