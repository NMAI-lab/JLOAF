% example: [a,b] = learnBNet(['traces-forcefourraydistance/trace-m0-ForceStraightLineAgent.txt';'traces-forcefourraydistance/trace-m1-ForceStraightLineAgent.txt'],10,2)

function [bnet,engine] = learnBNetGMM(traces,XSIZE,YSIZE)

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

% Detect the variales that are discrete:
var_size = max(data);
discrete = [];

for i = 1:VARS
	disc = 1;
	for j = 1:ncases
		disc = disc * (round(data(j,i))==data(j,i));
        if disc == 0
            break;
        end
	end
	if disc 
		discrete = [discrete ; i];
		var_size(i) = max(var_size(i),2);
	else 
		var_size(i) = 1;
	end
end

% Y depends on the rest: Ensure that all Gaussian nodes are after the class
% node and all discrete nodes are before in terms of heirachy
% DEFINED BY SACHA
for x = XSIZE+1:XSIZE+YSIZE
	for y = 1:XSIZE
      if(find(discrete==y))
        dag(y,x) = 1;  
      else
        dag(x,y) = 1;   
      end
	end
end

bnet = mk_bnet(dag, var_size, 'discrete', discrete);

for i = 1:VARS
	if ismember(i,discrete)
        bnet.CPD{i} = tabular_CPD(bnet, i);
	else
		bnet.CPD{i} = gaussian_CPD(bnet, i);
	end
end

%setting the directions to the correct distribution
%bnet.CPD{1} = vonMises_CPD(bnet,1);
%bnet.CPD{2} = vonMises_CPD(bnet,2);
%bnet.CPD{2} = vonMises_CPD(bnet, 2);
%bnet.CPD{4} = vonMises_CPD(bnet, 4);
%bnet.CPD{9} = vonMises_CPD(bnet, 9);

%removes the missing values from the cases
for i = 1:length(cases)*VARS
    if cases{i}==6.6
        cases{i}=[];
    end
end

engine = jtree_inf_engine(bnet);
%engine = likelihood_weighting_inf_engine(bnet); %use if chnodes->dnodes exist
%engine = var_elim_inf_engine(bnet);
[bnet,~, engine] = learn_params_em(engine, cases, 20, 1e-4);
%bnet = renormalizeDBNdistributions(bnet);
%engine = jtree_inf_engine(bnet);
%engine = likelihood_weighting_inf_engine(bnet);
