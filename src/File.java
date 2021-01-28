public class File {
    private String filename;
    private char type;

    public File(String f) {
        String[] infoFiles = f.split(" ");
        this.filename = infoFiles[infoFiles.length-1];
        this.type = infoFiles[0].charAt(0);
    }

    public String getFilename() {
        return filename;
    }

    public char getType() {
        return type;
    }

    public boolean fileIsDirectory() {
        return type == 'd' && fileIsPrintable();
    }

    public boolean fileIsPrintable() {
        return !filename.equals(".") && !filename.equals("..");
    }

}
