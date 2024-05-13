package socket;

public interface MidletLifecycle {

    boolean isPaused();
    void quit();
	void startClient();
	void showTextBox();
}
