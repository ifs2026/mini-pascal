import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de Símbolos: Armazena informações sobre as variáveis durante a compilação.
 * Utiliza uma pilha de mapas para gerenciar escopos aninhados (escopo global e local).
 * 
 * ESTRUTURA:
 * - Pilha de escopos: Cada mapa representa um nível de escopo
 * - Topo da pilha (peek): Escopo mais interno (atual)
 * - Base da pilha: Escopo global
 */
public class SymbolTable {

    // Pilha de escopos: Cada mapa representa um nível de escopo (ex: global, bloco BEGIN/END).
    // O topo da pilha (peek) é sempre o escopo mais interno (atual).
    private final Deque<Map<String, Type>> scopes = new ArrayDeque<>();

    /**
     * Construtor: Inicializa a tabela com o escopo global.
     * O escopo global sempre existe e nunca é removido.
     */
    public SymbolTable() {
        // Ao instanciar, cria o primeiro nível: o Escopo Global.
        scopes.push(new HashMap<>());
    }

    /**
     * Cria um novo escopo ao entrar em um bloco (ex: BEGIN).
     * Novas variáveis declaradas aqui não serão vistas por blocos externos.
     * 
     * Exemplo de uso:
     * begin
     *   enterScope();  // Cria escopo local
     *   var x: integer; // x é local
     * end;
     * exitScope();     // Remove escopo local
     */
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    /**
     * Destrói o escopo atual ao sair de um bloco (ex: END).
     * As variáveis locais declaradas neste bloco deixam de existir para o compilador.
     * 
     * PROTEÇÃO: Nunca remove o escopo global (size > 1).
     */
    public void exitScope() {
        // Proteção para garantir que nunca removeremos o escopo global.
        if (scopes.size() > 1) {
            scopes.pop();
        }
    }

    /**
     * Tenta registrar uma nova variável no escopo atual.
     * 
     * REGRA: Não se pode declarar duas variáveis com o mesmo nome no MESMO escopo.
     * PERMITIDO: Declarar variável com mesmo nome em escopos DIFERENTES (shadowing).
     * 
     * @param id Nome da variável.
     * @param type Tipo da variável (INTEGER, REAL, STRING, BOOLEAN).
     * @throws SemanticException Caso a variável já tenha sido declarada no MESMO escopo.
     * 
     * Exemplos:
     * - declare("x", INTEGER) → OK
     * - declare("x", REAL)    → ERRO (x já existe no escopo atual)
     * - enterScope(); declare("x", REAL) → OK (escopo diferente, shadowing)
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
     * 
     * BUSCA: Do topo da pilha (escopo local) para a base (escopo global).
     * SHADOWING: Se variável existe em múltiplos escopos, retorna a mais interna.
     * 
     * @param id Nome da variável buscada.
     * @return O Tipo da variável se encontrada.
     * @throws SemanticException Caso a variável não seja encontrada em nenhum escopo.
     * 
     * Exemplos:
     * - Global: x=INTEGER, Local: y=REAL
     * - lookup("x") → INTEGER (encontra no global)
     * - lookup("y") → REAL (encontra no local)
     * - lookup("z") → ERRO (não existe)
     * 
     * - Global: x=INTEGER, Local: x=REAL
     * - lookup("x") → REAL (shadowing, prioriza local)
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

    /**
     * Método de debug: Imprime todos os escopos e suas variáveis.
     * Útil para visualizar o estado da tabela durante desenvolvimento.
     * 
     * ADIÇÃO: Facilita debugging e compreensão de escopos aninhados.
     */
    public void printScopes() {
        System.out.println("\n=== TABELA DE SÍMBOLOS ===");
        int level = scopes.size() - 1;
        for (Map<String, Type> scope : scopes) {
            String scopeName = (level == 0) ? "Global" : "Local " + level;
            System.out.println(scopeName + ": " + scope);
            level--;
        }
        System.out.println("==========================\n");
    }

    /**
     * Verifica se uma variável está declarada no escopo ATUAL (não busca em escopos externos).
     * 
     * ADIÇÃO: Útil para validações específicas de redeclaração.
     * 
     * @param id Nome da variável.
     * @return true se existe no escopo atual, false caso contrário.
     */
    public boolean isDeclaredInCurrentScope(String id) {
        return scopes.peek().containsKey(id);
    }

    /**
     * Retorna o número de escopos ativos (incluindo global).
     * 
     * ADIÇÃO: Útil para testes e debugging.
     * 
     * @return Quantidade de escopos na pilha.
     */
    public int getScopeCount() {
        return scopes.size();
    }
}