package biogenesis.clade_analyzer.gui.downsamplers;

import java.util.List;

/**
 * Downsampler interface. Downsamplers are used to reduce the number of points in a chart,
 * so the rendering is faster.
 */
public interface Downsampler {
  /**
   * Downsamples the values and valueLabels.
   * Since java does not support multiple return values, the two output lists
   * are passed as parameters, and these lists will be mutated.
   * @param values The values to downsample.
   * @param valueLabels The labels for the values. The labels are displayed in the x axis.
   * @param maxPoints The maximum number of points to keep.
   * @param outputValues The downsampled values. This list will be mutated.
   * @param outputValueLabels The downsampled value labels. This list will be mutated.
   */
  public void downsample(List<Double> values, List<Integer> valueLabels, int maxPoints,
      List<Double> outputValues, List<Integer> outputValueLabels);

  /**
   * List of all downsamplers.
   */
  public static final Downsampler[] downsamplers = new Downsampler[] {
      new DownsamplerFirstMinMax(),
      new DownsamplerAverage(),
      new DownsamplerNoOp(),
  };
}
