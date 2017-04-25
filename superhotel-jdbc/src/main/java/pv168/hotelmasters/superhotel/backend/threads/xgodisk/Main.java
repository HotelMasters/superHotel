package pv168.hotelmasters.superhotel.backend.threads.xgodisk;

/**
 * @author Gabika Godiskova
 */
public class Main {
    public static void main(String[] args){
        for (int i = 1;i<=3;i++){
            Thread thread = new Thread(new CounterThread(), "Vlakno "+i);
            thread.start();
        }
    }
}
