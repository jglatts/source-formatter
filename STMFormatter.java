import java.io.File;

class STMFormatter {

    private String user;

    public STMFormatter() {
        // change to your username for the STMCube path
        user = "John";
    }

    private File[] findProjectCoreFiles(String project_name) {
        String folder_core = "C:\\Users\\" + user + "\\STM32CubeIDE\\workspace_1.11.0\\" + project_name + "\\Core\\Src";
        
        File directory = new File(folder_core);
        File[] contents = directory.listFiles();

        return contents;
    }

    private File[] findProjectIncFiles(String project_name) {
        String folder_inc = "C:\\Users\\" + user + "\\STM32CubeIDE\\workspace_1.11.0\\" + project_name + "\\Core\\Inc";
        
        File directory = new File(folder_inc);
        File[] contents = directory.listFiles();

        return contents;
    }

    public void format(String project_name) {
        updateFileContents(findProjectCoreFiles(project_name));
        updateFileContents(findProjectIncFiles(project_name));
    }

    private void updateFileContents(File[] files) {
        for (File f : files) {
            SourceFormatter sf = new SourceFormatter(f.getAbsolutePath(), f.getAbsolutePath(), true);
            System.out.println("Updating " + f.getAbsolutePath());  
            sf.generateFormat();
        }    
    }

    public static void main(String[] args) {
        STMFormatter stm = new STMFormatter();
        if (args.length == 0) {
            System.out.println("Error\nUsage: java STMFormatter <project_name>");
            return;
        }
        else {
            stm.format(args[0]);
        }
    }

}