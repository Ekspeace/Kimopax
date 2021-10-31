package com.ekspeace.kimopax.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.Model.User;
import com.ekspeace.kimopax.R;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {
    public static User currentUser;
    public static String UserEmailKey = "EMAIL_KEY";
    public static String UserPasswordKey = "PASSWORD_KEY";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static List<Service> services;

    public static Boolean isOnline(Context context)	{
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    public static void GeneratePdf(Context context, String path, List<Service> services) throws FileNotFoundException {
        int count = 0;
        float amount = 0;
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        document.setBackgroundColor(ColorConstants.BLUE);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        float col = 280f;
        float columnWidth[] = {col, col};
        Table table = new Table(columnWidth);

        Drawable drawable = context.getDrawable(R.drawable.kimopex_logo_white);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bytes);
        Image image = new Image(imageData);
        image.setWidth(200f);

        table.setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor((ColorConstants.WHITE));
        table.addCell(new Cell().add(image)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginTop(40f)
                .setMarginBottom(40f)
                .setMarginLeft(15f)
                .setFontSize(30f)
                .setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph("Kimopax PTY LTD \n 546, 16th Road Building 3\n Constantia Park Midrand \n Gauteng \n 1685"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginTop(40f)
                .setMarginBottom(40f)
                .setBorder(Border.NO_BORDER)
                .setPaddingRight(20f));

        float colWidth[] = {60, 370, 50, 80};
        Table customerInfoTable = new Table(colWidth);

        customerInfoTable.addCell(new Cell(0, 4).add(new Paragraph("Contact Information"))
                .setBold().setBorder(Border.NO_BORDER).setFontSize(14f));

        customerInfoTable.addCell(new Cell().add(new Paragraph("Email:")).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(new Paragraph("info@kimopax.com")).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        customerInfoTable.addCell(new Cell().add(new Paragraph("Telephone:")).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(new Paragraph("+27 11 258 4788")).setBorder(Border.NO_BORDER));
        customerInfoTable.addCell(new Cell().add(new Paragraph("Date:")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        customerInfoTable.addCell(new Cell().add(new Paragraph(Common.simpleDateFormat.format(new Date().getTime()))).setBorder(Border.NO_BORDER));

        float itemInfoColWidth[] = {50, 255, 255};
        Table itemInfoTable = new Table(itemInfoColWidth);

        itemInfoTable.addCell(new Cell().add(new Paragraph("No.")).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Service ")).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Unit Price (ZAR)")).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT));

        for(Service service : services) {
            itemInfoTable.addCell(new Cell().add(new Paragraph(++count+"")));
            itemInfoTable.addCell(new Cell().add(new Paragraph(service.getName())));
            String removePriceCom = service.getPrice().replace(".", ",");
            itemInfoTable.addCell(new Cell().add(new Paragraph("R"+removePriceCom)).setTextAlignment(TextAlignment.RIGHT));
            String removePriceSpace = service.getPrice().replace(" ", "");
            amount += Float.parseFloat(removePriceSpace);
        }
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String currency = numberFormat.format(amount);
        itemInfoTable.addCell(new Cell().add(new Paragraph("")).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE));
        itemInfoTable.addCell(new Cell().add(new Paragraph("Total Amount")).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT));
        itemInfoTable.addCell(new Cell().add(new Paragraph(currency)).setBackgroundColor(new DeviceRgb(42,51,56)).setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT));

        Drawable drawable1 = context.getDrawable(R.drawable.kimopax_icon);
        Bitmap bitmap1 = ((BitmapDrawable)drawable1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        byte[] bytes1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bytes1);
        Image image1 = new Image(imageData1);
        image1.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        float x = (PageSize.A4.getWidth() - image1.getImageScaledHeight()) / 2;
        float y = (PageSize.A4.getHeight() - image1.getImageScaledHeight()) / 2;
        image1.setFixedPosition(x, y);
        image1.setOpacity(0.2f);
        document.add(image1);


        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(customerInfoTable);
        document.add(new Paragraph("\n"));
        document.add(itemInfoTable);
        document.close();
    }
}
