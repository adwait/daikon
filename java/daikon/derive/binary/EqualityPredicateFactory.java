package daikon.derive.binary;

import daikon.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EqualityPredicateFactory extends BinaryDerivationFactory {

  // Debug
  public static final Logger debug =
      Logger.getLogger("daikon.derive.binary.EqualityPredicateFactory");

  @Override
  public BinaryDerivation @Nullable [] instantiate(VarInfo var1, VarInfo var2) {
    boolean enabled = EqualityPredicate.dkconfig_enabled;
    if (!enabled) {
      return null;
    }

    if (debug.isLoggable(Level.FINE)) {
      debug.fine("Trying to instantiate " + var1.name() + " and " + var2.name());
    }

    if (!var1.rep_type.isIntegral() || !var2.rep_type.isIntegral()) {
      return null;
    }

    if (EqualityPredicate.dkconfig_boolOnly) {
      if (var2.file_rep_type != ProglangType.INT) {
        return null;
      }
    }

    if (var1.derived != null || var2.derived != null) {
      // From derived variables.  Don't derive.
      return null;
    }

    // Now we finally can derive

    if (debug.isLoggable(Level.FINE)) {
      debug.fine(
          var1.ppt + ": " + var1.name() + " and " + var2.name() + " are worth deriving from");
    }

    return new BinaryDerivation[] {new EqualityPredicate(var1, var2, "myPred")};
  }
}
