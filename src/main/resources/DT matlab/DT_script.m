
%% Creating of data
%-----------------------
%
%
% program loads data only once, then it is counting with them. If we want
% to create it anew, we have to set
%     dataAlreadyDefined = 0;

% amount of data
nTraining = 2000;
nTesting = 1000;
nTypesOfSignal = 12;

% Nacteni dat. Dulezity je jen radek
% [data, flags, dataAlreadyDefined] = DT_loadFNALData(nTraining,nTesting);
try 
    if dataAlreadyDefined && nTraining==nTrainingOld && nTesting==nTestingOld
        disp('Data are already defined, I continue without loading them.');
    else
        [data, flags, weights dataAlreadyDefined] = DT_loadFNALData(nTraining,nTesting);
        nTrainingOld = nTraining;
        nTestingOld = nTesting;
    end
catch 
    [data, flags, weights, dataAlreadyDefined] = DT_loadFNALData(nTraining,nTesting);
    nTrainingOld = nTraining;
    nTestingOld = nTesting;
end

%% treninkova data
% pro nacitani jen fnalovskych tam dat do podminky 0
% jinak jsou to jen treninkova data
trenink = 1;
if trenink
    mu1 = 0;
    sigma1 = 1;
    mu2 = 0;
    sigma2 = 3;
    n1 = 300;
    n2 = 100;
    dim = 5;
    data = [(randn(n1,dim) - mu1)/sigma1^2; (randn(n2,dim) - mu2)/sigma2^2];
    flags = [ones(n1,1); 2*ones(n2,1)];
    weights = [ones(size(data,1),1)];
    dataAlreadyDefined = 0;
end

%% TOP5
if 0
    data = dataTOP5;
    weights = ones(size(data,1),1);
    dataAlreadyDefined = 0;
    flags = (1:size(data,1))';
    disp('nactena TOP5 data');
end

%% Training Divergence Tree
% --------------------------

close all 
%matlabpool open local
maxNClusters = 10;
nHist = 3;
nMin = 50; % minimal amount of data in the divided node. If the node has less data, we don't divide it.

[indeces, H, divergences] = DT_trainTree(data, weights, nHist, maxNClusters, nMin, flags);


%% Plotting of Histograms
for k = 1: 0%length(H)
    if ~isempty(H{k})
        figure
        surf(H{k});
        title(strcat('Histogram of node  ', num2str(k),'.'))
    end
end

%% Analysis of training data:

nTrainDataInCluster = zeros(maxNClusters,nTypesOfSignal);
% every row is one node 
% every column is one type of signal (tb, tqb, QCD,diboson,...)
for k = 1:size(nTrainDataInCluster,1)
    for l = 1:size(nTrainDataInCluster,2)
        nTrainDataInCluster(k,l) = sum(indeces==k & flags==l);
    end
end

% P ... matrix of probabilities, that the node contains given type of signal
P = zeros(maxNClusters,nTypesOfSignal);
for k = 1:size(P,1)
    P(k,:) = nTrainDataInCluster(k,:) ./ sum(nTrainDataInCluster(k,:));
end
if trenink
    P = P(:,1:2);
end
sizeOfCluster = sum(nTrainDataInCluster,2);
P = P(1:length(sizeOfCluster(sizeOfCluster>0)),:);
P
if trenink
    %P = P(1:length(sizeOfCluster(sizeOfCluster>0)),:);
    PP = round(P) .* [sizeOfCluster(sizeOfCluster()>0) sizeOfCluster(sizeOfCluster>0)];
    disp([[PP;1/0 1/0; sum(PP,1)]]);
end
%% Analysis of testing data:

nTestDataInCluster = zeros(maxNClusters,nTypesOfSignal);
% every row is one node 
% every column is one type of signal (tb, tqb, QCD,diboson,...)
for k = 1:size(P,1)
    for l = 1:size(nTestDataInCluster,2)
        nTestDataInCluster(k,l) = sum(indeces==k & flags==100 + l);
    end
end
% Q ... the same as P, but for testing datas 
Q = zeros(maxNClusters,nTypesOfSignal);
for k = 1:size(P,1)
   Q(k,:) = nTestDataInCluster(k,:) / sum(nTestDataInCluster(k,:));
end
if ~trenink
    Q
end



% display number of data (train and test) in each cluster and its addition to overall
 disp('cluster   nTrain    nTest    divergence')
 disp(' ')
 disp([(1:maxNClusters)', sum(nTrainDataInCluster,2), sum(nTestDataInCluster,2), divergences]) 


%% ROC
% test
% for k = 1:maxNClusters
%     
% end
% 
% res = [P(:,1), ]



