Challenges for Lifting
---

### Correctness check: scalability
- SymbiYosys:
  - used in 219C, instrument the verilator code with assumes and asserts
  - also can support temporal (SysVerilog) properties
- JasperGold, ..?
- Probably not much we can do for this, unless we look under the hood at the algorithms?
- Partitioning the synthesis task into subtasks
  - synth(f) --> synth(f1) and synth(f2) and synth(compose_f1_f2)
  - additional guidance on the relation between f1 outputs and f2 inputs

### Correctness check: environment assumptions
- This is kind of key, quite sensitive to minor mis-specs
- Example of environment assumptions for a 'simple' register-register operation on a pipelined core (riscv-mini):
- Pipelining makes the above task difficult, need to maintain shadow registers, BD-style verification

### Refinement Mapping:
- Also specify manually?
- Dual challenge: refinement mapping is often not an invariant map, need to accompany with "filtering" predicate
  - if $\Phi$ then mapping holds, else no mapping
  - $\Phi$ is tricky to come up with (often has micro-arch state variables)
- Can we automate this using some pattern detector tool? Daikon?
  - give a candidate set of mappings and Daikon can find possibilities?
  - can we use Daikon's "New Invariant" feature to provide arbitrary SMTLIB relations?
  - can we extend this to multi-cycle (LTL?) relations?
  - Daikon shortfalls: ~1000 variable traces





### Example
```verilog
    `ifdef FORMAL
    wire [31:0] fe_inst = __lft__tile__fe_inst;
    wire [31:0] ew_inst = __lft__tile__ew_inst;
    `define SUB_F7 (7'b0100000)
    `define ADD_F7 (7'b0000000)
    wire is_add =
    fe_inst[6:0] == 7'b0110011 &&
    fe_inst[11:7] != 5'b0
    ;

    reg init = 1;
    (* anyconst *) reg [31:0] tgt_rs1;
    (* anyconst *) reg [31:0] tgt_rs2;

    reg issued = 0;

    reg [3:0] counter = 0; // Used for debugging
    always @(posedge clock) begin
    counter = counter + 1;
    end

    // Eventually, if the user specifies a stream of assembly instructions we will generate a TGT_PC variable for each inst and a corresponding assumption
    `define TGT_PC (32'h200)
    always @(posedge clock) begin
    // Only reset on the first cycle
    assume(reset == init);
    init <= 0;

    if (reset) begin
        issued <= 0;
        // These assumptions are needed to ensure that EW_PC isn't initialized to TGT_PC which screws up issue detection in event of a stall, since EW_PC will continue to hold its (meaningless) initial value
        assume(
        __lft__tile__pc == 0 &&
        __lft__tile__fe_pc == 0 &&
        __lft__tile__ew_pc == 0
        );
    end
    // Observe when an instruction enters the pipeline
    if (!reset && __lft__tile__pc == `TGT_PC) begin
        issued <= 1;
    end
    // Check that a reset didn't occur last cycle and assume shadow values when an instruction hits this stage
    if (!reset && !$past(reset) && issued && __lft__tile__fe_pc == `TGT_PC) begin
        assume(
        is_add
        );
    end
    // Check that a reset didn't occur in the last 2 cycles
    // and apply assertions
    if (!reset && !$past(reset) && !$past(reset, 2) &&
        issued && __lft__tile__ew_pc == `TGT_PC) begin
        assert(
            {verilog_op} == {read_reg}
        );
    end
    // Uncomment to force a crash to see an example trace
    // assert(reset || __lft__tile__pc != 32'h204);
    end
    `endif
```

