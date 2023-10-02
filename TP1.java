import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
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

        Antrean antreanFT = wahana.antreanFT;
        Antrean antreanReguler = wahana.antreanReguler;

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

        pengunjung.jumlahBermainAntrean[idWahana-1] = pengunjung.jumlahBermain;

        int totalSize = antreanFT.size() + antreanReguler.size();
        out.println(totalSize);
    }

    static void processE() {
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        Antrean antreanFT = wahana.antreanFT;
        Antrean antreanReguler = wahana.antreanReguler;

        int kapasitas = wahana.kapasitas;
        int prioritas = wahana.prioritas;

        boolean isEmpty = true;

        int i = 0;
        while (!antreanFT.isEmpty() && i < prioritas) {
            Pengunjung p = antreanFT.poll();

            if (p.uang < wahana.harga)
                continue;

            isEmpty = false;

            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;

            if (p.uang == 0)
                daftarKeluar.add(p);

            out.print(p.id + " ");

            i++;
        }

        while (!antreanReguler.isEmpty() && i < kapasitas) {
            Pengunjung p = antreanReguler.poll();

            if (p.uang < wahana.harga)
                continue;

            isEmpty = false;

            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;

            if (p.uang == 0)
                daftarKeluar.add(p);
            
            out.print(p.id + " ");

            i++;
        }

        while (!antreanFT.isEmpty() && i < kapasitas) {
            Pengunjung p = antreanFT.poll();

            if (p.uang < wahana.harga)
                continue;

            isEmpty = false;

            p.uang -= wahana.harga;
            p.jumlahBermain++;
            p.poin += wahana.poin;

            if (p.uang == 0)
                daftarKeluar.add(p);
            
            out.print(p.id + " ");

            i++;
        }

        if (isEmpty) {
            out.println(-1);
            return;
        }

        out.println();
    }

    static void processS() {
        int idPengunjung = in.nextInt();
        Pengunjung pengunjung = listPengunjung[idPengunjung - 1];
        int idWahana = in.nextInt();
        Wahana wahana = listWahana[idWahana - 1];

        Antrean antreanFT = wahana.antreanFT;
        Antrean antreanReguler = wahana.antreanReguler;

        CustomSort c = new CustomSort(idWahana);
        antreanFT.sort(c);
        antreanReguler.sort(c);

        int kapasitas = Math.min(wahana.kapasitas, antreanFT.size() + antreanReguler.size());
        int prioritas = Math.min(wahana.prioritas, antreanFT.size());

        ListIterator<Pengunjung> it_1 = antreanFT.listIterator();
        ListIterator<Pengunjung> it_2 = antreanReguler.listIterator();

        int i = 0;
        while (
            it_1.hasNext() ||
            it_2.hasNext() ||
            i < antreanFT.size() + antreanReguler.size()
        ) {
            int j = 0;
            while (it_1.hasNext() && j < prioritas) {
                if (it_1.next() == pengunjung) {
                    if (pengunjung.uang < wahana.harga)
                        out.println(-1);
                    else
                        out.println(i + j + 1);
                    return;
                }
                j++;
            }
            while (it_2.hasNext() && j < kapasitas) {
                if (it_2.next() == pengunjung) {
                    if (pengunjung.uang < wahana.harga)
                        out.println(-1);
                    else
                        out.println(i + j + 1);
                    return;
                }
                j++;
            }
            i += j;
        }

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
        int[] jumlahBermainAntrean;
        boolean prioritas;
        int poin;
        boolean sudahKeluar;

        Pengunjung(String tipe, int uang) {
            this.tipe = tipe;
            this.uang = uang;
            this.id = idCounter;
            this.jumlahBermain = 0;
            this.jumlahBermainAntrean = new int[listWahana.length];
            this.prioritas = false;
            this.poin = 0;
            this.sudahKeluar = false;
            idCounter++;
        }

        public int compareTo(Pengunjung p) {
            if (this.id > p.id)
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
        Antrean antreanFT;
        Antrean antreanReguler;
        // Antrean antreanBermain;

        Wahana(int harga, int poin, int kapasitas, int persenPrioritas) {
            this.harga = harga;
            this.id = idCounter;
            this.poin = poin;
            this.kapasitas = kapasitas;
            this.prioritas = (int) Math.ceil((double) persenPrioritas / 100 * kapasitas);
            antreanFT = new Antrean(id);
            antreanReguler = new Antrean(id);
            // antreanBermain = new Antrean(id);
            idCounter++;
        }
    }

    static class Antrean extends LinkedList<Pengunjung> {
        int idWahana;
        
        public Antrean(int idWahana) {
            this.idWahana = idWahana;
        }

        public boolean add(Pengunjung pengunjung) {
            ListIterator<Pengunjung> it = listIterator();
            while (it.hasNext()) {
                Pengunjung p = it.next();
                if (
                    p.compareTo(pengunjung) > 0 ||
                    p.jumlahBermainAntrean[idWahana-1] > pengunjung.jumlahBermain
                ) {
                    it.previous();
                    break;
                }
            }

            it.add(pengunjung);

            return true;
        }
    }

    static class CustomSort implements Comparator<Pengunjung> {
        int idWahana;

        public CustomSort(int idWahana) {
            this.idWahana = idWahana;
        }

        public int compare(Pengunjung a, Pengunjung b) {
            int temp = a.jumlahBermainAntrean[idWahana-1] - b.jumlahBermain;
            if (temp != 0)
                return temp;
            return a.compareTo(b);
        }
    }

    // static class AntreanBermain extends ArrayList<Pengunjung> {
    //     int kapasitas;
    //     int prioritas;
    //     Antrean antreanFT;
    //     Antrean antreanReguler;

    //     AntreanBermain(int kapasitas, int prioritas, Antrean antreanFT, Antrean antreanReguler) {
    //         this.kapasitas = kapasitas;
    //         this.prioritas = prioritas;
    //         this.antreanFT = antreanFT;
    //         this.antreanReguler = antreanReguler;
    //     }

    //     public int size() {
    //         int antreanFTSize = antreanFT.size();
    //         int antreanRegulerSize = antreanReguler.size();
    //         if (antreanFTSize > prioritas)
    //             antreanFTSize = prioritas;
    //         int diff = kapasitas - antreanFTSize;
    //         if (antreanRegulerSize > diff)
    //             antreanRegulerSize = diff;
    //         return antreanFTSize + antreanRegulerSize;
    //     }
    // }

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