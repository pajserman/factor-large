import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Scanner;

public class WriteToFile {

    public static void main(String[] args) {
        int[] a = readNumbersFromFile("primes.txt", 1000);
        for (int i : a) {
            System.out.println(i);
        }
    }

    public static int[] readNumbersFromFile(String filePath, int numValues) {
        System.out.println("Loading primies from primes.txt (" + numValues + "st)");
        ArrayList<Integer> numbersList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            // Read line by line
            while ((line = reader.readLine()) != null && count < numValues) {
                // Split each line by spaces
                String[] numberStrings = line.trim().split("\\s+");

                // Process each number in the line
                for (String numberStr : numberStrings) {
                    if (count < numValues) { // Stop if we've reached the specified number of values
                        numbersList.add(Integer.parseInt(numberStr));
                        count++;
                    } else {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("File contains non-integer values.");
            e.printStackTrace();
        }

        // Convert ArrayList to int array
        int[] numbers = new int[numbersList.size()];
        for (int i = 0; i < numbersList.size(); i++) {
            numbers[i] = numbersList.get(i);
        }

        return numbers;
    }

    public static int[] getPrimes(int amount) {
        try {
            File myObj = new File("primes.txt");
            Scanner myReader = new Scanner(myObj);
            ArrayList<Integer> list = new ArrayList<>();
            int count = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parts = data.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    list.add(Integer.parseInt(parts[i]));
                    count++;
                }
                if (count >= amount)
                    myReader.close();
                break;
            }

            myReader.close();

            int[] intArray = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                intArray[i] = list.get(i);
            }

            for (int num : intArray) {
                System.out.print(num + " ");
            }

            return intArray;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new int[1];
        }
    }

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

    public static void removeNewlinesFromFile(String filePath) {
        try {
            // Read the file content as a single string
            String content = Files.readString(Path.of(filePath));

            // Remove all newline characters
            content = content.replace("\n", "");

            // Write the modified content back to the file
            Files.writeString(Path.of(filePath), content, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Newline characters removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}