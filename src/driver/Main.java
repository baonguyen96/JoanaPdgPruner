package driver;

import sdg.SDG;
import sdg.SdgComparor;

import java.io.File;
import java.util.LinkedList;


public class Main {

    private static LinkedList<SDG> sdgs = new LinkedList<>();

    public static void main(String[] args) {
        try {
            prunePDGs();
            comparePDGs();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prunePDGs() throws Exception {
        String[] packages = {"smalls"};
        String[] classes = {};
        String[] methods = {"main"};
        File sourceFolder = new File("pdg/smalls");
        File[] sourcePdgFiles = sourceFolder.listFiles();

        if(sourcePdgFiles == null) {
            return;
        }

        for(File file : sourcePdgFiles) {
            if(file.isDirectory()) {
                continue;
            }
            SDG sdg = new SDG();
            sdg.setRelevantPackage(packages);
            sdg.setRelevantClasses(classes);
            sdg.setRelevantMethods(methods);
            sdg.construct(file);
            sdgs.addLast(sdg);
            sdg.save(createOutputFolderPath(packages, classes, methods), file.getName());
        }

    }


    private static void comparePDGs() throws Exception {
        SdgComparor comparor = new SdgComparor();
        comparor.compare(sdgs.getFirst(), sdgs.get(1));
        comparor.save("./compareResults/", "3.txt");
    }


    private static String createOutputFolderPath(String[] packageNames, String[] classNames, String[] methodNames) {

        return "./outPDG/" +
                createPath("package", packageNames) +
                createPath("class", classNames) +
                createPath("method", methodNames) +
                "/";
    }


    private static String createPath(String type, String[] collections) {
        StringBuilder path = new StringBuilder();
        path.append(type);
        path.append("_");

        if(collections == null || collections.length == 0) {
            path.append("all");
        }
        else {
            for (int i = 0; i < collections.length; i++) {
                path.append(collections[i]);

                if (i < collections.length - 1) {
                    path.append("-");
                }
            }
        }
        path.append("/");
        return path.toString();
    }


}
