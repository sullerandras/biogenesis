package biogenesis.parallel_executor;

/**
 * The WorkerJob is responsible for executing one step of the simulation in parallel for a range of
 * lines in the OrganismBuckets.
 */
class WorkerJob {
  private final int startIndex;
  private final int endIndex;

  /**
   * Creates a new WorkerJob that will execute one step of the simulation for the lines from
   * startIndex to endIndex, inclusive.
   * @param startIndex the index of the first line to execute
   * @param endIndex the index of the last line to execute
   */
  WorkerJob(int startIndex, int endIndex) {
    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  /**
   * Executes one step of the simulation for the lines from startIndex to endIndex, inclusive.
   */
  public void run() {
    LinesLocker.lockRange(startIndex - 4, startIndex + 4);
    int index = startIndex;
    while (index <= endIndex) {
      ParallelExecutor.progressLineInBucket(index);
      LinesLocker.unlock(index - 4);
      index++;
      LinesLocker.lock(index + 4);
    }
    LinesLocker.unlockRange(index - 4, index + 4);
    // System.out.println("=====> job done in thread " + Thread.currentThread().getName());
  }
}