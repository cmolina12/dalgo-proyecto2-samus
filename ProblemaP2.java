import java.io.*;
import java.util.*;

public class ProblemaP2 {

    /**  
     * Representa un caso de prueba:  
     * - n   = número de plataformas (la 0 inicial + 1..n)  
     * - e   = energía inicial  
     * - blocked[i] = true si la plataforma i tiene robot  
     * - jumpPower[i] = longitud de salto especial en i (0 si no hay)  
     */
    static class TestCase {
        int n, e;
        boolean[] blocked;
        int[] jumpPower;

        TestCase(int n, int e, boolean[] blocked, int[] jumpPower) {
            this.n = n;
            this.e = e;
            this.blocked = blocked;
            this.jumpPower = jumpPower;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<TestCase> casos = readTestCases(br);

        for (TestCase tc : casos) {
            String resultado = solve(tc.n, tc.e, tc.blocked, tc.jumpPower);
            System.out.println(resultado);
        }
    }

    /**
     * Lee todos los casos de prueba de la entrada estándar.
     * @param br BufferedReader sobre System.in
     * @return lista de TestCase
     */
    static List<TestCase> readTestCases(BufferedReader br) throws IOException {
        List<TestCase> lista = new ArrayList<>();
        String line = br.readLine();
        if (line == null) return lista;

        int T = Integer.parseInt(line.trim());
        for (int tc = 0; tc < T; tc++) {
            // --- Leer línea con n y e ---
            do {
                line = br.readLine();
            } while (line != null && line.trim().isEmpty());
            StringTokenizer st = new StringTokenizer(line);
            int n = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());

            // --- Leer plataformas bloqueadas ---
            boolean[] blocked = new boolean[n + 1];
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    for (String tok : line.split("\\s+")) {
                        blocked[Integer.parseInt(tok)] = true;
                    }
                }
            }

            // --- Leer pares (plataforma, salto) ---
            int[] jumpPower = new int[n + 1];
            line = br.readLine();
            if (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    st = new StringTokenizer(line);
                    while (st.hasMoreTokens()) {
                        int p = Integer.parseInt(st.nextToken());
                        int s = Integer.parseInt(st.nextToken());
                        jumpPower[p] = s;
                    }
                }
            }

            lista.add(new TestCase(n, e, blocked, jumpPower));
        }
        return lista;
    }


        static class State {
            int pos, energy, parentIdx;
            String action;
            State(int pos, int energy, int parentIdx, String action) {
                this.pos = pos;
                this.energy = energy;
                this.parentIdx = parentIdx;
                this.action = action;
            }
        }

        static String solve(int n, int e0, boolean[] blocked, int[] jumpPower) {
            // 1. Inicializar estructuras
            int[] bestEnergy = new int[n + 1];
            Arrays.fill(bestEnergy, -1);
            List<State> states = new ArrayList<>();
            Queue<Integer> q = new ArrayDeque<>();
            TreeSet<Integer> libres = new TreeSet<>();
            for (int i = 0; i <= n; i++) {
                if (!blocked[i]) libres.add(i);
            }

            // 2. Estado inicial
            bestEnergy[0] = e0;
            states.add(new State(0, e0, -1, "")); // índice 0
            q.add(0);
            libres.remove(0);

            int goalIdx = -1;

            // 3. Bucle BFS
            while (!q.isEmpty()) {
                int curIdx = q.poll();
                State cur = states.get(curIdx);

                // Meta alcanzada
                if (cur.pos == n) {
                    goalIdx = curIdx;
                    break;
                }

                // 3.1. Teletransporte (usa energía por distancia)
                // obtenemos el subrango de destinos factibles
                // BFS – Teletransporte brute-force sin poda prematura
                for (int np = Math.max(0, cur.pos - cur.energy);
                        np <= Math.min(n, cur.pos + cur.energy);
                        np++) {
                    if (blocked[np]) continue;
                    int e2 = cur.energy - Math.abs(np - cur.pos);
                    if (e2 > bestEnergy[np]) {
                        bestEnergy[np] = e2;
                        states.add(new State(np, e2, curIdx, "T" + (np - cur.pos)));
                        q.add(states.size() - 1);
                    }
                }


                // 3.2. Salto especial (jumpPower[cur.pos])
                int s = jumpPower[cur.pos];
                if (s > 0) {
                    for (int d : new int[]{+s, -s}) {
                        int np = cur.pos + d;
                        if (np >= 0 && np <= n && !blocked[np] && cur.energy > bestEnergy[np]) {
                            bestEnergy[np] = cur.energy;
                            String act = "S" + (d > 0 ? "+" : "-");
                            states.add(new State(np, cur.energy, curIdx, act));
                            q.add(states.size() - 1);
                            libres.remove(np);
                        }
                    }
                }

                // 3.3. Caminar (+1 / -1)
                for (int d : new int[]{+1, -1}) {
                    int np = cur.pos + d;
                    if (np >= 0 && np <= n && !blocked[np] && cur.energy > bestEnergy[np]) {
                        bestEnergy[np] = cur.energy;
                        String act = "C" + (d > 0 ? "+" : "-");
                        states.add(new State(np, cur.energy, curIdx, act));
                        q.add(states.size() - 1);
                        libres.remove(np);
                    }
                }

            }

            // 4. Reconstrucción de la ruta si goalIdx != -1
            if (goalIdx == -1) {
                return "NO SE PUEDE";
            } else {
                List<String> acciones = new ArrayList<>();
                for (int idx = goalIdx; idx != 0; idx = states.get(idx).parentIdx) {
                    acciones.add(states.get(idx).action);
                }
                Collections.reverse(acciones);
                StringJoiner sj = new StringJoiner(" ");
                for (String a : acciones) sj.add(a);
                return acciones.size() + " " + sj.toString();
            }
        }

    }

