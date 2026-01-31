import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de Símbolos: Armazena informações sobre as variáveis durante a compilação.
 * Utiliza uma pilha de mapas para gerenciar escopos aninhados (escopo global e local).
 */
public class SymbolTable {

    // Pilha de escopos: Cada mapa representa um nível de escopo (ex: global, bloco BEGIN/END).
    // O topo da pilha (peek) é sempre o escopo mais interno (atual).
    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    public SymbolTable() {
        // Ao instanciar, cria o primeiro nível: o Escopo Global.
        scopes.push(new HashMap<>());
    }

    /**
     * Cria um novo escopo ao entrar em um bloco (ex: BEGIN).
     * Novas variáveis declaradas aqui não serão vistas por blocos externos.
     */
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    /**
     * Destrói o escopo atual ao sair de um bloco (ex: END).
     * As variáveis locais declaradas neste bloco deixam de existir para o compilador.
     */
    public void exitScope() {
        // Proteção para garantir que nunca removeremos o escopo global.
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    /**
     * Tenta registrar uma nova variável no escopo atual.
     * @param id Nome da variável.
     * @param type Tipo da variável (INTEGER, REAL, etc).
     * @throws SemanticException Caso a variável já tenha sido declarada no MESMO escopo.
     */
    public void declare(String id, Type type) {
        Map<String, Type> current = scopes.peek();
        if (current.containsKey(id)) {
            // Regra: Não se pode declarar duas variáveis com o mesmo nome no mesmo nível.
            throw new SemanticException("Variavel '" + id + "' ja declarada neste escopo");
        }
        current.put(id, type);
    }

    /**
     * Procura por uma variável em todos os escopos visíveis, do mais interno para o externo.
     * @param id Nome da variável buscada.
     * @return O Tipo da variável se encontrada.
     * @throws SemanticException Caso a variável não seja encontrada em nenhum escopo.
     */
    public Type lookup(String id) {
        // Percorre a pilha do topo para a base (do escopo local para o global).
        for (Map<String, Type> scope : scopes) {
            Type t = scope.get(id);
            if (t != null) {
                return t; // Encontrou a variável em algum nível de escopo visível.
            }
        }
        // Se percorreu toda a pilha e não achou, a variável não foi declarada.
        throw new SemanticException("Variavel '" + id + "' nao declarada");
    }
}