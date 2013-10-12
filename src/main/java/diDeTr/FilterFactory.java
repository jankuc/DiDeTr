package diDeTr;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.*;
import wekaexamples.filters.FilterTypeEnum;

/**
 * Created by kucerj28@fjfi.cvut.cz on 10/5/13.
 *
 *
 */
public class FilterFactory {
    private FilterTypeEnum type;
    private Filter filter;


    public FilterTypeEnum getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(FilterTypeEnum type) {
        this.type = type;
    }

    public Filter getFilter() {
        return filter;
    }

    /**
     *
     *
     * @param type ... FilterTypeEnum.{PCA, STANDARDIZE, NORMALIZE, PROJECT}
     * @param args
     * @throws Exception
     */
    public void initFilter(FilterTypeEnum type, Object args[]) throws Exception {
        this.type = type;

        if (type == FilterTypeEnum.PCA) {
            PrincipalComponents principalComponents = new PrincipalComponents();
            principalComponents.setOptions(new String[]{"-R","0.95"});
            filter = (Filter) principalComponents;

        } else if (type == FilterTypeEnum.STANDARDIZE) {
            filter = new Standardize();

        } else if (type == FilterTypeEnum.NORMALIZE) {
            filter = new Normalize();

        } else if (type == FilterTypeEnum.PROJECT) {
            AttributeSelection attributeSelection= new AttributeSelection();
            String[] options = new String[]{"-S","weka.attributeSelection.BestFirst " +  "-P " + Utils.printIntArrayAsCSV((int[]) args[0]) + ""};
            attributeSelection.setOptions(options);
            filter = (Filter) attributeSelection;
        }
    }

    /**
     * After this step, there are less variables in the Instances: <br/>
     *     PCA: 0.95 <br/>
     *     <br/>
     * If type==STANDARDIZE || NORMALIZE => we first standardize/normalize then we rank the scores and project the new variables via new filter => transform() is used 2times.
     *
     *
     * @param instances
     * @return
     * @throws Exception
     */
    public Instances transform(Instances instances) throws Exception {
        filter.setInputFormat(instances);
        Instances newInstances = Filter.useFilter(instances, filter);

        if (type.equals("standard") || type.equals("normal")) {

            int[] newAttributes = rankAttributes();

            FilterFactory filterFactory = new FilterFactory();
            filterFactory.initFilter(FilterTypeEnum.PROJECT, new Object[]{newAttributes});
            return filterFactory.transform(instances);
        }
        return newInstances;
    }


    private int[] rankAttributes() {
        throw new UnsupportedOperationException();
    }
}
