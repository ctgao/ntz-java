package rocks.zipcode;

import java.util.ArrayList;
import java.util.Set;

public class NoteList extends ArrayList<String> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // yes, really, it's just a no-additional functionality subclass.
    public NoteList(String s) {
        super();
        this.add(s);
    }

    // but i'm changing the toString to be prettier
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        int noteNum = 1;
        for (String k : this) {
            s.append("\t");
            s.append(noteNum++);
            s.append(") ");
            s.append(k);
            s.append("\n");
        }
        return s.toString();
    }
}
