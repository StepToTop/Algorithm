package xxl;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class Map {
    private int K;
    private int M;
    private int N;
    private int[][] initChess;
    private State OperateP;

    private TreeMap<String, State> Step;
    private TreeSet<State> Result;
    private TreeMap<String, String> Flag;

    public Map (int k, int m, int n, int[] init) {
        this.K = k;
        this.M = m;
        this.N = n;
        this.Step = new TreeMap<String, State>();
        this.Result = new TreeSet<State>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                if (o1.getScore() == o2.getScore()) {
                    if (o1.getNo().equals(o2.getNo())) {
                        return 0;
                    }
                    return 1;
                }
                return o2.getScore() - o1.getScore();
            }
        });
        this.Flag = new TreeMap<String, String>();


        initChess = new int[n][m];
        int i = 0;
        for(int j = 0; j < m; j++) {
            for (int t = 0; t < n; t++) {
                initChess[t][j] = init[i++];
            }
        }
        this.Step.put("0", new State(initChess, this.N, this.M, 0, "0"));

        OperateP = this.Step.get("0");//目前操作状态就是初始棋盘。
        /*State test = new State(OperateP.getState(), this.N, this.M, 4, "0C0");
        test = this.carpet(test);
        debugPrint(test.getState());*/
        ////////////////////////////////

        State tempC = this.carpet(OperateP);
        tempC.setNo(tempC.getNo() + "C");
        this.Step.put(tempC.getNo(), tempC);

        this.Flag.put(tempC.getList(), tempC.getNo());

        long startNs, endNs;
        startNs = System.nanoTime();
        play(this.Step.get("0C"), 1,true );
        endNs = System.nanoTime();
        System.out.println(this.Step.size());
        System.out.println(this.Result.size());
        bestSolutionDebugPrint(this.Result.first().getNo());
        System.out.println(this.Result.first().getScore() + "分");
        System.out.println("用时" + (endNs - startNs) + "纳秒");
        System.out.println(this.Result.first().getNo());
    }

    private State carpet (State temp) { //优化点，全局搜索
        State tempS = new State(temp.getState(), this.N, this.M, temp.getScore(), temp.getNo());
        int[][] Chess = tempS.getState();
        int[][] trap = new int[this.N][this.M];

        int Number = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.M; j++) {
                if (Chess[i][j] == 0) {
                    trap[i][j] = 1;
                    continue;
                }

                int xrCount = this.findXR(Chess, i, j);
                int ybCount = this.findYB(Chess, i, j);
                int xlCount = this.findXL(Chess, i, j);
                int ytCount = this.findYT(Chess, i, j);
                /*if (xrCount < 3 && ybCount < 3) {
                    trap[i][j] = 1;
                    continue;
                }*/

                if (xrCount + xlCount > 3 || ybCount + ytCount > 3) {
                    trap[i][j] = 0;
                    Number ++;
                } else {
                    trap[i][j] = 1;
                }

                //tempS.crossClear(j, i, 1, xrCount, 1, ybCount, getScore(xrCount) + getScore(ybCount));
            }
        }

        if (Number != 0) {
            int score = 0;
            int count = 0;

            for (int i = 0; i < this.N; i++) { //列
                for (int j = 0; j < this.M; j++) {
                    count += trap[i][j] == 1?0:1;
                    if (count != 0&& trap[i][j] == 1) {
                        score += this.getScore(count);
                        count = 0;
                    }
                }
                score += this.getScore(count);
                count = 0;
            }

            for (int j = 0; j < this.M; j++) { //行
                for (int i = 0; i < this.N; i++) {
                    count += trap[i][j] == 1?0:1;
                    if (count != 0 && trap[i][j] == 1) { //预清空
                        score += this.getScore(count);
                        count = 0;
                    }
                }
                score += this.getScore(count);
                count = 0;
            }

            tempS.fire(trap, score);
            return this.carpet(tempS);
        }
        return temp;
    }

    private int findXR (int[][] Chess, int x, int y) {
        int count = 1;

        if (x >= this.N) {
            return count;
        }

        int temp = Chess[x][y];

        for (int i = x + 1; i < this.N; i++) {
            if (Chess[i][y] == temp) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    private int findXL (int[][] Chess, int x,int y) {
        int count = 1;

        if (x <= 0) {
            return count;
        }

        int temp = Chess[x][y];

        for (int i = x - 1; i >= 0; i--) {
            if (Chess[i][y] == temp) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    private int findYB (int[][] Chess, int x, int y) {
        int count = 1;

        if (y >= this.M) {
            return count;
        }

        int temp = Chess[x][y];

        for (int i = y + 1; i < this.M; i++) {
            if (Chess[x][i] == temp) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    private int findYT (int[][] Chess, int x, int y) {
        int count = 1;

        if (y <= 0) {
            return count;
        }

        int temp = Chess[x][y];

        for (int i = y - 1; i >= 0; i--) {
            if (Chess[x][i] == temp) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    private int getScore(int count) {
        int score;
        if (count < 0) {
            return 0;
        }
        switch (count) {
            case 0:
            case 1:
            case 2:
                score = 0;
                break;
            case 3:
                score = 1;
                break;
            case 4:
                score = 4;
                break;
            default:
                score = 0;
        }
        return score;
    }

    private int play(State temp, int times, boolean findEnd) {
        int[][] chess = temp.getState();

        int number = 0;
        for (int j = 0; j < this.M; j++) {
            for (int i = 0; i < this.N; i++) {
                if (chess[i][j] == 0) {
                    continue;
                }

                if (i < this.N - 1) {
                    if (chess[i + 1][j] != 0 && chess[i][j] != chess[i + 1][j]) {
                        swap(chess, i, j, true);
                        //换过来的点
                        int Lxl = findXL(chess, i, j);
                        int Lyt = findYT(chess, i, j);
                        int Lyb = findYB(chess, i, j);

                        //换过去的点
                        int Ryt = findYT(chess, i+1, j);
                        int Rxr = findXR(chess, i+1, j);
                        int Ryb = findYB(chess, i+1, j);
                        if (Lxl >= 3 || Lyt + Lyb - 1 >=3 || Rxr >= 3 || Ryt + Ryb - 1 >= 3) {
                            //不一样的
                            State tempS = new State(chess, this.N, this.M, temp.getScore(), String.format("%s%d", temp.getNo(), number++));
                            tempS.crossClear(i, j, Lxl, 1, Lyt, Lyb, getScore(Lxl) + getScore(Lyt + Lyb - 1));
                            tempS.crossClear(i + 1, j, 1, Rxr, Ryt, Ryb, getScore(Rxr) + getScore(Ryt + Ryb - 1));
                            this.Step.put(tempS.getNo(), tempS);
                            State tempC = this.carpet(tempS);

                            //if (!this.Flag.containsKey(tempC.getList())) {
                                tempC.setNo(tempC.getNo() + "C");
                                this.Step.put(tempC.getNo(), tempC);
                                this.Flag.put(tempC.getList(), tempC.getNo());
                                if (findEnd) {
                                    if(this.play(tempC, 0, true) == 0) {
                                        this.Result.add(tempC);
                                    }
                                }else {
                                    if (times > 1) {
                                        if(this.play(tempC, times - 1, false) == 0) {
                                            this.Result.add(tempC);
                                        }
                                    } else {
                                        this.Result.add(tempC);
                                    }
                                }

                            //}


                        }
                        swap(chess, i, j, true);
                    }
                }

                if (j < this.M - 1) {
                    if (chess[i][j + 1] != 0 && chess[i][j] != chess[i][j + 1]) {
                        swap(chess, i, j, false);
                        //换过来的点
                        int Txl = findXL(chess, i, j);
                        int Txr = findXR(chess, i, j);
                        int Tyt = findYT(chess, i, j);

                        //换过去的点
                        int Bxl = findXL(chess, i, j + 1);
                        int Bxr = findXR(chess, i, j + 1);
                        int Byb = findYB(chess, i, j + 1);
                        if (Tyt >= 3 || Txl + Txr - 1 >=3 || Byb >= 3 || Bxl + Bxr - 1 >= 3) {
                            State tempS = new State(chess, this.N, this.M, temp.getScore(), String.format("%s%d", temp.getNo(), number++));
                            tempS.crossClear(i, j, Txl, Txr, Tyt, 1, getScore(Tyt) + getScore(Txl + Txr - 1));
                            tempS.crossClear(i, j + 1, Bxl, Bxr, 1, Byb, getScore(Byb) + getScore(Bxl + Bxr - 1));
                            this.Step.put(tempS.getNo(), tempS);
                            State tempC = this.carpet(tempS);

                            //if (!this.Flag.containsKey(tempC.getList())) {
                                tempC.setNo(tempC.getNo() + "C");
                                this.Step.put(tempC.getNo(), tempC);
                                this.Flag.put(tempC.getList(), tempC.getNo());
                                if (findEnd) {
                                    if(this.play(tempC, 0, true) == 0) {
                                        this.Result.add(tempC);
                                    }
                                }else {
                                    if (times > 1) {
                                        if(this.play(tempC, times - 1, false) == 0) {
                                            this.Result.add(tempC);
                                        }
                                    } else {
                                        this.Result.add(tempC);
                                    }
                                }

                            //}
                        }
                        swap(chess, i, j, false);
                    }
                }
            }
        }
        return number;

    }



    private void swap(int[][] Chess, int x, int y, boolean Right) {
        int temp = Chess[x][y];
        if (Right) {
            Chess[x][y] = Chess[x + 1][y];
            Chess[x + 1][y] = temp;
            return ;
        }
        Chess[x][y] = Chess[x][y + 1];
        Chess[x][y + 1] = temp;
    }

    private void debugPrint(int[][] chess) {
        for (int i = 0; i < this.M; i++) {
            for(int j = 0; j < this.N; j++) {
                System.out.print(chess[j][i] + " ");
            }
            System.out.println();
        }
    }

    private void bestSolutionDebugPrint(String list) {
        String temp = "";
        String[] part = list.split("C");
        for (int i = 0; i < part.length; i++) {
            temp = temp + part[i];
            debugPrint(this.Step.get(temp).getState());
            System.out.println("after clear");
            temp = temp + "C";
            debugPrint(this.Step.get(temp).getState());
            System.out.println("next Step");
        }
    }
}
