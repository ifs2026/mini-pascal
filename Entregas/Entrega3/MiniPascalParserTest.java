import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MiniPascalParserTest {

    private miniPascalParser criarParserSemErros(String codigo) {
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        miniPascalParser parser = new miniPascalParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg,
                                    RecognitionException e) {
                fail("Erro de sintaxe na linha " + line + ":" + charPositionInLine + " - " + msg);
            }
        });
        return parser;
    }

    private ParseTree parseAndPrintTree(String codigo) {
        miniPascalParser parser = criarParserSemErros(codigo);
        ParseTree tree = parser.program();

        System.out.flush(); 
        System.out.println("\n> TESTANDO CODIGO:");
        System.out.println(codigo);
        System.out.println("=== ARVORE PARSE (hierarquica) ===");
        printTreeIndented(tree, parser, 0);
        System.out.println("==================================");
        System.out.flush(); 

        return tree;
    }

    private void printTreeIndented(ParseTree node, miniPascalParser parser, int level) {
        String indent = "  ".repeat(level);
        String prefix = level == 0 ? "" : (level == 1 ? "+-- " : "|-- ");
        String nodeText = node.getText().replace("\n", "\\n").replace("\r", "\\r");

        String ruleName = "terminal";
        
        if (node instanceof RuleNode ruleNode) {
            int ruleIndex = ruleNode.getRuleContext().getRuleIndex();
            if (ruleIndex >= 0 && ruleIndex < parser.getRuleNames().length) {
                ruleName = parser.getRuleNames()[ruleIndex];
            }
        }

        System.out.println(indent + prefix + ruleName + " (" + nodeText + ")");

        for (int i = 0; i < node.getChildCount(); i++) {
            printTreeIndented(node.getChild(i), parser, level + 1);
        }
    }

    @Test
    @Order(1)
    void programaValidoNaoDaErro() {
        String codigo = """
            program p;
            begin
            end.
            """;
        ParseTree tree = parseAndPrintTree(codigo);
        assertNotNull(tree);
        assertEquals("program", tree.getChild(0).getText());
    }

    @Test
    @Order(2)
    void programaComIfElseValido() {
        String codigo = """
            program p;
            var x: integer;
            begin
                if x > 0 then x := x - 1 else x := 0;
            end.
            """;
        ParseTree tree = parseAndPrintTree(codigo);
        assertNotNull(tree);
    }

    @Test
    @Order(3)
    void programaComWhileValido() {
        String codigo = """
            program w;
            var i: integer;
            begin
                i := 0;
                while i < 10 do i := i + 1;
            end.
            """;
        ParseTree tree = parseAndPrintTree(codigo);
        assertNotNull(tree);
    }

    @Test
    @Order(4)
    void programaComForValido() {
        String codigo = """
            program f;
            var i: integer;
            begin
                for i := 1 to 5 do print(i);
            end.
            """;
        ParseTree tree = parseAndPrintTree(codigo);
        assertNotNull(tree);
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    @Order(5)
    void testVariavelNaoDeclarada() {
        String codigo = """
            program p;
            begin
                x := 10;
            end.
            """;
        System.out.println("\n> TESTANDO CODIGO (SEMANTICO):");
        System.out.println(codigo);
        
        SemanticException ex = assertThrows(SemanticException.class, 
            () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage()); // Mensagem incluída aqui
        System.out.println("----------------------------------");
        System.out.flush();
        
        assertEquals("Variavel 'x' nao declarada", ex.getMessage());
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    @Order(6)
    void testRedeclaracaoNoMesmoEscopo() {
        String codigo = """
            program p;
            var x, x: integer;
            begin
            end.
            """;
        System.out.println("\n> TESTANDO CODIGO (SEMANTICO):");
        System.out.println(codigo);
        
        SemanticException ex = assertThrows(SemanticException.class, 
            () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage()); // Mensagem incluída aqui
        System.out.println("----------------------------------");
        System.out.flush();
        
        assertEquals("Variavel 'x' ja declarada neste escopo", ex.getMessage());
    }

    @SuppressWarnings("ThrowableNotThrown")
    @Test
    @Order(7)
    void testEscopoAninhado() {
        String codigo = """
            program p;
            var x: integer;
            begin
                x := 5;
                begin
                    var y: integer;
                    y := 10;
                end;
                y := 20;
            end.
            """;
        System.out.println("\n> TESTANDO CODIGO (SEMANTICO):");
        System.out.println(codigo);
        
        SemanticException ex = assertThrows(SemanticException.class, 
            () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage()); // Mensagem incluída aqui
        System.out.println("----------------------------------");
        System.out.flush();
        
        assertEquals("Variavel 'y' nao declarada", ex.getMessage());
    }
}