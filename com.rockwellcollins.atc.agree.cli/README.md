# Running AGREE from the command line

AGREE can be run from the command line.  All AGREE tool preferences, along with other run options can be specified as command line arguments.  Usage is as follows:
  
| Argument | Description |  
|---|---|  
| -analyzeUnspecifiedAADLProperties  | optional, analyze unspecified AADL properties |  
| -application <arg>                 | required, the name of this analysis (com.rockwellcollins.atc.agree.cli.Agree) |  
| -c,--compImpl <arg>                | required, qualified component implementation name |  
| -consistencyDepth <arg>            | optional, consistency depth, default 1 | 
| -data <arg>                        | required, path of workspace | 
| -disableKInduction                 | optional, disable k-induction | 
| -displayCounterexamplesAsDecimal   | optional, display counterexamples as decimal | 
| -f,--files <arg>                   | optional, supplementary AADL files (absolute paths) | 
| -generateBlamedCounterexamples     | optional, generate blamed counterexamples |
| -generateSmoothCounterexamples     | optional, generate smooth counterexamples | 
| -getSetOfSupport                   | optional, get set of support | 
| -h,--help                          | print this message | 
| -m,--modelChecker <arg>            | optional, model checker, default JKind | 
| -maxInductionDepth <arg>           | optional, maximum induction depth, default 200 | 
| -maxPDRInstances <arg>             | optional, maximum number of PDR instances, default 0 | 
| -noInductiveCounterexamples        | optional, do not generate inductive counterexamples | 
| -noSplash                          | optional, hide the splash screen | 
| -o,--output <arg>                  | required, output JSON file absolute path | 
| -p,--project <arg>                 | required, project path (relative to workspace) | 
| -s,--solver <arg>                  | optional, SMT solver, default SMTInterpol | 
| -strategy <arg>                    | required, verification strategy (single, all, monolithic, realizability) | 
| -t,--timeout <arg>                 | optional, timeout (ms), default 100 | 
| -v,--validationOnly                | validation only, default false | 
| -w,--exitOnValidtionWarning        | exit on validation warning, default false | 

# Example

Using the AGREE toy_example to demonstrate usage, assume the project folder is located at C:\models\toy_example.  To perform a single layer analysis on the top_level.impl component in the Integer package, run the following command:

`osate.exe -application com.rockwellcollins.atc.agree.cli.Agree -noSplash -data C:/models -p Toy_Example -c Integer_Toy::top_level.Impl -strategy single -o C:/models/toy_example/output/AgreeResults.json`

Executing `osatec.exe` with the same arguments will capture debug output in the terminal.

The AGREE output will be written to a file in json format if the `-o` argument is provided.
