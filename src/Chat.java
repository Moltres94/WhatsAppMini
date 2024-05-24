package socket;
import java.util.Vector;

public class Chat {
	private String name;
	private Vector messages = new Vector();
	
	public String getName()
	{
		return this.name;
	}
	public String getLastMessage()
	{
		return (String)messages.elementAt(messages.size()-1);
	}

	public void addMessage(String message)
	{
		messages.addElement(message);
	}
	
	public Chat(String name, String message) {
		this.name=name;
		addMessage(message);
	}
	public boolean equals(Object obj)
	{
	   if (this == obj) return true;
	   if (obj == null) return false;
	   if (!(obj instanceof Chat)) return false;

	   Chat chat = (Chat) obj;
	   return this.name.equals(chat.name);
	}
}
