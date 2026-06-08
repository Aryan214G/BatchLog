package com.log.util.pdf;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class PdfPageContext {

    private PDPage page;
    private PDPageContentStream content;
    private float currentY;

    public PdfPageContext(
            PDPage page,
            PDPageContentStream content,
            float currentY
    ) {
        this.page = page;
        this.content = content;
        this.currentY = currentY;
    }

    public PDPage getPage() {
        return page;
    }

    public PDPageContentStream getContent() {
        return content;
    }

    public float getCurrentY() {
        return currentY;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }
}
