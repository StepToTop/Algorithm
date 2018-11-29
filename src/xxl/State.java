package xxl;

public class State {
    private int[][]state;
    private int M, N, Score;
    private String No, List;
    public State (int[][] chess, int m, int n, int score, String no) {
        this.M = m;
        this.N = n;
        this.No = no;
        this.Score = score;
        this.state = new int[M][N];
        for (int i = 0; i < M; i++) {
            this.state[i] = chess[i].clone();
        }
        this.setList();
    }

    public int[][] getState() {
        return this.state;
    }

    public void setNo(String no) {
        this.No = no;
    }

    public String getNo() {
        return this.No;
    }

    /*public void clear(int x, int y, int xCount, int yCount, int score) {
        int i, j, temp;
        this.Score += score;

        for (i = y + yCount; i >= 0; i--) {
            temp = 0;
            if (i - yCount - 1 >= 0) {
                temp = this.state[x][i - yCount - 1];
            }
            this.state[x][i] = temp;
        }

        for (i = x + 1; i <= x + xCount; i++) {
            for (j = y ; j >= 0; j--) {
                temp = 0;
                if (j - 1 >= 0) {
                    temp = this.state[i][j - 1];
                }
                this.state[i][j] = temp;
            }
        }
    }*/

    public void crossClear(int x, int y, int xLCount, int xRCount, int yTCount, int yBCount, int score) {
        if (score == 0) {
            return;
        }
        int i;
        this.Score += score;
        int yCount = yTCount + yBCount - 2, xCount = xLCount + xRCount - 2;

        int y1 = y - yTCount + 1;
        int x1 = x - xLCount + 1;

        yCount = yCount < 2?0:yCount;
        xCount = xCount < 2?0:xCount;

        for (i = y1 + yCount; i >= y1; i--) {  //如果没有count就只是消掉他自己
            this.state[x][i] = 0;
        }

        for (i = x1; i <= x1 + xCount; i++) {
            this.state[i][y] = 0;
        }

        fallDown();

    }

    public int getScore(){
        return this.Score;
    }

    public void fire(int[][]killR, int score) {
        this.Score += score;
        for (int i = 0; i < this.M; i++) {
            for (int j = 0; j < this.N; j++) {
                this.state[i][j] = this.state[i][j] * killR[i][j];
            }
        }
        fallDown();
    }

    private void fallDown() {

        for (int i = 0; i < this.M; i++) {
            int[] temp = new int[this.N];
            int k = this.N - 1;
            for (int j = this.N - 1; j >= 0; j--) {
                if (this.state[i][j] == 0) {
                    continue;
                }
                temp[k--] = this.state[i][j];
            }
            this.state[i] = temp.clone();
        }
        setList();
    }

    private void setList() {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < this.N; j++) {
            for (int i = 0; i < this.M; i++) {
                sb.append(this.state[i][j]);
            }
        }
        this.List = sb.toString();
    }

    public String getList() {
        return this.List;
    }

}
