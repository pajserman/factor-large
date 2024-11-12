import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter; // Import the FileWriter class
import java.io.IOException; // Import the IOException class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriteToFile {

    public static void writeMatrix(int[][] M) {
        try {
            FileWriter myWriter = new FileWriter("in.txt");
            myWriter.write("" + M.length + " " + M[0].length + "\n");
            for (int i = 0; i < M.length; i++) {
                for (int j = 0; j < M[0].length; j++) {
                    myWriter.write(M[i][j] + " ");
                }
                myWriter.write("\n");
            }
            myWriter.close();
            // System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> getPrimes(double amount) {
        try {
            File myObj = new File("primes.txt");
            Scanner myReader = new Scanner(myObj);
            ArrayList<Integer> list = new ArrayList<>();
            double count = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    list.add(Integer.parseInt(parts[i]));
                    count++;
                }
                if (count >= amount)
                    break;
            }
            myReader.close();
            return list;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new ArrayList<Integer>();
        }
    }

    public static int[][] getData() {
        try {
            File myObj = new File("out.txt");
            Scanner myReader = new Scanner(myObj);
            int length = Integer.parseInt(myReader.nextLine());
            int[][] rows = new int[length][];
            int count = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");
                int[] numbers = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    numbers[i] = Integer.parseInt(parts[i]);
                }
                rows[count] = numbers;
                count++;
            }
            myReader.close();
            return rows;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new int[3][3];
        }
    }
}