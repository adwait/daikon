// ***** This file is automatically generated from ThreeScalar.java.jpp
package daikon.inv.ternary.threeScalar;

import daikon.*;
import daikon.inv.*;
import daikon.inv.ternary.TernaryInvariant;
import org.checkerframework.checker.interning.qual.Interned;
import org.checkerframework.checker.lock.qual.GuardSatisfied;
import org.plumelib.util.Intern;
import typequals.prototype.qual.NonPrototype;
import typequals.prototype.qual.Prototype;

/**
 * Abstract base class for invariants over three numeric variables. An example is {@code z = ax + by
 * + c}.
 */
public class Sum extends ThreeScalar {
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20210730;

  public static boolean dkconfig_enabled = Invariant.invariantEnabledDefault;

  protected Sum(PptSlice ppt) {
    super(ppt);
  }

  protected @Prototype Sum() {
    super();
  }
  
  private static @Prototype Sum proto = new @Prototype Sum();

  /** Returns the prototype invariant. */
  public static @Prototype Sum get_proto() {
    return proto;
  }

  /** returns whether or not this invariant is enabled */
  @Override
  public boolean enabled() {
    return dkconfig_enabled;
  }

  /** instantiate an invariant on the specified slice */
  @Override
  public Sum instantiate_dyn(@Prototype Sum this, PptSlice slice) {
    return new Sum(slice);
  }

  // A printed representation for user output
  @Override
  public String format_using(@GuardSatisfied Sum this, OutputFormat format) {
    return var1().name() + " " + var2().name() + " sum inv!" ;
  }

  /**
   * Presents a sample to the invariant. Returns whether the sample is consistent with the
   * invariant. Does not change the state of the invariant.
   *
   * @param count how many identical samples were observed in a row. For example, three calls to
   * check_modified with a count parameter of 1 is equivalent to one call to check_modified with a
   * count parameter of 3.
   * @return whether or not the sample is consistent with the invariant
   */
  // public abstract InvariantStatus check_modified(long v1, long v2, long v3, int count);

  @Override
  public InvariantStatus check_modified(long v1, long v2, long v3, int count) {
    if (v1 + v2 != v3) {
      return InvariantStatus.FALSIFIED;
    }
    return InvariantStatus.NO_CHANGE;
  }

  /**
   * Similar to {@link #check_modified} except that it can change the state of the invariant if
   * necessary. If the invariant doesn't have any state, then the implementation should simply call
   * {@link #check_modified}. This method need not check for falsification; that is done by the
   * caller.
   */
  // public abstract InvariantStatus add_modified(long v1, long v2, long v3, int count);
  @Override
  public InvariantStatus add_modified(long v1, long v2, long v3, int count) {
    return check_modified(v1, v2, v3, count);
  }

  @Override
  protected double computeConfidence() {
    // Assume that every variable has a .5 chance of being positive by
    // chance.  Then a set of n values have a have (.5)^n chance of all
    // being positive by chance.
    return 1;
    //  - Math.pow(.5, ppt.num_samples());
  }

  /** By default, do nothing if the value hasn't been seen yet. Subclasses can override this. */
  // public InvariantStatus add_unmodified(long v1, long v2, long v3, int count) {
  //   return InvariantStatus.NO_CHANGE;
  // }
  @Override
  public boolean isSameFormula(Invariant other) {
    assert other instanceof Sum;
    return true;
  }

}
