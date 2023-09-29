import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;

    static Wahana[] listWahana;
    static Pengunjung[] listPengunjung;
    static ArrayDeque<Pengunjung> daftarKeluar;

    private static void processA() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        LinkedList<Pengunjung> antreanFT1 = wahana.antreanFT1;
        PriorityQueue<Pengunjung> antreanFT2 = wahana.antreanFT2;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        int uangPengunjung = pengunjung.uang;
        int hargaWahana = wahana.harga;
        int antreanFT1Size = antreanFT1.size();
        int totalSize = antreanFT1Size + antreanFT2.size() + antreanReguler.size();
        
        if (uangPengunjung < hargaWahana) {
            out.println(-1);
            return;
        }

        int kapasitasFT1 = wahana.prioritas;
        String tipePengunjung = pengunjung.tipe;

        if (tipePengunjung.equals("R"))
            antreanReguler.add(pengunjung);
        else {
            ListIterator<Pengunjung> it = antreanFT1.listIterator();
            while (it.hasNext()) {
                Pengunjung p = it.next();
                if (p.id > pengunjung.id) {
                    it.previous();
                    break;
                }
            }
            it.add(pengunjung);

            if (antreanFT1Size == kapasitasFT1) {
                Pengunjung temp = antreanFT1.pollLast();
                antreanFT2.add(temp);
            }
        }

        out.println(totalSize + 1);
    }

    private static void processE() {
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        LinkedList<Pengunjung> antreanFT1 = wahana.antreanFT1;
        PriorityQueue<Pengunjung> antreanFT2 = wahana.antreanFT2;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        int kapasitas = wahana.kapasitas;
        int prioritas = wahana.prioritas;
        int antreanFT1Size = antreanFT1.size();
        int antreanFT2Size = antreanFT2.size();
        int antreanRegulerSize = antreanReguler.size();
        int totalSize = antreanFT1Size + antreanFT2Size + antreanRegulerSize;

        if (totalSize == 0) {
            out.println(-1);
            return;
        }

        for (int i = 0; i < antreanFT1Size; i++) {
            Pengunjung p = antreanFT1.poll();
            if (p.uang < wahana.harga) {
                antreanFT1Size++;
                antreanFT2Size--;
                continue;
            }
            else if (p.uang == wahana.harga) {
                antreanFT1Size++;
                antreanFT2Size--;
                daftarKeluar.add(p);
                continue;
            }
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != antreanFT1Size - 1)
                out.print(" ");
        }

        for (int i = 0; i < antreanRegulerSize; i++) {
            if (i == 0)
                out.print(" ");
            Pengunjung p = antreanReguler.poll();
            if (p.uang < wahana.harga)
                continue;
            else if (p.uang == wahana.harga)
                daftarKeluar.add(p);
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != antreanRegulerSize - 1)
                out.print(" ");
        }

        for (int i = 0; i < antreanFT2Size; i++) {
            if (i == 0)
                out.print(" ");
            Pengunjung p = antreanFT2.poll();
            if (p.uang < wahana.harga)
                continue;
            else if (p.uang == wahana.harga)
                daftarKeluar.add(p);
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != antreanFT2Size - 1)
                out.print(" ");
        }

        out.println();
    }

    private static void processS() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        LinkedList<Pengunjung> antreanFT1 = wahana.antreanFT1;
        PriorityQueue<Pengunjung> antreanFT2 = wahana.antreanFT2;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        if (
            antreanReguler.contains(pengunjung) ||
            antreanFT1.contains(pengunjung) ||
            antreanFT2.contains(pengunjung)
        )
            out.println(idPengunjung);
        else
            out.println(-1);
    }

    private static void processF() {
        int P = in.nextInt();

        if (daftarKeluar.isEmpty()) {
            out.println(-1);
            return;
        }

        if ()
    }

    private static void processO() {
        int idPengunjung = in.nextInt();
    }

    private static void processQuery(String query) {
        switch (query) {
            case "A":
                processA();
                break;
            case "E":
                processE();
                break;
            case "S":
                processS();
                break;
            case "F":
                processF();
                break;
            case "O":
                processO();
                break;
        }
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int M = in.nextInt(); // jumlah wahana
        listWahana = new Wahana[M];
        for (int i = 0; i < M; i++) {
            int h = in.nextInt(); // harga wahana
            int p = in.nextInt(); // point wahana
            int kp = in.nextInt(); // kapasitas wahana
            int ft = in.nextInt(); // persentase prioritas fast track
            listWahana[i] = new Wahana(h, p, kp, ft);
        }

        int N = in.nextInt(); // jumlah pengunjung
        listPengunjung = new Pengunjung[N];
        for (int i = 0; i < N; i++) {
            String t = in.next(); // tipe pengunjung
            int u = in.nextInt(); // uang pengunjung
            listPengunjung[i] = new Pengunjung(t, u);
        }

        int T = in.nextInt(); // jumlah query
        for (int i = 0; i < T; i++) {
            String query = in.next();
            processQuery(query);
        }

        out.close();
    }

    static class Pengunjung implements Comparable<Pengunjung> {
        static int idCounter = 1;
        int id;
        String tipe;
        int uang;
        int jumlahBermain;
        boolean prioritas;
        int poin;

        Pengunjung(String tipe, int uang) {
            this.tipe = tipe;
            this.uang = uang;
            this.id = idCounter;
            this.jumlahBermain = 0;
            this.prioritas = false;
            this.poin = 0;
            idCounter++;
        }

        public int compareTo(Pengunjung p) {
            if (this.tipe.equals("FT") && p.tipe.equals("R"))
                return 1;
            else if (this.tipe.equals("R") && p.tipe.equals("FT"))
                return -1;
            else if (this.jumlahBermain > p.jumlahBermain)
                return 1;
            else if (this.jumlahBermain == p.jumlahBermain)
                return 0;
            else return -1;
        }

        public String toString() {
            return Integer.toString(id);
        }
    }

    static class Wahana {
        static int idCounter = 1;
        int id;
        int harga;
        int poin;
        int kapasitas;
        int prioritas;
        LinkedList<Pengunjung> antreanFT1;
        PriorityQueue<Pengunjung> antreanReguler;
        PriorityQueue<Pengunjung> antreanFT2;
        
        Wahana(int harga, int poin, int kapasitas, int persenPrioritas) {
            this.harga = harga;
            this.id = idCounter;
            this.poin = poin;
            this.kapasitas = kapasitas;
            this.prioritas = (int) Math.ceil((double) persenPrioritas / 100 * kapasitas);
            this.antreanFT1 = new LinkedList<>();
            this.antreanReguler = new PriorityQueue<>();
            this.antreanFT2 = new PriorityQueue<>();
            idCounter++;
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}