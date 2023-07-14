/*
 *  Formatter for C-Source and header files
 *  Will change all brackets starting on a new line
 *  Will also, optionally, remove all comments
 *  
 *  author: John Glatts
 *  date:   7/13/23
 */
import java.util.*;
import java.io.*;

class SourceFormatter {

    private String in_file;
    private String out_file;
    private boolean remove_comments;

    public SourceFormatter() {
        this("", "o.txt", false);
    }

    public SourceFormatter(String in_file) {
        this(in_file, "o.txt", false);
    }

    public SourceFormatter(String in_file, boolean remove_comments) {
        this(in_file, "o.txt", remove_comments);
    }

    public SourceFormatter(String in_file, String out_file, boolean remove_comments) {
        this.in_file = in_file;
        this.out_file = out_file;
        this.remove_comments = remove_comments;
    }

    private Scanner openFileScanner() {
        try {
            return new Scanner(new File(in_file));
        } catch(Exception e) {
            return null;
        }
    }

    public boolean generateFormat()  {
        Scanner scanner = openFileScanner();
        ArrayList<String> source = new ArrayList<>();
        int count = 0;

        while (scanner.hasNextLine()) {
            String source_line = scanner.nextLine(); 
            if (checkTokens(source_line.split(" ")) && count != 0) {
                replace(count, source);
            }
            else {
                source.add(source_line);
                count++;
            }
        }

        if (remove_comments) {
            removeComments(source);
        }

        return writeOut(source, new File(out_file));
    }

    private void removeComments(ArrayList<String> source) {
        ArrayList<Integer> remove_indices = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).contains("/*")) {
                while (true) {
                    remove_indices.add((i));
                    if (source.get(i++).contains("*/")) 
                        break;
                }
            }
        }

        for (int i = 0; i < remove_indices.size(); i++) {
            int index = (i == 0) ? remove_indices.get(i).intValue() : remove_indices.get(i).intValue() - i;
            source.remove(index);

        }

    }

    private boolean checkTokens(String[] tokens) {
        boolean paran_check = false;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals(")")) 
                paran_check = true;
            if (!paran_check && tokens[i].equals("{"))
                return true;
        }
        return false;
    }

    private boolean writeOut(ArrayList<String> source, File file) {
        try {
            FileWriter f = new FileWriter(file);
            for (String s : source) {
                f.write(s + "\n");
            }
            f.close();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    private void replace(int index, ArrayList<String> source) {
        source.set(index-1, source.get(index-1) + " {");
    }


 }