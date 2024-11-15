import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class App {
    private final static int[] factorBase = WriteToFile.readNumbersFromFile("primes.txt", 1024);
    private final static BigInteger[] FACTOR_BASE = new BigInteger[factorBase.length];
    // private final static String N_IN = "92434447339770015548544881401"; // task 4
    private final static String N_IN = "149106521126845407396329"; // ditt nummer
    private final static BigInteger N = new BigInteger(N_IN);
    private final static int L = factorBase.length + 10;
    private static BigInteger[] rs = new BigInteger[L];

    public static void main(String[] args) throws Exception {
        // preparing factor base
        for (int i = 0; i < factorBase.length; i++) {
            FACTOR_BASE[i] = BigInteger.valueOf(factorBase[i]);
        }

        // starting timing
        long startTime = System.nanoTime();

        System.out.println("Calculating r values...");
        // generating smooth numbers, P and M matrix, storing r values in rs
        List<int[][]> PM = Pmatrix(false);

        // printMatrix(PM.get(0));
        // System.out.println("---------");
        // printMatrix(PM.get(1));

        int[][] P = PM.get(0);

        WriteToFile.writeMatrix(PM.get(1));

        System.out.println("Waiting for GaussBin.exe...");
        ProcessBuilder processBuilder = new ProcessBuilder("GaussBin.exe", "in.txt", "out.txt");
        Process process = processBuilder.start();

        // Wait for the process to finish
        process.waitFor();

        // for every solution multuply rows and try founding p and q
        int[][] solutions = WriteToFile.getData();

        for (int s = 0; s < solutions.length; s++) {
            BigInteger x = BigInteger.ONE;
            BigInteger y = BigInteger.ONE;
            int[] factors = new int[factorBase.length];
            for (int i = 0; i < solutions[s].length; i++) {
                if (solutions[s][i] == 1) {
                    // multiplying rows (x)
                    x = x.multiply(rs[i]);

                    // getting rows to multiply (y)
                    for (int j = 0; j < factorBase.length; j++) {
                        if (P[i][j] > 0) {
                            factors[j] += P[i][j];
                        }
                    }

                }

            }
            // flyttar ut tvån genom att ta index minux 2 samtidigt multiplerar jag ihop y
            for (int i = 0; i < factors.length; i++) {
                if (factors[i] > 0) {
                    factors[i] /= 2;
                    y = y.multiply(FACTOR_BASE[i].pow(factors[i])).mod(N);
                }
            }

            x = x.mod(N);
            System.out.println("Trying solution: " + (s + 1));
            System.out.println(x.toString() + " = " + y.toString());
            BigInteger a = y.subtract(x);
            BigInteger p = a.gcd(N);
            Boolean notAnswer = p.equals(BigInteger.ONE) || p.equals(N);
            if (!notAnswer) {
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("----------------------------");
                System.out.println("Found p and q:");
                System.out.println("p: " + p);
                System.out.println("q: " + N.divide(p));
                System.out.println("(" + duration / 1000000 + " ms)");
                System.out.println("----------------------------");

                break;
            }

        }

    }

    static void printMatrix(int[][] M) {
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }

    static List<int[][]> Pmatrix(boolean print) {
        int[][] P = new int[L][factorBase.length];
        int[][] M = new int[L][factorBase.length];

        int count = 0;
        for (int i = 1; i < L * 2; i++) {
            for (int j = 1; j < L * 2; j++) {
                if (count == L) {
                    System.out.println("Found Enough unique r values (" + L + " st)");
                    return Arrays.asList(P, M);
                }
                BigInteger r = likelySmooth(i, j);
                int[] primes = primeFactorsAndSmooth(r.pow(2).mod(N));
                if (primes[0] != -1) {
                    if (print) {
                        System.out.print(i + " " + j + " " + r + " " + r.pow(2).mod(N));
                        for (int l = 0; l < primes.length; l++) {
                            if (primes[l] != 0) {
                                System.out.print(" " + factorBase[l] + "(" + primes[l] + ")");
                            }
                        }
                        System.out.println();
                    }
                    int[] Pmod2 = mod2array(primes);
                    if (isUnique(M, Pmod2)) {
                        M[count] = Pmod2;
                        P[count] = primes;
                        rs[count] = r;
                        count++;
                    }

                }
            }
        }

        return Arrays.asList(P, M);
    }

    static boolean isUnique(int[][] M, int[] Pmod2) {
        for (int i = 0; i < M.length; i++) {
            int count = 0;
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] == Pmod2[j])
                    count++;
            }
            if (count == M[0].length) {
                // System.out.println("Found duplicate!");
                return false;
            }
        }
        return true;
    }

    static int[] mod2array(int[] a) {
        int[] temp = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            temp[i] = a[i] % 2;
        }
        return temp;
    }

    // retunerear r (inte r^2)
    static BigInteger likelySmooth(int k, int j) {
        return BigInteger.valueOf(k).multiply(N).sqrt().add(BigInteger.valueOf(j));
    }

    // retunerar en int array där placering mostvarar primtalet och värdet antalet
    // av den faktorn
    static int[] primeFactorsAndSmooth(BigInteger r) {

        int[] primes = new int[factorBase.length];
        for (int i = 0; i < factorBase.length; i++) {
            BigInteger[] temp = { BigInteger.ZERO, BigInteger.ZERO };
            Boolean done = false;
            BigInteger temp_r = r;
            while (!done) {
                temp = temp_r.divideAndRemainder(FACTOR_BASE[i]);
                if (temp[1].equals(BigInteger.ZERO)) {
                    temp_r = temp[0];
                    // System.out.println(temp[0] + " " + temp[1]);
                    primes[i]++;
                } else {
                    done = true;
                }
            }

        }
        BigInteger product = BigInteger.ONE;
        for (int j = 0; j < factorBase.length; j++) {
            if (primes[j] != 0) {
                product = product.multiply(FACTOR_BASE[j].pow(primes[j]));
            }
        }

        if (product.equals(r)) {
            return primes;
        } else {
            primes[0] = -1;
            return primes;
        }

    }
}