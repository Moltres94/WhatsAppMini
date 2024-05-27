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
	//MOTO
	//protected int KEY_UP=-1;
	//protected int KEY_DOWN=-6;
	//protected int KEY_SOFT1=-21;
	//protected int KEY_SOFT2=-22;
	//SIEMENS
	//protected int KEY_DOWN=-60;
	//protected int KEY_UP=-59;
	//protected int KEY_SOFT1=-1;
	//protected int KEY_SOFT2=-4;

	protected final MidletLifecycle lifecycle;
	protected final OnClientListener listener;

	public Screen(MidletLifecycle lifecycle, OnClientListener listener,DrawScreen ds ) {
		this.lifecycle = DrawScreen.lifecycle;
		this.listener = DrawScreen.listener;
		this.drawScreen=ds;
		w = DrawScreen.w;
		h = DrawScreen.h;
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
	
    public void onDraw(Graphics g) {
    }

    public void onKeyPressed(int keyCode) {
    }
}