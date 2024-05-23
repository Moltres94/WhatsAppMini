package socket;
/*
 * FontClass.java
 *
 *  created June 16 2008
 *  author  magdelphi
 *  magdelphi@rambler.ru
 *
 */
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.IOException;

public class FontClass {
	private byte buff[] = new byte[768];//������  ������� �������� �� ����� xxxxx.dat
	private static Image fontImage = null;
	private static int[] buf;//������ ������ �������
	private int width = 0;
	private int Color = 0;
	int h_char;//������ ��������
	int italic =0;//���� ����� �������� italic

	/** �����������  FontClass */
	public FontClass()
	{ }

	//���������� �������� ����� �� ������������ a-�����, RGB
	private int toBGR(int a, int r, int g, int b){
		return (b|(g<<8)|(r<<16)|(a<<24));
	}

    //������������� ������� ���� ����������� ���� �� ������������  a-a�����, RGB
    public void setColor(int a, int r, int g, int b){
		Color=toBGR(a,r,g,b);
    }


//*******************   ������� �� ����� ���� ������   *************************
public int drawChar(Graphics g, char c, int left, int top) {
	int result=0;
	if (fontImage != null) {
		String s=String.valueOf(c);
/*  unicode to ansi  */
		int ch = s.charAt(0) ;
		ch = ch == 0x400 ? 0xa7 : ch == 0x450 ? 0xb7 : ch;
		ch = ch > 0x400 ? ch - 0x350 : ch;
		int ind = ((int)(ch)-0x20)*3;//�������� ������ � ������� xxxxx.dat
		int len=0;//�������� � ������� xxxxx.png
		int hlen = (buff[ind+1] & 0x00ff)<<8;//������� ����
		len=(buff[ind] & 0x00ff)+hlen;  //�������� � ������� xxxxx.png
		int width_char= buff[ind+2]+italic;//������ �������
		fontImage.getRGB(buf, 0, width_char, len-2, 0,width_char, h_char);//������� � �����
		
		for(int i=0;i<buf.length;i++)
		{
			int color = (buf[i] &0x00ffffff);//������ ������ RGB
			if (color == 0) color =  Color;//���� ������ ������ � ����
			buf[i] = color;
		}
		
		g.drawRGB(buf, 0, width_char, left, top, width_char, h_char, true);
		if (c==' '){width_char=h_char>>2;}//���� ������
			result=width_char;
		}
		return result;
    }
	//*******************   ������ �������  *************************
	public int widthChar(char c) {
		String s=String.valueOf(c);
		int ch = s.charAt(0) ;
			ch = ch == 0x400 ? 0xa7 : ch == 0x450 ? 0xb7 : ch;
			ch = ch > 0x400 ? ch - 0x350 : ch;
			int ind = ((int)(ch)-0x20)*3;//�������� ������ � ������� xxxxx.dat
			int width_char= buff[ind+2]+italic;//������ �������
			if (c==' '){width_char=h_char>>2;}//���� ������
		return width_char;
	}
//******************************************************************************
	public int textWidth(String s) {
		int len = 0;
		if (s.length()>0){
			int w = 0;
			for (int i = 0; i < s.length(); i++) {
				w=widthChar(s.charAt(i));
				len=len+w;
			}
		}
		return len;
    }
	public int getHeight(){
		return h_char;
	}
//*****   ������� ������ ��������  **********************************************
    public void drawString(Graphics g, String s, int left, int top) {
        int len = left;
        int w = 0;
        int max_width =g.getClipWidth();
        for (int i = 0; i < s.length(); i++) {
//          if s.charAt(i) == " "
            w=drawChar(g, s.charAt(i), len, top);
            len=len+w;
//            if (len >= max_width-1) return;
        }
        width=len;
    }

//**************   �������������, �������� �������� � �������   ****************
    public void Init(String name_font){
        try {//-----  �������� image ��������  ---------------
			fontImage = Image.createImage("/"+name_font+".png");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
//*************   �������� ������� ������������ ��������  *********************
        InputStream is = getClass().getResourceAsStream("/"+name_font+".dat");
         //System.out.println("f2s8.dat ���������");
        int off = 0;
        int readBytes = 0;
		int n_buf;
        try
        {
			while ( (readBytes = is.read(buff, off, buff.length)) > -1) {}//�������� � �����
			h_char=buff[0];//������ ��������
			System.out.println("H char: " + h_char);
			if  (buff[1] ==1) {italic=h_char/4;}//���� fontstyle = [italic] ����������� ������ �������
        }
        catch (Exception e)
        {
			System.out.println("Exception: " + e.toString());
        }

		n_buf =h_char*h_char;// ���-�� ���� 1 ����������
		//System.out.println(n_buf);
		buf = new int[n_buf];
    }

    //�������� ��������
    public void Destroy(){
        buff = null;
        buf = null;
        fontImage = null;
    }





}
