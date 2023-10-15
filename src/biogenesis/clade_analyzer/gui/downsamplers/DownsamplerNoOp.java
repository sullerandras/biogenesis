package biogenesis.clade_analyzer.gui.downsamplers;

import java.util.List;

/**
 * Downsampler that keeps all data points.
 */
public class DownsamplerNoOp implements Downsampler {
  @Override
  public void downsample(List<Double> values, List<Integer> valueLabels, int maxPoints, List<Double> outputValues,
      List<Integer> outputValueLabels) {
    outputValues.addAll(values);
    outputValueLabels.addAll(valueLabels);
  }

  /**
   * Returns the name of the downsampler. Used to choose the downsampler in the GUI.
   */
  @Override
  public String toString() {
    return "Keep all data points";
  }
}
