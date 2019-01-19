package com.github.joonasvali.naturalmouse.support;

/**
 * Speed for the mouse movement
 * Speed defines how slow or fast the cursor is moving at a particular moment, defining the characteristics
 * of movement itself not the trajectory, but how jagged or smooth, accelerating or decelerating, the movement is.
 */
public class Speed {
  private static final int AVERAGE_BUCKET_VALUE = 100;

  private final double[] buckets;

  /**
   * @param characteristics the characteristics array, which can be any size, contain non-negative numbers.
   * The values in the array are translated to speed and all values are relative. For example an
   * array of [1,2,3,4] has the same meaning as [100, 200, 300, 400] or [10, 10, 20, 20, 30, 30, 40, 40]
   * Every array element describes a time of the movement, so that in array of n-elements every element is
   * describing (100 / n)% of the movement. In an array of [1,2,3,4] every element is responsible for
   * 25% of time and the movement is accelerating - in the last 25% of time the mouse cursor is 4 times faster
   * than it was in the first 25% of the time.
   */
  public Speed(double[] characteristics) {
    buckets = normalizeBuckets(characteristics);
  }

  /**
   * Normalizes the characteristics to have an average of AVERAGE_BUCKET_VALUE
   *
   * @param speedCharacteristics an array of values which describe how the mouse should move at each moment
   * @return the normalized bucket array
   */
  private double[] normalizeBuckets(double[] speedCharacteristics) {
    double[] buckets = new double[speedCharacteristics.length];
    long sum = 0;
    for (int i = 0; i < speedCharacteristics.length; i++) {
      if (speedCharacteristics[i] < 0) {
        throw new IllegalArgumentException("Invalid SpeedCharacteristics at [" + i + "] : " + speedCharacteristics[i]);
      }
      sum += speedCharacteristics[i];
    }

    if (sum == 0) {
      throw new IllegalArgumentException("Invalid speedCharacteristics. All array elements can't be 0.");
    }
    /*
     * By multiplying AVERAGE_BUCKET_VALUE to buckets.length we get a required fill for the buckets,
     * For example if there are 5 buckets then 100 * 5 gives us 500, which is how much the buckets should
     * contain on total ideally. Then we divide it by the sum which we got from adding all contents of characteristics
     * array together. The resulting value describes the speedCharacteristics array and how much is missing or
     * overfilled in it. for example when we get 0.5, then we know it contains twice as much as our normalized
     * buckets array should have and we multiply all characteristics values by 0.5, this preserves the
     * characteristics, but reduces the values to levels our algorithm knows how to work with.
     */
    double multiplier = (double) AVERAGE_BUCKET_VALUE * buckets.length / sum;
    for (int i = 0; i < speedCharacteristics.length; i++) {
      buckets[i] = speedCharacteristics[i] * multiplier;
    }
    return buckets;
  }

  public double[] getSpeedCharacteristics() {
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
    int bucketFrom = (int) (completion * buckets.length);
    // Define the last bucket we read from.
    int bucketUntil = Math.max(bucketFrom, (int) ((completion + completionStep) * buckets.length - 1));
    /* First we get the distance / steps, which is distance per step: DPS.
     *   This is what we would essentially return from this method on constant speed:
     *     "A distance we need to move the mouse for this step."
     *   However the idea is not to have constant speed, so we proceed further.
     *   Every next step the 'distance' and 'steps' passed to this method will be the same,
     *   so we would get the same result and continue returning constant value until completion reaches value '1'
     *   it would also turn out to be an average step size, and we will use that fact in the name of the variable
     *   this gets assigned to and in the following logic.
     *
     * Then we divide DPS by the number of buckets we read from this step (bucketUntil - bucketFrom + 1).
     * Then we have a value which is DPS per bucket.
     * Its essentially an average distance per step divided per buckets read.
     */
    double averageSpeedPerBucket = distance / steps / (bucketUntil - bucketFrom + 1);

    /* We will now read the mentioned buckets and divide their content by AVERAGE_BUCKET_VALUE = 100,
     * this gives a multiplier for averageSpeedPerBucket, which we multiply and sum the result to speed.
     *
     * The resulting value is speed with the properties read from the bucket, so when the values at
     * buckets are lower than AVERAGE_BUCKET_VALUE the speed is lower than average and when higher than average
     * the resulting speed, as well, is higher than average speed.
     */
    double speed = 0;
    for (int i = bucketFrom; i <= bucketUntil; i++) {
      speed += buckets[i] / AVERAGE_BUCKET_VALUE * averageSpeedPerBucket;
    }
    return speed;
  }
}
