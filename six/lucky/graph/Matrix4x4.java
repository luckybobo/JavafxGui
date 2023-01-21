package six.lucky.graph;

public class Matrix4x4 {
    double[][] mat= new double[4][4];
    public Matrix4x4(){
        setToIdentity();
    }
    public void setToIdentity(){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                mat[i][j]=(i==j?1:0);
            }
        }
    }
    public void clear(){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                mat[i][j]=0;
            }
        }
    }
    public void clone(Matrix4x4 m){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                mat[i][j]=m.mat[i][j];
            }
        }
    }
    public static Matrix4x4 multiply(Matrix4x4 m1,Matrix4x4 m2) {
        Matrix4x4 ret=new Matrix4x4();
        ret.clear();
        for (int i = 0;i < 4;++i) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    ret.mat[i][j] += m1.mat[i][k] * m2.mat[k][j];
                }
            }
        }
        return ret;
    }
    public static Vector3D multiply(Matrix4x4 m,Vector3D v){
        return new Vector3D(
                m.get(0,0)*v.getX()+m.get(0,1)*v.getY()+m.get(0,2)*v.getZ()+m.get(0,3)*v.getW(),
                m.get(1,0)*v.getX()+m.get(1,1)*v.getY()+m.get(1,2)*v.getZ()+m.get(1,3)*v.getW(),
                m.get(2,0)*v.getX()+m.get(2,1)*v.getY()+m.get(2,2)*v.getZ()+m.get(2,3)*v.getW(),
                m.get(3,0)*v.getX()+m.get(3,1)*v.getY()+m.get(3,2)*v.getZ()+m.get(3,3)*v.getW()
        );
    }
    public double get(int i,int j){return mat[i][j];}
    public void set(int i,int j,double v){mat[i][j]=v;}
    public void print(){
        for(int i=0;i<4;++i){
            for(int j=0;j<4;++j){
                System.out.print(mat[i][j]);
                System.out.print('\t');
            }
            System.out.print('\n');
        }
    }
}
