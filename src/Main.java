import java.io.File;


public class Main {

    public static void main(String[] args) {
        try {
            String packageName = "smalls";
            String[] methods = {};
            File folder = new File("pdg/" + packageName);
            File[] files = folder.listFiles();

            for(File file : files) {
                if(file.isDirectory()) {
                    continue;
                }
                SDG sdg = new SDG();
                sdg.setRelevantPackage(packageName);
                sdg.setRelevantMethods(methods);
                sdg.construct("pdg/" + packageName + "/" + file.getName());
                sdg.save(createOutputFolderPath(packageName, methods), file.getName());
            }

            System.out.println();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String createOutputFolderPath(String packageName, String[] methodNames) {
        StringBuilder path = new StringBuilder();

        path.append("outPDG/");
        path.append(packageName);
        path.append("/");

        if(methodNames == null || methodNames.length == 0) {
            path.append("allMethods");
        }
        else {
            for (int i = 0; i < methodNames.length; i++) {
                path.append(methodNames[i]);

                if (i < methodNames.length - 1) {
                    path.append("-");
                }
            }
        }
        path.append("/");

        return path.toString();
    }

}
