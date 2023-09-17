package biogenesis.parallel_executor;

import java.util.concurrent.Semaphore;

/**
 * This class is responsible for locking and unlocking the vertical lines in OrganismBuckets,
 * so that the WorkerThreads can work on them in parallel without overlapping.
 */
class LinesLocker {
  static Semaphore[] semaphores;

  static void initialize(int lineCount) {
    if (semaphores == null || semaphores.length != lineCount) {
      semaphores = new Semaphore[lineCount];
      for (int i = 0; i < lineCount; i++) {
        semaphores[i] = new Semaphore(1);
      }
    }
  }

  /**
   * Locks the line with the given index, if it's >= 0 and < lineCount.
   * @param index the index of the line to lock
   */
  static void lock(int index) {
    if (index >= 0 && index < semaphores.length) {
      // System.out.println("======> lock line " + index + " by thread " + Thread.currentThread().getName());
      semaphores[index].acquireUninterruptibly();
      // System.out.println("======> lock line " + index + " by thread " + Thread.currentThread().getName() + " done");
    }
  }

  /**
   * Unlocks the line with the given index, if it's >= 0 and < lineCount.
   * @param index the index of the line to unlock
   */
  static void unlock(int index) {
    if (index >= 0 && index < semaphores.length) {
      // System.out.println("======> unlock line " + index + " by thread " + Thread.currentThread().getName());
      semaphores[index].release();
      // System.out.println("======> unlock line " + index + " by thread " + Thread.currentThread().getName() + " done");
    }
  }

  /**
   * Locks the lines from 'from' to 'to', inclusive.
   * @param from the index of the first line to lock
   * @param to the index of the last line to lock
   */
  static void lockRange(int from, int to) {
    for (int i = from; i <= to; i++) {
      lock(i);
    }
  }

  /**
   * Unlocks the lines from 'from' to 'to', inclusive.
   * @param from the index of the first line to unlock
   * @param to the index of the last line to unlock
   */
  static void unlockRange(int from, int to) {
    for (int i = from; i <= to; i++) {
      unlock(i);
    }
  }
}