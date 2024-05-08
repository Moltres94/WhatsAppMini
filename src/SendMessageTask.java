package socket;

import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.*;

public class SendMessageTask implements Runnable {

    private final static byte[] RN = "\r\n".getBytes();

    private final OutputStream os;
    private final SocketConnection sc;
    private final String message;

    public SendMessageTask(OutputStream os, SocketConnection sc, String message) {
        this.os = os;
        this.sc = sc;
        this.message = message;
    }

    @Override
    public void run() {
        /* Проверяем сокет. Если он не создан или закрыт, то выдаем исключение */
        if (sc == null) {
            System.out.println("Невозможно отправить данные. Сокет не создан или закрыт");
        }
        /* Отправка данных */
        try {
            os.write(message.getBytes());
            os.write(RN);
        } catch (IOException e) {
            System.err.println("couldn't send the message");
        }
    }

}
