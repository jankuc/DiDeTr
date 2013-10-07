package DiDeTr;

/**
 * Created by kucerj28@fjfi.cvut.cz on 9/30/13.
 */
public class Dataset {
    private double dim;
    private double n;
    private double[][] data;
    /**
     * Always has dimension n.
     */
    private double[] weights;
    /**
     * Always has dimension n.
     */
    private String[] name;

    public double getDim() {
        return dim;
    }

    public void setDim(double dim) {
        this.dim = dim;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }
}
