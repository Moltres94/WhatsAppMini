package socket;
public interface OnClientListener {
	int  getStatus();
    void onStatus(int status);
    void onMessage(String message);
	void showTextBox();
	void sendMessage(String message);
}
