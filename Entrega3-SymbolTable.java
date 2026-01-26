import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public SymbolTable() {
        scopes.push(new HashMap<>()); // escopo global diretamente
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    public void declare(String id, Type type) {
        Map<String, Type> current = scopes.peek();
        if (current.containsKey(id)) {
            throw new SemanticException("Variável '" + id + "' já declarada neste escopo");
        }
        current.put(id, type);
    }

    public Type lookup(String id) {
        for (Map<String, Type> scope : scopes) {
            Type t = scope.get(id);
            if (t != null) return t;
        }
        throw new SemanticException("Variável '" + id + "' não declarada");
    }
}
