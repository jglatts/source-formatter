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

    /**
     * Default Constructor
     */
    public SourceFormatter() {
        this("", "o.txt", false);
    }

    /**
     * Constructor specifing the input file
     * 
     * @param in_file, input file for formatting
     */    
    public SourceFormatter(String in_file) {
        this(in_file, "o.txt", false);
    }

    /**
     * Constructor specifing the input file and comment removal flag
     * 
     * @param in_file, input file for formatting
     * @param remove_comments, flag for comment removal
     */
    public SourceFormatter(String in_file, boolean remove_comments) {
        this(in_file, "o.txt", remove_comments);
    }

    /**
     * Constructor specifing the input file, output file and comment removal flag
     * 
     * @param in_file, input file for formatting
     * @param out_file, output file to save formatted code
     * @param remove_comments, flag for comment removal
     */
    public SourceFormatter(String in_file, String out_file, boolean remove_comments) {
        this.in_file = in_file;
        this.out_file = out_file;
        this.remove_comments = remove_comments;
    }

    /**
     * Open and return a Scanner object with the input file
     * 
     * @return the scanner object, null on failure
     */
    private Scanner openFileScanner() {
        try {
            return new Scanner(new File(in_file));
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Client driver method for generating the formatted code
     * 
     * @return true on success, false on failure
     */
    public boolean generateFormat()  {
        Scanner scanner = openFileScanner();
        ArrayList<String> source = new ArrayList<>();
        
        parseBrackets(scanner, source);
        if (remove_comments) {
            removeComments(source); 
        }

        scanner.close();
        return writeOut(source, new File(out_file));
    }

    /**
     *  Go through the source file and find any malformated brackets
     *  Replace the lines with the correct styled brackets
     * 
     * @param scanner, Scanner object 
     * @param source,  List of strings to store formatted code
     */
    private void parseBrackets(Scanner scanner, ArrayList<String> source) {
        int count = 0;
        
        while (scanner.hasNextLine()) {
            String source_line = scanner.nextLine(); 
            if (checkTokens(source_line.split(" ")) && count != 0) {
                replace(count, source);
                checkNewLineForComments(source, count);
            }
            else {
                source.add(source_line);
                count++;
            }
        }
    }

    /**
     * Check a newly generated line for any comments
     * 
     * @param source, source code as a list of strings
     * @param count, index of the line in the list
     */
    private void checkNewLineForComments(ArrayList<String> source, int count) {
        String line = source.get(count - 1);
        if (line.contains("/")) 
            source.set(count - 1, line.substring(0, line.indexOf("/")) + " { ");
    }

    /*
     * Driver method for removing comments
     */
    private void removeComments(ArrayList<String> source) {
        checkMultiLineComments(source);
        checkSingleLineComments(source);
    }

    /**
     * Check for single line comments, and remove if found, in the source code
     * 
     * @param source, source code as a list of strings
     */
    private void checkSingleLineComments(ArrayList<String> source) {
        for (int i = 0; i < source.size(); i++) {
            String[] words = source.get(i).split(" ");
            for (int j = 0; j < words.length; j++) {
                if (words[j].contains("/*") || words[j].equals("/*")) {
                    setSourceIndex(source, i, "/*");
                    break;
                }
                else if (words[j].contains("//") || words[j].equals("//")) {
                    setSourceIndex(source, i, "//");
                    break;
                }
            }
        }        
    }

    /**
     * Replce a string in the source code specified at a given index
     * 
     * @param source, source code as a list of strings
     * @param i, index of string to be modified
     * @param seq, string to get index of
     */
    private void setSourceIndex(ArrayList<String> source, int i, String seq) {
        int end_index = source.get(i).indexOf(seq);
        source.set(i, source.get(i).substring(0, end_index));
    }

    /**
     * Check for mutliline comments in the source code
     * 
     * @param source, source code as a list of strings
     */
    private void checkMultiLineComments(ArrayList<String> source) {
        ArrayList<Integer> remove_indices = new ArrayList<>();
        
        for (int i = 0; i < source.size(); i++) {
            String[] words = source.get(i).trim().split(" ");
            if (words[0].contains("/*") ) {
                while (true) {
                    if (source.get(i).contains("*/")) {
                        remove_indices.add(i);
                        break;
                    }
                    remove_indices.add((i++));
                }
            }
        }

        for (int i = 0; i < remove_indices.size(); i++) {
            int index = (i == 0) ? remove_indices.get(i).intValue() : remove_indices.get(i).intValue() - i;
            source.remove(index);
        }
    }

    /**
     * Check a line for a valid token or malformatted bracket
     * 
     * @param tokens, arryay of strings composed of the source code line
     * @return true if malformatted bracked is found, false if valid token is found
     */
    private boolean checkTokens(String[] tokens) {
        boolean ret = false;

        for (int i = 0; i < tokens.length; i++) {
            if (checkValidToken(tokens[i])) {
                return false;
            }
            if (tokens[i].equals("{"))
                ret = true;
        }

        return ret;
    }

    /**
     * Check a line of code, C-like, for a valide token
     * If a valid token is found, we ignore the line for further processing
     * 
     * @param line, line to be examined
     * @return true if valid token is found, false otherwise
     */
    private boolean checkValidToken(String line) {
        line = line.trim();
        String[] valid_tokens = {"struct", "enum", "#define", "[]", ")", "(", "}"};

        for (String tok : valid_tokens) {
            if (line.contains(tok)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Write the formatted code to the output file
     * 
     * @param source, formatted soure code as list of strings
     * @param file, outfile
     * @return true on success, false otherwise
     */
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

    /**
     * Reads a filename from stdin to format
     * Typically called when piping input form the cmd line
     * java SourceFormatter < list_of_files.txt 
     */
    public static void pipe() {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String file_name = scanner.nextLine();
            new SourceFormatter(file_name, "new_"+ file_name, true).generateFormat();
            System.out.println("Please see new_" + file_name);
        }
        
        scanner.close();
    }

    /**
     * Main method
     * 
     * @param args, command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0)
            pipe();
        else {
                new SourceFormatter(args[0], "new_"+ args[0], true).generateFormat();
                System.out.println("Please see new_" + args[0]);            
        }    
    }


 }