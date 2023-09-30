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

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        daftarKeluar = new ArrayDeque<>();

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

    static void processA() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        PriorityQueue<Pengunjung> antreanFT = wahana.antreanFT;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;
        AntreanBermain antreanBermain = wahana.antreanBermain;

        int uangPengunjung = pengunjung.uang;
        int hargaWahana = wahana.harga;
        if (uangPengunjung < hargaWahana || pengunjung.sudahKeluar) {
            out.println(-1);
            return;
        }

        String tipePengunjung = pengunjung.tipe;
        if (tipePengunjung.equals("FT"))
            antreanFT.add(pengunjung);
        else
            antreanReguler.add(pengunjung);

        int kapasitas = wahana.kapasitas;
        if (antreanBermain.size() < kapasitas)
            antreanBermain.add(pengunjung);

        int totalSize = antreanFT.size() + antreanReguler.size();
        out.println(totalSize);
    }

    static void processE() {
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        PriorityQueue<Pengunjung> antreanFT = wahana.antreanFT;
        PriorityQueue<Pengunjung> antreanReguler = wahana.antreanReguler;
        LinkedList<Pengunjung> antreanBermain = wahana.antreanBermain;

        if (antreanBermain.isEmpty()) {
            out.println(-1);
            return;
        }

        while (!antreanBermain.isEmpty()) {
            Pengunjung p = antreanBermain.poll();
            if (p.uang < wahana.harga) {
                continue;
            }
            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;
            out.print(p.id + " ");
            if (p.uang == 0)
                daftarKeluar.add(p);
        }

        out.println();

        antreanFT.clear();
        antreanReguler.clear();
    }

    static void processS() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        LinkedList<Pengunjung> antreanBermain = wahana.antreanBermain;
        int index = antreanBermain.indexOf(pengunjung);

        out.println(index);
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
        
        pengunjung.sudahKeluar = true;

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

    static class Pengunjung implements Comparable<Pengunjung> {
        static int idCounter = 1;
        int id;
        String tipe;
        int uang;
        int jumlahBermain;
        boolean prioritas;
        int poin;
        boolean sudahKeluar;

        Pengunjung(String tipe, int uang) {
            this.tipe = tipe;
            this.uang = uang;
            this.id = idCounter;
            this.jumlahBermain = 0;
            this.prioritas = false;
            this.poin = 0;
            this.sudahKeluar = false;
            idCounter++;
        }

        public int compareTo(Pengunjung p) {
            // if (this.tipe.equals("FT") && p.tipe.equals("R"))
            //     return 1;
            // else if (this.tipe.equals("R") && p.tipe.equals("FT"))
            //     return -1;
            if (this.jumlahBermain > p.jumlahBermain)
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
        AntreanBermain antreanBermain;
        
        Wahana(int harga, int poin, int kapasitas, int persenPrioritas) {
            this.harga = harga;
            this.id = idCounter;
            this.poin = poin;
            this.kapasitas = kapasitas;
            this.prioritas = (int) Math.ceil((double) persenPrioritas / 100 * kapasitas);
            this.antreanFT = new PriorityQueue<>();
            this.antreanReguler = new PriorityQueue<>();
            this.antreanBermain = new AntreanBermain(prioritas);
            idCounter++;
        }
    }

    static class AntreanBermain extends LinkedList<Pengunjung> {
        int prioritas;
        int ftCount;

        AntreanBermain(int prioritas) {
            this.prioritas = prioritas;
            this.ftCount = 0;
        }

        public boolean add(Pengunjung pengunjung) {
            ListIterator<Pengunjung> it;
            if (this.ftCount < this.prioritas)
                it = this.listIterator();
            else
                it = this.listIterator(this.prioritas);

            while (it.hasNext()) {
                Pengunjung p = it.next();
                if (pengunjung.tipe.equals("FT")) {
                    if (ftCount < prioritas) {
                        if (pengunjung.compareTo(p) > 0 || p.tipe.equals("R")) {
                            it.previous();
                            this.ftCount++;
                            break;
                        }
                    } else {
                        if (p.tipe.equals("R"))
                            continue;
                        if (pengunjung.compareTo(p) > 0) {
                            it.previous();
                            this.ftCount++;
                            break;
                        }
                    }
                } else if (pengunjung.tipe.equals("R")) {
                    if (ftCount < prioritas) {
                        if (p.tipe.equals("FT"))
                            continue;
                        if (pengunjung.compareTo(p) > 0) {
                            it.previous();
                            break;
                        }
                    } else {
                        if (pengunjung.compareTo(p) > 0 || p.tipe.equals("FT")) {
                            it.previous();
                            break;
                        }
                    }
                }
            }
            it.add(pengunjung);
            
            return true;
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