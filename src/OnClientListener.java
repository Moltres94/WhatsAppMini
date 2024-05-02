package socket;
public interface OnClientListener {

    void onStatus(String status);
    void onMessage(String message);

}
