package com.adapter.contentloaderadapter.service.impl;

import com.adapter.contentloaderadapter.service.ContentProcessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ContentProcessorServiceImpl implements ContentProcessorService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(ContentProcessorServiceImpl.class);


    private final String domClickTopic = "dom-events";
    private final String cianTopic = "cian-events";
    private final String rosreestrTopic = "ros-events";
    private final String domclickFileName = "static/data/domClick.xlsx";
    private final String cianFileName = "static/data/cian.xlsx";
    private final String rosrestrFileName = "static/data/rosreestr.xlsx";
    private final int domClickAndCianCadastrIndex = 16;
    private final int domClickAndCianTypeIndex = 2;
    private final int domClickAndCianSquareIndex = 5;
    private final int domClickAndCianPriceIndex = 8;
    private final int rosreestrCadastrIndex = 1;
    private final int rosreestrTypeIndex = 9;
    private final int rosreestrSquareIndex = 5;
    private final int rosreestrPriceIndex = 10;

    @Autowired
    public ContentProcessorServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<String> rosreestrUpload() {
        uploadRawEstates(rosreestrTopic, rosrestrFileName, rosreestrCadastrIndex, rosreestrTypeIndex, rosreestrSquareIndex, rosreestrPriceIndex);
        return ResponseEntity.ok("Отправка файлов Росреестра прошла успешно");
    }

    @Override
    public ResponseEntity<String> cianUpload() {
        uploadRawEstates(cianTopic, cianFileName, domClickAndCianCadastrIndex, domClickAndCianTypeIndex, domClickAndCianSquareIndex, domClickAndCianPriceIndex);
        return ResponseEntity.ok("Отправка файлов Циана прошла успешно");
    }

    @Override
    public ResponseEntity<String> domClickUpload() {
        uploadRawEstates(domClickTopic, domclickFileName, domClickAndCianCadastrIndex, domClickAndCianTypeIndex, domClickAndCianSquareIndex, domClickAndCianPriceIndex);
        return ResponseEntity.ok("Отправка файлов ДомКлика прошла успешно");
    }

    private void uploadRawEstates(String topic, String fileName, int cadastrIndex, int typeIndex, int squareIndex, int priceIndex) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)){
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    return;
                }

                String cadastr = getCellString(row.getCell(cadastrIndex));
                String type = findCellType(getCellString(row.getCell(typeIndex)), i);
                String square = getCellString(row.getCell(squareIndex));
                String price = getCellString(row.getCell(priceIndex)).replaceAll("\\D", "");

                ObjectNode rawEstate = objectMapper.createObjectNode();
                rawEstate.put("square", square);
                rawEstate.put("price", price);
                rawEstate.put("type", type);
                rawEstate.put("cadastr", cadastr);

                kafkaTemplate.send(topic, rawEstate.toString());
            }

        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getCellString(XSSFCell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            default -> {
                log.error("Неверный тип ячейки в строке " + cell.getRowIndex());
                yield "";
            }
        };
    }

    private String findCellType(String string, int index) {
        if (string.toLowerCase().contains("квартир") || string.toLowerCase().contains("помещение")) {
           return  "RESIDENTIAL";
        } else if (string.toLowerCase().contains("гараж")) {
            return  "GARAGE";
        } else if (string.toLowerCase().contains("офис")) {
            return  "COMMERCIAL";
        }else {
            throw new IllegalArgumentException("неверный тип помещения в строке " + index);
        }

    }
}
