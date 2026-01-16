package com.kinibus.apps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_detail); // Layout XML 1 Anda

        // Ambil data dari Intent
        ticket = (Ticket) getIntent().getSerializableExtra("TICKET_DATA");

        if (ticket != null) {
            setupViews();
        }
    }

    private void setupViews() {
        // Binding Views (Sesuaikan ID dengan XML layout 1 Anda)
        ((TextView) findViewById(R.id.tv_start_time)).setText(ticket.getJam_berangkat());
        ((TextView) findViewById(R.id.tv_end_time)).setText(ticket.getJam_sampai());
        ((TextView) findViewById(R.id.tv_start_loc)).setText(ticket.getRute_awal());
        ((TextView) findViewById(R.id.tv_end_loc)).setText(ticket.getRute_akhir());

        // Nama Bus & Jenis (Menyesuaikan struktur XML Anda yang mungkin perlu sedikit penyesuaian ID)
        TextView tvBusName = findViewById(R.id.tv_bus_name_placeholder); // Tambahkan ID ini di XML jika belum ada
        if(tvBusName != null) tvBusName.setText(ticket.getBus_nama());

        // QR Code Generation menggunakan API (Simple way)
        ImageView qrImage = findViewById(R.id.img_qr_code); // Pastikan ada ID ini di ImageView QR
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + ticket.getRef_code();

        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.drawable.ic_launcher_background) // ganti dengan placeholder drawable
                .into(qrImage);

        // Tombol Download PDF (Tambahkan button di XML layout detail Anda jika belum ada)
        Button btnDownload = findViewById(R.id.btn_download_pdf);
        btnDownload.setOnClickListener(v -> generatePDF());

        // Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Halaman A4
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Styling PDF
        paint.setTextSize(20);
        paint.setFakeBoldText(true);
        canvas.drawText("E-TIKET PERJALANAN", 180, 50, paint);

        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        canvas.drawText("Ref Code: " + ticket.getRef_code(), 50, 90, paint);

        canvas.drawLine(50, 100, 545, 100, paint);

        int yPos = 130;
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText("Detail Penumpang", 50, yPos, paint);

        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        yPos += 25;
        canvas.drawText("Nama: " + ticket.getPenumpang(), 50, yPos, paint);
        yPos += 20;
        canvas.drawText("Kursi: " + ticket.getSeat(), 50, yPos, paint);

        yPos += 40;
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        canvas.drawText("Detail Bus", 50, yPos, paint);

        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        yPos += 25;
        canvas.drawText("Bus: " + ticket.getBus_nama() + " (" + ticket.getJenis() + ")", 50, yPos, paint);
        yPos += 20;
        canvas.drawText("Tanggal: " + ticket.getTanggal_sewa(), 50, yPos, paint);
        yPos += 20;
        canvas.drawText("Rute: " + ticket.getRute_awal() + " -> " + ticket.getRute_akhir(), 50, yPos, paint);
        yPos += 20;
        canvas.drawText("Jam: " + ticket.getJam_berangkat() + " - " + ticket.getJam_sampai(), 50, yPos, paint);

        pdfDocument.finishPage(page);

        // Simpan File
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), ticket.getRef_code() + ".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Disimpan di: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }
}
