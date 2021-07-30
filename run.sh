#!/bin/bash

EXAMPLE_DIR=$DAIKONDIR/examples/my-examples

INV_CONFIG=(
    --user-defined-invariant daikon.inv.unary.scalar.Even
)

java -cp "$DAIKONDIR/daikon.jar:$DAIKONDIR/synthinv" daikon.Daikon "${INV_CONFIG[@]}" --config run.config $EXAMPLE_DIR/sample1.dtrace $EXAMPLE_DIR/sample.decls