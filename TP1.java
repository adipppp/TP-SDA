import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;

    static Wahana[] listWahana;
    static Pengunjung[] listPengunjung;
    static ArrayDeque<Pengunjung> daftarKeluar;
    static ArrayList<Pengunjung> sudahKeluar;

    static int binarySearch(ArrayList<Pengunjung> sortedArray, Pengunjung pengunjung) {
        int low = 0;
        int high = sortedArray.size();
        if (high == 0) return -1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Pengunjung p = sortedArray.get(mid);
            if (p.id == pengunjung.id)
                return mid;
            else if (p.id < pengunjung.id)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    static void processA() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        PriorityQueue<Pengunjung> antreanFT = wahana.antreanFT;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        int uangPengunjung = pengunjung.uang;
        int hargaWahana = wahana.harga;
        if (
            uangPengunjung < hargaWahana ||
            binarySearch(sudahKeluar, pengunjung) != -1
        ) {
            out.println(-1);
            return;
        }

        String tipePengunjung = pengunjung.tipe;
        if (tipePengunjung.equals("FT"))
            antreanFT.add(pengunjung);
        else
            antreanReguler.add(pengunjung);

        int totalSize = antreanFT.size() + antreanReguler.size();
        out.println(totalSize);
    }

    static void processE() {
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        PriorityQueue<Pengunjung> antreanFT = wahana.antreanFT;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        int kapasitas = wahana.kapasitas;
        int prioritas = wahana.prioritas;
        int antreanFTSize = antreanFT.size();
        int antreanRegulerSize = antreanReguler.size();
        int totalSize = antreanFTSize + antreanRegulerSize;

        if (totalSize == 0) {
            out.println(-1);
            return;
        }

        int remaining = kapasitas;

        int ftCount;
        if (prioritas < antreanFTSize)
            ftCount = prioritas;
        else
            ftCount = antreanFTSize;

        for (int i = 0; i < ftCount; i++) {
            Pengunjung p = antreanFT.poll();
            boolean breakLoop = false;
            while (p.uang < wahana.harga) {
                if (antreanFT.isEmpty())
                    breakLoop = true;
                else
                    p = antreanFT.poll();
            }
            if (breakLoop)
                break;
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != ftCount - 1)
                out.print(" ");

            remaining--;
        }

        if (antreanRegulerSize > 0) out.print(" ");

        int regCount;
        if (remaining < antreanRegulerSize)
            regCount = remaining;
        else
            regCount = antreanRegulerSize;

        for (int i = 0; i < regCount; i++) {
            Pengunjung p = antreanReguler.poll();
            boolean breakLoop = false;
            while (p.uang < wahana.harga) {
                if (antreanReguler.isEmpty())
                    breakLoop = true;
                else
                    p = antreanReguler.poll();
            }
            if (breakLoop)
                break;
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != regCount - 1)
                out.print(" ");

            remaining--;
        }

        antreanFTSize = antreanFT.size();
        if (antreanFTSize > 0) out.print(" ");

        int ftSisa;
        if (remaining < antreanFTSize)
            ftSisa = remaining;
        else
            ftSisa = antreanFTSize;

        for (int i = 0; i < ftSisa; i++) {
            Pengunjung p = antreanFT.poll();
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id);
            if (i != antreanRegulerSize - 1)
                out.print(" ");
        }

        out.println();
    }

    static void processS() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        PriorityQueue<Pengunjung> antreanFT = wahana.antreanFT;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;

        if (antreanFT.contains(pengunjung) || antreanReguler.contains(pengunjung))
            out.println(idPengunjung);
        else
            out.println(-1);
    }

    static void processF() {
        int P = in.nextInt();

        if (daftarKeluar.isEmpty()) {
            out.println(-1);
            return;
        }

        Pengunjung pengunjung;
        if (P == 0)
            pengunjung = daftarKeluar.pollFirst();
        else
            pengunjung = daftarKeluar.pollLast();
        
        ListIterator<Pengunjung> it = sudahKeluar.listIterator();
        while (it.hasNext()) {
            Pengunjung p = it.next();
            if (p.id > pengunjung.id) {
                it.previous();
                break;
            }
        }
        it.add(pengunjung);

        out.println(pengunjung.poin);
    }

    static void processO() {
        int idPengunjung = in.nextInt();
    }

    static void processQuery(String query) {
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

        daftarKeluar = new ArrayDeque<>();
        sudahKeluar = new ArrayList<>();

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
            else if (this.jumlahBermain < p.jumlahBermain)
                return -1;
            else if (this.id > p.id)
                return 1;
            else if (this.id < p.id)
                return -1;
            else
                return 0;
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
        PriorityQueue<Pengunjung> antreanFT;
        PriorityQueue<Pengunjung> antreanReguler;
        
        Wahana(int harga, int poin, int kapasitas, int persenPrioritas) {
            this.harga = harga;
            this.id = idCounter;
            this.poin = poin;
            this.kapasitas = kapasitas;
            this.prioritas = (int) Math.ceil((double) persenPrioritas / 100 * kapasitas);
            this.antreanFT = new PriorityQueue<>();
            this.antreanReguler = new PriorityQueue<>();
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