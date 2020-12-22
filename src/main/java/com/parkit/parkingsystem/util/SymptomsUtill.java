package com.parkit.parkingsystem.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SymptomsUtill {
    public static List<String> readSymptomsFromDocument(String path) throws IOException {
        final BufferedReader reader = new BufferedReader(new FileReader(path));
        final List<String> symptomsList = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            symptomsList.add(line);
            line = reader.readLine();
        }
        return symptomsList;
    }

    public static Map<String, Long> getSymptomsDictionaryFromList(List<String> symptoms) {
        return symptoms.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()));
    }
}
