package socket;
import javax.microedition.lcdui.*;
import java.util.Vector;

public abstract class Screen{
	protected DrawScreen drawScreen;
    protected int w,h;
	protected Image splash = null;
	protected Image avatar = null;
	protected FontClass MFont;
	
	//SE+NOKIA
	protected int KEY_UP=-1;
	protected int KEY_DOWN=-2;
	protected int KEY_SOFT1=-6;
	protected int KEY_SOFT2=-7;
	protected int KEY_FIRE=-5;
	//MOTO
	//protected int KEY_UP=-1;
	//protected int KEY_DOWN=-6;
	//protected int KEY_SOFT1=-21;
	//protected int KEY_SOFT2=-22;
	//protected int KEY_FIRE=-20;
	//MOTO2
	//protected int KEY_UP=1;
	//protected int KEY_DOWN=6;
	//protected int KEY_SOFT1=21;
	//protected int KEY_SOFT2=22;
	//protected int KEY_FIRE=20;
	//SIEMENS
	//protected int KEY_DOWN=-60;
	//protected int KEY_UP=-59;
	//protected int KEY_SOFT1=-1;
	//protected int KEY_SOFT2=-4;

	protected final MidletLifecycle lifecycle;
	protected final OnClientListener listener;

	public Screen(MidletLifecycle lifecycle, OnClientListener listener,DrawScreen ds ) {
		this.lifecycle = lifecycle;
		this.listener = listener;
		this.drawScreen=ds;
		splash = DrawScreen.splash;
		avatar = DrawScreen.avatar;
		MFont = DrawScreen.MFont;
	}

	protected void clearScreen(Graphics g){
        int color=g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(0,0,w,h);
        g.setColor(color);
    }
	public void setScreenSize(int w, int h){
		this.w=w;
		this.h=h;
	}
    public void onDraw(Graphics g) {
    }

    public void onKeyPressed(int keyCode) {
		if ((keyCode==-8)||(keyCode==-10)||(keyCode==-11))   listener.sendMessage("Exit");
    }
}