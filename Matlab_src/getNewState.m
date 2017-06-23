function [ newState ] = getNewState(input, state, action, dbn)
%Get new state given past action, past state and current observation
%   The input contains all the information required to perform inference

ss = dbn.nnodes_per_slice; %number of nodes per slice
t = 2; %time slices
evidence = cell(ss,t);

for i = 1:length(input)
    evidence{i+ss+1} = input(i); % input from java start from 
end

evidence{1} = state; % add state 
evidence{2+length(input)} = action; % add action

pos = length(input)+3; % position of new State
TnewState = convert_to_table(dbn.CPD{pos}, family(dbn.dag,pos), evidence);
[~, newState] = max(TnewState);

end

