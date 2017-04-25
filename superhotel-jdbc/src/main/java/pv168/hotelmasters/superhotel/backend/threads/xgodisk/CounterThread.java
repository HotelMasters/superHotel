package pv168.hotelmasters.superhotel.backend.threads.xgodisk;

/**
 * @author Gabika Godiskova
 */
public class CounterThread implements Runnable{
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    private static int count = 0;
    private static final Object synchronizer = new Object();
    @Override
    public void run() {
        while(true) {
            synchronized (synchronizer) {
                count += 1;
                if (count >= 51) {
                    return;
                }
                System.out.println(Thread.currentThread().getName() + ": " + count);
            }
            for (long i = 0; i < 100000;i++){}
        }
    }
}
