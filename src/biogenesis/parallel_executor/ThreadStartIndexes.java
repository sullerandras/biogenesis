package biogenesis.parallel_executor;

/**
 * ThreadStartIndexes is responsible for calculating the start indexes for the WorkerThreads.
 * It has the ability to make a range bigger or smaller, depending on the load of the threads.
 */
public class ThreadStartIndexes {
  /**
   * The shortest range that a thread can have. The adjust method will not make a range smaller
   * than this.
   */
  public static final int MIN_RANGE_SIZE = 5;

  /**
   * The start indexes for the threads.
   * The array is one bigger than threadCount to avoid `if`.
   */
  private static int[] startIndexes;
  private static int threadCount;
  private static int lineCount;

  public static void initialize(int threadCount, int lineCount) {
    if (startIndexes != null && threadCount == ThreadStartIndexes.threadCount && lineCount == ThreadStartIndexes.lineCount) {
      return; // already initialized
    }

    ThreadStartIndexes.threadCount = threadCount;
    ThreadStartIndexes.lineCount = lineCount;

    // Initialize startIndexes
    startIndexes = new int[threadCount+1]; // index where this thead starts
    for (int i = 0; i < threadCount; i++) {
      startIndexes[i] = lineCount * i / threadCount;
    }
    startIndexes[threadCount] = lineCount;
  }

  public static int getStartIndex(int threadIndex) {
    return startIndexes[threadIndex];
  }

  public static int getEndIndex(int threadIndex) {
    // The array is one bigger than threadCount to avoid `if`.
    return startIndexes[threadIndex + 1] - 1;
  }

  public static void adjust(int shortestIndex, long shortestNanos, int longestIndex, long longestNanos) {
    if (shortestIndex == longestIndex) {
      return;
    }

    if (longestNanos < shortestNanos * 1.1) {
      // System.out.println("=====> diff is too small");
      return;
    }

    if (longestIndex < shortestIndex) {
      for (int i = longestIndex + 1; i <= shortestIndex; i++) {
        if (getRangeSize(i - 1) > MIN_RANGE_SIZE) {
          startIndexes[i]--;
        }
      }
    } else {
      for (int i = shortestIndex + 1; i <= longestIndex; i++) {
        if (getRangeSize(i) > MIN_RANGE_SIZE) {
          startIndexes[i]++;
        }
      }
    }
  }

  private static int getRangeSize(int threadIndex) {
    return getEndIndex(threadIndex) - getStartIndex(threadIndex) + 1;
  }

  /**
   * Only here for testing. Clears startIndexes.
   */
  public static void clear() {
    startIndexes = null;
  }
}
