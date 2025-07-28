/**
 * SecretFinder.java
 * 
 * Implementation of Shamir's Secret Sharing Scheme using Lagrange interpolation.
 * 
 * This program:
 * 1. Reads encoded shares from JSON files with different base encodings
 * 2. Converts share values from various bases to decimal
 * 3. Uses Lagrange interpolation to reconstruct the original secret
 * 4. Supports arbitrary precision arithmetic using BigInteger
 * 
 * Key Features:
 * - Multi-base decoding (binary, octal, hexadecimal, etc.)
 * - Threshold cryptography (k-out-of-n secret sharing)
 * - Large number support for cryptographic applications
 * 
 * Dependencies: json-simple library for JSON parsing
 */

import java.math.BigInteger;
import java.util.*;
import java.io.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class SecretFinder {

    // Point class for holding (x, y) pairs
    static class Point {
        BigInteger x, y;
        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // Convert a string from a given base to BigInteger
    static BigInteger convertBase(String val, int base) {
        BigInteger result = BigInteger.ZERO;
        BigInteger b = BigInteger.valueOf(base);
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            int digit = Character.digit(c, base);
            result = result.multiply(b).add(BigInteger.valueOf(digit));
        }
        return result;
    }

    // Read JSON and extract points
    static List<Point> readPointsFromJson(String filename, int[] nk) {
        List<Point> points = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new FileReader(filename));
            JSONObject keys = (JSONObject) obj.get("keys");
            nk[0] = ((Long) keys.get("n")).intValue();
            nk[1] = ((Long) keys.get("k")).intValue();

            for (Object key : obj.keySet()) {
                if (key.equals("keys")) continue;
                JSONObject point = (JSONObject) obj.get(key);
                int base = Integer.parseInt((String) point.get("base"));
                String value = (String) point.get("value");
                BigInteger x = new BigInteger(key.toString());
                BigInteger y = convertBase(value, base);
                points.add(new Point(x, y));
            }

        } catch (Exception e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
        return points;
    }

    // Lagrange interpolation at x = 0 to find constant term
    static BigInteger lagrangeInterpolation(List<Point> points) {
        BigInteger secret = BigInteger.ZERO;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;

            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = points.get(j).x;
                num = num.multiply(xj.negate());
                den = den.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(num).divide(den);
            secret = secret.add(term);
        }

        return secret;
    }

    public static void main(String[] args) {
        int[] nk1 = new int[2]; // [n, k]
        int[] nk2 = new int[2]; // [n, k]

        // Read testcases
        List<Point> points1 = readPointsFromJson("testcase1.json", nk1);
        List<Point> points2 = readPointsFromJson("testcase2.json", nk2);

        // Sort points to pick first k (you may pick any valid k)
        Collections.sort(points1, Comparator.comparing(p -> p.x));
        Collections.sort(points2, Comparator.comparing(p -> p.x));

        List<Point> subset1 = points1.subList(0, nk1[1]);
        List<Point> subset2 = points2.subList(0, nk2[1]);

        // Interpolation to find secret
        BigInteger secret1 = lagrangeInterpolation(subset1);
        BigInteger secret2 = lagrangeInterpolation(subset2);

        // Output
        System.out.println("Secret 1: " + secret1);
        System.out.println("Secret 2: " + secret2);
    }
}
