function [action] = getAction(input, dbn, engine)
%getAction takes the input and state and predicts the most likely action
%   no details

ss = dbn.nnodes_per_slice; %number of nodes per slice
t = 2; %time slices
evidence = cell(ss,t);

for i = 1:length(input)
    evidence{i} = input(i); % input from java
end
pos = length(input)+1;
%Taction = convert_to_table(dbn.CPD{pos}, family(dbn.dag,pos), evidence);
%[~, action] = max(Taction);

engine = enter_evidence(engine,evidence);
marg = marginal_nodes(engine,pos);
action = max(marg);

end

