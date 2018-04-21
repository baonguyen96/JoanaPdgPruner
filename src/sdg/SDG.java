package sdg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;


public class SDG {

    private LinkedList<SdgNode> nodes;
    private String title;
    private LinkedList<String> removedIDs;


    public SDG() {
        nodes = new LinkedList<>();
        title = "";
        removedIDs = new LinkedList<>();
    }


    public void setRelevantPackage(String[] packages) {
        SdgNode.setRelevantPackages(packages);
    }


    public void setRelevantClasses(String[] classes) {
        SdgNode.setRelevantClasses(classes);
    }


    public void setRelevantMethods(String[] methods) {
        SdgNode.setRelevantMethods(methods);
    }


    public void construct(File pdgFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(pdgFile);
        String line = "";
        SdgNode node = new SdgNode();
        int totalNodes = 0;

        System.out.println(pdgFile.getName());

        if (scanner.hasNextLine()) {
            title = scanner.nextLine();
        }
        else {
            return;
        }

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.contains("{")) {
                totalNodes++;
                node = new SdgNode();
                node.append(line);
            }
            else if (line.contains("}")) {
                // prevent last line of PDG
                if (node != null) {
                    node.append(line);

                    // exclude nodes that are not relevant to the desired output
                    if (node.isRelevant()) {
                        nodes.addLast(node);
                    }
                    else {
                        removedIDs.addLast(node.getId());
                    }

                    node = null;
                }
            }
            else {
                node.append(line);
            }

        }

        prune();
        System.out.println("Total  = " + totalNodes);
        System.out.println("Remain = " + nodes.size());
        System.out.println("Remove = " + removedIDs.size());
        System.out.println();

    }


    private void prune() {
        LinkedList<SdgNode> prunedNodes = new LinkedList<>();

        for (SdgNode node : nodes) {
            for (String id : removedIDs) {
                node.prune(id);
            }
            prunedNodes.addLast(node);
        }

        nodes = deepCopyNodes(prunedNodes);
    }


    private LinkedList<SdgNode> deepCopyNodes(LinkedList<SdgNode> list) {
        LinkedList<SdgNode> copy = new LinkedList<>();

        for (SdgNode node : list) {
            copy.addLast(node);
        }

        return copy;
    }


    public void save(String folderPath, String fileName) throws Exception {
        File folder = new File(folderPath);
        folder.mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(folderPath + fileName, false);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);

        printWriter.println(title);
        while (!nodes.isEmpty()) {
            printWriter.print(nodes.removeFirst().toString());
        }
        printWriter.println("}");
        printWriter.close();
    }

}
