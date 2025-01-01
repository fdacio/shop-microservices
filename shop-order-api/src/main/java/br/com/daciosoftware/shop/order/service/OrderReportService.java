package br.com.daciosoftware.shop.order.service;

import br.com.daciosoftware.shop.models.dto.order.ItemDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderDTO;
import br.com.daciosoftware.shop.models.dto.order.OrderSummaryReportDTO;
import br.com.daciosoftware.shop.order.repository.OrderReportRepositoryImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Font.FontStyle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class OrderReportService {

    private static final int SCALE_PERC_LOGO = 50;

    @Autowired
    private OrderReportRepositoryImpl orderReportRepository;

    @Autowired
    private OrderService orderService;

    public ByteArrayOutputStream getReportDemoVenda(Long shopId)  throws DocumentException {
        Document document = new Document();
        document.setMargins(20, 20, 30, 30);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        addHeaderReport(document, "Demo Order");

        OrderDTO shopDTO = orderService.findById(shopId);
        addRowsReportDemoVenda(document, shopDTO);

        document.close();

        return outputStream;

    }

    public ByteArrayOutputStream getReportResumoVendas(LocalDate dataInicio, LocalDate dataFim) throws DocumentException {

        OrderSummaryReportDTO shopReportDTO = orderReportRepository.getOrderByDate(dataInicio, dataFim);

        Document document = new Document();
        document.setMargins(20, 20, 30, 30);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        document.open();

        addHeaderReport(document, "Resumo de Vendas");

        Font fontPeriodo = new Font(FontFamily.HELVETICA, 14, FontStyle.BOLD.ordinal());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String periodo = String.format("Período: %s à %s", dataInicio.format(dtf), dataFim.format(dtf));
        document.add(new Phrase(periodo, fontPeriodo));

        addRowsReportResumoVendas(document, shopReportDTO);

        document.close();

        return outputStream;
    }

    private void addRowsReportResumoVendas(Document document, OrderSummaryReportDTO shopReportDTO) throws DocumentException {

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        float[] widths = {40, 60};
        table.setWidths(widths);

        table.addCell("Quantidade de vendas no período:");
        PdfPCell pdfPCellCount = new PdfPCell(new Phrase(String.format("%d", shopReportDTO.getCount())));
        pdfPCellCount.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(pdfPCellCount);

        table.addCell("Total de vendas no período:");
        PdfPCell pdfPCellTotal = new PdfPCell(new Phrase(String.format("R$ %,.2f", shopReportDTO.getTotal())));
        pdfPCellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(pdfPCellTotal);

        table.addCell("Valor médio de vendas no período:");
        PdfPCell pdfPCellMean = new PdfPCell(new Phrase(String.format("R$ %,.2f", shopReportDTO.getMean())));
        pdfPCellMean.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(pdfPCellMean);

        document.add(table);

    }

    private void addRowsReportDemoVenda(Document document, OrderDTO shopDTO) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        float[] widths = {20, 30, 30, 20};
        table.setWidths(widths);

        Font fontBold = new Font(FontFamily.HELVETICA, 12, FontStyle.BOLD.ordinal());
        Font fontNormal = new Font(FontFamily.HELVETICA, 12, FontStyle.NORMAL.ordinal());
        final float PADDING = 8;

        PdfPCell cell1 = new PdfPCell(new Phrase("Nº", fontBold));
        cell1.setPadding(PADDING);
        PdfPCell cell2 = new PdfPCell(new Phrase("Data", fontBold));
        cell2.setPadding(PADDING);
        PdfPCell cell3 = new PdfPCell(new Phrase("Hora", fontBold));
        cell3.setPadding(PADDING);
        PdfPCell cell4 = new PdfPCell(new Phrase("Valor Total", fontBold));
        cell4.setPadding(PADDING);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);

        DateTimeFormatter dtfData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfHora = DateTimeFormatter.ofPattern("HH:mm:ss");


        String numero = String.valueOf(shopDTO.getId());
        String data = shopDTO.getData().format(dtfData);
        String hora = shopDTO.getData().format(dtfHora);
        String valorTotal = String.format("R$ %,.2f", shopDTO.getTotal());

        PdfPCell pdfPCellNum = new PdfPCell(new Phrase(numero, fontNormal));
        PdfPCell pdfPCellData = new PdfPCell(new Phrase(data, fontNormal));
        PdfPCell pdfPCellHora = new PdfPCell(new Phrase(hora, fontNormal));
        PdfPCell pdfPCellValor = new PdfPCell(new Phrase(valorTotal, fontNormal));
        pdfPCellValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellNum.setPadding(PADDING);
        pdfPCellData.setPadding(PADDING);
        pdfPCellHora.setPadding(PADDING);
        pdfPCellValor.setPadding(PADDING);

        table.addCell(pdfPCellNum);
        table.addCell(pdfPCellData);
        table.addCell(pdfPCellHora);
        table.addCell(pdfPCellValor);

        String customerName = shopDTO.getCustomer().getNome();
        String customerCPF = shopDTO.getCustomer().getCpf();

        PdfPCell pdfPCellLabelName = new PdfPCell(new Phrase("Nome", fontBold));
        pdfPCellLabelName.setColspan(3);
        pdfPCellLabelName.setPadding(PADDING);
        PdfPCell pdfPCellLabelCpf = new PdfPCell(new Phrase("CPF", fontBold));
        pdfPCellLabelCpf.setPadding(PADDING);

        table.addCell(pdfPCellLabelName);
        table.addCell(pdfPCellLabelCpf);

        PdfPCell pdfPCellCustomerName = new PdfPCell(new Phrase(customerName, fontNormal));
        pdfPCellCustomerName.setColspan(3);
        pdfPCellCustomerName.setPadding(PADDING);

        PdfPCell pdfPCellCustomerCpf = new PdfPCell(new Phrase(customerCPF, fontNormal));
        pdfPCellCustomerCpf.setPadding(PADDING);

        table.addCell(pdfPCellCustomerName);
        table.addCell(pdfPCellCustomerCpf);

        PdfPCell pdfPCellLabelItens = new PdfPCell(new Phrase("Itens", fontNormal));
        pdfPCellLabelItens.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellLabelItens.setColspan(4);
        table.addCell(pdfPCellLabelItens);

        PdfPTable tableItens = new PdfPTable(5);
        tableItens.setWidthPercentage(100);
        float[] widthsItens = {10, 45, 15, 15, 15};
        tableItens.setWidths(widthsItens);
        PdfPCell cell5 = new PdfPCell(new Phrase("Item", fontBold));
        cell5.setPadding(PADDING);
        PdfPCell cell6 = new PdfPCell(new Phrase("Descrição", fontBold));
        cell6.setPadding(PADDING);
        PdfPCell cell7 = new PdfPCell(new Phrase("Quantidade", fontBold));
        cell7.setPadding(PADDING);
        PdfPCell cell8 = new PdfPCell(new Phrase("Valor Unit.", fontBold));
        cell8.setPadding(PADDING);
        PdfPCell cell9 = new PdfPCell(new Phrase("Valor Total", fontBold));
        cell9.setPadding(PADDING);

        tableItens.addCell(cell5);
        tableItens.addCell(cell6);
        tableItens.addCell(cell7);
        tableItens.addCell(cell8);
        tableItens.addCell(cell9);

        int n = 1;
        for (ItemDTO item: shopDTO.getItens()) {

            String num =  String.valueOf(n);
            String nome = item.getProduct().getNome();
            String quantidade = String.valueOf(item.getQuantidade());
            String vrUnit = String.format("R$ %,.2f", item.getPreco());
            String vrTotal = String.format("R$ %,.2f", item.getQuantidade() * item.getPreco());


            PdfPCell cell10 = new PdfPCell(new Phrase( num, fontNormal));
            cell10.setPadding(PADDING);
            PdfPCell cell11 = new PdfPCell(new Phrase( nome, fontNormal));
            cell11.setPadding(PADDING);
            PdfPCell cell12 = new PdfPCell(new Phrase( quantidade, fontNormal));
            cell12.setPadding(PADDING);
            cell12.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell13 = new PdfPCell(new Phrase( vrUnit, fontNormal));
            cell13.setPadding(PADDING);
            cell13.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell14 = new PdfPCell(new Phrase( vrTotal, fontNormal));
            cell14.setPadding(PADDING);
            cell14.setHorizontalAlignment(Element.ALIGN_RIGHT);

            tableItens.addCell(cell10);
            tableItens.addCell(cell11);
            tableItens.addCell(cell12);
            tableItens.addCell(cell13);
            tableItens.addCell(cell14);

            n++;
        }

        document.add(table);
        document.add(tableItens);
    }

    private void addHeaderReport(Document document, String title) throws DocumentException {

        PdfPTable tableHeader = new PdfPTable(3);
        tableHeader.setWidthPercentage(100);
        float[] widths = {15, 70, 15};
        tableHeader.setWidths(widths);

        PdfPCell pdfPCellImg =  getLogo().isPresent() ? new PdfPCell(getLogo().get()) : new PdfPCell();
        pdfPCellImg.setBorderWidth(0);
        tableHeader.addCell(pdfPCellImg);

        Font fontTitle = new Font(FontFamily.HELVETICA, 18, FontStyle.BOLD.ordinal());
        PdfPCell pdfPCellTitulo = new PdfPCell(new Phrase(title, fontTitle));
        pdfPCellTitulo.setBorderWidth(0);
        pdfPCellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);

        tableHeader.addCell(pdfPCellTitulo);
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorderWidth(0);
        tableHeader.addCell(pdfPCell);

        document.add(tableHeader);

    }

    private Optional<Image> getLogo() {
        URL logoResource = this.getClass().getClassLoader().getResource("static/images/logo.png");
        if (logoResource == null) {
            return Optional.empty();
        }
        try {
            Image img = Image.getInstance(logoResource.toURI().toString());
            img.scalePercent(SCALE_PERC_LOGO);
            return Optional.of(img);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
