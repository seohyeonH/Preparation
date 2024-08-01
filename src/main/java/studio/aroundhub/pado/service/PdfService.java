package studio.aroundhub.pado.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import studio.aroundhub.member.repository.UserSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class PdfService {
    private static final String FONT_PATH = "C:\\Users\\s0761\\workspace\\Practice\\UserProject\\UserProgram\\fonts\\NotoSansKR-Regular.ttf";
    @Value("${pdf.storage.dir}")
    private String storageDir;

    private static final float MARGIN = 50;
    private static final float PADDING = 12;
    private static final float TITLE_FONT_SIZE = 24;
    private static final float CONTENT_FONT_SIZE = 12;

    public File generatePdf(String content, UserSession user) throws IOException {
        String fileName = content.hashCode() + ".pdf";
        File file = new File(storageDir, fileName);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, new File(FONT_PATH));
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);

            try {
                contentStream.setFont(font, TITLE_FONT_SIZE);
                contentStream.beginText();
                contentStream.newLineAtOffset(
                        (page.getMediaBox().getWidth() - getStringWidth("진정서", font, TITLE_FONT_SIZE)) / 2,
                        page.getMediaBox().getHeight() - MARGIN - 30
                );
                contentStream.showText("진정서");
                contentStream.endText();

                contentStream.setFont(font, CONTENT_FONT_SIZE);
                float yPosition = page.getMediaBox().getHeight() - MARGIN * 2 - PADDING * 2;
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, yPosition);

                contentStream.newLineAtOffset(0, -10);
                yPosition = addText(contentStream, "피진정인: " + user.getRespondentName(), font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -3);
                yPosition = addText(contentStream, "    (" + user.getRespondentContact() + ")", font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -5);
                yPosition = addText(contentStream, "진정인: " + user.getUserName(), font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -3);
                yPosition = addText(contentStream, "    (" + user.getPhoneNumber() + ")", font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -5);
                yPosition = addText(contentStream, "진정인의 외국인 등록번호: " + user.getForeignNumber(), font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -30);

                String[] lines = formatContent(content, font, CONTENT_FONT_SIZE, page.getMediaBox().getWidth() - 2 * MARGIN - PADDING * 2);
                for (String line : lines) {
                    if (yPosition < MARGIN) {
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
                        contentStream.setFont(font, CONTENT_FONT_SIZE);
                        contentStream.beginText();
                        yPosition = page.getMediaBox().getHeight() - MARGIN;
                        contentStream.newLineAtOffset(MARGIN, yPosition);
                    }
                    yPosition = addText(contentStream, line, font, CONTENT_FONT_SIZE, yPosition);
                    contentStream.newLineAtOffset(0, -3);
                }

                contentStream.newLineAtOffset(0, -30);
                addText(contentStream, "일시: " + LocalDate.now(), font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -5);
                addText(contentStream, "회사 주소: " + user.getCompanyAddress(), font, CONTENT_FONT_SIZE, yPosition);
                contentStream.newLineAtOffset(0, -5);
                addText(contentStream, "회사명: " + user.getCompanyName(), font, CONTENT_FONT_SIZE, yPosition);

                contentStream.endText();
            } finally {
                contentStream.close();
            }

            document.save(file);
        }
        return file;
    }

    private float addText(PDPageContentStream contentStream, String text, PDType0Font font, float fontSize, float yPosition) throws IOException {
        if (text != null && !text.isEmpty()) {
            contentStream.newLineAtOffset(0, -PADDING);
            contentStream.showText(text);
        }
        return yPosition - PADDING;
    }

    private String[] formatContent(String content, PDType0Font font, float fontSize, float maxWidth) throws IOException {
        String[] lines = content.split("\\r?\\n");
        StringBuilder allLines = new StringBuilder();

        for (String line : lines) {
            String[] words = line.split(" ");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                if (getStringWidth(currentLine + " " + word, font, fontSize) > maxWidth) {
                    allLines.append(currentLine.toString().trim()).append("\n");
                    currentLine = new StringBuilder();
                }
                currentLine.append(word).append(" ");
            }
            allLines.append(currentLine.toString().trim()).append("\n");
        }

        return allLines.toString().split("\n");
    }

    private float getStringWidth(String text, PDType0Font font, float fontSize) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
    }
}
