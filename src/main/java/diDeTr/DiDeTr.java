package diDeTr;

import org.apache.commons.math.util.MathUtils;
import weka.classifiers.functions.LeastMedSq;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import wekaexamples.filters.FilterTypeEnum;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by kucerj28@fjfi.cvut.cz on 9/29/13.
 */
public class DiDeTr {

    private Instances dataset;
    private Instances trainingSet;
    private Instances testingSet;
    private Instances validatingSet;
    private Instances workingSet;
    private ArrayList<Node> nodes;

    private FilterFactory filterFactory;



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

    public void init(FilterTypeEnum filterTypeEnum) throws Exception {
        filterFactory = new FilterFactory();
        filterFactory.initFilter(filterTypeEnum, null);
    }


    public void divideNode(int index) throws Exception {

        workingSet = filterFactory.transform(workingSet);

        int sliceDim = 2;
        int[][] slices = getSlices(sliceDim);



    }

    private int[][] getSlices(int sliceDim) {
        int numOfCombinations = 0;
        try {
            numOfCombinations = LeastMedSq.combinations(workingSet.numAttributes(), sliceDim);
        } catch (Exception e) {
            System.out.println("You've tried to create slice of of dimension greater than the input dimension.");
            System.exit(1);
        }
        int[][] slices = new int[numOfCombinations][sliceDim];
        Combination combination = new Combination(workingSet.numAttributes(),sliceDim);
        int i = 0;
        while (combination.hasNext()) {
            slices[i] =  combination.next();
        }
        return slices;
    }


    /**
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Getters and Setters %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     */


    public Instances getDataset() {
        return dataset;
    }

    public void setDataset(Instances dataset) {
        this.dataset = dataset;
    }

    public Instances getTrainingSet() {
        return trainingSet;
    }

    public Instances getTestingSet() {
        return testingSet;
    }

    public Instances getValidatingSet() {
        return validatingSet;
    }
}

