import java.math.BigInteger;

public class App {
    private final static int[] factorBase = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };
    private final static BigInteger[] FACTOR_BASE = new BigInteger[factorBase.length];
    private final static String N_IN = "16637";
    private final static BigInteger N = new BigInteger(N_IN);
    private final static int L = factorBase.length + 2;

    public static void main(String[] args) throws Exception {
        // saving N as bigInt

        // preparing factor base
        for (int i = 0; i < factorBase.length; i++) {
            FACTOR_BASE[i] = BigInteger.valueOf(factorBase[i]);
        }

        // generating smooth numbers
        int[][] P = Pmatrix(true);

    }

    static int[][] Pmatrix(boolean print) {
        int[][] P = new int[L][factorBase.length];
        int count = 0;
        for (int i = 3; i < 14; i++) {
            for (int j = 2; j < 8; j++) {
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
                    P[count] = primes;
                    count++;
                }
            }
        }

        if (print) {
            for (int[] row : P) {
                for (int column : row) {
                    System.out.print(column + " ");
                }
                System.out.println();
            }
        }

        return P;
    }

    // retunerear r (inte r^2)
    static BigInteger likelySmooth(int k, int j) {
        return BigInteger.valueOf(k).multiply(N).sqrt().add(BigInteger.valueOf(j));
    }

    // retunerar en int array där placering mostvarar primtalet och värdet antalet
    // av den faktorn
    static int[] primeFactorsAndSmooth(BigInteger r) {

        int[] primes = new int[factorBase.length + 1];
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