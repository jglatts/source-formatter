# source-formatter
Java program to read C-like source code and remove new line starting brackets 
<br>
Can also remove comments

## Run the Demo
From your cmd line:
<br>
```
git clone https://github.com/jglatts/source-formatter
cd source-formatter
javac main.java
java main
```

## Example Usage 

```java
public static void main(String[] args) {
    SourceFormatter sourceFormatter = new SourceFormatter("t.txt", true); 
    sourceFormatter.generateFormat();
}
```
<br>
Pipe in a txt file full of file names
<br>


```
java SourceFormatter < files.txt
```

<br>
For single file usage
<br>


```
java SourceFormatter big_file.c
```

<br>

## Format STM32Cube code
```
java STMFormatter <project_name>
``'