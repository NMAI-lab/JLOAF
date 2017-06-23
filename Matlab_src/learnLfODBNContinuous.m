% Example:
% [a,b] = learnLfODBN(['traces-forcefourraydistance/trace-m0-ForceStraightLineAgent.txt';'traces-forcefourraydistance/trace-m1-ForceStraightLineAgent.txt'], 10,4,10,2)

function [bnet,engine] = learnLfODBNContinuous(traces,EMIterations,STATES,XSIZE,YSIZE)
%function [bnet,engine] = learnLfODBN(traces,EMIterations,STATES,XSIZE,YSIZE)

CSIZE = 1;
VARS = CSIZE+XSIZE+YSIZE;	% we add one for the internal state
alldata = [];
data = [];
cases = cell(1,size(traces,1));
for i = 1:size(traces,1)
	data = load(traces(i,:));
%   data = data(1:100,:); 
	alldata = [alldata ; data];
	seqlen = size(data, 1);		% number of data points
	datac = cell(VARS,seqlen);
%	disp(['data size: ' num2str(size(data))]);
%	disp(['datac size: ' num2str(size(datac))]);
	datac([CSIZE+1:VARS], :) = num2cell(data');
%	datac
	cases{i} = datac;
end

intra = zeros(VARS,VARS);			% we will have VARS variables
% C depends on X:
for c = 1:CSIZE
  for x = 1:XSIZE
	intra(x+CSIZE,c) = 1;
  end
end
% Y depends on X and C:
for y = 1:YSIZE
	for x = 1:XSIZE
	  intra(x+CSIZE,y+CSIZE+XSIZE) = 1;
	end
	for c = 1:CSIZE
	  intra(c,y+CSIZE+XSIZE) = 1;
	end
end
inter = zeros(VARS,VARS);			% we will have VARS variables
% C depends on C-1:
for c1 = 1:CSIZE
  for c2 = 1:CSIZE
	inter(c1,c2) = 1;
  end
end
% C depends on Y-1:
for y = 1:YSIZE
	for c = 1:CSIZE
  		inter(y+CSIZE+XSIZE,c) = 1;
	end
end

% observable nodes:	
onodes = [CSIZE+1:VARS];

% equivalence classes:
eclass1 = [1:VARS];									% slice 1
eclass2 = [VARS+1:VARS+CSIZE CSIZE+1:VARS];			% slice 2
eclass = [eclass1 eclass2];
eclasses = max(eclass);	

% Detect the variales that are discrete:
var_size = [STATES max(alldata)];
discrete = [1];
for i = 1:VARS-1
	disc = 1;
	for j = 1:size(alldata,1)
		disc = disc * (round(alldata(j,i))==alldata(j,i));
		if alldata(j,i)<1
			disc = 0;
		end
	end
	if disc 
		discrete = [discrete i+1];
		if var_size(i+1) == 1
			var_size(i+1) = 2;
		end	
	else 
		var_size(i+1) = 1;
	end
end
%alldata

disp(['all data size: ' num2str(size(alldata))]);
disp(['Equivalence classes: ' num2str(eclasses)]);
disp(['var_size: ' num2str(var_size)]);
disp(['discrete: ' num2str(discrete)]);
%alldata(1:10,:)

cnodes = mysetdiff(1:VARS, discrete);%calculate cnodes

bnet = mk_dbn(intra, inter, var_size, 'discrete', discrete, 'eclass1', eclass1, 'eclass2', eclass2, 'observed', onodes);	
for i = 1:VARS
	j1 = eclass1(i);
	j2 = eclass2(i);
	if ismember(i,discrete)
        parents = bnet.parents(i); % get parents for specific node
        parents = parents{1,1}; % change from cell to array
        dcs = myintersect(cnodes,parents);% compare cnodes and parents of the current node
        if(~isempty(dcs))
            bnet.CPD{j1} = softmax_CPD(bnet, j1);
            bnet.CPD{j2} = softmax_CPD(bnet, j2);
        else
            bnet.CPD{j1} = tabular_CPD(bnet, j1);
            bnet.CPD{j2} = tabular_CPD(bnet, j2);
        end
	else
		bnet.CPD{j1} = gaussian_CPD(bnet, j1);
		bnet.CPD{j2} = gaussian_CPD(bnet, j2);
	end
end
%bnet = renormalizeDBNdistributions(bnet);

engine = smoother_engine(jtree_2TBN_inf_engine(bnet));
%engine = jtree_dbn_inf_engine(bnet);
%engine = jtree_unrolled_dbn_inf_engine(bnet, 5);
%engine = smoother_engine(hmm_2TBN_inf_engine(bnet));
disp('Learning... LFODBN');	
%bnet = learn_params_em(engine, cases, 'max_iter', EMIterations);
[bnet, LLtrace] = learn_params_dbn_em(engine, cases, 'max_iter', EMIterations);
%bnet = renormalizeDBNdistributions(bnet);
%engine = jtree_dbn_inf_engine(bnet);
%engine = jtree_unrolled_dbn_inf_engine(bnet, 5);
engine = smoother_engine(jtree_2TBN_inf_engine(bnet));
