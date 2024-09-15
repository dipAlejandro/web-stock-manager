package com.dahl.webstockmanager.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class CSVReader<T> {

    // El método ahora recibe la clase del tipo T como argumento
    public List<T> read(MultipartFile file, Class<T> type) {
        List<T> exportables = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            // Usa la clase 'type' para el tipo genérico en CsvToBeanBuilder
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .withIgnoreEmptyLine(true)
                    .build();

            exportables = csvToBean.parse(); // Parseo correcto con el tipo T
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exportables;
    }
}
