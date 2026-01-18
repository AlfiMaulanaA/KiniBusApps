package com.kinibus.apps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
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
        setContentView(R.layout.riwayat_detail);

        // Ambil data dari Intent
        ticket = (Ticket) getIntent().getSerializableExtra("TICKET_DATA");

        if (ticket != null) {
            setupViews();
        } else {
            Toast.makeText(this, "Data tiket tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupViews() {
        // Setup Text Views
        ((TextView) findViewById(R.id.tv_start_time)).setText(ticket.getJam_berangkat());
        ((TextView) findViewById(R.id.tv_end_time)).setText(ticket.getJam_sampai());
        ((TextView) findViewById(R.id.tv_start_loc)).setText(ticket.getRute_awal());
        ((TextView) findViewById(R.id.tv_end_loc)).setText(ticket.getRute_akhir());

        TextView tvBusName = findViewById(R.id.tv_bus_name_placeholder);
        if(tvBusName != null) tvBusName.setText(ticket.getBus_nama());

        // QR Code Generation
        ImageView qrImage = findViewById(R.id.img_qr_code);
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" + ticket.getRef_code();

        Glide.with(this)
                .load(qrUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(qrImage);

        // Tombol Download PDF
        Button btnDownload = findViewById(R.id.btn_download_pdf);
        if (btnDownload != null) {
            btnDownload.setOnClickListener(v -> generatePDF());
        }

        // Back Button
        ImageView btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        // Halaman A4
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Styling PDF Sederhana
        paint.setTextSize(20);
        paint.setFakeBoldText(true);
        canvas.drawText("E-TIKET PERJALANAN KINIBUS", 140, 50, paint);

        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        canvas.drawText("Ref Code: " + ticket.getRef_code(), 50, 90, paint);
        canvas.drawLine(50, 100, 545, 100, paint);

        int yPos = 130;
        canvas.drawText("Nama: " + ticket.getPenumpang(), 50, yPos, paint);
        yPos += 25;
        canvas.drawText("Bus: " + ticket.getBus_nama() + " (" + ticket.getJenis() + ")", 50, yPos, paint);
        yPos += 25;
        canvas.drawText("Rute: " + ticket.getRute_awal() + " -> " + ticket.getRute_akhir(), 50, yPos, paint);
        yPos += 25;
        canvas.drawText("Waktu: " + ticket.getTanggal_sewa() + " (" + ticket.getJam_berangkat() + ")", 50, yPos, paint);

        pdfDocument.finishPage(page);

        // Simpan File
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Tiket-" + ticket.getRef_code() + ".pdf");
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