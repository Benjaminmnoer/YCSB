package site.ycsb.generator;

/**
 * Generate a popularity distribution of items, skewed to favor recent items significantly more than older items.
 */
public class ConsecutiveAccessesGenerator extends NumberGenerator {
  private final int consecutiveAccesses;
  private int currentAccesses;
  private final UniformLongGenerator uniformGenerator;

  public ConsecutiveAccessesGenerator() {
    this(1000, 5);
  }

  public ConsecutiveAccessesGenerator(int numberOfKeys, int consecutiveAccesses) {
    this.consecutiveAccesses = consecutiveAccesses;
    currentAccesses = 0;
    uniformGenerator = new UniformLongGenerator(0, numberOfKeys);
    setLastValue(uniformGenerator.nextValue());
  }

  /**
   * Generate the next string in the distribution, skewed Zipfian favoring the items most recently returned by
   * the basis generator.
   */
  @Override
  public Long nextValue() {
    if (currentAccesses >= consecutiveAccesses){
      setLastValue((long)uniformGenerator.nextValue());
      currentAccesses = 0;
    }

    currentAccesses++;
    return (long)lastValue();
  }

  @Override
  public double mean() {
    throw new UnsupportedOperationException("Can't compute mean of non-stationary distribution!");
  }

  public static void main(String[] args) {
    ConsecutiveAccessesGenerator gen = new ConsecutiveAccessesGenerator();
    for (int i = 0; i < Integer.parseInt(args[0]); i++) {
      System.out.println(gen.nextValue());
    }
  }
}
