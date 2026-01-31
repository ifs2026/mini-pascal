import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Garante a ordem de execução
class MiniPascalLexerTest {

    private CommonTokenStream lex(String codigo) {
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        return tokens;
    }

    private void printTokens(String codigo, CommonTokenStream tokens) {
        System.out.flush(); // Limpa o buffer antes de imprimir
        System.out.println("\n> TESTANDO CODIGO:");
        System.out.println(codigo);
        System.out.println("=== TOKENS LEXER ===");
        for (Token t : tokens.getTokens()) {
            // EOF (fim de arquivo) costuma ser o tipo -1, ignoramos para limpeza ou tratamos
            if (t.getType() != Token.EOF) {
                System.out.println("linha " + t.getLine() + ", coluna " + t.getCharPositionInLine() +
                                   ", tipo " + t.getType() + " , texto \"" + t.getText() + "\"");
            }
        }
        System.out.println("=====================");
        System.out.flush(); // Garante que a impressão saia antes do próximo teste
    }

    @Test
    @Order(1)
    void testaTokensSimples() {
        String codigo = """
            var x: integer; x := 10;
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(2)
    void testaTokensComIfElse() {
        String codigo = """
            program p; var x: integer; begin if x > 0 then x := x - 1 else x := 0; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(3)
    void testaTokensComWhile() {
        String codigo = """
            program w; var i: integer; begin i := 0; while i < 10 do i := i + 1; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(4)
    void testaTokensComStringEBool() {
        String codigo = """
            program s; var msg: string; var b: boolean; begin msg := 'ok'; b := true; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(5)
    void testaTokensComIO() {
        String codigo = """
            program io;
            var valor: integer;
            begin
                read(valor);
                print('O valor digitado foi: ', valor);
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(6)
    void testaTokensComExpressoesComplexas() {
        String codigo = """
            program expr;
            var a, b, resultado: integer;
            begin
                a := 5 + 3 * 2 - 4;
                b := 10;
                resultado := (a > b) and (b <> 0) or not (a = 10);
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(7)
    void testaTokensComComentario() {
        String codigo = """
            program coment;
            { Este eh um comentario de bloco }
            var x: integer; (* outro comentario *)
            begin
                x := 42; // comentario de linha
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(8)
    void testaTokensComMultiplasVariaveisEDiferentesTipos() {
        String codigo = """
            program vars;
            var idade: integer;
                altura: real;
                ativo: boolean;
                nome: string;
            begin
                idade := 25;
                altura := 1.75;
                ativo := true;
                nome := 'Jose';
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(9)
    void testaTokensComRepeticoesAninhadas() {
        String codigo = """
            program aninhado;
            var i, j: integer;
            begin
                for i := 1 to 5 do
                    while j < 10 do
                        j := j + 1;
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        assertFalse(tokens.getTokens().isEmpty());
    }

    @Test
    @Order(10)
    void testaTokensComStringComApostrofoEscapado() {
        String codigo = """
            program string;
            var mensagem: string;
            begin
                mensagem := 'O preco eh R$ 10,00 e o nome eh ''Joao''.';
            end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);

        long qtdStrings = tokens.getTokens().stream()
                .filter(t -> t.getType() == miniPascalLexer.STRING_LITERAL)
                .count();
        assertEquals(1, qtdStrings);
    }
}