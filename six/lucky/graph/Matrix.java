package six.lucky.graph;
public class Matrix {
    private double[][] mat;
    public Matrix(int a, int b){
        mat=new double[a][b];
        setToIdentity();
    }
    public Matrix(Matrix m){
        clone(m);
    }
    public void setToIdentity(){
        int i=0;
        for(double[] ceil: mat){
            for(int j=0;j<ceil.length;++j){
                mat[i][j]=(i==j?1:0);
                System.out.println(mat[i][j]);
            }
            i++;
        }
    }
    public void clear(){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                mat[i][j]=0;
            }
        }
    }
    public void clone(Matrix m){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                mat[i][j]=m.mat[i][j];
            }
        }
    }
    public static Matrix multiply(Matrix m1, Matrix m2) {
        Matrix ret=new Matrix(m2.size()[0],m2.size()[1]);
        ret.clear();
        for (int i = 0;i < ret.size()[0];++i) {
            for (int j = 0; j < ret.size()[0]; j++) {
                for (int k = 0; k < ret.size()[0]; k++) {
                    ret.mat[i][j] += m1.mat[i][k] * m2.mat[k][j];
                }
            }
        }
        return ret;
    }
    public double at(int i,int j){return mat[i][j];}
    public void st(int i,int j,double v){mat[i][j]=v;}
    public int[] size(){
        int i=0;
        for (double[] ceil:mat) {
            for (double ignored :ceil){
                i++;
            }
        }
        return new int[]{mat.length,i};
    }
}
