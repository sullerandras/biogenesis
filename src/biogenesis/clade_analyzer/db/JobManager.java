package biogenesis.clade_analyzer.db;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JobManager {
  private static final Object monitor = new Object();

  private static List<Job> jobs = new ArrayList<>();
  private static List<ActionListener> tasksDoneListeners = new ArrayList<>();

  static {
    Thread jobManagerThread = new Thread() {
      public void run() {

        while (true) {
          Job job;
          synchronized (monitor) {
            while (jobs.isEmpty()) {
              notifyTasksDone();
              try {
                monitor.wait();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            job = jobs.remove(0);
          }
          job.runJob();
        }
      }
    };
    jobManagerThread.setDaemon(true);
    jobManagerThread.setName("JobManager");
    jobManagerThread.start();
  }

  public static void addJob(Job job) {
    synchronized (monitor) {
      jobs.add(job);
      monitor.notify();
    }
  }

  /**
   * Add a listener to be notified when all jobs are done.
   * @param listener
   */
  public static void addTasksDoneListener(ActionListener listener) {
    tasksDoneListeners.add(listener);
  }

  private static void notifyTasksDone() {
    for (ActionListener listener : tasksDoneListeners) {
      listener.actionPerformed(null);
    }
  }
}
