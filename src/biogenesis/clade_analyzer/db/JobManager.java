package biogenesis.clade_analyzer.db;

import java.util.ArrayList;
import java.util.List;

public class JobManager {
  private static final Object monitor = new Object();

  private static List<Job> jobs = new ArrayList<>();

  static {
    Thread jobManagerThread = new Thread() {
      public void run() {

        while (true) {
          Job job;
          synchronized (monitor) {
            while (jobs.isEmpty()) {
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
}
