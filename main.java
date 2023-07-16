public class main {
    public static void main(String[] args) {
        new SourceFormatter("t.txt", true).generateFormat();
        new SourceFormatter("big_file.c", "02.c", true).generateFormat();
        new SourceFormatter("another_big.c", "03.c", true).generateFormat();

    }
}
