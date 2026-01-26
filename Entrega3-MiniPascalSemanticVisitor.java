import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Visitor semântico completo para Mini-Pascal.
 * Compatível com a gramática fornecida.
 */
public class MiniPascalSemanticVisitor extends miniPascalBaseVisitor<Void> {

    private final SymbolTable table = new SymbolTable();

    // === Declarações de variáveis ===
    @Override
    public Void visitVariableDeclarationPart(miniPascalParser.VariableDeclarationPartContext ctx) {
        if (ctx != null) {
            for (miniPascalParser.VariableDeclarationContext decl : ctx.variableDeclaration()) {
                visit(decl);
            }
        }
        return null;
    }

    @Override
    public Void visitVariableDeclaration(miniPascalParser.VariableDeclarationContext ctx) {
        Type type = Type.valueOf(ctx.type_().getText().toUpperCase());
        List<miniPascalParser.IdentifierContext> ids = ctx.identifierList().identifier();
        for (miniPascalParser.IdentifierContext idCtx : ids) {
            String id = idCtx.getText();
            table.declare(id, type);
        }
        return null;
    }

    // === Atribuição :=
    @Override
    public Void visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String id = ctx.identifier().getText();
        table.lookup(id); // verifica se foi declarada
        visit(ctx.expression()); // visita expressão à direita (para checar vars usadas)
        return null;
    }

    // === Qualquer uso de identificador (em expressões, parâmetros, etc.) ===
    @Override
    public Void visitIdentifier(miniPascalParser.IdentifierContext ctx) {
        String id = ctx.getText();
        table.lookup(id); // lança exceção se não declarada
        return super.visitIdentifier(ctx);
    }

    // === Gerenciamento de escopos em BEGIN ... END ===
    @Override
    public Void visitCompoundStatement(miniPascalParser.CompoundStatementContext ctx) {
        table.enterScope();
        super.visitCompoundStatement(ctx);
        table.exitScope();
        return null;
    }

    // === Bloco principal ===
    @Override
    public Void visitBlock(miniPascalParser.BlockContext ctx) {
        visitVariableDeclarationPart(ctx.variableDeclarationPart());
        visitCompoundStatement(ctx.compoundStatement());
        return null;
    }

    // === Programa completo ===
    @Override
    public Void visitProgram(miniPascalParser.ProgramContext ctx) {
        visit(ctx.block());
        return null;
    }

    // === If / Else ===
    @Override
    public Void visitIfStatement(miniPascalParser.IfStatementContext ctx) {
        visit(ctx.expression());           // condição
        visit(ctx.statement(0));           // then
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1));       // else
        }
        return null;
    }

    // === While ===
    @Override
    public Void visitWhileStatement(miniPascalParser.WhileStatementContext ctx) {
        visit(ctx.expression());           // condição
        visit(ctx.statement());            // corpo
        return null;
    }

    // === For (corrigido: na sua gramática, for tem identifier := expression TO/DOWNTO expression) ===
    @Override
    public Void visitForStatement(miniPascalParser.ForStatementContext ctx) {
        // Verifica a variável de controle
        table.lookup(ctx.identifier().getText());

        // Visita expressão inicial e final
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        // Visita o corpo
        visit(ctx.statement());
        return null;
    }

    // === Método estático para testes ===
    public static void checkSemantics(String code) {
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        miniPascalParser parser = new miniPascalParser(tokens);
        parser.removeErrorListeners(); // evita mensagens de erro sintático no console

        ParseTree tree = parser.program();

        MiniPascalSemanticVisitor visitor = new MiniPascalSemanticVisitor();
        visitor.visit(tree);
    }
}