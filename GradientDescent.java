package gradientdescentapp;

public class GradientDescent {

    public String[] nameСolumn; // имена таблицы *
    public int C, R; // column, row
    public float MU, S; // max - min, среднее для столбца, стандартное отклонение
    public float[][] dataTable; // float данные

    GradientDescent(dataFromFile dff) { // читаем в конструкторе класс, содержащий 2 массива - заголовок с названиями столбцов и данные в 2-мерном массиве
        nameСolumn = dff.getHeadline();
        dataTable = dff.getData();
        C = nameСolumn.length;
        R = dataTable.length;
    }

    public float[][] getReadArr() {
        return dataTable;
    }

    public float[] getNormalizeColumn(String str) { // нормализация столбца
        float[] nc = new float[R];
//        System.out.println("-- " + inArr.length);
        float mu, max, min; // среднее и макс-мин столбца
        int col = getColumn(str);
//        System.out.println("-- " + col);
        mu = dataTable[0][col];
        max = dataTable[0][col];
        min = dataTable[0][col];
        for (int i = 1; i < R; i++) {
            mu += dataTable[i][col];
            if (max < dataTable[i][col]) {
                max = dataTable[i][col];
            }
            if (min > dataTable[i][col]) {
                min = dataTable[i][col];
            }
        }

        MU = mu / R;
        S = 0; // стандартное отклонение

        for (int i = 0; i < R; i++) {
            S = S + (dataTable[i][col] - MU) * (dataTable[i][col] - MU);
        }

        S = (float) Math.sqrt(S / R);

        for (int i = 0; i < R; i++) {
            nc[i] = (dataTable[i][col] - MU) / S;
        }
//        System.out.print("mu " + MU);
//        System.out.print("  s " + S);
//        System.out.println("  max " + max + "  min " + min);

//        for (int i = 0; i < R; i++) {
//            System.out.println(i + " > " + nc[i]);
//        }
        return nc;
    }

    public float[] getNormalColumn(String str) { // восстановление из нормализованных данных
        float[] nc = getNormalizeColumn(str);
        float[] res = new float[R];
        for (int i = 0; i < R; i++) {
            res[i] = nc[i] * S + MU;
        }
        for (int i = 0; i < R; i++) {
            System.out.println(i + " - " + res[i]);
        }
        return res;
    }

    public int getColumn(String s) { // определяем номер столбца по его имени
        int res = -1;
        for (int i = 1; i < C; i++) {
            if (nameСolumn[i] == null ? s == null : nameСolumn[i].equals(s)) {
                res = i;
                break;
            }

        }
        return res;
    }

    public void displayDataTable() { // вывод таблицы
        System.out.println("DataTable");
        for (int i = 0; i < C; i++) {
            System.out.printf("%9s", nameСolumn[i]);
        }
        System.out.println("");
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                System.out.printf("%9.3f", dataTable[i][j]);
            }
            System.out.println("");
        }
    }

    public boolean[] getCrossValidIndex(int k, int iter) { // массив boolean на основе DataTable с флагами 0 - training и 1 - test; k - число подвыборок; iter - номер итерации
        boolean[] testFlag = new boolean[R];
        if (iter >= k) {
            System.out.println("превышение");
            return null;
        }
        double size = (double) R / k;
        System.out.println("size " + size);
        int dimSize = (int) Math.round(size);
        int lastDimSize = R - dimSize * (k - 1);
        if (lastDimSize <= 0) {
            System.out.println("параметры не подходят");
            return null;
        }

        System.out.println("dimSize " + dimSize + " - " + "lastDimSize " + lastDimSize);

        int startTest = (int) (iter * Math.round(size));
        int endTest;
        if ((dimSize * iter + lastDimSize) >= R) {
            System.out.println("111");
            endTest = startTest + lastDimSize - 1;
        } else {
            System.out.println("222");
            endTest = startTest + dimSize - 1;
        }

        System.out.println("start " + startTest + " end " + endTest);

        for (int i = startTest; i <= endTest; i++) {
            testFlag[i] = true;
        }

//        for (int i = 0; i < R; i++) {
//            System.out.println(i  + " " + testFlag[i]);
//        }
        return testFlag;
    }

    public float[][] getCrossValidTestArr(int k, int iter) { // массив данных для теста -  k - число подвыборок; iter - номер итерации
        boolean[] flag = getCrossValidIndex(k, iter);
        if (flag == null) {
            return null;
        }
        int size = 0;
        for (int i = 0; i < R; i++) {
            if (flag[i]) {
                size++;
            }
        }
        float res[][] = new float[size][];
        int iTest = 0;
        for (int i = 0; i < R; i++) {
            if (flag[i]) {
                res[iTest] = dataTable[i];
                iTest++;
            }
        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < C; j++) {
                System.out.printf("%9.3f", res[i][j]);
            }
            System.out.println("");
        }
        return res;
    }

    public float[][] getCrossValidTrainArr(int k, int iter) { // // массив данных для обучения -  k - число подвыборок; iter - номер итерации
        boolean[] flag = getCrossValidIndex(k, iter);
        if (flag == null) {
            return null;
        }
        int size = 0;
        for (int i = 0; i < R; i++) {
            if (!flag[i]) {
                size++;
            }
        }
        float res[][] = new float[size][];
        int iTest = 0;
        for (int i = 0; i < R; i++) {
            if (!flag[i]) {
                res[iTest] = dataTable[i];
                iTest++;
            }
        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < C; j++) {
                System.out.printf("%9.3f", res[i][j]);
            }
            System.out.println("");
        }
        return res;
    }

    public float getWArr(String x, String y) { // поиск W для нормализованных X и Y

        float[] XN;
        float[] YN;
        float w1 = 0;
        float w2 = 0;
        XN = getNormalizeColumn(x);
        YN = getNormalizeColumn(y);

//        for (int i = 0; i < inArr.length; i++) {
//            res [i][0] = XN [i];
//            res [i][1] = YN [i];
//        }
//        for (int i = 0; i < R; i++) {
//            System.out.println(i + "-  x- " + XN[i] + "   y- " + YN[i]);
//        }
        for (int i = 0; i < R; i++) {
            w1 = w1 + XN[i] * YN[i];
            w2 = w2 + XN[i] * XN[i];
        }

//        System.out.println("W1 " + w1 + "   W2 " + w2 + "    W " + w1 / w2);
        return w1 / w2;

    }

    public float MSE(String x, String y) {
        float w = getWArr(x, y);
        float[] XN;
        float[] YN;
        XN = getNormalizeColumn(x);
        YN = getNormalizeColumn(y);
        float res = 0;
        for (int i = 0; i < R; i++) {
            res = res + (XN[i] * w - YN[i]) * (XN[i] * w - YN[i]);
//            System.out.println("- " + res);
        }
//        System.out.println("W " + w);
        return res / R;
    }

    public float gradientDescentNew(String x, String y, float tolerance, float step) { // град. спуск

        float[] XN;
        float[] YN;
        XN = getNormalizeColumn(x);
        YN = getNormalizeColumn(y);
        float deltaW = 0;
        float wi = 1;
        do {

            for (int i = 1; i < R; i++) { // ищем deltaW для коэффициента wi
                deltaW = deltaW + (YN[i] - XN[i] * wi);
            }            
            deltaW = deltaW / R;

            wi = wi - deltaW * step; // шаг для след. итерации While

            System.out.println("wi " + wi + "  deltaW " + deltaW);

        } while (deltaW > tolerance);
        return wi; // вывод wi где deltaW < точности tolerance
    }

}
