package diDeTr.tutorial;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.evaluation.CrossValidation;
import net.sf.javaml.classification.evaluation.PerformanceMeasure;
import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.featureselection.scoring.GainRatio;
import net.sf.javaml.tools.data.FileHandler;
import net.sf.javaml.tools.weka.WekaAttributeSelection;
import net.sf.javaml.tools.weka.WekaClassifier;
import net.sf.javaml.tools.weka.WekaClusterer;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.functions.SMO;
import weka.clusterers.SimpleKMeans;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by kucerj28@fjfi.cvut.cz on 9/29/13.
 */
public class JMLTutorial {

    public static void wekaClusterTry() throws Exception {
        Dataset data = FileHandler.loadDataset(new File("iris.data"), 4, ",");

        SimpleKMeans xm = new SimpleKMeans();
        xm.setNumClusters(3);
            /* Wrap Weka clusterer in bridge */
        Clusterer jmlxm = new WekaClusterer(xm);
            /* Perform clustering */
        Dataset[] clusters = jmlxm.cluster(data);
            /* Output results */
        System.out.println(clusters.length);

        for (int i = 0; i < clusters.length; i++) {
            System.out.println("Cluster " + i + "[numOfData = " + clusters[i].size() + "]: ");
            System.out.println(clusters[i]);
        }
    }

    public static void wekaClassifierTry() throws IOException {
        /* Load data */
        Dataset data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
/* Create Weka classifier */
        SMO smo = new SMO();
/* Wrap Weka classifier in bridge */
        Classifier javamlsmo = new WekaClassifier(smo);
/* Initialize cross-validation */
        CrossValidation cv = new CrossValidation(javamlsmo);
/* Perform cross-validation */
        Map<Object, PerformanceMeasure> pm = cv.crossValidation(data);

/* Output results */
        System.out.println(pm);
    }


    public static void featureScoring() throws IOException {

        // all scoring methods implement following method:
        // public double score(int attIndex);

            /* Load the iris data set */
        Dataset data = FileHandler.loadDataset(new File("iris.data"),4, ",");
            /* Create a feature scoring algorithm */
        GainRatio ga = new GainRatio();
            /* Apply the algorithm to the data set */
        ga.build(data);
            /* Print out the score of each attribute */
        for (int i = 0; i < ga.noAttributes(); i++)
            System.out.println(ga.score(i));
    }

    public static void featureRanking() throws IOException {

        // all scoring methods implement following method:
        // public int rank(int attIndex);

            /* Load the iris data set */
        Dataset data = FileHandler.loadDataset(new File("iris.data"),4, ",");
            /* Create a feature ranking algorithm */
        RecursiveFeatureEliminationSVM svmrfe = new RecursiveFeatureEliminationSVM(0.2);
            /* Apply the algorithm to the data set */
        svmrfe.build(data);
            /* Print out the score of each attribute */
        for (int i = 0; i < svmrfe.noAttributes(); i++)
            System.out.println(svmrfe.rank(i));
    }

    public static void wekaFeatureSelection() throws IOException{
        /* Load the iris data set */
        Dataset data = FileHandler.loadDataset(new File("iris.data"), 4, ",");
            /*Create a Weka AS Evaluation algorithm */
        ASEvaluation eval = new GainRatioAttributeEval();
            /* Create a Weka's AS Search algorithm */
        ASSearch search = new Ranker();
            /* Wrap WEKAs' Algorithms in bridge */
        WekaAttributeSelection wekaattrsel = new WekaAttributeSelection(eval,search);
            /* Apply the algorithm to the data set */
        wekaattrsel.build(data);
            /* Print out the score and rank of each attribute */
        for (int i = 0; i < wekaattrsel.noAttributes()-1; i++)
        System.out.println("Attribute " + i + " Ranks " + wekaattrsel.rank(i));
    }
}
