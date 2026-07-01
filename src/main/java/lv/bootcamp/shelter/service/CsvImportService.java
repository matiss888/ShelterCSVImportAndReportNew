package lv.bootcamp.shelter.service;

import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvImportService {

    public ImportResult importAnimals(Path inputPath) throws IOException {
        log.info("Starting import from {}", inputPath);

        List<Animal> allAnimals = new ArrayList<>();

        // TODO Step 1:
        // 1) Read intake.csv with UTF-8.
        // 2) Skip header row.
        // 3) Skip malformed rows and log warnings.
        // 4) Allow blank age as unknown (null), but reject non-numeric age values.
        // 5) Parse intakeDate using DateTimeFormatter.
        // 6) Map each row to Animal object.

        List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        int skippedRows = 0;

        for(int i = 1; i < lines.size(); i++) {
            String oneCSVline = lines.get(i);
            String[] parts = oneCSVline.split(",", -1);

            if (parts.length < 5) {
                log.warn("Skipping malformed row {}: ", oneCSVline);
                skippedRows++;
                continue;
            }

            String nameData = parts[0].trim();
            String speciesData = parts[1].trim();
            String ageData = parts[2].trim();
            String vaccinatedData = parts[3].trim();
            String intakeDateData = parts[4].trim();

            if (nameData.isEmpty() || speciesData.isEmpty() || vaccinatedData.isEmpty()) {
                log.warn("Skipping malformed row {}: ", oneCSVline);
                skippedRows++;
                continue;
            }

            Integer age;

            if(ageData.isEmpty()) {
                age = null;
            } else {
                try {
                    age = Integer.parseInt(ageData);
                    if (age <= 0) {
                        log.warn("Skipping row {}: ", oneCSVline);
                        skippedRows++;
                        continue;
                    }
                } catch (NumberFormatException numberFormatException) {
                    log.warn("Skipping row {}: ", oneCSVline);
                    skippedRows++;
                    continue;
                }
            }
            boolean vaccinated = Boolean.parseBoolean(vaccinatedData.trim());

            LocalDate intakeDate;
            try {
                intakeDate = LocalDate.parse(
                        intakeDateData.trim(),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException dateTimeParseException) {
                log.warn("Skipping row {}: ", oneCSVline);
                skippedRows++;
                continue;
            }

            Animal animal = new Animal(nameData, speciesData, age, vaccinated,intakeDate);
            allAnimals.add(animal);
        }

        return new ImportResult(allAnimals, skippedRows);
    }
}
