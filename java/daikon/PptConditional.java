package daikon;

import daikon.split.*;
import utilMDE.Assert;

// Information about a disjunctive program point that represents just part
// of the data.
// This doesn't do any direct computation, instead deferring that to its
// views that are slices.

// This perhaps shouldn't extend PptTopLevel; fix that in the future.
// For now, it's convenient to take advantage of its functionality.
// And they're so similar that maybe this is the right thing after all.
public final class PptConditional
  extends PptTopLevel
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  Ppt parent;
  Splitter splitter;
  // indicates whether we're on the true side or the false side of the Splitter
  boolean splitter_inverse;

  // This does not install the variable values.  The reason is that it's
  // more efficient to do that for two PptConditional objects at once.

  public PptConditional(PptTopLevel parent, Splitter splitter, boolean splitter_inverse) {
    super(parent.name + ";condition=\"" + (splitter_inverse ? "not(" + splitter.condition() + ")" : splitter.condition()) + "\"",
	  VarInfo.arrayclone_simple(parent.trace_and_orig_and_const_vars()));
    // Assert.assert(splitter.instantiated() == false);
    this.parent = parent;
    this.splitter = splitter.instantiate(this);
    this.splitter_inverse = splitter_inverse;
    // this.controlling_ppts.add(parent); // [INCR]
    // Assert.assert(splitter.instantiated() == false);
    // Assert.assert(this.splitter.instantiated() == true);
  }

  // This is tested after constructing a PptConditional but before
  // installing it on any lists.  It should perhaps be checked earlier, but
  // it's convenient (for the Splitter writer) to do so after instantiating.
  public boolean splitter_valid() {
    return splitter.valid();
  }


  // Call this for tuples that are guaranteed to pass the test.
  void add_nocheck(ValueTuple vt, int count) {
    super.add(vt, count);
  }

  void add(ValueTuple vt, int count) {
    // This try block may be a very inefficient way to do this computation.
    // Perhaps figure out another way, or invalidate the whole PptConditional
    // if any exception is thrown.
    try {
      boolean splitter_test = splitter.test(vt);
      if (splitter_inverse ? (! splitter_test) : splitter_test)
        super.add(vt, count);
    } catch (Exception e) {
      // If an exception is thrown, don't put the data on either side
      // of the split.
    }
  }

  private void writeObject(java.io.ObjectOutputStream out)
    throws java.io.IOException
  {
    // Remove the splitter itself, in case it was compiled on the fly
    // (and so could not be re-loaded).  Later, perhaps change
    // de-serialization so that it is reconstructed on the fly.
    if (splitter != null) {
      Package pkg = splitter.getClass().getPackage();
      if (pkg == null ) { // no package
	splitter = null;
      }
    }
    out.defaultWriteObject();
  }

}
