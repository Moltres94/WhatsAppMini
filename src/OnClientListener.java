package socket;
public interface OnClientListener {

    void onStatus(int status);
    void onMessage(String message);
	void showTextBox();
	void sendMessage(String message);
}
