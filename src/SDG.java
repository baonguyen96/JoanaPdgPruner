import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class SDG {

    private static class Node {
        private ArrayList<String> content;
        private String id;
        private static String relevantPackage;
        private static String[] relevantMethods;

        Node() {
            content = new ArrayList<>();
        }

        void append(String line) {
            content.add(line);
            if(line.contains("{")) {
                id = line.split("\\s+")[1];
            }
        }

        boolean isRelevant() {
            return isRelevantPackages() && isRelevantMethods();
        }


        private boolean isRelevantPackages() {
            int relevantBothSandBvals = 0;

            for(String s : content) {
                if(s.contains(String.format("S \"%s/", relevantPackage))) {
                    relevantBothSandBvals++;
                }
                else if(s.contains(String.format("B \"%s.", relevantPackage))) {
                    relevantBothSandBvals++;
                }

                if(relevantBothSandBvals == 2) {
                    break;
                }
            }
            return relevantBothSandBvals == 2;
        }


        private boolean isRelevantMethods() {
            if(relevantMethods == null || relevantMethods.length == 0) {
                return true;
            }
            else {
                for(String s : content) {
                    for(String method : relevantMethods) {
                        if(s.startsWith(String.format("B \"%s.", relevantPackage)) &&
                                s.contains(method)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }


        void prune(String idToExclude) {
            for(int i = 0; i < content.size(); i++) {
                String s = String.format("^[A-Z]{2} %s;$", idToExclude);
                if(content.get(i).matches(s)) {
                    content.remove(i);
                }
            }
        }


        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for(String s : content) {
                stringBuilder.append(s).append("\r\n");
            }

            return stringBuilder.toString();
        }

    }

    private LinkedList<Node> nodes;
    private String title;
    private LinkedList<String> removedIDs;

    public SDG() {
        nodes = new LinkedList<>();
        title = "";
        removedIDs = new LinkedList<>();
    }


    public void setRelevantPackage(String packageName) {
        Node.relevantPackage = packageName;
    }


    public void setRelevantMethods(String... methodsToAnalyze) {
        Node.relevantMethods = methodsToAnalyze;
    }


    public void construct(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String line = "";
        Node node = new Node();
        int totalNodes = 0;

        System.out.println(file.getName());

        if(scanner.hasNextLine()) {
            title = scanner.nextLine();
        }
        else {
            return;
        }

        while(scanner.hasNextLine()) {
            line = scanner.nextLine();

            if(line.contains("{")) {
                totalNodes++;
                node = new Node();
                node.append(line);
            }
            else if(line.contains("}")) {
                // prevent last line
                if(node != null) {
                    node.append(line);

                    // exclude nodes that are related to internal Java
                    if(node.isRelevant()) {
                        nodes.addLast(node);
                    }
                    else {
                        removedIDs.addLast(node.id);
                    }

                    node = null;
                }
            }
            else {
                node.append(line);
            }

        }

        System.out.println("Total =  " + totalNodes);
        System.out.println("Remain = " + nodes.size());
        System.out.println("Remove = " + removedIDs.size());
        System.out.println();
        prune();

    }


    private void prune() {
        LinkedList<Node> prunedNodes = new LinkedList<>();

        for(Node node : nodes) {
            for(String id : removedIDs) {
                node.prune(id);
            }
            prunedNodes.addLast(node);
        }

        nodes = deepCopyNodes(prunedNodes);
    }


    private LinkedList<Node> deepCopyNodes(LinkedList<Node> list) {
        LinkedList<Node> copy = new LinkedList<>();

        for(Node node : list) {
            copy.addLast(node);
        }

        return copy;
    }


    public void save(String folderPath, String fileName) throws Exception {
        File folder = new File(folderPath);
        folder.mkdir();
        FileOutputStream fileOutputStream = new FileOutputStream(folderPath + fileName);
        PrintWriter printWriter = new PrintWriter(fileOutputStream, true);

        printWriter.println(title);
        while(!nodes.isEmpty()) {
            printWriter.print(nodes.removeFirst().toString());
        }
        printWriter.println("}");
    }


}
