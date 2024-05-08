package socket;
public interface OnClientListener {

    void onStatus(int status);
    void onMessage(String message);

}
