package gradientdescentapp;

public class dataFromFile { // два массива в классе при чтении из файла

    private String[] headline;
    private float[][] data;

    public dataFromFile(String[] h, float[][] d) {
        headline = h;
        data = d;
    }

    public String[] getHeadline() {
        return headline;
    }

    public float[][] getData() {
        return data;
    }
}
