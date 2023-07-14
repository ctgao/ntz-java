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
                System.err.print(a+" ");
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
            ntzEngine.printResults();
        }
        /*
         * what other method calls do you need here to implement the other commands??
         */
        ntzEngine.saveDatabase();
    }

    private void addToCategory(String category, String[] argv) {
        // parse the argv first
        String noteText = removeQuotes(parseNote(category, argv));
        category = removeQuotes(category);

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
        category = removeQuotes(category);
        int noteNumber = Integer.parseInt(argv[2]) - 1;

        // string is ready to be added to a note list
        filemap.get(category).remove(noteNumber);
        if(filemap.get(category).isEmpty()){
            filemap.remove(category);
        }
    }

    private String parseNote(String category, String[] input){
        // parse the argv first
        StringBuilder sb = new StringBuilder();
        boolean quoteFound = false;
        for(String s : input){
            if(s.contains(category)){
                continue;
            }
            quoteFound = (s.indexOf('\"') != -1) || quoteFound;
            if(quoteFound){
                sb.append(s);
                sb.append(" ");
            }
        }
        // delete the extra space
        sb.deleteCharAt(sb.lastIndexOf(" "));

        return sb.toString();
    }

    private String removeQuotes(String in){
        return in.replace("\"", "");
    }

    private void editNote(String category, String[] argv) {
        // parse the argv first
        category = removeQuotes(category);
        int noteNumber = Integer.parseInt(argv[2]) - 1;
        String noteText = removeQuotes(parseNote(category, argv));

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
