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
        AntreanBermain antreanBermain = wahana.antreanBermain;

        if (antreanBermain.size() == 0) {
            out.println(-1);
            return;
        }

        while (antreanBermain.size() > 0) {
            Pengunjung p = antreanBermain.remove(0);
            if (p.uang < wahana.harga) {
                daftarKeluar.add(p);
                if (p.tipe.equals("R"))
                    p = antreanReguler.poll();
                else {
                    if (!antreanFT.isEmpty())
                        p = antreanFT.poll();
                    else
                        p = antreanReguler.poll();
                }
                antreanBermain.add(p);
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

        AntreanBermain antreanBermain = wahana.antreanBermain;
        int urutan = antreanBermain.indexOf(pengunjung) + 1;

        out.println(urutan);
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
            // return 1;
            // else if (this.tipe.equals("R") && p.tipe.equals("FT"))
            // return -1;
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
            antreanFT = new PriorityQueue<>();
            antreanReguler = new PriorityQueue<>();
            antreanBermain = new AntreanBermain(kapasitas, prioritas);
            idCounter++;
        }
    }

    static class AntreanBermain {
        int kapasitas;
        int prioritas;
        ArrayList<Pengunjung> antreanPrio;
        ArrayList<Pengunjung> antreanReguler;
        ArrayList<Pengunjung> antreanSisa;

        AntreanBermain(int kapasitas, int prioritas) {
            this.kapasitas = kapasitas;
            this.prioritas = prioritas;
            antreanPrio = new ArrayList<>(prioritas);
            antreanReguler = new ArrayList<>(kapasitas - prioritas);
            antreanSisa = new ArrayList<>();
        }

        public void add(Pengunjung pengunjung) {
            ListIterator<Pengunjung> it = null;
            if (pengunjung.tipe.equals("R")) {
                it = antreanReguler.listIterator();
                while (it.hasNext()) {
                    Pengunjung p = it.next();
                    if (p.compareTo(pengunjung) > 0) {
                        it.previous();
                        break;
                    }
                }
            }
            else {
                if (antreanPrio.size() < prioritas) {
                    it = antreanPrio.listIterator();
                    while (it.hasNext()) {
                        Pengunjung p = it.next();
                        if (p.compareTo(pengunjung) > 0) {
                            it.previous();
                            break;
                        }
                    }
                } else {
                    it = antreanSisa.listIterator();
                    while (it.hasNext()) {
                        Pengunjung p = it.next();
                        if (p.compareTo(pengunjung) > 0) {
                            it.previous();
                            break;
                        }
                    }
                }
            }

            it.add(pengunjung);
        }

        public Pengunjung remove(int index) {
            Pengunjung p;
            if (index < antreanPrio.size())
                p = antreanPrio.remove(index);
            else if (index < antreanPrio.size() + antreanReguler.size())
                p = antreanReguler.remove(index);
            else
                p = antreanSisa.remove(index);
            return p;
        }

        public int size() {
            return antreanPrio.size() + antreanReguler.size() + antreanSisa.size();
        }

        public int indexOf(Pengunjung pengunjung) {
            int temp = indexOf(pengunjung, antreanPrio);
            if (temp != -1)
                return temp;
            int offset = antreanPrio.size();
            temp = indexOf(pengunjung, antreanReguler);
            if (temp != -1)
                return temp + offset;
            offset += antreanReguler.size();
            temp = indexOf(pengunjung, antreanSisa);
            return temp + offset;
        }

        private int indexOf(Pengunjung pengunjung, ArrayList<Pengunjung> container) {
            if (container.size() == 0)
                return -1;
            int left = 0, right = container.size() - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                Pengunjung p = container.get(mid);

                if (p.compareTo(pengunjung) == 0)
                    return mid;

                if (p.compareTo(pengunjung) < 0)
                    left = mid + 1;

                else
                    right = mid - 1;
            }

            return -1;
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