package sample.apache.poi.excel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.poi.ss.util.CellReference;

public class WriteXMLStreaming {
    private int rowIdx;

    public void startXML(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeStartElement("worksheet");
        writer.writeAttribute("xmlns", "http://schemas.openxmlformats.org/spreadsheetml/2006/main");
        writer.writeStartElement("sheetData");
    }

    public void endXML(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndDocument();
    }

    public void createRow(XMLStreamWriter writer, int rowIdx) throws XMLStreamException {
        this.rowIdx = rowIdx;
        writer.writeStartElement("row");
        writer.writeAttribute("r", String.valueOf(rowIdx));
    }

    public void endRow(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeEndElement();
    }

    public void createCell(XMLStreamWriter writer, int colIdx, String value) throws XMLStreamException {
        writer.writeStartElement("c");
        writer.writeAttribute("r", new CellReference(rowIdx -1, colIdx-1).formatAsString());
        writer.writeAttribute("t", "inlineStr");
        writer.writeAttribute("s", "5");
        writer.writeStartElement("is");
        writer.writeStartElement("t");
        writer.writeCData(value);
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        XMLOutputFactory factory      = XMLOutputFactory.newInstance();
        WriteXMLStreaming a = new WriteXMLStreaming();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(
                    new FileWriter("d:/output2.xml"));
            a.startXML(writer);

            Random rnd = new Random();
            Calendar calendar = Calendar.getInstance();
            for(int i = 1 ; i<=100000 ; i++){
                a.createRow(writer,1);

                a.createCell(writer, 1,  "Hello, " + i + "!");
                a.createCell(writer, 2,  String.valueOf((double)rnd.nextInt(100)/100));
                a.createCell(writer, 3,  String.valueOf((double)rnd.nextInt(10)/10));
                a.createCell(writer, 4,  String.valueOf(rnd.nextInt(10000)));
                a.createCell(writer, 5,  calendar.getTime().toString());
                a.createCell(writer, 6,  "Hello, " + i + "!");
                a.createCell(writer, 7,  String.valueOf((double)rnd.nextInt(100)/100));
                a.createCell(writer, 8,  String.valueOf((double)rnd.nextInt(10)/10));
                a.createCell(writer, 9,  String.valueOf(rnd.nextInt(10000)));
                a.createCell(writer, 10, calendar.getTime().toString());

                a.endRow(writer);
            }

            a.endXML(writer);

            writer.flush();
            writer.close();

        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
    }
}

