# Copyright (c) 2021, Collins Aerospace.
# Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
# including any software or models in source or binary form, as well as any drawings, specifications, 
# and documentation (collectively "the Data"), to deal in the Data without restriction, including
# without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
# and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
# subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in all copies or 
# substantial portions of the Data.
# 
# THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
# LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
# IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
# FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
# ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.

The addVerificationSubSys.m file is used to auto copy the source model to the destination model
and create a Verification Subsystem into the destination model.
The Verification Subsystem is composed of a MATLAB function containing the code read in 
from the MATLAB script file translated from AGREE.

To use, place the addVerificationSubSys.m and the MATLAB script translated from AGREE
into a folder under the MATLAB search path.

Invoke the addVerificationSubSys function via the MATLAB command line like the following:
addVerificationSubSys('srcMdlName', 'destMdlName', 'verifySysName', 'scriptName')

Example:
addVerificationSubSys('MC_ToyC_sl_Before2.slx', 'MC_ToyC_sl_After_Auto2.slx', 'MC_ToyC', 'check_top_level_Impl_Instance.m')

This script has been tested with MATLAB versions 2014b and 2015b.