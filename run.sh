#!/bin/bash

EXAMPLE_DIR=$DAIKONDIR/examples/my-examples

INV_CONFIG=(
    --user-defined-invariant daikon.inv.unary.scalar.Even
    --user-defined-invariant daikon.inv.ternary.threeScalar.Sum
)

echo python3 $EXAMPLE_DIR/tracegen.py $EXAMPLE_DIR/sample1.dtrace
python3 $EXAMPLE_DIR/tracegen.py $EXAMPLE_DIR/sample1.dtrace

java -cp "$DAIKONDIR/daikon.jar:$DAIKONDIR/synthinv" daikon.Daikon "${INV_CONFIG[@]}" --config run.config $EXAMPLE_DIR/sample1.dtrace $EXAMPLE_DIR/sample.decls
# java -cp "$DAIKONDIR/daikon.jar" daikon.Daikon --config run.config $EXAMPLE_DIR/sample1.dtrace $EXAMPLE_DIR/sample.decls