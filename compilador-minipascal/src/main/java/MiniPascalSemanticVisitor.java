import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Analisador Semântico: Percorre a árvore sintática para validar regras de negócio da linguagem.
 * 
 * Responsabilidades principais:
 * - Garantir que toda variável seja declarada antes do uso.
 * - Impedir declarações duplicadas no mesmo escopo.
 * - Gerenciar a visibilidade de variáveis em escopos aninhados (Globais vs Locais).
 * 
 * AJUSTES IMPLEMENTADOS:
 * - Comentários expandidos para facilitar compreensão.
 */
public class MiniPascalSemanticVisitor extends miniPascalBaseVisitor<Void> {

    // A Tabela de Símbolos armazena as variáveis e seus tipos em cada nível de escopo
    private final SymbolTable table = new SymbolTable();

    // === Parte 1: Declarações de Variáveis ===

    /**
     * Visita a seção 'VAR' do programa.
     * Processa todas as declarações de variáveis antes do bloco BEGIN/END.
     * 
     * Exemplo:
     * var x, y: integer;
     *     nome: string;
     */
    @Override
    public Void visitVariableDeclarationPart(miniPascalParser.VariableDeclarationPartContext ctx) {
        if (ctx != null) {
            // Itera sobre cada linha de declaração (ex: x, y: integer;)
            for (miniPascalParser.VariableDeclarationContext decl : ctx.variableDeclaration()) {
                visit(decl);
            }
        }
        return null;
    }

    /**
     * Processa uma linha de declaração (ex: x, y: INTEGER;).
     * Mapeia os identificadores ao tipo correspondente na Tabela de Símbolos.
     * 
     * Fluxo:
     * 1. Extrai o tipo (INTEGER, REAL, STRING, BOOLEAN)
     * 2. Converte texto para enum Type
     * 3. Registra cada identificador na tabela
     * 4. Lança SemanticException se variável já existe no escopo atual
     */
    @Override
    public Void visitVariableDeclaration(miniPascalParser.VariableDeclarationContext ctx) {
        // Converte o texto do tipo (string) para o Enum Type (INTEGER, REAL, etc)
        Type type = Type.valueOf(ctx.type_().getText().toUpperCase());
        List<miniPascalParser.IdentifierContext> ids = ctx.identifierList().identifier();
        
        // Registra cada variável na tabela de símbolos
        for (miniPascalParser.IdentifierContext idCtx : ids) {
            String id = idCtx.getText();
            // Registra a variável. Se já existir no escopo atual, a tabela lançará erro.
            table.declare(id, type);
        }
        return null;
    }

    // === Parte 2: Verificação de Uso (Lookup) ===

    /**
     * Valida atribuições (ex: x := 10).
     * Verifica se 'x' foi previamente declarado.
     * 
     * PREPARADO PARA EXTENSÃO: Verificação de tipos (comentado abaixo)
     * 
     * Exemplo:
     * x := 10;        // OK se x foi declarado como INTEGER
     * y := 'texto';   // OK se y foi declarado como STRING
     * z := 5;         // ERRO se z não foi declarado
     */
    @Override
    public Void visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String id = ctx.identifier().getText();
        
        // Verifica se variável foi declarada
        table.lookup(id);           // Se não achar 'id', lança exceção semântica
        
        // EXTENSÃO FUTURA: Verificação de tipos
        // Type leftType = table.lookup(id);
        // Type rightType = inferType(ctx.expression());
        // if (!isTypeCompatible(leftType, rightType)) {
        //     throw new SemanticException("Tipos incompativeis: " + leftType + " vs " + rightType);
        // }
        
