function indeces = DT_data2Clusters(data, flags, type)

numOfTypes = 5;

types = {'kmeans', 'fuzzy', 'mbc', 'chou_kmeans'};



if strcmp(type, types{1})
	%% kmeans
	indeces = kmeans(data,2);
elseif strcmp(type, types{2}) 
	%% fuzzy
    e = 1;
    a = 1;
    b = 1;
    F = 1;
    indeces = rfuzzy2(e,a,data,b,F);
elseif strcmp(type, types{3})
	%% mbc
	boolTrain = flags < 100;
	indeces = MBC_dismix([data(boolTrain,:) flags(boolTrain,:)],[data(~boolTrain,:) flags(~boolTrain,:)]);
elseif strcmp(type, types{4})
	%% chou_kmeans
	indeces = DT_chou_KMeans(data,2);
else 
	%% error (wrong parameter)
	for k = 1:length(types)
		types{k} = [types{k}, ' '];
	end
	error('Specified separation method does not exist. Please choose one of: \n %s', [types{:}])
end