
 
import java.util.ArrayList;
import java.util.List;
 
/**
 * A simple example of thread pool design pattern.
 * @author aj001si
 *
 */
public class MyThreadPool {
 
 private final List<Runnable> TASK_QUEUE = new ArrayList<Runnable>();
 private final List<WorkerThread> WORKER_THREADS = new ArrayList<WorkerThread>();
 private boolean shutdown = false;
 
 public MyThreadPool(int poolSize){
    if(poolSize == 0){
        throw new IllegalArgumentException("Pool size can't be zero.");
     }
    //Create a pool of worker with given size
    for(int i=1; i <= poolSize; i++){
       WorkerThread thread = new WorkerThread("Worker thread " + i);
       WORKER_THREADS.add(thread);
       thread.start();
    }
  }
 
   //Assign some work
   public void addTask(Runnable task){
      //Add new task in TASK_QUEUE and notify all workers that wake up new task just arrived
      synchronized (TASK_QUEUE) {
         TASK_QUEUE.add(task);
         TASK_QUEUE.notifyAll();
      }
   }
 
  //It time to shut it down
  public void shutdown(){
     //Set flag for all worker threads that the pool is shutting down.
     shutdown = true;
     //Notify all waiting threads
     synchronized (TASK_QUEUE) {
     TASK_QUEUE.notifyAll();
   }
 }
 
 /**
 * This class represents worker thread to executes tasks from TASK_QUEUE.
 *
 */
   private class WorkerThread extends Thread {
 
     public WorkerThread(String threadName){
        super(threadName);
     }
 
    public void run(){
    //Each worker will continue until client will not call shutdown or wait if task_queue is empty.
    while (!shutdown || !TASK_QUEUE.isEmpty() ){
       try{
        synchronized(TASK_QUEUE){
          Runnable task= null;
          for (Runnable task1 : TASK_QUEUE){
              task = task1;
              break;
          }
          //Got some work
          if (task !=null){
          TASK_QUEUE.remove(task);
          task.run();
        }else {
       //Nothing to do so it will wait for some work
       if ( ! shutdown || ! TASK_QUEUE.isEmpty()){
         TASK_QUEUE.wait();
       }
   }
   //If this worker does not has anything to do than release resource(CPU) for others.
   Thread.yield();
  }
 }catch(Exception ex){
 ex.printStackTrace();
 }
 if(shutdown){
 System.out.println(this.getName() + " bye bye !!");
 }
 }
 }
 }
 
//Test client
 public static void main(String[] args) {
    MyThreadPool pool = new MyThreadPool(2);
    pool.addTask(new Runnable() {
         @Override
         public void run() {
           System.out.println("Say Hello ...First thread");
         }
     });
    pool.addTask(new Runnable() {
         @Override
         public void run() {
           System.out.println("Say Hello ...Second thread");
          }
    });
    pool.shutdown();
 }
}
