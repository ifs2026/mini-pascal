import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class MiniPascalLexerTest {

    @Test
    public void testaTokensSimples() {
        String codigo = "var x: integer; x := 10;";
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComIfElse() {
        String codigo = "program p; var x: integer; begin if x > 0 then x := x - 1 else x := 0; end.";
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComWhile() {
        String codigo = "program w; var i: integer; begin i := 0; while i < 10 do i := i + 1; end.";
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComStringEBool() {
        String codigo = "program s; var msg: string; var b: boolean; begin msg := 'ok'; b := true; end.";
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComIO() {
        String codigo = """
            program io;
            var valor: integer;
            begin
                read(valor);
                print('O valor digitado foi: ', valor);
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComExpressoesComplexas() {
        String codigo = """
            program expr;
            var a, b, resultado: integer;
            begin
                a := 5 + 3 * 2 - 4;
                b := 10;
                resultado := (a > b) and (b <> 0) or not (a = 10);
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComComentario() {
        String codigo = """
            program coment;
            { Este eh um comentario de bloco }
            var x: integer; (* outro comentario *)
            begin
                x := 42; // comentario de linha
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComMultiplasVariaveisEDiferentesTipos() {
        String codigo = """
            program vars;
            var idade: integer;
                altura: real;
                ativo: boolean;
            begin
                idade := 25;
                altura := 1.75;
                ativo := true;
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComRepeticoesAninhadas() {
        String codigo = """
            program aninhado;
            var i, j: integer;
            begin
                for i := 1 to 5 do
                    while j < 10 do
                        j := j + 1;
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    public void testaTokensComStringComApostrofoEscapado() {
        String codigo = """
            program string;
            var mensagem: string;
            begin
                mensagem := 'O preco eh R$ 10,00 e o nome eh ''Joao''.';
            end.
            """;
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        assertFalse(tokens.getTokens().isEmpty());
    }

}