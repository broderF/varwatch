/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author broder
 */
public class JsonFileLoader {

    public static String getStringFromFile(String filePath) {
        List<String> readAllLines = new ArrayList<>();
        try {
            readAllLines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException ex) {
            Logger.getLogger(JsonFileLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        String collectJoin = readAllLines.stream().map(String::trim).collect(Collectors.joining()).replaceAll("\\s", "");
        return collectJoin;
    }
}
