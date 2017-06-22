% example: [a,b] = learnBNet(['traces-forcefourraydistance/trace-m0-ForceStraightLineAgent.txt';'traces-forcefourraydistance/trace-m1-ForceStraightLineAgent.txt'],10,2)

function [bnet,engine] = learnBNet(traces,XSIZE,YSIZE)

VARS = XSIZE+YSIZE;
data = [];
for i = 1:size(traces,1)
  tmp = load(traces(i,:));
  data = [data ; tmp];
end
ncases = size(data, 1);			% number of data points
cases = cell(VARS,ncases);		% create an empty table to store the data to be given to the learning algorithm
cases(:,:) = num2cell(data(:,:)');	% copy the data
	
dag = zeros(VARS,VARS);			% we will have VARS variables
% Y depends on the rest:
for y = XSIZE+1:XSIZE+YSIZE
	for x = 1:XSIZE
	  dag(x,y) = 1;
	end
end

% Detect the variales that are discrete:
var_size = max(data);
discrete = [];

for i = 1:VARS
	disc = 1;
	for j = 1:ncases
		disc = disc * (round(data(j,i))==data(j,i));
	end
	if disc 
		discrete = [discrete ; i];
		var_size(i) = max(var_size(i),2);
	else 
		var_size(i) = 1;
	end
end

cnodes = mysetdiff(1:VARS, discrete);%calculate cnodes

bnet = mk_bnet(dag, var_size, 'discrete', discrete);

for i = 1:VARS
	if ismember(i,discrete)
        dcs = myintersect(children(dag,cnodes),i);
        if(~isempty(dcs)) %checks if the children of cts nodes intersect with and discrete nodes 
            bnet.CPD{i} = softmax_CPD(bnet,i);
        else
            bnet.CPD{i} = tabular_CPD(bnet, i);
        end
	else
		bnet.CPD{i} = gaussian_CPD(bnet, i);
	end
end

engine = jtree_inf_engine(bnet);
%engine = likelihood_weighting_inf_engine(bnet); %use if chnodes->dnodes exist
%engine = var_elim_inf_engine(bnet);
bnet = learn_params_em(engine, cases);
%bnet = renormalizeDBNdistributions(bnet);
%engine = jtree_inf_engine(bnet);
engine = likelihood_weighting_inf_engine(bnet);
