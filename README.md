# JoanaPdgPruner

Custom filter to selectively prune generated PDGs from [JOANA](https://github.com/joana-team/joana) to improve usability.

## How to Use

### Pruning

In [Main](./src/driver/Main.java), change this code snippet to configure:

```java
String[] packages = {"package1"};           // consider only codes from package1
String[] classes = {"class1", "class2"};    // consider only codes from class1 and class2
String[] methods = {};                      // consider all methods
File sourceFolder = new File("folderPath");
```

For `packages`, `classes`, and `methods`: list of desired code to keep. If any of these is left empty, it means consider all. For example, if `methods` is empty, it means "_consider all methods_". Hence, in the configuration above, it means: "Consider codes {from package1} AND {from class1 and class2} AND {all methods}".

### Comparing

The following code snippet will automatically compare 2 PDGs (JOANA format). It displays the number of different nodes to the console and save the full list of nodes (and their IDs) to the specified file.

```java
SdgComparor comparor = new SdgComparor();
comparor.compare(firstPDG, secondPDG);
comparor.save("folderPath", "fileName");
```