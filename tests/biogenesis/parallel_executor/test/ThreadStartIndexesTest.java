package biogenesis.parallel_executor.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import biogenesis.parallel_executor.ThreadStartIndexes;

public class ThreadStartIndexesTest {
  @Before
  public void setUp() {
    ThreadStartIndexes.clear();
  }

  @Test
  public void testInitialize() {
    ThreadStartIndexes.initialize(4, 100);
    assertThreadStartIndexes(0, 25, 50, 75, 100);
    assertThreadEndIndexes(24, 49, 74, 99);
  }

  @Test
  public void testAdjust_shortest3_longest0() {
    ThreadStartIndexes.initialize(4, 100);
    ThreadStartIndexes.adjust(3, 100, 0, 200);
    assertThreadStartIndexes(0, 24, 49, 74, 100);
  }

  @Test
  public void testAdjust_shortest2_longest0() {
    ThreadStartIndexes.initialize(4, 100);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 24, 49, 75, 100);
  }

  @Test
  public void testAdjust_shortest0_longest3() {
    ThreadStartIndexes.initialize(4, 100);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 26, 51, 76, 100);
  }

  @Test
  public void testAdjust_minRangeSize1() {
    ThreadStartIndexes.initialize(4, 30);
    assertThreadStartIndexes(0, 7, 15, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 6, 14, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 5, 13, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 5, 12, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 5, 11, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 5, 10, 22, 30);
    ThreadStartIndexes.adjust(2, 100, 0, 200);
    assertThreadStartIndexes(0, 5, 10, 22, 30);
  }

  @Test
  public void testAdjust_minRangeSize2() {
    ThreadStartIndexes.initialize(4, 30);
    assertThreadStartIndexes(0, 7, 15, 22, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 8, 16, 23, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 9, 17, 24, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 10, 18, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 11, 19, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 12, 20, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 13, 20, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 14, 20, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 15, 20, 25, 30);
    ThreadStartIndexes.adjust(0, 100, 3, 200);
    assertThreadStartIndexes(0, 15, 20, 25, 30);
  }

  private void assertThreadStartIndexes(int ... expected) {
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], ThreadStartIndexes.getStartIndex(i));
    }
  }

  private void assertThreadEndIndexes(int ... expected) {
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], ThreadStartIndexes.getEndIndex(i));
    }
  }
}
