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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ContentProcessorServiceImpl implements ContentProcessorService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(ContentProcessorServiceImpl.class);

    @Autowired
    public ContentProcessorServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public ResponseEntity<String> rosreestrUpload() {

        ObjectNode objectNode = objectMapper.createObjectNode(); // формируем json во время чтения xlsx


//        kafkaTemplate.send("ros-events", "");
        return ResponseEntity.ok("Отправка файлов Росреестра прошла успешно");
    }

    @Override
    public ResponseEntity<String> cianUpload() {
        InputStream domClickStream = getClass().getClassLoader().getResourceAsStream("static/data/domClick.xlsx");
//        kafkaTemplate.send("dom-events", "");
        return ResponseEntity.ok("Отправка файлов Циана прошла успешно");
    }

    @Override
    public ResponseEntity<String> domClickUpload() {
        try (FileInputStream fileInputStream = new FileInputStream("F:\\diske\\MyProgects\\RealEstateCRM\\ContentLoaderAdapter\\src\\main\\resources\\static\\data\\domClick.xlsx")){
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);

                String square = getCellString(row.getCell(5));
                String price = getCellString(row.getCell(8)).replaceAll("\\D", "");
                String type = findCellType(getCellString(row.getCell(2)), i);

                ObjectNode rawEstate = objectMapper.createObjectNode();
                rawEstate.put("square", square);
                rawEstate.put("price", price);
                rawEstate.put("type", type);

                kafkaTemplate.send("dom-events", rawEstate.toString());
            }

        }catch (IOException e){
            return ResponseEntity.badRequest().body("Ошибка чтения файла");
        }

        return ResponseEntity.ok("Отправка файлов ДомКлика прошла успешно");
    }

    public String getCellString(XSSFCell cell) {
        if (cell == null) throw new IllegalArgumentException("ячейка пустая");
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default: throw new IllegalArgumentException("неверный тип данных ячейки");
        }
    }

    public String findCellType(String string, int index) {
        if (string.toLowerCase().contains("квартир")) {
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
