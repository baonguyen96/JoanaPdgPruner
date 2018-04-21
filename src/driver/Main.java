package driver;

import sdg.SDG;

import java.io.File;


public class Main {

    public static void main(String[] args) {
        try {
            String[] packages = {"samples"};
            String[] classes = {"EqualSCD", "Infeasible"};
            String[] methods = {"main", "SRaise"};
            File sourceFolder = new File("pdg/samples");
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
                sdg.save(createOutputFolderPath(packages, classes, methods), file.getName());
            }

            System.out.println();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
