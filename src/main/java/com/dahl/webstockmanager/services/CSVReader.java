package com.dahl.webstockmanager.services;

import com.dahl.webstockmanager.entities.Exportable;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CSVReader {

    public List<Exportable> read(MultipartFile file, Boolean withHeaders, Class<? extends Exportable> clazz) {

        List<Exportable> exportables = new ArrayList<>();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<Exportable> csvToBean = new CsvToBeanBuilder<Exportable>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            if (withHeaders) {
                csvToBean.setMappingStrategy(new HeaderColumnNameMappingStrategy<>());
            }

            for (Exportable e : csvToBean) {
                exportables.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exportables;
    }
}
