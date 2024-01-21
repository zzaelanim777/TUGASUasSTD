
package uasstd;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UasSTD {

    // Record untuk data Tamu Hotel
    public record Tamu(String nama, long tlp, long nik) {}

    // Record untuk data Tipe Kamar
    public record Kamar(String tipe, String kode, int harga, boolean tersedia) {}

    // Inisialisasi ArrayList untuk menyimpan daftar tipe kamar
    static ArrayList<Kamar> daftarKamar = new ArrayList<>();

    // Record untuk memilih Kamar
    public record PilihKamar(String kode_kamar) {}

    // Stack untuk menyimpan kode room yang dipilih
    static Stack<PilihKamar> indeksKodeKamar = new Stack<>();

    // Queue untuk menyimpan daftar tamu hotel
    static Queue<Tamu> daftarTamu = new LinkedList<>();

    // LinkedList untuk menyimpan riwayat transaksi beserta tanggal check-in, check-out, dan total bayar
    static LinkedList<Transaksi> riwayatTransaksi = new LinkedList<>();

    // Record untuk Transaksi
    public record Transaksi(String nama, String tipe_kamar, String no_kamar, LocalDate checkin,
                            LocalDate checkout, int harga, int totalBayar) {}

    public static void main(String[] args) {
        // Inisialisasi beberapa tipe kamar
        daftarKamar.add(new Kamar("Luxury", "L001", 1000000, true));
        daftarKamar.add(new Kamar("Deluxe", "D001", 700000, true));
        daftarKamar.add(new Kamar("Standard", "S001", 300000, true));

        Scanner scanner = new Scanner(System.in);

        boolean isProgramBerjalan = true;

        while (isProgramBerjalan) {
            // Menampilkan menu
            System.out.println("Menu Kasir Reservasi Hotel:");
            System.out.println("1. Lihat Daftar Tipe Kamar");
            System.out.println("2. Reservasi Kamar");
            System.out.println("3. Lihat Daftar Tamu Hotel");
            System.out.println("4. Lihat Riwayat Transaksi");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu (1-5): ");

            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    // Menampilkan daftar tipe kamar
                    System.out.println("Daftar Tipe Kamar:");
                    for (Kamar k : daftarKamar) {
                        String Status = k.tersedia() ? "tersedia" : "disewa";
                        System.out.println(k.tipe() + " - " + k.kode() + " - RP " + k.harga() + Status);
                    }
                    break;
                case 2:
                    // Reservasi Kamar
                    System.out.print("Masukkan kode kamar yang ingin dipesan: ");
                    String kodeKamarDipilih = scanner.next();

                    PilihKamar pilihKamar = new PilihKamar(kodeKamarDipilih);
                    indeksKodeKamar.push(pilihKamar);

                    // Input data tamu
                    System.out.print("Masukkan nama tamu: ");
                    String namaTamu = scanner.next();
                    System.out.print("Masukkan nomor telepon tamu: ");
                    long tlpTamu = scanner.nextLong();
                    System.out.print("Masukkan nomor KTP tamu: ");
                    long nikTamu = scanner.nextLong();
                    
                    // disimpan di queue daftar tamu
                    daftarTamu.add(new Tamu(namaTamu, tlpTamu, nikTamu));
                    
                    // Input tanggal check-in
                    System.out.print("Masukkan tanggal check-in (YYYY-MM-DD): ");
                    String strCheckin = scanner.next();
                    LocalDate checkin = LocalDate.parse(strCheckin, DateTimeFormatter.ISO_DATE);

                    // Input tanggal check-out
                    System.out.print("Masukkan tanggal check-out (YYYY-MM-DD): ");
                    String strCheckout = scanner.next();
                    LocalDate checkout = LocalDate.parse(strCheckout, DateTimeFormatter.ISO_DATE);

                    // Hitung total bayar
                    int totalHari = (int) checkin.until(checkout, java.time.temporal.ChronoUnit.DAYS);
                    int hargaKamar = getHargaKamarByKode(kodeKamarDipilih);
                    int totalBayar = totalHari * hargaKamar;

                    // Menampilkan konfirmasi transaksi
                    System.out.println("Reservasi berhasil!");
                    System.out.println("Detail Transaksi:");
                    System.out.println("Tamu: " + namaTamu);
                    System.out.println("Kamar: " + kodeKamarDipilih);
                    System.out.println("Tanggal Check-in: " + checkin);
                    System.out.println("Tanggal Check-out: " + checkout);
                    System.out.println("Total Bayar: RP " + totalBayar);
                    System.out.println("-------------------------------");

                    // Menyimpan transaksi ke riwayat
                    riwayatTransaksi.add(new Transaksi(namaTamu, getTipeKamarByKode(kodeKamarDipilih),
                            kodeKamarDipilih, checkin, checkout, hargaKamar, totalBayar));
                    break;
                case 3:
                    // Lihat Daftar Tamu Hotel
                    System.out.println("Daftar Tamu Hotel:");
                    for (Tamu t : daftarTamu) {
                        System.out.println("Nama: " + t.nama() + ", No. Telp: " + t.tlp() + ", No. KTP: " + t.nik());
                    }
                    break;
                case 4:
                    // Lihat Riwayat Transaksi
                    System.out.println("Riwayat Transaksi:");
                    for (Transaksi transaksi : riwayatTransaksi) {
                        System.out.println("Nama: " + transaksi.nama() + ", Tipe Kamar: " + transaksi.tipe_kamar() +
                                ", No. Kamar: " + transaksi.no_kamar() + ", Check-in: " + transaksi.checkin() +
                                ", Check-out: " + transaksi.checkout() + ", Total Bayar: RP " + transaksi.totalBayar());
                    }
                    break;
                case 5:
                    // Keluar dari program
                    isProgramBerjalan = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    // Metode untuk mendapatkan tipe kamar berdasarkan kode kamar
    private static String getTipeKamarByKode(String kodeKamar) {
        for (Kamar k : daftarKamar) {
            if (k.kode().equals(kodeKamar)) {
                return k.tipe();
            }
        }
        return null;
    }

    // Metode untuk mendapatkan harga kamar berdasarkan kode kamar
    private static int getHargaKamarByKode(String kodeKamar) {
        for (Kamar k : daftarKamar) {
            if (k.kode().equals(kodeKamar)) {
                return k.harga();
            }
        }
        return 0;
    }
}
