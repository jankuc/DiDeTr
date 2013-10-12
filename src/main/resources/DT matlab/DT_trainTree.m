function [indeces H divergence]=DT_trainSupervisedTree(data, weights, nHist,maxNClusters,nMin, flags)
%
%[indeces divergence]=DT_trainTree(data,nHist,maxNClusters,nMin)
%
%   data ......... data, ktera budeme delit
%   nHist ........ prednastaveni deleni histogramu. 2D histogram bude mit nHist*nHist binu 
%   maxNClusters . maximalni pocet shluku, ktery chceme obdrzet
%   nMin ......... minimalni pocet bodu ve shluku, abychom ho jeste delili.
%
%   indeces ...... vektor priuslusnosti dat k jednotlivym shlukum (podle indexu)
%                       size = (n,1)                   
%   divergence .... vektor prispevku jednotlivych shluku k vysledne divergenci
%                       size = (maxNClusters,1)
%   flags flagy prislusnosti data k signalu

%data = DT_transformData(data,'PCAcov');
plot(data(:,2),data(:,3),'r+')
%title('data')

%divMin = 0; % pokud nastavim nenulovou hodnotu, shluky s hodnotou divergence < divMin nebudou dale deleny.
[n,dim] = size(data);
indeces=ones(n,1); % vektor indexu znacicich, ke kteremu shluku dane datum patri
divergence= - Inf * ones(maxNClusters,1); % vektor hodnot divergenci danych shluku
divergence(1) = 1; % delime vzdy shluk s nejvyssim prispevkem a ted chceme delit 1.

odlozeneShluky=zeros(maxNClusters,1); % vektor priznaku {0,1} jestli je shluk s timto indexem zakazano stepit.
odkladame = 1; % nastavime na 1, pokud nektere shluky nechceme dale delit. toto kriterium se tam ale pak musi pridat

%% priprava, na pocitani histogramu kazdeho nodu
histDim = 2; % number of parameters in histogram we want saved (dimension)
histDim = min(histDim,dim);
dataHist = DT_transformData(data,'PCAcov'); % data, ktera pouzijeme pro tvoreni histogramu
dataHist = dataHist(:,1:histDim);
H = cell(maxNClusters,1); % in here we'll store all of the histograms
H{1} = DT_histcn(dataHist, 'AccumData',weights) / n;

for cDeleni = 1:maxNClusters-1
    
    divergenceTmp = divergence; % na divergenceTmp budu delat nejake osklive upravy a divergence si necham nezmenene.
    [~, index] = max(divergenceTmp(divergenceTmp < inf)); %najde shluk s nejvetsim prispevkem
    while odlozeneShluky(index) == 1 || sum(indeces == index) < nMin % pokud je tento shluk v odlozenych, nebo ma malo dat najdu si dalsi s nejvetsim atd.
        divergenceTmp(index) = -inf;
        [~, index] = max(divergenceTmp);
        if divergenceTmp(index) == -inf
            break
        end
    end
    
    if max(divergenceTmp) == -inf
        disp('Vsechny shluky uz jsou odlozene nebo maji malo dat, takze koncime.');
        break;
    end
    disp(strcat('delim shluk c._',num2str(index)))
    % ted mam index shluku s nejvetsim prispevkem
    [indeces, prispevek1, prispevek2]=DT_divideNode(data(indeces == index,:), weights(indeces == index,:) ,indeces, index ,nHist, flags(indeces == index,:)); %deleni prvniho shluku
    divergence(index) = prispevek1; % v prispevcich musim zmenit  hodnoty stepeneho clusteru
    divergence(max(indeces)) = prispevek2; % a noveho clusteru
    
    % actualization of histograms
    ind = index;
    H{ind} = DT_histcn(dataHist(indeces == ind, :), 'AccumData',weights(indeces == ind)) / sum(indeces == ind);
    ind = max(indeces);
    H{ind} = DT_histcn(dataHist(indeces == ind, :), 'AccumData',weights(indeces == ind)) / sum(indeces == ind);
    
    if odkladame
        for k = 1:length(odlozeneShluky)
             if var(flags(indeces==k))==0 %  shluk je homogenni
                odlozeneShluky(k) = 1;
             else
                 odlozeneShluky(k) = 0;
             end
        end
    end
    figure;
    scatter(data(:,2), data(:,3),10*indeces,indeces,'+');
    colormap(jet(maxNClusters))
    title(strcat('po deleni na ', num2str(cDeleni+1), ' shluk{y/u}.'));
    colorbar
end


