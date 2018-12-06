import bridge.*;

public class Main {

    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            System.out.println("///////////在"+ i*1000 +"个点，"+ 2000*i +"条边的情况下///////////");
            int j = 3;
            long dfs = 0, union = 0;
            while(j-- != 0) {
                new GenJ("D:\\Algorithm\\src\\bridge\\testD", i * 1000, i * 2000);
                //new Deal();
                dfs += (new Deal()).newDFSDeal();
                union += (new Deal()).UnionNewDeal();
            }
            System.out.println("///////////DFS："+ dfs/3000000 +"毫秒；Union:"+ union/3000000 +"毫秒///////////");

        }
        /*(new Deal()).newDFSDeal();
        (new Deal()).UnionDeal();
        (new Deal()).UnionNewDeal();*/
    }

}