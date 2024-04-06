package biogenesis.parallel_executor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import biogenesis.Organism;
import biogenesis.OrganismBuckets;
import biogenesis.Utils;
import biogenesis.VisibleWorldInterface;

/**
 * This class is responsible for executing one step of the simulation in parallel for all organisms.
 */
public class ParallelExecutor {
  private static Collection<Organism> checkedOrganisms = Collections.synchronizedSet(new HashSet<>());
  private static List<WorkerThread> workerThreads = new ArrayList<>();

  private static Collection<Organism> organisms;
  private static OrganismBuckets organismBuckets;
  private static VisibleWorldInterface visibleWorld;

  /**
   * Progress one step for all organisms. Depending on Utils.THREAD_COUNT, it will do it in parallel
   * or in serial.
   * This will wait for all organisms to finish before returning, even when we use parallel execution.
   */
  public static void progressAllOrganisms(Collection<Organism> organisms, OrganismBuckets organismBuckets,
      VisibleWorldInterface visibleWorld) {
    ParallelExecutor.organisms = organisms;
    ParallelExecutor.organismBuckets = organismBuckets;
    ParallelExecutor.visibleWorld = visibleWorld;

    final int organismCount = organisms.size();
    final int threadCount = Utils.between(Utils.THREAD_COUNT, 1, 100);

    if (threadCount > 1) {
      progressAllOrganismsInParallel(organismCount, threadCount);
    } else {
      progressAllOrganismsInSerial(organismCount);
    }
  }

  private static void progressAllOrganismsInSerial(int organismCount) {
    for (Organism b : organisms.toArray(new Organism[0])) {
      if (!b.move()) {
        organisms.remove(b);
        if (visibleWorld.getSelectedOrganism() == b) {
          visibleWorld.setSelectedOrganism(null);
        }
      }
    }
  }

  private static void progressAllOrganismsInParallel(int organismCount, int threadCount) {
    // System.out.println("========================================================================================== in thread "+Thread.currentThread().getName());
    if (checkedOrganisms == null) {
      checkedOrganisms = new HashSet<>();
    }
    checkedOrganisms.clear();

    int lineCount = organismBuckets.getMaxWidth() + 1;
    if (threadCount * 5 > lineCount) { // not enough gap between threads
      progressAllOrganismsInSerial(organismCount);
      return;
    }

    if (workerThreads == null) {
      workerThreads = new ArrayList<>();
    }
    if (workerThreads.size() != threadCount) {
      // stop all worker threads
      for (WorkerThread workerThread : workerThreads) {
        workerThread.interrupt();
      }

      // create new worker threads
      workerThreads.clear();
      for (int i = 0; i < threadCount; i++) {
        WorkerThread workerThread = new WorkerThread();
        workerThread.start();
        workerThreads.add(workerThread);
      }

      ThreadStartIndexes.initialize(threadCount, lineCount);
    }
    LinesLocker.initialize(lineCount);

    // Split the buckets into vertical lines.
    // Do one line per thread.
    // Do it until all lines are done.

    WorkerJob[] jobs = new WorkerJob[threadCount];

    // do one line per thread
    for (int i = 0; i < threadCount; i++) {
      jobs[i] = new WorkerJob(ThreadStartIndexes.getStartIndex(i), ThreadStartIndexes.getEndIndex(i));
      workerThreads.get(i).addJob(jobs[i]);
    }

    long shortestNanos = Long.MAX_VALUE;
    int shortestIndex = -1;
    long longestNanos = Long.MIN_VALUE;
    int longestIndex = -1;

    // wait till all jobs finish
    for (int i = 0; i < threadCount; i++) {
      // System.out.println("======> waiting for thread " + workerThreads.get(i).getName() + " to finish");
      workerThreads.get(i).waitTillDone();
      // System.out.println("======> waiting for thread " + workerThreads.get(i).getName() + " to finish done");
      if (jobs[i].nanos < shortestNanos) {
        shortestNanos = jobs[i].nanos;
        shortestIndex = i;
      }
      if (jobs[i].nanos > longestNanos) {
        longestNanos = jobs[i].nanos;
        longestIndex = i;
      }
    }

    ThreadStartIndexes.adjust(shortestIndex, shortestNanos, longestIndex, longestNanos);
  }

  /**
   * Progresses all organisms in a vertical line of buckets in organismBuckets.
   * This will be called by WorkerJob, and it should be thread safe.
   * Organisms will be added to checkedOrganisms as they are processed,
   * so that they will not be processed again.
   *
   * @param index the x index of the buckets that we need to process.
   */
  static void progressLineInBucket(final int index) {
    for (int y = 0; y <= organismBuckets.getMaxHeight(); y++) {
      Collection<Organism> bucket = organismBuckets.getBucket(index, y);
      for (Organism o : bucket) {
        synchronized (checkedOrganisms) {
          if (!checkedOrganisms.add(o)) {
            continue;
          }
        }
        if (!o.move()) {
          organisms.remove(o);
          if (visibleWorld.getSelectedOrganism() == o) {
            visibleWorld.setSelectedOrganism(null);
          }
        }
      }
    }
  }
}
