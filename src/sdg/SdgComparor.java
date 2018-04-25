package sdg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;


public class SdgComparor {

    private LinkedList<String[]> differentNodes;

    public SdgComparor() {
        differentNodes = new LinkedList<>();
    }


    public void compare(SDG first, SDG second) {
        differentNodes.addLast(new String[]{first.getHeader(), second.getHeader()});

        int firstSdgSize = first.size();
        int secondSdgSize = second.size();
        int commonSize = firstSdgSize >= secondSdgSize
                ? secondSdgSize : firstSdgSize;

        for(int i = 0; i < commonSize; i++) {
            SdgNode firstNode = first.getNodeAt(i);
            SdgNode secondNode = second.getNodeAt(i);

            if(!firstNode.equals(secondNode)) {
                differentNodes.addLast(new String[]{firstNode.getId(), secondNode.getId()});
            }
        }

        for(int i = commonSize; i < firstSdgSize; i++) {
            differentNodes.addLast(new String[]{first.getNodeAt(i).getId(), ""});
        }

        for(int i = commonSize; i < secondSdgSize; i++) {
            differentNodes.addLast(new String[]{"", second.getNodeAt(i).getId()});
        }

        System.out.printf("Total of %d nodes difference\n", differentNodes.size() - 1);

    }


    public void save(String folderPath, String fileName) throws Exception {
        File folder = new File(folderPath);
        folder.mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(folderPath + fileName, false);
        PrintWriter printWriter = new PrintWriter(fileOutputStream);

        String[] headers = differentNodes.removeFirst();
        printWriter.printf("%-20s %s\n", "<PDG 1>", "<PDG 2>");

        while (!differentNodes.isEmpty()) {
            String[] pair = differentNodes.removeFirst();
            String firstNodeId = pair[0];
            String secondNodeId = pair[1];

            if(!firstNodeId.equals("") && !firstNodeId.contains(".")) {
                firstNodeId = "Node " + firstNodeId;
            }

            if(!secondNodeId.equals("") && !secondNodeId.contains(".")) {
                secondNodeId = "Node " + secondNodeId;
            }

            printWriter.printf("%-20s %s\n", firstNodeId, secondNodeId);

        }

        printWriter.println();
        printWriter.printf("PDG 1 = %s\n", headers[0]);
        printWriter.printf("PDG 2 = %s\n", headers[1]);

        printWriter.close();

    }

}
