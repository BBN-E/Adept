/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

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