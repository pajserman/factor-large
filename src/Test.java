import java.math.BigInteger;

public class Test {
    private final static int[] factorBase = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };
    // private final static int[] factorBase = { 11, 13 };
    private final static BigInteger[] FACTOR_BASE = new BigInteger[factorBase.length];

    public static void main(String[] args) {
        // preparing factor base
        for (int i = 0; i < factorBase.length; i++) {
            FACTOR_BASE[i] = BigInteger.valueOf(factorBase[i]);
        }

        BigInteger r = new BigInteger("582");

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
            System.out.println("success");
        } else {
            System.out.println("Failed");
        }

        for (int i : primes) {
            System.out.print(i);
        }

    }
}
