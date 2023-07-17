/**
 *  Java progam to go through a STMCubeIDE project directory
 *  and format the core\inc and core\src files
 * 
 * author: John Glatts
 * date: 7/16/23
 */
import java.io.File;

class STMFormatter {

    private String user;

    public STMFormatter(String user) {
        // change to your username for the STMCube path
        this.user = user;
    }

    private File[] findProjectFiles(File dir) {
        return dir.listFiles();
    }

    public void format(String project_name) {
        updateFileContents(findProjectFiles(new File("C:\\Users\\" + user + "\\STM32CubeIDE\\workspace_1.11.0\\" + project_name + "\\Core\\Src")));
        updateFileContents(findProjectFiles(new File("C:\\Users\\" + user + "\\STM32CubeIDE\\workspace_1.11.0\\" + project_name + "\\Core\\Inc")));
    }

    private void updateFileContents(File[] files) {
        for (File f : files) {
            System.out.println("Updating " + f.getAbsolutePath());  
            SourceFormatter sf = new SourceFormatter(f.getAbsolutePath(), f.getAbsolutePath(), true);
            sf.generateFormat();
        }    
    }

    public static void main(String[] args) {
        STMFormatter stm = new STMFormatter("john");
        if (args.length == 0) {
            System.out.println("Error\nUsage: java STMFormatter <project_name>");
            return;
        }
        else {
            stm.format(args[0]);
        }
    }

}