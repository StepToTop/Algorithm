package sortMethod;

public class Sort  {
    private long startNs, endNs;
    private int n, a[], method;
    private long Time[],sum;

    public Sort(int size, int type) { //构造函数，初始化
        method = type;
        Time = new long[20];
        n = size;
        sum = 0;
    }

    private void GenerateArray() { //生成0~10,000,000的随机数组
        a = new int[n];
        for (int i = 0; i <  n; i++) {
            a[i] = (int)(Math.random()*10000000);
        }
    }

    private void Bubble_sort() {  //冒泡排序
        for (int i = n - 1; i > 0; i--) {  //要遍历n - 1遍，第k遍的时候，倒数k个数字已经是有序状态
            for (int j = 0; j < i; j++) {  //每次遍历k次，时间复杂度为O(n)
                if (a[j] > a[j + 1]) {  //将大的数往后排
                    int temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }

    private void Quick_sort(int l, int r) { //数组遍历一次的时间复杂度为O(n)，每一次遍历的复杂度为一颗二叉树的深度
        if(l >= r)  //左右重合，已经是尽头
            return ;
        int i = l, j = r, temp = a[i];  //取左右尽头的数为哨兵和最左边的数为基准值
        while (i < j) {
            while (i < j && a[j] > temp) { //哨兵j左移直到找到一个数k小于等于基准值
                j--;
            }
            if (i < j) { //最左边的基准值被替换为小数k
                a[i++] = a[j];
            }
            while (i < j && a[i] < temp) { //哨兵i往右走，寻找在哨兵j左边大于等于基准值的值
                i++;
            }
            if (i < j) { //如果存在，则取缔数字k所在的位置，不存在，就将基准值安放在数字k的位置
                a[j--] = a[i];
            }
        }
        a[i] = temp; //填补基准值到一个排序好的序前面一位，以分割得前后两数组都比基准值大/小，说明得到一个已经排好得数
        Quick_sort(l, i-1);
        Quick_sort(i+1, r);
    }

    private void Select_sort() { //遍历n个数，循环n-1次；
        int min;
        for (int i = 0; i < n; i++) {
            min = i;  //先假定i是最小的数的数位，找到最小的那个数，i之前的数已经是最小的了
            for(int j = i + 1; j < n; j++) {
                if (a[j] < a[min])
                    min = j;
            }
            if (min != i) { //如果存在比假定位更小的数位，就进行交换
                int temp;
                temp = a[i];
                a[i] = a[min];
                a[min] = temp;
            }
        }
    }

    private void Insert_sort() { //双循环，O(n^2)
        for (int i = 1; i < n; i++) { //i之前的数默认是已经排列好了的序
            int j;
            for (j = i-1; j >= 0; j--) { //往前一个一个地找，找到比a[i]大的数
                if (a[j] < a[i]) { //如果没有，就将i后移一位
                    break;
                }
            }
            if (j != i - 1) { //如果找到了合适的位置了，就一位一位的往后挪
                int temp = a[i],k;
                for (k = i - 1; k > j; k--) {
                    a[k + 1] = a[k];
                }
                a[k + 1] = temp;
            }
        }
    }

    private void Merge_divide(int start, int end) { //归并，先分离后排序
        if(start >= end) {
            return ;
        }
        int mid = (end + start)/2;  //每次分离都一分为二
        Merge_divide(start, mid);
        Merge_divide(mid+1, end);
        merge(start, mid, end); //分离到一组一个数的时候将进行归并
    }

    private void merge(int start, int mid, int end) {//分配到临时数组，进行一种类似选择排序的操作，然后替换到原数组
        int[] temp = new int[end - start + 1];
        int i = start, k = 0, j = mid + 1;
        while(i <= mid && j <= end) {
            if (a[i] <= a[j]) {
                temp[k++] = a[i++];
            } else {
                temp[k++] = a[j++];
            }
        }
        while(i <= mid) {
            temp[k++] = a[i++];
        } // 将剩下的都整合进去，此时一方已经到了尽头，另外一方剩下的比已有的大
        while(j <= end) {
            temp[k++] = a[j++];
        }
        for (i = 0; i < k; i++) { //将排序后的重新整合进原数组
            a[start + i] = temp[i];
        }
    }

    public void run() {
        for (int i = 0; i < 20; i++ ) {
            GenerateArray();
            startNs = System.nanoTime(); //获取开始的时间
            switch(method) {
                case 1:
                    Bubble_sort();
                    break;
                case 2:
                    Select_sort();
                    break;
                case 3:
                    Insert_sort();
                    break;
                case 4:
                    Merge_divide(0, n - 1);
                    break;
                default:
                    Quick_sort(0, n - 1);
                    break;
            }
            endNs = System.nanoTime(); //获取结束的时间
            Time[i] = endNs - startNs; //这个就是程序运行的时间
            sum += Time[i];
        }
        //debug(); //用于输出调试
        report(); //输出时间
    }

    private void debug() {
        for(int i=0; i<n; i++) {
            System.out.printf("%d ",a[i]);
        }
        System.out.println();
    }

    private void report() {
        String type;
        switch (method) {
            case 1:
                type = "冒泡排序";
                break;
            case 2:
                type = "选择排序";
                break;
            case 3:
                type = "插入排序";
                break;
            case 4:
                type = "归并排序";
                break;
            default:
                type = "快速排序";
                break;
        }
        System.out.printf("对%d个随机数进行%s所需平均时间为%d纳秒\n",n,type,sum/20);
        for (int i = 0; i < 20; i++) {
            System.out.printf("%d ", Time[i]);
        }
        System.out.println();
    }
}
