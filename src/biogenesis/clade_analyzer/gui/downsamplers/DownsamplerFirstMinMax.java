package biogenesis.clade_analyzer.gui.downsamplers;

import java.util.List;

/**
 * Downsampler that keeps the first value, the minimum value and the maximum value in each batch.
 * This downsampler should keep the spikes in the data, but also keeps the data more noisy.
 */
public class DownsamplerFirstMinMax implements Downsampler {
  @Override
  public void downsample(List<Double> values, List<Integer> valueLabels, int maxPoints,
      List<Double> outputValues, List<Integer> outputValueLabels) {
    if (maxPoints <= 1) {
      outputValues.addAll(values);
      outputValueLabels.addAll(valueLabels);
      return;
    }
    maxPoints -= maxPoints % 3; // make sure it's a multiple of 3

    int step = values.size() / (maxPoints / 3);
    int i = 0;
    while (i < values.size()) {
      int startIndex = i;
      int endIndex = Math.min(i + step - 1, values.size() - 1);

      // keep the first value
      outputValues.add(values.get(startIndex));
      outputValueLabels.add(valueLabels.get(startIndex));

      // keep the minimum value
      double min = Double.MAX_VALUE;
      int minIndex = -1;
      for (int j = startIndex; j <= endIndex; j++) {
        if (values.get(j) < min) {
          min = values.get(j);
          minIndex = j;
        }
      }

      // keep the maximum value
      double max = Double.MIN_VALUE;
      int maxIndex = -1;
      for (int j = startIndex; j <= endIndex; j++) {
        if (values.get(j) > max) {
          max = values.get(j);
          maxIndex = j;
        }
      }

      if (minIndex >= 0 && minIndex < maxIndex) {
        outputValues.add(min);
        outputValueLabels.add(valueLabels.get(minIndex));
        outputValues.add(max);
        outputValueLabels.add(valueLabels.get(maxIndex));
      } else if (maxIndex >= 0 && minIndex > maxIndex) {
        outputValues.add(max);
        outputValueLabels.add(valueLabels.get(maxIndex));
        outputValues.add(min);
        outputValueLabels.add(valueLabels.get(minIndex));
      } else if (minIndex >= 0) {
        outputValues.add(min);
        outputValueLabels.add(valueLabels.get(minIndex));
      } else if (maxIndex >= 0) {
        outputValues.add(max);
        outputValueLabels.add(valueLabels.get(maxIndex));
      }

      i += step;
    }

    // keep the last value if it's not in the list already
    if (outputValueLabels.size() > 0
        && outputValueLabels.get(outputValueLabels.size() - 1) != valueLabels.get(valueLabels.size() - 1)) {
      outputValues.add(values.get(values.size() - 1));
      outputValueLabels.add(valueLabels.get(valueLabels.size() - 1));
    }
  }

  /**
   * Returns the name of the downsampler. Used to choose the downsampler in the GUI.
   */
  @Override
  public String toString() {
    return "Keep first, min, max of batch";
  }
}
