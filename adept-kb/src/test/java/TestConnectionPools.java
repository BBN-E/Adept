/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

import adept.kbapi.KB;
import adept.kbapi.KBParameters;
import adept.kbapi.sql.QuickJDBC;

public class TestConnectionPools {
	private static final Object lock = new Object();
	private static int finishedThreads = 0;
	private static final int totalThreads = 10;

	public static void main(String[] args) throws Exception {
		KBParameters kbParameters = new KBParameters();
		KB kb = new KB(kbParameters);
		QuickJDBC quickJDBC = new QuickJDBC(kbParameters);

		// Spin up multiple threads that will constantly query the DB
		for (int i = 0; i < totalThreads; i++) {
			Thread.sleep(1000);
			QueryThread qt = new QueryThread(kb);
			qt.start();
		}

		while (totalThreads > finishedThreads) {
			quickJDBC.printPoolInfo();
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
	private KB kb;

	QueryThread(KB qp) {
		kb = qp;
	}

	public void run() {
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(1000);
				kb.getEntitiesByStringReference("India");
			} catch (Exception e) {
			}
		}
		TestConnectionPools.incrementFinishedThreads();
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}
