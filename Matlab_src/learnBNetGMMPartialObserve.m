% example: [a,b] = learnBNet(['traces-forcefourraydistance/trace-m0-ForceStraightLineAgent.txt';'traces-forcefourraydistance/trace-m1-ForceStraightLineAgent.txt'],10,2)

function [bnet,engine] = learnBNetGMMPartialObserve(traces,XSIZE,YSIZE)

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
% node and all discrete nodes connect to the gaussian node as well
% Ideally we have a set structure where the class node is connected to each
% Gaussian but the discrete nodes are only connceted to a specific gaussian
% So the incoming data has to be identified somehow. 
% DEFINED BY SACHA (Requires hardcoding for now)

%goalSeenR
dag(3,7)=1;dag(3,9)=1;dag(10,3)=1;
%goalSeenL
dag(5,1)=1;dag(5,4)=1;dag(10,5)=1;
%BallSeen
dag(6,2)=1;dag(6,8)=1;dag(10,6)=1;
%Action - connecting the action to each cts node
for index=1:XSIZE
    if ~ismember(index,discrete)
        dag(VARS,index)=1;
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

%setting the angle data to vonMises d
%bnet.CPD{2} = vonMises_CPD(bnet, 2);
%bnet.CPD{4} = vonMises_CPD(bnet, 4);
%bnet.CPD{9} = vonMises_CPD(bnet, 9);

bnet = learn_params(bnet, cases);
engine = jtree_inf_engine(bnet);