        visit(ctx.expression());    // Valida se as variáveis usadas na expressão à direita existem
        return null;
    }

    /**
     * Chamado sempre que um identificador aparece em qualquer lugar (expressões, comandos).
     * 
     * Este é o ponto de validação mais importante:
     * - Garante que toda variável usada foi declarada
     * - Busca em todos os escopos visíveis (do mais interno para o externo)
     * 
     * Exemplo:
     * x := y + z;  // Valida que y e z existem
     */
    @Override
    public Void visitIdentifier(miniPascalParser.IdentifierContext ctx) {
        String id = ctx.getText();
        table.lookup(id);           // Validação crucial: variável existe em algum escopo visível?
        return super.visitIdentifier(ctx);
    }

    // === Parte 3: Gerenciamento de Escopos ===

    /**
     * Gerencia blocos BEGIN ... END.
     * Cria um novo nível na pilha de escopos ao entrar e descarta ao sair.
     * 
     * Fluxo:
     * 1. enterScope() - Cria novo mapa na pilha (escopo local)
     * 2. Visita todos os comandos dentro do bloco
     * 3. exitScope() - Remove escopo local (variáveis locais deixam de existir)
     * 
     * Exemplo:
     * begin
     *   var x: integer;  // x é local a este bloco
     *   x := 10;
     * end;
     */
    @Override
    public Void visitCompoundStatement(miniPascalParser.CompoundStatementContext ctx) {
        table.enterScope();         // Cria novo contexto (ex: variáveis locais de um bloco)
        super.visitCompoundStatement(ctx); // Visita todos os comandos dentro do BEGIN/END
        table.exitScope();          // Remove as variáveis locais da memória do compilador
        return null;
    }

    // === Parte 4: Controle de Fluxo ===

    /**
     * Processa o bloco principal do programa.
     * Ordem: declarações de variáveis → comandos
     */
    @Override
    public Void visitBlock(miniPascalParser.BlockContext ctx) {
        visitVariableDeclarationPart(ctx.variableDeclarationPart());
        visitCompoundStatement(ctx.compoundStatement());
        return null;
    }

    /**
     * Ponto de entrada: processa o programa completo.
     */
    @Override
    public Void visitProgram(miniPascalParser.ProgramContext ctx) {
        visit(ctx.block());
        return null;
    }

    /**
     * Valida estrutura IF/THEN/ELSE.
     * Garante que variáveis na condição e nos blocos existem.
     * 
     * Exemplo:
     * if x > 5 then    // Valida que x existe
     *   y := 1         // Valida que y existe
     * else
     *   y := 0;
     */
    @Override
    public Void visitIfStatement(miniPascalParser.IfStatementContext ctx) {
        visit(ctx.expression());      // Valida variáveis na condição do IF
        visit(ctx.statement(0));      // Valida o bloco THEN
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));  // Valida o bloco ELSE
        }
        return null;
    }

    /**
     * Valida laço WHILE.
     * Garante que variáveis na condição e no corpo existem.
     * 
     * Exemplo:
     * while i < 10 do  // Valida que i existe
     *   i := i + 1;    // Valida que i existe
     */
    @Override
    public Void visitWhileStatement(miniPascalParser.WhileStatementContext ctx) {
        visit(ctx.expression());   // Condição do loop
        visit(ctx.statement());    // Corpo do loop
        return null;
    }

    /**
     * Valida o laço FOR (ex: for i := 1 to 10 do ...).
     * Garante que a variável de controle 'i' existe.
     * 
     * Exemplo:
     * for i := 1 to 10 do  // Valida que i foi declarado
     *   print(i);
     */
    @Override
    public Void visitForStatement(miniPascalParser.ForStatementContext ctx) {
        table.lookup(ctx.identifier().getText()); // Variável de controle deve estar declarada
        visit(ctx.expression(0)); // Valor inicial
        visit(ctx.expression(1)); // Valor final
        visit(ctx.statement());   // Bloco repetido
        return null;
    }

    // === Parte 5: Método Estático para Testes ===

    /**
     * Ponto de entrada para disparar a análise semântica de forma isolada.
     * Usado em testes unitários (MiniPascalParserTest).
     * 
     * Fluxo:
     * 1. Cria lexer/parser a partir do código
     * 2. Gera árvore sintática
     * 3. Percorre árvore com visitor semântico
     * 4. Lança SemanticException se houver erro
     * 
     * @param code Código Mini-Pascal a ser analisado
     * @throws SemanticException Se houver erro semântico
     */
    public static void checkSemantics(String code) {
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        miniPascalParser parser = new miniPascalParser(tokens);
        
        parser.removeErrorListeners(); // Foca apenas em erros semânticos, ignora sintáticos aqui
        ParseTree tree = parser.program();
        
        MiniPascalSemanticVisitor visitor = new MiniPascalSemanticVisitor();
        visitor.visit(tree); // Inicia a caminhada pela árvore
    }
}