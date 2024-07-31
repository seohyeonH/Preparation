package studio.aroundhub.pado.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

@Service
@RequiredArgsConstructor
public class PdfService {
    private static final String FONT_PATH = "C:\\Users\\s0761\\workspace\\Practice\\UserProject\\UserProgram\\fonts\\NotoSansKR-Regular.ttf";
    @Value("${pdf.storage.dir}")
    private String storageDir;

    public File generatePdf(String content) throws IOException {
        String fileName = content.hashCode() + ".pdf";
        File file = new File(storageDir, fileName);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = PDType0Font.load(document, new File(FONT_PATH));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 700);

                for (String line : content.split("\n")) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -14);
                }
                contentStream.endText();
            }
            document.save(file);
        }
        return file;
    }
}
