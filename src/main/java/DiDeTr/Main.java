package DiDeTr;

import wekaexamples.filters.FilterTypeEnum;

/**
 * Created by kucerj28@fjfi.cvut.cz on 10/6/13.
 */
public class Main {

   public static void main(String[] args) throws Exception {

       DiDeTr diDeTr = new DiDeTr();
       diDeTr.loadIrisArff();
       diDeTr.createSets();

       int[] newAttributes = new int[]{1,2,3};
       Object[] arguments = new Object[]{newAttributes};
       FilterFactory filterFactory = new FilterFactory();
       filterFactory.initFilter(FilterTypeEnum.PROJECT, arguments);
       filterFactory.transform(diDeTr.getTrainingSet());

       System.out.println(filterFactory);

   }

}
