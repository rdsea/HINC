/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.impl;

import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.InfoSourceSettings;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import at.ac.tuwien.dsg.hinc.abstraction.ResourceDriver.ProviderAdaptor;

/**
 * Default driver to get raw information from file
 * Needed settings:
 *  - endpoint: the folder to scan
 *  - filter: the file name pattern
 * @author hungld
 */
public class RawInfoCollectorFromFile implements ProviderAdaptor {

    InfoSourceSettings.InfoSource infoSource;
    
    @Override
    public Map<String, String> getRawInformation(InfoSourceSettings.InfoSource infoSource) {
        String mainFolder = infoSource.getSettings().get("endpoint");
        System.out.println("Checking folder:" + mainFolder);
        // scan and read all file in dir recursively

        List<String> fileNames = new ArrayList<>();
        getFileNames(fileNames, Paths.get(mainFolder));

        final String nameFilter = infoSource.getSettings().get("filter");
        if (nameFilter != null && !nameFilter.isEmpty()) {
            filterFileNames(fileNames, nameFilter);
        }

        HashMap<String,String> result = new HashMap<>();

        System.out.println("There are " + fileNames.size() + " files in the folder to read !");
        // each file contains info of single sensor/actuator
        for (String filePath : fileNames) {
            System.out.println("Reading file: " + filePath);
            String json;
            try {
                json = new String(Files.readAllBytes(Paths.get(filePath)));
                System.out.println("OK, Loaded the Json file content: \n " + json);
                result.put(filePath, json);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    static private List<String> getFileNames(List<String> fileNames, Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (path.toFile().isDirectory()) {
                    getFileNames(fileNames, path);
                } else {
                    fileNames.add(path.toAbsolutePath().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    static private void filterFileNames(List<String> fileNames, String endWith) {
        Iterator<String> ite = fileNames.iterator();
        while (ite.hasNext()) {
            File f = new File(ite.next());
            if (f.isDirectory() || (!f.getName().endsWith(endWith))) {
                ite.remove();
            }
        }
    }

}
