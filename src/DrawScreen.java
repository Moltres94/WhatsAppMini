package socket;
import javax.microedition.lcdui.*;
 
public class DrawScreen extends Canvas{
    private int w,h;
    private Image splash = null;
	private String message = "Test";

	private  void clearScreen(Graphics g){
        int color=g.getColor();
        g.setColor(255, 255, 255);
        g.fillRect(0,0,w,h);
        g.setColor(color);
    }
	
    public DrawScreen() {
        w = getWidth();
        h = getHeight();

        try {
            splash = Image.createImage("/logo80.png");
        } catch (Exception e) {}
    }

	public void setMessage(String mes)
	{
		message=mes;
		repaint();
	}
    protected void paint(Graphics g) {
		clearScreen(g);
		g.drawString(message, 20,50,Graphics.LEFT|Graphics.BOTTOM);
        g.drawImage(splash, w/2, h/2, 3);
    }

    protected void keyPressed(int keyCode) {
        message="WhatsApp!";
		repaint();
    }
}