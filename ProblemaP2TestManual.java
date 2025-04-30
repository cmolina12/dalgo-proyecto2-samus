import java.io.*;
import java.util.*;

public class ProblemaP2TestManual {

    public static void main(String[] args) throws IOException {
        // Simulated input
        String input = """
                2
                5 10
                2 4
                1 3 3 2
                3 5
                1
                2 4
                """;

        BufferedReader br = new BufferedReader(new StringReader(input));
        List<ProblemaP2.TestCase> testCases = ProblemaP2.readTestCases(br);

        // Print the results to verify
        System.out.println("Number of test cases: " + testCases.size());

        for (int i = 0; i < testCases.size(); i++) {
            ProblemaP2.TestCase tc = testCases.get(i);
            System.out.println("Test Case " + (i + 1) + ":");
            System.out.println("  n = " + tc.n);
            System.out.println("  e = " + tc.e);
            System.out.println("  blocked = " + Arrays.toString(tc.blocked));
            System.out.println("  jumpPower = " + Arrays.toString(tc.jumpPower));
        }
    }
}