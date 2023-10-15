package biogenesis.clade_analyzer.gui.downsamplers;

import java.util.List;

/**
 * Downsampler that averages the values in each batch.
 * The averaging may hide sudden spikes in the data.
 */
public class DownsamplerAverage implements Downsampler {
  public void downsample(List<Double> values, List<Integer> valueLabels, int maxPoints,
      List<Double> outputValues, List<Integer> outputValueLabels) {
    if (values.size() != valueLabels.size()) {
      throw new IllegalArgumentException("values and valueLabels must have the same size");
    }
    if (maxPoints <= 1) {
      outputValues.addAll(values);
      outputValueLabels.addAll(valueLabels);
      return;
    }

    int step = values.size() / maxPoints;
    int i = 0;
    while (i < values.size()) {
      int startIndex = i;
      int endIndex = Math.min(i + step - 1, startIndex);

      // calculate the average
      double sum = 0;
      for (int j = startIndex; j <= endIndex; j++) {
        sum += values.get(j);
      }
      double average = sum / (endIndex - startIndex + 1);
      int averageIndex = (startIndex + endIndex) / 2;
      outputValues.add(average);
      outputValueLabels.add(valueLabels.get(averageIndex));

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
    return "Average of batch";
  }
}
