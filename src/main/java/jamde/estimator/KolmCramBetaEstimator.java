/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jamde.estimator;

import jamde.OtherUtils;
import jamde.distribution.Distribution;

/**
 * 
 * 
 * @author honza
 */
public class KolmCramBetaEstimator extends KolmCram{
    private double b;
    
    public KolmCramBetaEstimator(int p, int q, double b) {
        super(p,q);
        this.b = b;
    }
    
    @Override
    public double countDistance(Distribution distr, double[] data) {
        double m = 2* Math.pow(data.length, b);
        return super.countDistance(distr, data, m);
    }

    @Override
    public String getClassicTableName() {
        return("$ \\mathrm{KC}^\\frac{p}{q}_{2n^\\beta}, p="+OtherUtils.num2str(p) + ", \\quad q="+OtherUtils.num2str(q) + ", \\quad \\beta=" +OtherUtils.num2str(b) + "$");
    }
    
}
