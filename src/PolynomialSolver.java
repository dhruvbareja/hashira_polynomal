import org.json.JSONObject;
import java.math.BigInteger;
import java.util.Scanner;

public class PolynomialSolver {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (sc.hasNextLine()) {
            inputBuilder.append(sc.nextLine());
        }
        JSONObject parsedJson = new JSONObject(inputBuilder.toString());
        int n = parsedJson.getJSONObject("keys").getInt("n");
        int k = parsedJson.getJSONObject("keys").getInt("k");
        BigInteger[] rootsArr = new BigInteger[n];
        int idx = 0;
        for (String key : parsedJson.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject root = parsedJson.getJSONObject(key);
            int base = Integer.parseInt(root.getString("base"));
            String val = root.getString("value");
            BigInteger number = new BigInteger(val, base);
            rootsArr[idx] = number;
            idx++;
        }
        BigInteger[] usedRoots = new BigInteger[k];
        System.arraycopy(rootsArr, 0, usedRoots, 0, k);
        BigInteger[] coeff = {BigInteger.ONE};

        for (BigInteger r : usedRoots) {
            coeff = multiplyPoly(coeff, new BigInteger[]{BigInteger.ONE, r.negate()});
        }

        for (int i = 0; i < coeff.length; i++) {
            System.out.print(coeff[i] + " ");
        }
        System.out.println();
        System.out.println(toReadableString(coeff));
    }

    static BigInteger[] multiplyPoly(BigInteger[] a, BigInteger[] b) {
        BigInteger[] res = new BigInteger[a.length + b.length - 1];
        for (int i = 0; i < res.length; i++) res[i] = BigInteger.ZERO;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                res[i+j] = res[i+j].add(a[i].multiply(b[j]));
            }
        }
        return res;
    }

    static String toReadableString(BigInteger[] coeffs) {
        StringBuilder sb = new StringBuilder();
        int deg = coeffs.length - 1;
        for (int i = 0; i < coeffs.length; i++) {
            BigInteger c = coeffs[i];
            if (c.equals(BigInteger.ZERO)) { deg--; continue; }
            if (sb.length() > 0 && c.signum() >= 0) sb.append(" + ");
            else if (c.signum() < 0) sb.append(" - ");
            BigInteger abs = c.abs();
            if (!abs.equals(BigInteger.ONE) || deg == 0) sb.append(abs);
            if (deg > 0) sb.append("x");
            if (deg > 1) sb.append("^").append(deg);
            deg--;
        }
        return sb.toString();
    }
}