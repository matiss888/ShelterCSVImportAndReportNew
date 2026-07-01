package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.util.*;
import java.util.stream.Collectors;

public class ShelterAnalyticsService {

    public ShelterReportData buildReportData(ImportResult importResult) {
        List<Animal> allAnimals = importResult.allAnimals();

        Set<String> uniqueSpecies = new TreeSet<>();
        List<String> animalsNeedingVetInput = new ArrayList<>();

        // TODO Step 2:
        // Fill all collections:
        // - allAnimals (already available from import)
        // - uniqueSpecies
        // - animalsBySpecies
        // - animalsNeedingVetInput with format name(species)

        Map<String, List<Animal>> animalsBySpecies = allAnimals.stream()
                .collect(Collectors.groupingBy(Animal::getSpecies));

        for(Animal animal : allAnimals) {
            uniqueSpecies.add(animal.getSpecies());

            if(animal.getAge() == null) {
                animalsNeedingVetInput.add(animal.getName() + "("+ animal.getSpecies() +")");
            }
        }


        // TODO Step 3:
        // Add necessary fields to ShelterReportData
        // Use stream pipelines for:
        // - vaccinated vs unvaccinated counts per species
        // - oldest animal per species (excluding unknown ages)
        Map<String, Long> vaccinatedPerSpecies = allAnimals.stream()
                .filter(Animal::isVaccinated)
                .collect(Collectors.groupingBy(Animal::getSpecies,Collectors.counting()));

        Map<String, Long> unvaccinatedPerSpecies = allAnimals.stream()
                .filter(animal -> !animal.isVaccinated())
                .collect(Collectors.groupingBy(Animal::getSpecies, Collectors.counting()));

        Map<String, Animal> oldestAnimalPerSpecies = allAnimals.stream()
                .filter(animal -> animal.getAge() != null)
                .collect(Collectors.groupingBy(
                        Animal::getSpecies,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Animal::getAge)),Optional::get)
                ));

        return new ShelterReportData(importResult,
                allAnimals,
                uniqueSpecies,
                animalsBySpecies,
                animalsNeedingVetInput,
                vaccinatedPerSpecies,
                unvaccinatedPerSpecies,
                oldestAnimalPerSpecies);
    }
}
