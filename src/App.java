import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class App {
    private final static int[] factorBase = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };
    private final static BigInteger[] FACTOR_BASE = new BigInteger[factorBase.length];
    private final static String N_IN = "392742364277";
    private final static BigInteger N = new BigInteger(N_IN);
    private final static int L = 1024 + 10;
    private static int[] rs = new int[L];

    public static void main(String[] args) throws Exception {
        // saving N as bigInt

        // preparing factor base
        for (int i = 0; i < factorBase.length; i++) {
            FACTOR_BASE[i] = BigInteger.valueOf(factorBase[i]);
        }

        // generating smooth numbers, P and M matrix, storing r values in rs
        List<int[][]> PM = Pmatrix(false);

        // printMatrix(PM.get(0));
        // System.out.println("---------");
        // printMatrix(PM.get(1));

        int[][] P = PM.get(0);

        WriteToFile.writeMatrix(PM.get(1));

        Runtime.getRuntime().exec(".\\GaussBin.exe .\\in.txt .\\out.txt");

        // for every solution multuply rows and try founding p and q
        int[][] solutions = WriteToFile.getData();

        for (int s = 0; s < solutions.length; s++) {
            BigInteger x = BigInteger.ONE;
            BigInteger y = BigInteger.ONE;
            for (int i = 0; i < solutions[s].length; i++) {
                if (solutions[s][i] == 1) {
                    x = x.multiply(BigInteger.valueOf((long) rs[i]));

                    for (int j = 0; j < P[i].length; j++) {
                        if (P[i][j] > 0) {
                            y = y.multiply(FACTOR_BASE[j].pow(P[i][j]));
                            // System.out.println(FACTOR_BASE[j].pow(P[i][j]));
                            // System.out.println(P[i][j]);
                        }
                    }
                }
            }
            x = x.mod(N);
            y = y.mod(N).sqrt();
            // System.out.println(x.toString() + " = " + y.toString());

            BigInteger a = y.subtract(x);
            BigInteger p = a.gcd(N);
            Boolean notAnswer = p.equals(BigInteger.ONE) || p.equals(N);
            if (!notAnswer) {
                System.out.println(p);
                System.out.println(N.divide(p));
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
        for (int i = 3; i < 10; i++) {
            for (int j = 2; j < 100; j++) {
                if (count == L) {
                    break;
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
                        rs[count] = (int) r.longValueExact();
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