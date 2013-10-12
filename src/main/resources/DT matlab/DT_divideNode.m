function [indexNew, prispevek1, prispevek2] = DT_divideNode(clusterForDivision, weights, indexOld, indexOfDividedCluster,  nHist, flags, divisionType)
% [indexNew, prispevek1, prispevek2] = TreeDiv_stepeniA(clusterForDivision, indexOld, indexOfDividedCluster,  nHist)
%
% clusterForDivision = data, ktera budeme delit
% weights = vahy dat
% indexOld = indexy VSECH dat, ne jen tech v clusterForDivision
% indexOf DividedCluster = index shluku, ktery se deli
% flags = flagy jednotlivych druhu signalu
% nHist = prednastaveni deleni histogramu

n=size(clusterForDivision,1);
%% Transformace dat
dataTransfed = DT_transformData(clusterForDivision,'pcacov');
dim = size(dataTransfed,2);

%% VSECHNA DELENI DO DVOU SHLUKU

divergence = @divRenyi;
alpha = 0.8;
numOfPC = 15; % maximalni pocet promennych se kterymi budeme pocitat
dimOfHist = 2; % dimenze empiricke distribucni funkce. MAXimalne 7, vic matlab nezvlande pametove a navic je to blbost

numOfCountedVars = min(numOfPC,dim); % bude pocitat jen par hlavnich komponent, nebo vsechny dimenze
varCombinations = combnk(1:numOfCountedVars,dimOfHist); % matice kombinaci komponent

Dmin  = inf*ones(size(varCombinations,1),1);
D1min = inf*ones(size(varCombinations,1),1);
D2min = inf*ones(size(varCombinations,1),1);
I = cell(size(varCombinations,1),1);
kk = 0;
while kk == 0%Dmin == inf % vetsinou klasifikace probehne spravne, ale obcas ne, takze ji zkusi udelat znovu
    kk = kk + 1;
    
    for k = 1:size(varCombinations,1)
        dataNew = dataTransfed(:,varCombinations(k,:)); % vyber novych parametru z transformovanych dat
        for l = 1:3 % shlukovka probehne vickrat aby to vybralo nejlepsi shluky.
            indeces = DT_data2Clusters(dataNew,flags, divisionType);
            I{k} = indeces;
            
            cluster1 = dataNew((indeces==1),:);
            n1 = size(cluster1,1);
            cluster2 = dataNew((indeces==2),:);
            n2 = size(cluster2,1);
            
            H1 = DT_histcn(cluster1, 'AccumData',weights(indeces==1)) / n1; % empiricke distribuce s vahami
            H2 = DT_histcn(cluster2, 'AccumData',weights(indeces==2)) / n2;
            H = DT_histcn(dataNew, 'AccumData',weights) / n;
            
            %D1 = feval(divergence,H,H1,alpha);
            %D2 = feval(divergence,H,H2,alpha);
            D1min(k) = divergence(H,H1,alpha);
            D2min(k) =  divergence(H,H2,alpha);
            Dmin(k) = (n1*D1min(k) + n2*D2min(k))/n;
%             Dnew = (n1*D1 + n2*D2)/n;
%             if Dnew < Dmin
%                 indexBest = indeces;
%                 Dmin = Dnew;
%                 D1min = D1;
%                 D2min = D2;
%             end
        end
    end
    
    if kk > 5
        disp('Zacykeni v kmeans pri deleni shluku. Chtel jsem udelat shluk s jednim datem, coz je pro nasledny histogram nepripustne. Ukoncete pomoci prikazu dbquit.');
        keyboard
    end
end

[~,minInd] = min(Dmin); % ktere deleni  bylo nejlepsi
indexBest = DT_data2Clusters(dataTransfed(:,varCombinations(minInd,:)),flags);

prispevek1=(sum(I{minInd} == 1)/n)*D1min(minInd);
prispevek2=(sum(I{minInd} == 2)/n)*D2min(minInd);

% precislovani indexBest, tak aby mel unikatni indexy vzhledem k indexOld.
indexBest(indexBest==1) = -1;
indexBest(indexBest==2) = max(indexOld) + 1; % jeden z novych shluku bude mit novy index
indexBest(indexBest==-1) = indexOfDividedCluster; % druhy bude mit index puvodniho deleneho shluku.

figure;
scatter(clusterForDivision(:,1), clusterForDivision(:,2),10*indexBest,indexBest,'+');
colormap(jet(max(indexBest)))
title('deleni Shluku');

indexNew = indexOld;
indexNew( indexNew(:,end)==indexOfDividedCluster, end) = indexBest;

end