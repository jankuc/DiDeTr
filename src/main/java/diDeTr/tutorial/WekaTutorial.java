package diDeTr.tutorial;



import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.datagenerators.ClusterDefinition;
import weka.datagenerators.clusterers.SubspaceCluster;
import weka.datagenerators.clusterers.SubspaceClusterDefinition;


/**
 * Created by kucerj28@fjfi.cvut.cz on 10/1/13.
 */
public class WekaTutorial {
    public Instances dataset;
    public Instances trainingSet;
    public Instances testingSet;
    public Instances validatingSet;
    public Classifier classifier;
    public Clusterer clusterer;


    // http://weka.wikispaces.com/Programmatic+Use

    public void classificationTutorial() throws Exception {

        loadIrisArff();
        createSets();
        chooseClassifier();
        trainClassifier();
        testClassifier();
        useClassifier();

    }

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
        dataset = DataSource.read("iris.csv");
        dataset.setClassIndex(dataset.numAttributes() - 1);
    }

    public void loadIrisArff() throws Exception {
        dataset = DataSource.read("/home/honza/Documents/FJFI/diDeTr/iris.arff");
        dataset.setClassIndex(dataset.numAttributes() - 1);
    }

    public void chooseClassifier() {
        classifier = (Classifier) new NaiveBayesMultinomial();
    }

    public void trainClassifier() throws Exception {
        classifier.buildClassifier(trainingSet);
    }

    public void chooseClusterer() throws Exception {
        SubspaceCluster subClusterer = new SubspaceCluster();
        ClusterDefinition[] subspaceClusterDefinition = new SubspaceClusterDefinition[3];
        subClusterer.setClusterDefinitions(subspaceClusterDefinition);
    }

    public void trainClusterer() {
    }

    public void testClusterer() {

    }

    public void useClusterer() {

    }

    public void testClassifier() throws Exception {
        // (weka.classifiers.Evaluation) for testing classifier
        Evaluation eTest = new Evaluation(trainingSet);
        eTest.evaluateModel(classifier, testingSet);

        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);

        // Get the confusion matrix
        System.out.println("Confusion Matrix: ");
        double[][] cmMatrix = eTest.confusionMatrix();
        for (int i = 0; i < cmMatrix.length; i++) {
            for (int j = 0; j < cmMatrix[0].length; j++) {
                System.out.print(cmMatrix[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public void useClassifier() throws Exception {
        for (int i=0;i<validatingSet.numInstances();i++){
            double[] fDistribution = classifier.distributionForInstance(validatingSet.instance(i));
            System.out.println("" + Math.round(fDistribution[0]*100)/100.0 + "_" + Math.round(fDistribution[1]*100)/100.0 + "_" + Math.round(fDistribution[2]*100)/100.0 + "........."  + validatingSet.instance(i).classValue());
        }

    }
}
