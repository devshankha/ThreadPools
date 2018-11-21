import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class ThreadPool {
	int noOfThreads;
	boolean shutDown;
	WorkerThread workers[];
	BlockingQueue queue;
	ThreadPool (int num){
		noOfThreads = num;
		for (int i=0;i < noOfThreads; i++){
			workers[i] = new WorkerThread();
			workers[i].start();			
		}
		queue = new LinkedBlockingDeque();
	}

	public void execute(Runnable task) {
		synchronized (queue) {
			queue.add(task);
			queue.notify();
		}
	}
	 public void shutdown() {
	        while (!queue.isEmpty()) {
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                //interruption
	            }
	        }
	        shutDown = true;
	        for (WorkerThread workerThread: workers) {
	            workerThread.interrupt();
	        }
	    }

	private class WorkerThread extends Thread {
		public void run(){
			Runnable task;
			while (!shutDown){
				synchronized(queue){
					while (queue.isEmpty()){
						try {
							queue.wait();							
						}catch (InterruptedException e){
							
						}						
					}
					task = (Runnable) queue.poll();	
					try {
						//null check here
						task.run();						
					}catch (RuntimeException e){
						
					}
				}				
			}			
		}		
	}

}
