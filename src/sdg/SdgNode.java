package sdg;

import java.util.ArrayList;


class SdgNode {
    private ArrayList<String> content;
    private String id;
    private static String[] relevantPackages;
    private static String[] relevantClasses;
    private static String[] relevantMethods;


    SdgNode() {
        content = new ArrayList<>();
        id = "";
    }


    String getId() {
        return id;
    }


    static void setRelevantPackages(String[] relevantPackages) {
        SdgNode.relevantPackages = relevantPackages;
    }


    static void setRelevantClasses(String[] relevantClasses) {
        SdgNode.relevantClasses = relevantClasses;
    }


    static void setRelevantMethods(String[] relevantMethods) {
        SdgNode.relevantMethods = relevantMethods;
    }


    void append(String line) {
        content.add(line);
        if (line.contains("{")) {
            id = line.split("\\s+")[1];
        }
    }


    boolean isRelevant() {
        return isRelevantPackages() && isRelevantClasses() && isRelevantMethods();
    }


    private boolean isRelevantPackages() {
        int relevantBothSandBvals = 0;

        for (String packageName : relevantPackages) {
            for (String s : content) {
                if (s.startsWith(String.format("S \"%s/", packageName))) {
                    relevantBothSandBvals++;
                }
                else if (s.startsWith(String.format("B \"%s.", packageName))) {
                    relevantBothSandBvals++;
                }

                if (relevantBothSandBvals == 2) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isRelevantClasses() {
        return isRelevantContext(relevantClasses, true);
    }


    private boolean isRelevantMethods() {
        return isRelevantContext(relevantMethods, false);
    }


    private boolean isRelevantContext(String[] context, boolean isCheckingClasses) {
        if (context == null || context.length == 0) {
            return true;
        }
        else {
            for (String c : context) {
                for (String s : content) {
                    if (isCheckingClasses) {
                        if (s.contains("." + c + ".")) {
                            return true;
                        }
                    }
                    else {
                        if (s.contains("." + c + "(")) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }


    void prune(String idToExclude) {
        for (int i = 0; i < content.size(); i++) {
            String s = String.format("^[A-Z]{2} %s;$", idToExclude);
            if (content.get(i).matches(s)) {
                content.remove(i);
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String s : content) {
            stringBuilder.append(s).append("\r\n");
        }

        return stringBuilder.toString();
    }


    @Override
    public boolean equals(Object obj) {
        SdgNode anotherNode = (SdgNode) obj;

        if (this.content.size() != anotherNode.content.size()) {
            return false;
        }

        for (int i = 0; i < this.content.size(); i++) {
            String thisLine = this.content.get(i);
            String anotherLine = anotherNode.content.get(i);

            if (!thisLine.equals(anotherLine) &&
                    !areLinesCanBeIgnored(thisLine, anotherLine)) {
                return false;

            }

        }

        return true;
    }


    private boolean areLinesCanBeIgnored(String firstLine, String secondLine) {
        final String V_PATTERN = "^V \"\\S+.\\S+.\\S+\\(\\S+$";
        final String S_PATTERN = "^S \"\\S+/\\S+.java\":\\S+$";
        final String B_PATTERN = "^B \"\\S+.\\S+.\\S+\\(\\[\\S+$";

        return (firstLine.matches(V_PATTERN) && secondLine.matches(V_PATTERN)) ||
                (firstLine.matches(S_PATTERN) && secondLine.matches(S_PATTERN)) ||
                (firstLine.matches(B_PATTERN) && secondLine.matches(B_PATTERN));
    }

}
