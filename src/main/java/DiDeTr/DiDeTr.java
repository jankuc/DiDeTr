package DiDeTr;

import DiDeTr.Tutorial.WekaTutorial;
import weka.classifiers.trees.DecisionStump;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import wekaexamples.filters.FilterTypeEnum;

import static DiDeTr.Tutorial.JMLTutorial.*;

/**
 * Created by kucerj28@fjfi.cvut.cz on 9/29/13.
 */
public class DiDeTr {

    private Instances dataset;
    private Instances trainingSet;
    private Instances testingSet;
    private Instances validatingSet;

    private Filter filter;


    /**
     * it si divided into Three sets (60% train, 20% test, 20% validate )
     */
    public void createSets() {
        int n = dataset.numInstances();
        trainingSet = new Instances(dataset, (int) (dataset.numInstances() * 0.6));
        testingSet = new Instances(dataset, (int) (dataset.numInstances() * 0.2));
        validatingSet = new Instances(dataset, (int) (dataset.numInstances() * 0.2));

        for (int i = 0; i < n; i = i + 5) {
            trainingSet.add(dataset.instance(i));
        }
        for (int i = 1; i < n; i = i + 5) {
            testingSet.add(dataset.instance(i));
        }
        for (int i = 2; i < n; i = i + 5) {
            trainingSet.add(dataset.instance(i));
        }
        for (int i = 3; i < n; i = i + 5) {
            validatingSet.add(dataset.instance(i));
        }
        for (int i = 4; i < n; i = i + 5) {
            trainingSet.add(dataset.instance(i));
        }
    }

    public void loadIrisCsv() throws Exception {
        dataset = ConverterUtils.DataSource.read("iris.csv");
        dataset.setClassIndex(dataset.numAttributes() - 1);
    }

    public void loadIrisArff() throws Exception {
        dataset = ConverterUtils.DataSource.read("iris.arff");
        dataset.setClassIndex(dataset.numAttributes() - 1);
    }

    public Instances getDataset() {
        return dataset;
    }

    public void setDataset(Instances dataset) {
        this.dataset = dataset;
    }

    public Instances getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(Instances trainingSet) {
        this.trainingSet = trainingSet;
    }

    public Instances getTestingSet() {
        return testingSet;
    }

    public void setTestingSet(Instances testingSet) {
        this.testingSet = testingSet;
    }

    public Instances getValidatingSet() {
        return validatingSet;
    }

    public void setValidatingSet(Instances validatingSet) {
        this.validatingSet = validatingSet;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}

