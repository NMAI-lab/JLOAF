function [state] = getInitialState(input, dbn, engine)
%Getting the initial state given the observation
%   Calculates the state by using the evidence

%ss = dbn.nnodes_per_slice; %number of nodes per slice
%t = 2; %time slices
evidence = cell(1,length(dbn.CPD));

for i = 1:length(input)
    evidence{i+1} = input(i); % input from java
end
%Tstate = convert_to_table(dbn.CPD{1}, family(dbn.dag,1), evidence);
%[~, state] = max(Tstate);
engine = enter_evidence(engine,evidence);
marg = marginal_nodes(engine,1);
state = max(marg);
end

