package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;
//import com.motorola.multimedia.Lighting;

public class ChatScreen extends Screen{
	
	private MultiLineText MLT;
	private Chat chat;
	private String name="null";

	public ChatScreen(MidletLifecycle lifecycle, OnClientListener listener, DrawScreen ds) {
		super(lifecycle, listener, ds);
		
		MLT=new MultiLineText(MFont);
	}
	public void setChat(Chat c)
	{
		this.chat=c;
		this.name=c.getName();
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
		
		MFont.drawString(g,"Статус: "+drawScreen.status, 5,16);
		if (drawScreen.statusID==200) MFont.drawString(g,drawScreen.userCount, w-5-MFont.textWidth(drawScreen.userCount),16);
		MFont.drawString(g,"Назад", 5,h-14);
		if (drawScreen.statusID==200) MFont.drawString(g,"Отправ.", w-5-MFont.textWidth(drawScreen.commandText), h-14);MFont.drawString(g,drawScreen.commandText, w-5-MFont.textWidth(drawScreen.commandText), h-14);

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
		
		if (drawScreen.isFirstRun) MLT.SetTextPar(5,33, w-10,h-47,5,g,"start");//41 56	
		
		//MLT.DrawMultStr();//Выводим текст на экран.

		drawHead(g);
    }

    public void onKeyPressed(int keyCode) {
        if (drawScreen.statusID<200) drawScreen.addMessage("KeyCode: "+keyCode,2);
		if (keyCode==KEY_SOFT1) drawScreen.goToChatList();
		if (keyCode==KEY_SOFT2) 
		{
			if (drawScreen.statusID==200) sendCommand();
		}

		if (keyCode==KEY_UP) MLT.MoveUp();
		else if (keyCode==KEY_DOWN) MLT.MoveDown();
		if (keyCode==35) listener.sendMessage("Следует отметить, что синтетическое тестирование влечет за собой процесс внедрения и модернизации экспериментов, поражающих по своей масштабности и грандиозности. Вот вам яркий пример современных тенденций — современная методология разработки выявляет срочную потребность системы обучения кадров, соответствующей насущным потребностям.");
    }
}