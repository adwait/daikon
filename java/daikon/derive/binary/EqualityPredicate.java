package daikon.derive.binary;

import daikon.*;
import daikon.derive.*;
import java.util.logging.Logger;
import org.checkerframework.checker.lock.qual.GuardSatisfied;
import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.plumelib.util.Intern;

public final class EqualityPredicate extends BinaryDerivation {

  // We are Serializable
  static final long serialVersionUID = 20210811L;

  /** Debug tracer. */
  public static final Logger debug = Logger.getLogger("daikon.derive.binary.SequencesPredicate");

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /** Boolean. True iff SequencesPredicate derived variables should be generated. */
  public static boolean dkconfig_enabled = true;

  // ! what does this mean?
  /**
   * Boolean. True if Daikon should only generate derivations on fields of the same data structure.
   */
  public static boolean dkconfig_fieldOnly = true;

  // ! what is this?
  /** Boolean. True if Daikon should only generate derivations on boolean predicates. */
  public static boolean dkconfig_boolOnly = true;

  @Override
  public VarInfo var1(@GuardSatisfied EqualityPredicate this) {
    return base1;
  }

  @Override
  public VarInfo var2(@GuardSatisfied EqualityPredicate this) {
    return base2;
  }

  // Name of this predication
  private String predname;

  /**
   * Create a new SequencesJoin derivation.
   *
   * @param vi1 the first of the two variables this is based on
   * @param vi2 the second of the two variables this is based on
   */
  EqualityPredicate(VarInfo vi1, VarInfo vi2, String argName) {
    super(vi1, vi2);
    predname = argName;
  }

  /**
   * Returns a subset of val1 such that the corresponding element in var2 equals this.choose. It is
   * assumed that val1 and val2 originated from the same, larger data structure.
   *
   * @param full_vt the value tuple of a program point to compute the derived value from
   */
  @Override
  public ValueAndModified computeValueAndModifiedImpl(ValueTuple full_vt) {
    Object val1 = var1().getValue(full_vt);
    Object val2 = var2().getValue(full_vt);

    boolean isLong1 = true;
    boolean isLong2 = true; // They must equal in the end

    if (val1 == null) {
      isLong1 = false;
    }

    if (val2 == null) {
      isLong2 = false;
    }

    if (isLong1 && !(val1 instanceof Integer)) {
      isLong1 = false;
    }

    if (isLong2 && !(val2 instanceof Integer)) {
      isLong2 = false;
    }

    // ! why this assertion? something to do with Factory?
    // assert val2 == null || val2 instanceof long[];

    if (!(isLong1 && isLong2)) {
      // This derived variable is no longer interesting
      return new ValueAndModified(null, ValueTuple.MISSING_NONSENSICAL);
    }

    // !  why these assertions?
    assert isLong1 && isLong2;

    // this remains the same
    int mod = ValueTuple.UNMODIFIED;
    int mod1 = var1().getModified(full_vt);
    int mod2 = var2().getModified(full_vt);
    if (mod1 == ValueTuple.MODIFIED) mod = ValueTuple.MODIFIED;
    if (mod2 == ValueTuple.MODIFIED) mod = ValueTuple.MODIFIED;
    if (mod1 == ValueTuple.MISSING_NONSENSICAL) mod = ValueTuple.MISSING_NONSENSICAL;
    if (mod2 == ValueTuple.MISSING_NONSENSICAL) mod = ValueTuple.MISSING_NONSENSICAL;
    /*
     * v1\v2  Unm  Mod  Mis
     *
     * Unm    Unm  Mod  Mis
     * Mod    Mod  Mod  Mis
     * Mis    Mis  Mis  Mis
     */

    int value1 = (int) val1;
    int value2 = (int) val2;
    int result;
    if (value1 == value2) {
      result = 1;
    } else {
      result = 0;
    }
    return new ValueAndModified(Intern.intern(result), mod);
  }

  @Override
  protected VarInfo makeVarInfo() {
    return VarInfo.make_function("eqPredicateSlice", var1(), var2());
  }

  @SideEffectFree
  @Override
  public String toString(@GuardSatisfied EqualityPredicate this) {
    return String.format(
        "[EqualityPredicate of %s and %s for %s]", var1().name(), var2().name(), predname);
  }

  @Pure
  @Override
  public boolean isSameFormula(Derivation other) {
    if (other instanceof EqualityPredicate) {
      EqualityPredicate o = (EqualityPredicate) other;
      return o.var1().equals(var1()) && o.var2().equals(var2());
    }
    return false;
  }

  @SideEffectFree
  @Override
  public String esc_name(String index) {
    return String.format("predicate(%s,%s)", var1().name(), var2().name());
  }
}
