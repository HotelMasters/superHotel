package pv168.hotelmasters.superhotel.backend.threads.xgodisk;

/**
 * @author Gabika Godiskova
 */
public class CounterThread implements Runnable{

    private static int count = 0;
    private static final Object synchronizer = new Object();
    @Override
    public void run() {
        while(true) {
            synchronized (synchronizer) {
                if (count >= 51) {
                    return;
                }
                System.out.println(Thread.currentThread().getName() + ": " + count);
                count += 1;
            }
            for (long i = 0; i < 10000000;i++){}
        }
    }

}
