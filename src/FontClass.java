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
	private byte buff[] = new byte[768];//данные  таблицы символов из файла xxxxx.dat
	private static Image fontImage = null;
	private static int[] buf;//данные одного символа
	private int width = 0;
	private int Color = 0;
	int h_char;//высота символов
	int italic =0;//флаг стиля символов italic

	/** конструктор  FontClass */
	public FontClass()
	{ }

	//Возвращает значение цвета из составляющих a-фльфа, RGB
	private int toBGR(int a, int r, int g, int b){
		return (b|(g<<8)|(r<<16)|(a<<24));
	}

    //Устанавливает текущий цвет отображения букв по составляющим  a-aфльфа, RGB
    public void setColor(int a, int r, int g, int b){
		Color=toBGR(a,r,g,b);
    }


//*******************   Выводит на экран один символ   *************************
public int drawChar(Graphics g, char c, int left, int top) {
	int result=0;
	if (fontImage != null) {
		String s=String.valueOf(c);
/*  unicode to ansi  */
		int ch = s.charAt(0) ;
		ch = ch == 0x400 ? 0xa7 : ch == 0x450 ? 0xb7 : ch;
		ch = ch > 0x400 ? ch - 0x350 : ch;
		int ind = ((int)(ch)-0x20)*3;//смещение данных в таблице xxxxx.dat
		int len=0;//смещение в таблице xxxxx.png
		int hlen = (buff[ind+1] & 0x00ff)<<8;//старший байт
		len=(buff[ind] & 0x00ff)+hlen;  //смещение в таблице xxxxx.png
		int width_char= buff[ind+2]+italic;//ширина символа
		fontImage.getRGB(buf, 0, width_char, len-2, 0,width_char, h_char);//считать в буфер
		
		for(int i=0;i<buf.length;i++)
		{
			int color = (buf[i] &0x00ffffff);//читаем только RGB
			if (color == 0) color =  Color;//если черный красим в цвет
			buf[i] = color;
		}
		
		g.drawRGB(buf, 0, width_char, left, top, width_char, h_char, true);
		if (c==' '){width_char=h_char>>2;}//если пробел
			result=width_char;
		}
		return result;
    }
	//*******************   ширина символа  *************************
	public int widthChar(char c) {
		String s=String.valueOf(c);
		int ch = s.charAt(0) ;
			ch = ch == 0x400 ? 0xa7 : ch == 0x450 ? 0xb7 : ch;
			ch = ch > 0x400 ? ch - 0x350 : ch;
			int ind = ((int)(ch)-0x20)*3;//смещение данных в таблице xxxxx.dat
			int width_char= buff[ind+2]+italic;//ширина символа
			if (c==' '){width_char=h_char>>2;}//если пробел
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
//*****   Выводит строку символов  **********************************************
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

//**************   Инициализация, создание объектов и буферов   ****************
    public void Init(String name_font){
        try {//-----  загрузка image символов  ---------------
			fontImage = Image.createImage("/"+name_font+".png");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
//*************   загрузка таблицы расположения символов  *********************
        InputStream is = getClass().getResourceAsStream("/"+name_font+".dat");
         //System.out.println("f2s8.dat загружено");
        int off = 0;
        int readBytes = 0;
		int n_buf;
        try
        {
			while ( (readBytes = is.read(buff, off, buff.length)) > -1) {}//копируем в буфер
			h_char=buff[0];//высота символов
			System.out.println("H char: " + h_char);
			if  (buff[1] ==1) {italic=h_char/4;}//если fontstyle = [italic] увеличиваем ширину символа
        }
        catch (Exception e)
        {
			System.out.println("Exception: " + e.toString());
        }

		n_buf =h_char*h_char;// кол-во байт 1 знакоместо
		//System.out.println(n_buf);
		buf = new int[n_buf];
    }

    //Удаление объектов
    public void Destroy(){
        buff = null;
        buf = null;
        fontImage = null;
    }





}
