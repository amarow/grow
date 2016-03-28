package de.ama.grow.util;

import java.awt.*;
import java.io.*;
import java.util.Random;


public class Util {

    private static Random random = new Random(Integer.MAX_VALUE);


    public static void sleep(int pause) {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
        }
    }


    public static Color darker(Color in, float factor) {
        return new Color(
                Math.max((int) (in.getRed() * factor), 0),
                Math.max((int) (in.getGreen() * factor), 0),
                Math.max((int) (in.getBlue() * factor), 0),
                in.getAlpha());
    }

    public static Color brighter(Color in, float factor) {
        int r = in.getRed();
        int g = in.getGreen();
        int b = in.getBlue();
        int alpha = in.getAlpha();
        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new Color(Math.min((int) (r / factor), 255),
                Math.min((int) (g / factor), 255),
                Math.min((int) (b / factor), 255),
                alpha);
    }

    public static Color randomColor() {
        return new Color(random.nextInt());
    }

    public static String[] removeBracetSequence(String str, String startChars, String stopChars) {
        String[] ret = new String[]{"", ""};

        int startBracet = str.indexOf(startChars);
        int stopBracet = str.indexOf(stopChars);

        if (startBracet > 0 && stopBracet > 0) {
            String a = str.substring(0, startBracet);
            String b = str.substring(stopBracet + 1);
            ret[0] = a + b;
            ret[1] = str.substring(startBracet, stopBracet);
        }
        return ret;
    }


    public static boolean isNotEmpty(String in) {
        return !isEmpty(in);
    }

    public static boolean isEmpty(String in) {
        return in == null || in.isEmpty();
    }

    public static String readFile(File file) {
        if (!file.exists()) {
            throw new RuntimeException("File does not exist: " + file);
        }
        String LF = System.getProperty("line.separator");

        StringBuilder contents = new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(LF);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

    public static void saveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
