package pv168.hotelmasters.superhotel.backend.threads.xlesko2;

/**
 * @author Kristian Lesko
 */
public class FiftyCounterThread implements Runnable {
    private static int THREAD_COUNT = 3;
    private static int END_COUNT = 50;
    private static volatile int count = -1;
    private static final Object sync = new Object();

    @Override
    public void run() {
        while (true) {
            synchronized (sync) {
                if (count >= END_COUNT) {
                    break;
                }
                count++;
                System.out.println(Thread.currentThread().getName() + ": " + count);
            }
            spendTime(10000000);
        }
    }

    private void spendTime(long count) {
        int a = 0;
        for (long i = 0; i < count; i++) {
            a += 1;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(new FiftyCounterThread(), "Thread " + (i + 1));
            thread.start();
        }
    }
}
