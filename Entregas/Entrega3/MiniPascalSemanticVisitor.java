import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Analisador Semântico: Percorre a árvore sintática para validar regras de negócio da linguagem.
 * Responsabilidades principais:
 * - Garantir que toda variável seja declarada antes do uso.
 * - Impedir declarações duplicadas no mesmo escopo.
 * - Gerenciar a visibilidade de variáveis em escopos aninhados (Globais vs Locais).
 */
public class MiniPascalSemanticVisitor extends miniPascalBaseVisitor<Void> {

    // A Tabela de Símbolos armazena as variáveis e seus tipos em cada nível de escopo
    private final SymbolTable table = new SymbolTable();

    // === Parte 1: Declarações de Variáveis ===

    /**
     * Visita a seção 'VAR' do programa.
     */
    @Override
    public Void visitVariableDeclarationPart(miniPascalParser.VariableDeclarationPartContext ctx) {
        if (ctx != null) {
            for (miniPascalParser.VariableDeclarationContext decl : ctx.variableDeclaration()) {
                visit(decl);
            }
        }
        return null;
    }

    /**
     * Processa uma linha de declaração (ex: x, y: INTEGER;).
     * Mapeia os identificadores ao tipo correspondente na Tabela de Símbolos.
     */
    @Override
    public Void visitVariableDeclaration(miniPascalParser.VariableDeclarationContext ctx) {
        // Converte o texto do tipo (string) para o Enum Type (INTEGER, REAL, etc)
        Type type = Type.valueOf(ctx.type_().getText().toUpperCase());
        List<miniPascalParser.IdentifierContext> ids = ctx.identifierList().identifier();
        
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
     */
    @Override
    public Void visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String id = ctx.identifier().getText();
        table.lookup(id);           // Se não achar 'id', lança exceção semântica
        visit(ctx.expression());    // Valida se as variáveis usadas na expressão à direita existem
        return null;
    }

    /**
     * Chamado sempre que um identificador aparece em qualquer lugar (expressões, comandos).
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
     */
    @Override
    public Void visitCompoundStatement(miniPascalParser.CompoundStatementContext ctx) {
        table.enterScope();         // Cria novo contexto (ex: variáveis locais de um bloco)
        super.visitCompoundStatement(ctx); // Visita todos os comandos dentro do BEGIN/END
        table.exitScope();          // Remove as variáveis locais da memória do compilador
        return null;
    }

    // === Parte 4: Controle de Fluxo ===

    @Override
    public Void visitBlock(miniPascalParser.BlockContext ctx) {
        visitVariableDeclarationPart(ctx.variableDeclarationPart());
        visitCompoundStatement(ctx.compoundStatement());
        return null;
    }

    @Override
    public Void visitProgram(miniPascalParser.ProgramContext ctx) {
        visit(ctx.block());
        return null;
    }

    @Override
    public Void visitIfStatement(miniPascalParser.IfStatementContext ctx) {
        visit(ctx.expression());      // Valida variáveis na condição do IF
        visit(ctx.statement(0));      // Valida o bloco THEN
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));  // Valida o bloco ELSE
        }
        return null;
    }

    @Override
    public Void visitWhileStatement(miniPascalParser.WhileStatementContext ctx) {
        visit(ctx.expression());   // Condição do loop
        visit(ctx.statement());    // Corpo do loop
        return null;
    }

    /**
     * Valida o laço FOR (ex: for i := 1 to 10 do ...).
     * Garante que a variável de controle 'i' existe.
     */
    @Override
    public Void visitForStatement(miniPascalParser.ForStatementContext ctx) {
        table.lookup(ctx.identifier().getText()); // Variável de controle deve estar declarada
        visit(ctx.expression(0)); // Valor inicial
        visit(ctx.expression(1)); // Valor final
        visit(ctx.statement());   // Bloco repetido
        return null;
    }

    /**
     * Ponto de entrada para disparar a análise semântica de forma isolada.
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