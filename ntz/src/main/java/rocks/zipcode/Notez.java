package rocks.zipcode;

/**
 * ntz main command.
 */
public final class Notez {

    private FileMap filemap;

    public Notez() {
        this.filemap  = new FileMap();
    }
    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String argv[]) {
        boolean _debug = false;
        // for help in handling the command line flags and data!
        if (_debug) {
            System.err.print("Argv: [");
            for (String a : argv) {
                System.err.print(a+", ");
            }
            System.err.println("]");
        }

        Notez ntzEngine = new Notez();

        ntzEngine.loadDatabase();

        /*
         * You will spend a lot of time right here.
         *
         * instead of loadDemoEntries, you will implement a series
         * of method calls that manipulate the Notez engine.
         * See the first one:
         */
//        ntzEngine.loadDemoEntries();
        // TESTING
//        argv = "-r \"Another note\"".split(" ");
//        argv = "-c \"note2\" \"testing Notes\"".split(" ");
//        argv = "-f \"note2\" 1".split(" ");
//        argv = "-e \"General\" 1 \"replace\"".split(" ");

        if (argv.length == 0) { // there are no commandline arguments
            //just print the contents of the filemap.
            ntzEngine.printResults();
            // and print the help menu
            ntzEngine.helpMenu();
        } else {
            if (argv[0].equals("-r")) {
                ntzEngine.addToCategory("General", argv);
            } // this should give you an idea about how to TEST the Notez engine
              // without having to spend lots of time messing with command line arguments.
            else if(argv[0].equals("-c")){
                // creates and/or appends to a category
                ntzEngine.addToCategory(argv[1], argv);
            }
            else if(argv[0].equals("-f")){
                // forgets a note
                ntzEngine.removeFromCategory(argv[1], argv);
            }
            else if(argv[0].equals("-e")){
                // edit or replace a note
                ntzEngine.editNote(argv[1], argv);
            }
//            ntzEngine.printResults();
        }
        /*
         * what other method calls do you need here to implement the other commands??
         */
        ntzEngine.saveDatabase();
    }

    private void helpMenu() {
        System.out.println(" --- COMMANDS MENU --- ");
        System.out.println("-r(emember in General) \"note contents\"");
        System.out.println("-c(reate) \"category name\" \"note contents\"");
        System.out.println("-f(orget) \"category name\" note-integer");
        System.out.println("-e(dit) \"category name\" note-integer \"new note contents\"\n");
    }

    private void addToCategory(String category, String[] argv) {
        // parse the argv first
        String noteText;
        if(argv[0].equals("-r")){
            noteText = parseNote(1, argv);
        }
        else { // creating the category maybe
            noteText = parseNote(2, argv);
        }

        // string is ready to be added to a note list
        if(!filemap.containsKey(category)){
            filemap.put(category, new NoteList(noteText));
        }
        else{
            filemap.get(category).add(noteText);
        }
    }

    private void saveDatabase() {
        filemap.save();
    }

    private void loadDatabase() {
        filemap.load();
    }

    public void printResults() {
        System.out.println(this.filemap.toString());
    }

    public void loadDemoEntries() {
        filemap.put("General", new NoteList("The Very first Note"));
        filemap.put("note2", new NoteList("A secret second note"));
        filemap.put("category3", new NoteList("Did you buy bread AND eggs?"));
        filemap.put("anotherNote", new NoteList("Hello from ZipCode!"));
    }
    /*
     * Put all your additional methods that implement commands like forget here...
     */
    private void removeFromCategory(String category, String[] argv) {
        // parse the argv first
        int noteNumber = Integer.parseInt(argv[2]) - 1;

        // string is ready to be added to a note list
        filemap.get(category).remove(noteNumber);
        if(filemap.get(category).isEmpty()){
            filemap.remove(category);
        }
    }

    private String parseNote(int startIndex, String[] input){
        // parse the argv first
        StringBuilder sb = new StringBuilder();
        for(int i = startIndex; i < input.length; i++){
            sb.append(input[i]);
            sb.append(" ");
        }
        String retVal = sb.toString();

        return retVal.substring(0, retVal.length() - 1);
    }

    private void editNote(String category, String[] argv) {
        // parse the argv first
        int noteNumber = Integer.parseInt(argv[2]) - 1;
        String noteText = parseNote(3, argv);

        try {
            // string is ready to be replacing a file
            filemap.get(category).remove(noteNumber);
            filemap.get(category).add(noteNumber, noteText);
        }
        catch (Exception e){
            // got here bc you tried to delete something that don't exist
//            System.out.println("ohno plz fix me");
        }
    }
}
