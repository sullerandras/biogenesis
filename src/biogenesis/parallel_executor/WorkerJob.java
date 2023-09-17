package biogenesis.parallel_executor;

/**
 * The WorkerJob is responsible for executing one step of the simulation in parallel for a range of
 * lines in the OrganismBuckets.
 */
class WorkerJob {
  private static final int RANGE_TO_CLEAR = ThreadStartIndexes.MIN_RANGE_SIZE - 1;

  private final int startIndex;
  private final int endIndex;

  /**
   * How long it took to execute this job, in nanoseconds.
   */
  public long nanos;

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
    long start = System.nanoTime();

    LinesLocker.lockRange(startIndex - RANGE_TO_CLEAR, startIndex + RANGE_TO_CLEAR);
    int index = startIndex;
    while (index <= endIndex) {
      ParallelExecutor.progressLineInBucket(index);
      LinesLocker.unlock(index - RANGE_TO_CLEAR);
      index++;
      LinesLocker.lock(index + RANGE_TO_CLEAR);
    }
    LinesLocker.unlockRange(index - RANGE_TO_CLEAR, index + RANGE_TO_CLEAR);
    // System.out.println("=====> job done in thread " + Thread.currentThread().getName());

    nanos = System.nanoTime() - start;
  }

  @Override
  public String toString() {
    return String.format("(%6.2fms, %2d range)", nanos / 1_000_000.0, endIndex - startIndex + 1);
  }
}