package com.github.joonasvali.naturalmouse.support;

/**
 * Flow for the mouse movement
 * Flow defines how slow or fast the cursor is moving at a particular moment, defining the characteristics
 * of movement itself not the trajectory, but how jagged or smooth, accelerating or decelerating, the movement is.
 */
public class Flow {
  private static final int AVERAGE_BUCKET_VALUE = 100;

  private final double[] buckets;

  /**
   * @param characteristics the characteristics array, which can be any size, contain non-negative numbers.
   * The values in the array are translated to flow and all values are relative. For example an
   * array of [1,2,3,4] has the same meaning as [100, 200, 300, 400] or [10, 10, 20, 20, 30, 30, 40, 40]
   * Every array element describes a time of the movement, so that in array of n-elements every element is
   * describing (100 / n)% of the movement. In an array of [1,2,3,4] every element is responsible for
   * 25% of time and the movement is accelerating - in the last 25% of time the mouse cursor is 4 times faster
   * than it was in the first 25% of the time.
   */
  public Flow(double[] characteristics) {
    buckets = normalizeBuckets(characteristics);
  }

  /**
   * Normalizes the characteristics to have an average of AVERAGE_BUCKET_VALUE
   *
   * @param flowCharacteristics an array of values which describe how the mouse should move at each moment
   * @return the normalized bucket array
   */
  private double[] normalizeBuckets(double[] flowCharacteristics) {
    double[] buckets = new double[flowCharacteristics.length];
    long sum = 0;
    for (int i = 0; i < flowCharacteristics.length; i++) {
      if (flowCharacteristics[i] < 0) {
        throw new IllegalArgumentException("Invalid FlowCharacteristics at [" + i + "] : " + flowCharacteristics[i]);
      }
      sum += flowCharacteristics[i];
    }

    if (sum == 0) {
      throw new IllegalArgumentException("Invalid FlowCharacteristics. All array elements can't be 0.");
    }
    /*
     * By multiplying AVERAGE_BUCKET_VALUE to buckets.length we get a required fill for the buckets,
     * For example if there are 5 buckets then 100 * 5 gives us 500, which is how much the buckets should
     * contain on total ideally. Then we divide it by the sum which we got from adding all contents of characteristics
     * array together. The resulting value describes the FlowCharacteristics array and how much is missing or
     * overfilled in it. for example when we get 0.5, then we know it contains twice as much as our normalized
     * buckets array should have and we multiply all characteristics values by 0.5, this preserves the
     * characteristics, but reduces the values to levels our algorithm knows how to work with.
     */
    double multiplier = (double) AVERAGE_BUCKET_VALUE * buckets.length / sum;
    for (int i = 0; i < flowCharacteristics.length; i++) {
      buckets[i] = flowCharacteristics[i] * multiplier;
    }
    return buckets;
  }

  public double[] getFlowCharacteristics() {
    return buckets;
  }

  /**
   * This returns step size for a single axis.
   *
   * @param distance the total distance current movement has on current axis from beginning to target in pixels
   * @param steps number of steps the current movement involves
   * @param completion value between 0 and 1, the value describes movement completion in time
   * @return the step size which should be taken next
   */
  public double getStepSize(double distance, int steps, double completion) {
    // This is essentially how big is a single completion step,
    // so we can expect next 'completion' is current completion + completionStep
    double completionStep = 1d / steps;
    // Define the first bucket we read from
    double bucketFrom = (completion * buckets.length);
    // Define the last bucket we read from
    double bucketUntil = ((completion + completionStep) * buckets.length);

    double bucketContents = getBucketsContents(bucketFrom, bucketUntil);
    // This shows how much distance is assigned to single contents value in the buckets.
    // For example if this gets assigned to 0.4, then for every value in the bucket
    // the cursor needs to travel 0.4 pixels, so for a bucket containing 50, the mouse
    // travelling distance is 0.4 * 50 = 20pixels
    double distancePerBucketContent = distance / (buckets.length * AVERAGE_BUCKET_VALUE);

    return bucketContents * distancePerBucketContent;
  }

  /**
   * Summarizes the bucket contents from bucketFrom to bucketUntil, where
   * provided parameters may have decimal places. In that case the value
   * from first or last bucket is just a fragment of it's full value, depending how
   * large portion the decimal place contains. For example getBucketContents(0.6, 2.4)
   * returns 0.4 * bucket[0] + 1 * bucket[1] + 0.4 * bucket[2]
   * @param bucketFrom bucket from where to start reading
   * @param bucketUntil bucket where to read
   * @return the sum of the contents in the buckets
   */
  private double getBucketsContents(double bucketFrom, double bucketUntil) {
    double sum = 0;
    for (int i = (int)bucketFrom; i < bucketUntil; i++) {
      double value = buckets[i];
      double endMultiplier = 1;
      double startMultiplier = 0;
      if (bucketUntil < i+1) {
        endMultiplier = bucketUntil - (int)bucketUntil;
      }
      if ((int)bucketFrom == i) {
        startMultiplier = bucketFrom - (int)bucketFrom;
      }
      value *= endMultiplier - startMultiplier;
      sum += value;
    }

    return sum;
  }
}
