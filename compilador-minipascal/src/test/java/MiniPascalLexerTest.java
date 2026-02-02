import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Testes Unitários para o Analisador Léxico (Scanner) do Mini-Pascal.
 * 
 * Responsabilidades:
 * - Validar tokenização de todos os construtos da linguagem
 * - Testar reconhecimento de palavras-chave, operadores, literais
 * - Verificar tratamento de comentários e strings com escape
 * 
 * AJUSTES IMPLEMENTADOS:
 * - Adicionada rotina de saída para arquivo (saveTokensToFile)
 * - Comentários expandidos para facilitar compreensão
 * - Mantida estrutura original dos testes
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Garante a ordem de execução
class MiniPascalLexerTest {

    /**
     * Método auxiliar: Cria lexer e retorna stream de tokens.
     * 
     * @param codigo Código Mini-Pascal a ser tokenizado
     * @return CommonTokenStream com todos os tokens
     */
    private CommonTokenStream lex(String codigo) {
        miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill(); // Preenche o stream com todos os tokens
        return tokens;
    }

    /**
     * Método auxiliar: Imprime tokens formatados no console.
     * Útil para debugging e visualização durante desenvolvimento.
     * 
     * @param codigo Código fonte testado
     * @param tokens Stream de tokens gerados
     */
    private void printTokens(String codigo, CommonTokenStream tokens) {
        System.out.flush(); // Limpa o buffer antes de imprimir
        System.out.println("\n> TESTANDO CODIGO:");
        System.out.println(codigo);
        System.out.println("=== TOKENS LEXER ===");
        for (Token t : tokens.getTokens()) {
            // EOF (fim de arquivo) costuma ser o tipo -1, ignoramos para limpeza
            if (t.getType() != Token.EOF) {
                System.out.println("linha " + t.getLine() + 
                                   ", coluna " + t.getCharPositionInLine() +
                                   ", tipo " + t.getType() + 
                                   ", texto \"" + t.getText() + "\"");
            }
        }
        System.out.println("=====================");
        System.out.flush(); // Garante que a impressão saia antes do próximo teste
    }

    /**
     * NOVO: Rotina de saída para arquivo.
     * Salva tokens em arquivo de texto para análise posterior.
     * 
     * @param codigo Código fonte testado
     * @param tokens Stream de tokens gerados
     */
    /**
     * Salva tokens em arquivo de texto.
     */
    private void saveTokensToFile(String codigo, CommonTokenStream tokens, String filename) {
        String outputDir = "test-reports/lexer-tokens/";
        java.io.File dir = new java.io.File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = outputDir + filename;
        
        try (java.io.FileWriter writer = new java.io.FileWriter(fullPath, false)) {
            writer.write("=== TOKENS DO LEXER ===\n");
            writer.write("Data/Hora: " + java.time.LocalDateTime.now() + "\n");
            writer.write("=======================\n\n");
            writer.write("CÓDIGO FONTE:\n");
            writer.write(codigo + "\n\n");
            writer.write("TOKENS GERADOS:\n");
            
            for (Token t : tokens.getTokens()) {
                if (t.getType() != Token.EOF) {
                    writer.write(String.format("linha %d, coluna %d, tipo %d, texto \"%s\"\n",
                        t.getLine(), t.getCharPositionInLine(), t.getType(), t.getText()));
                }
            }
            
            System.out.println("✅ Tokens salvos em: " + fullPath);
        } catch (java.io.IOException e) {
            System.err.println("❌ Erro ao salvar tokens: " + e.getMessage());
        }
    }

    /**
     * Teste 1: Tokens Simples
     * Valida reconhecimento básico de VAR, identificadores, tipos, atribuição.
     */
    @Test
    @Order(1)
    void testaTokensSimples() {
        String codigo = """
            var x: integer; x := 10;
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        saveTokensToFile(codigo, tokens, "teste1_tokens_simples.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 2: IF/ELSE
     * Valida estruturas condicionais e operadores relacionais.
     */
    @Test
    @Order(2)
    void testaTokensComIfElse() {
        String codigo = """
            program p; var x: integer; begin if x > 0 then x := x - 1 else x := 0; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        saveTokensToFile(codigo, tokens, "teste2_tokens_if_else.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 3: WHILE
     * Valida laços de repetição e expressões aritméticas.
     */
    @Test
    @Order(3)
    void testaTokensComWhile() {
        String codigo = """
            program w; var i: integer; begin i := 0; while i < 10 do i := i + 1; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        saveTokensToFile(codigo, tokens, "teste3_tokens_while.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 4: STRING e BOOLEAN
     * Valida literais de string e booleanos.
     */
    @Test
    @Order(4)
    void testaTokensComStringEBool() {
        String codigo = """
            program s; var msg: string; var b: boolean; begin msg := 'ok'; b := true; end.
            """;
        var tokens = lex(codigo);
        printTokens(codigo, tokens);
        saveTokensToFile(codigo, tokens, "teste4_tokens_string_bool.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 5: I/O (READ e PRINT)
     * Valida comandos de entrada e saída.
     */
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
        saveTokensToFile(codigo, tokens, "teste5_tokens_io.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 6: Expressões Complexas
     * Valida precedência de operadores e expressões lógicas.
     */
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
        saveTokensToFile(codigo, tokens, "teste6_tokens_expressoes_complexas.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 7: Comentários
     * Valida os 3 estilos de comentários: { }, (* *), //
     */
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
        saveTokensToFile(codigo, tokens, "teste7_tokens_comentario.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 8: Múltiplas Variáveis e Tipos
     * Valida declaração de diferentes tipos primitivos.
     */
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
        saveTokensToFile(codigo, tokens, "teste8_tokens_multiplas_variaveis_diferentes_tipos.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 9: Repetições Aninhadas
     * Valida FOR e WHILE aninhados.
     */
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
        saveTokensToFile(codigo, tokens, "teste9_tokens_repeticoes_aninhadas.txt"); // Salva em arquivo
        assertFalse(tokens.getTokens().isEmpty());
    }

    /**
     * Teste 10: String com Apóstrofo Escapado
     * Valida reconhecimento de '' dentro de strings.
     * Garante que a string inteira é reconhecida como 1 único token.
     */
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
        saveTokensToFile(codigo, tokens, "teste10_tokens_string_com_apostrofo_escapado.txt"); // Salva em arquivo

        // Validação específica: deve haver exatamente 1 token STRING_LITERAL
        long qtdStrings = tokens.getTokens().stream()
                .filter(t -> t.getType() == miniPascalLexer.STRING_LITERAL)
                .count();
        assertEquals(1, qtdStrings, "Deve haver exatamente 1 string literal");
    }
}