function addVerificationSubSys(srcMdlName, destMdlName, verifySysName, scriptName)

%% Copyright (c) 2021, Collins Aerospace.
%% Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
%% 
%% Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
%% including any software or models in source or binary form, as well as any drawings, specifications, 
%% and documentation (collectively "the Data"), to deal in the Data without restriction, including
%% without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
%% and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
%% subject to the following conditions:
%% 
%% The above copyright notice and this permission notice shall be included in all copies or 
%% substantial portions of the Data.
%% 
%% THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
%% LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
%% IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
%% FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
%% ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
%%
%% Example srcMdlName: 'MC_ToyC_sl_Before2.slx'
%% Example destMdlName: 'MC_ToyC_sl_After_Auto2.slx'
%% Example verifySysName: 'MC_ToyC'
%% Example scriptName: 'check_top_level_Impl_Instance.m'

src_path = which(srcMdlName);
[dir_path,~,~] = fileparts(src_path);
dest_path = fullfile(dir_path,destMdlName);
copyfile(src_path,dest_path,'f');
open_system(destMdlName);

%% Add Verification Block

dest_sys = regexp(destMdlName,'(\w+).(\w+)', 'tokens', 'once');
dest_sys = dest_sys{1};
sysToVerify = strcat(dest_sys, '/', verifySysName);

code = fileread(scriptName);

%% Find Name

name = regexp(code,'function\s+(\w+)\s*\(','tokens','once');
name = name{1};

%% Add Block

parent = get_param(sysToVerify,'Parent');

src = 'simulink/User-Defined Functions/MATLAB Function';
dst = horzcat(parent,'/',name);

sysThatVerifies = add_block(src,dst,...
    'MakeNameUnique','on',...
    'MaskType','VerificationSubsystem',...
    'Position',[560 450 695 500]);

sysThatVerifies = getfullname(sysThatVerifies);

%% Add Code

root = sfroot;

chart = root.find('-isa','Stateflow.EMChart','Path',sysThatVerifies);
chart.Script = code;

%% Connect I/O

src = get_param(sysToVerify,'LineHandles');
src = horzcat(src.Inport,src.Outport);
src = arrayfun(@(x) get_param(x,'SrcPortHandle'),src);

dst = get_param(sysThatVerifies,'PortHandles');
dst = horzcat(dst.Inport,dst.Outport);

for i = 1:numel(src)
    
    add_line(parent,src(i),dst(i),'autorouting','on');
end

end
