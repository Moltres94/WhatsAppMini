package socket.utils;

public class BlockingRunner extends Thread{

    private final int capacity;
    private final Runnable[] queue;
    private int pos = 0;
    private boolean isRunning = false;
    private int lastTasks = 0;

    public BlockingRunner(int capacity) {
        this.capacity = capacity;
        this.queue = new Runnable[capacity];
    }

    public void startThread() {
        if (!isInterrupted() && !isRunning) {
            super.start();
        }
    }

    public final void start() {
        System.err.println("please use startThread() instead");
    }

    public synchronized void addTask(Runnable runnable) {
        pos = ++pos % capacity;
        queue[pos] = runnable;
        notify();
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            while (isRunning) {
                int completed = 0;
                for (int i = 0; i < capacity; i++) {
                    if (queue[i] != null) {
                        completed++;
                        Runnable task = queue[i];
                        try {
                            task.run();
                        }catch (Exception exception) {
                            isRunning = false;
                            System.err.println("task was thrown an exception. exit from loop");
                            break;
                        }
                        queue[i] = null;
                    }
                }
                lastTasks = completed;
                if (completed == 0) {
                    wait();
                }
            }
        } catch (InterruptedException exception) {
            isRunning = false;
            System.err.println("Thread was interrupted");
        }
    }

    public synchronized int size() {
        return lastTasks;
    }

    public synchronized void clear() {
        for (int i = 0; i < capacity; i++) {
            if (queue[i] != null) {
                queue[i] = null;
            }
        }
    }

}
