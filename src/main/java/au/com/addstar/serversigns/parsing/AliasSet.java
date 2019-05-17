package au.com.addstar.serversigns.parsing;

import java.util.Collection;
import java.util.HashSet;

public class AliasSet extends HashSet<String> {
    public AliasSet(String... aliases) {
        super(java.util.Arrays.asList(aliases));
    }

    public AliasSet(Collection<String> aliases) {
        super(aliases);
    }

    public boolean matches(String input) {
        for (String next : this) {
            if (next.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }
}


/* Location:              C:\Users\benjamincharlton\Downloads\ServerSigns.jar!\de\czymm\serversigns\parsing\AliasSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */