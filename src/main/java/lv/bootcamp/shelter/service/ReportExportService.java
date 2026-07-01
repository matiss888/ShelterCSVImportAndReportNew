package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) throws IOException {
        // TODO Step 4:
        // 1) Write upload-report.txt in required format.
        // 2) Include generated date, imported/skipped totals.
        // 3) Include unique species and per-species breakdown.
        // 4) Include oldest animal per species.
        // 5) Include animalsNeedingVetInput as name(species), name2(species2).
        // 6) Use UTF-8 and try-with-resources.

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath.toFile(), StandardCharsets.UTF_8))) {
            bw.write("=== Shelter Intake Report ===");
            bw.newLine();
            bw.write("Date generated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            bw.newLine();
            bw.newLine();
            bw.write("Total imported: " + reportData.allAnimals().size());
            bw.newLine();
            bw.write("Total skipped: " + reportData.importResult().skippedRows());
            bw.newLine();
            bw.newLine();
            bw.write("--- Unique species ---");
            bw.newLine();
            bw.write(String.join(", ", reportData.uniqueSpecies()));
            bw.newLine();
            bw.newLine();
            bw.write("--- Per-species breakdown ---");
            bw.newLine();
            for (String species : reportData.uniqueSpecies()) {
                int totalAnimals = reportData.animalsBySpecies().get(species).size();
                long vaccinated = reportData.vaccinatedPerSpecies().getOrDefault(species, 0L);
                bw.write(species + ": " + totalAnimals + " total, " + vaccinated + " vaccinated");
                bw.newLine();
            }
            bw.newLine();
            bw.write("--- Oldest per species ---");
            bw.newLine();
            for (String species : reportData.uniqueSpecies()) {
                Animal oldest = reportData.oldestAnimalPerSpecies().get(species);
                if (oldest != null) {
                    bw.write(species + ": " + oldest.getName() + " (age " + oldest.getAge() + ")");
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.write("--- Needs Vet input to determine age ---");
            bw.newLine();
            bw.write(String.join(", ", reportData.animalsNeedingVetInput()));
            bw.newLine();

        }
    }
}
