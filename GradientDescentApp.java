package gradientdescentapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GradientDescentApp {

    public static dataFromFile ReadFile(String filepath) { // чтение массива из файла

        String line;
        String[] lineArr;
        String[] headline;
        float[][] data;

        try {
            File inFile = new File(filepath);
            FileReader fr = new FileReader(inFile);
            BufferedReader reader = new BufferedReader(fr);
            headline = reader.readLine().split(",");
            ArrayList<String> linesDataTable = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                linesDataTable.add(line);
            }
            data = new float[linesDataTable.size()][headline.length];
            for (int i = 0; i < linesDataTable.size(); i++) {
                lineArr = linesDataTable.get(i).split(",");
                for (int j = 0; j < headline.length; j++) {
                    data[i][j] = Float.parseFloat(lineArr[j]);
                }
            }
            fr.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return new dataFromFile(headline, data);
    }

    public static void main(String[] args) {
        dataFromFile rd = ReadFile("src\\GradientDescentApp\\boston_housing.csv"); // читаем в 2 массива

        GradientDescent gd = new GradientDescent(rd); // создаем класс с разделенными данными из файла для обработки

        System.out.println("W : " + gd.getWArr("RM", "MEDV")); //вычисляем w для нормализованных x y (RM MEDV)
        System.out.println("----------------------------------------");

        System.out.println("MSE : " + gd.MSE("RM", "MEDV"));
        System.out.println("----------------------------------------");   


        System.out.println("find : " + gd.gradientDescentNew("RM", "MEDV", 0.0000001f, 0.3f)); 

//////////////////////////////////////// работа с частью исходных данных
//        System.out.println("----------------------------------------");
//
//        gd.getCrossValidTestArr(10,0); // выделение части массива для теста (делим на 10 частей, 0 итерация из 9)
//        gd.getCrossValidTrainArr(10,0); // выделение части массива для тренировки (делим на 10 частей, 0 итерация из 9)
    }
}
