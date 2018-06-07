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

discrete = [3;4;6;7;11];
var_size = [1   1    2     2     1     2     2     1    1     1    3];
%ballclose
dag(3,11)=1;
%goalSeenR
dag(4,8)=1;dag(4,10)=1;dag(4,11)=1;
%goalSeenL
dag(6,1)=1;dag(6,5)=1;dag(6,11)=1;
%BallSeen
dag(7,2)=1;dag(7,9)=1;dag(7,11)=1;
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

% for i = 1:length(cases)*VARS
%     if cases{i}==6.6
%         cases{i}=[];
%     end
% end

%setting the angle data to vonMises d
% bnet.CPD{2} = vonMises_CPD(bnet, 2);
% bnet.CPD{4} = vonMises_CPD(bnet, 4);
% bnet.CPD{9} = vonMises_CPD(bnet, 9);
% engine = jtree_inf_engine(bnet);
% [bnet,~, engine] = learn_params_em(engine, cases, 20, 1e-4);
bnet = learn_params(bnet, cases);
engine = jtree_inf_engine(bnet);
% 
% discrete = [3;5;6;10];
% var_size = [1   1     2     1     2     2     1     1     1     3];
% goalSeenR
% dag(3,7)=1;dag(3,9)=1;dag(3,10)=1;
% goalSeenL
% dag(5,1)=1;dag(5,4)=1;dag(5,10)=1;
% BallSeen
% dag(6,2)=1;dag(6,8)=1;dag(6,10)=1;

