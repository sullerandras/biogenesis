package biogenesis.parallel_executor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * WorkerThread is responsible for executing WorkerJobs. One WorkerJob is added for every step of the
 * simulation.
 * This is just a wrapper around a Thread, with extra functionality of waiting for WorkerJobs to be
 * added, and notifying when they're done.
 */
class WorkerThread extends Thread {
  private static int nextWorkerThreadId = 0;

  private Queue<WorkerJob> jobs = new LinkedList<>();
  private Semaphore doneSemaphore = new Semaphore(1);

  /**
   * Creates a new WorkerThread.
   * Creating threads is expensive, so we create them once and reuse them.
   */
  WorkerThread() {
    super("WorkerThread-" + nextWorkerThreadId++);
    doneSemaphore.acquireUninterruptibly();
  }

  /**
   * Executes WorkerJobs until interrupted.
   */
  @Override
  public void run() {
    while (!isInterrupted()) {
      WorkerJob job = null;
      synchronized (jobs) {
        if (jobs.size() > 0) {
          job = jobs.remove();
        }
      }
      if (job != null) {
        job.run();
      } else {
        // wait for a job
        // System.out.println("=====> gonna wait for a job in thread " + Thread.currentThread().getName() + "");
        try {
          synchronized (jobs) {
            // System.out.println("=======> notifying thread " + Thread.currentThread().getName() + " done");
            doneSemaphore.release();
            jobs.wait();
          }
        } catch (InterruptedException e) {
          // System.out.println("=====> thread " + Thread.currentThread().getName() + " interrupted");
          break;
        }
      }
    }
  }

  /**
   * Adds a WorkerJob to the queue, and return immediately.
   * Use waitTillDone() to wait for the job to be done.
   * @param job the WorkerJob to add
   */
  void addJob(WorkerJob job) {
    // System.out.println("======> adding a job in thread " + Thread.currentThread().getName() + " to process lines " + job.startIndex + " to " + job.endIndex + "");
    synchronized (jobs) {
      jobs.add(job);
    }
    doneSemaphore.acquireUninterruptibly();
    synchronized (jobs) {
      jobs.notify();
    }
  }

  /**
   * Waits for the current WorkerJob to be done.
   */
  void waitTillDone() {
    doneSemaphore.acquireUninterruptibly();
    doneSemaphore.release();
  }
}