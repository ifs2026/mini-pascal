import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class MiniPascalParserTest {

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

    @Test
    public void programaValidoNaoDaErro() {
        String codigo = "program p; var x: integer; begin x := 10; end.";
        miniPascalParser parser = criarParserSemErros(codigo);
        parser.program();
    }

    @Test
    public void programaComIfElseValido() {
        String codigo = "program p1; var x: integer; begin x := 10; if x > 0 then x := x - 1 else x := 0; end.";
        miniPascalParser parser = criarParserSemErros(codigo);
        parser.program();
    }

    @Test
    public void programaComWhileValido() {
        String codigo = "program p2; var i: integer; begin i := 0; while i < 5 do begin print(i); i := i + 1; end end.";
        miniPascalParser parser = criarParserSemErros(codigo);
        parser.program();
    }

    @Test
    public void programaComForValido() {
        String codigo = "program p3; var i: integer; begin for i := 1 to 10 do print(i); end.";
        miniPascalParser parser = criarParserSemErros(codigo);
        parser.program();
    }

    @Test
    public void testVariavelNaoDeclarada1() {
        String codigo = "program p; begin x := 10; end.";
        assertThrows(SemanticException.class, () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
    }

    @Test
    public void testVariavelNaoDeclarada() {
        String codigo = "program p; begin x := 10; end.";
        SemanticException ex = assertThrows(SemanticException.class, 
                () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        assertEquals("Variável 'x' não declarada", ex.getMessage());
    }
    @Test
    public void testRedeclaracaoNoMesmoEscopo1() {
        String codigo = "program p; var x, x: integer; begin end.";
        assertThrows(SemanticException.class, () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
    }

    @Test
    public void testRedeclaracaoNoMesmoEscopo() {
        String codigo = "program p; var x, x: integer; begin end.";
        SemanticException ex = assertThrows(SemanticException.class, 
                () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        assertEquals("Variável 'x' já declarada neste escopo", ex.getMessage());
    }

    @Test
    public void testEscopoAninhado1() {
        String codigo = """
                        program p;
                        var x: integer;
                        begin
                            x := 5;
                            begin
                                var y: integer;
                                y := 10;
                            end
                            y := 20;
                        end.""";
        assertThrows(SemanticException.class, () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
    }

    @Test
    public void testEscopoAninhado() {
    String codigo = """
                    program p;
                    var x: integer;
                    begin
                        x := 5;
                        begin
                            var y: integer;
                            y := 10;
                        end
                        y := 20;
                    end.""";
        SemanticException ex = assertThrows(SemanticException.class, 
                () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
        assertEquals("Variável 'y' não declarada", ex.getMessage());
    }
}