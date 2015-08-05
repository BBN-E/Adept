import adept.common.Entity;
import adept.common.KBID;
import adept.kbapi.KBQueryProcessor;
import adept.kbapi.sql.QuickJDBC;
import java.util.List;

public class TestConnectionPools {
    private static final Object lock = new Object();
    private static int finishedThreads = 0;
    private static final int totalThreads = 10;
    
    public static void main(String[] args) throws Exception {
        KBQueryProcessor testQueryProcessor = new KBQueryProcessor();
        
        // Spin up multiple threads that will constantly query the DB
        for (int i = 0; i < totalThreads; i++) {
            Thread.sleep(1000);
            QueryThread qt = new QueryThread(testQueryProcessor);
            qt.start();
        }
        
        while (totalThreads > finishedThreads) {
            QuickJDBC.printPoolInfo();
            System.out.println();
            Thread.sleep(3000);
        }
    }
    
    public static void incrementFinishedThreads() {
        synchronized (lock) {
            finishedThreads++;
        }        
    }
}

class QueryThread implements Runnable {
    private Thread t;
    private KBQueryProcessor queryProcessor;
   
    QueryThread(KBQueryProcessor qp){
       queryProcessor = qp;
    }
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
                List<KBID> entities = queryProcessor.getEntitiesByStringReference("India");
            } catch (Exception e) {}
        }
        TestConnectionPools.incrementFinishedThreads();
    }
   
    public void start ()
    {
        if (t == null)
        {
            t = new Thread(this);
            t.start ();
        }
    }    
}
