import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

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

/**
 * Testes Unitários para o Parser Sintático e Análise Semântica do Mini-Pascal.
 * 
 * RESPONSABILIDADES:
 * - Validar geração correta de AST (Abstract Syntax Tree)
 * - Testar análise semântica (variáveis não declaradas, redeclarações, escopos)
 * - Verificar detecção de erros sintáticos e semânticos
 * 
 * AJUSTES IMPLEMENTADOS:
 * - ✅ Salvamento de árvores de parse em test-reports/parse-trees/
 * - ✅ Salvamento de erros semânticos em test-reports/semantic-errors/
 * - ✅ Arquivos sempre sobrescritos (não acumulam)
 * - ✅ Timestamp em cada arquivo
 * - ✅ Comentários expandidos para facilitar compreensão
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MiniPascalParserTest {

    /**
     * Cria parser com listener customizado que falha imediatamente em erros sintáticos.
     * Usado em testes que esperam sucesso sintático.
     * 
     * @param codigo Código Mini-Pascal a ser parseado
     * @return Parser configurado
     */
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

    /**
     * Salva a árvore de parse em arquivo de texto.
     * 
     * AJUSTE: Salva em test-reports/parse-trees/ com timestamp.
     * 
     * @param tree Árvore de parse
     * @param parser Parser usado
     * @param filename Nome do arquivo de saída
     */
    private void saveTreeToFile(ParseTree tree, miniPascalParser parser, String filename) {
        String outputDir = "test-reports/parse-trees/";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = outputDir + filename;
        
        try (FileWriter writer = new FileWriter(fullPath, false)) { // false = sobrescrever
            writer.write("=== ÁRVORE DE PARSE (AST) ===\n");
            writer.write("Data/Hora: " + LocalDateTime.now() + "\n");
            writer.write("=============================\n\n");
            
            // Escreve árvore hierárquica
            writeTreeIndented(tree, parser, 0, writer);
            
            System.out.println("✅ Árvore salva em: " + fullPath);
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar árvore: " + e.getMessage());
        }
    }

    /**
     * Escreve árvore de parse de forma hierárquica em arquivo.
     * 
     * @param node Nó atual da árvore
     * @param parser Parser para obter nomes de regras
     * @param level Nível de indentação
     * @param writer Writer para escrever no arquivo
     */
    private void writeTreeIndented(ParseTree node, miniPascalParser parser, int level, FileWriter writer) throws IOException {
        String indent = "  ".repeat(level);
        String prefix = level == 0 ? "" : (level == 1 ? "+-- " : "|-- ");
        String nodeText = node.getText().replace("\n", "\\n").replace("\r", "\r");

        String ruleName = "terminal";
        
        if (node instanceof RuleNode ruleNode) {
            int ruleIndex = ruleNode.getRuleContext().getRuleIndex();
            if (ruleIndex >= 0 && ruleIndex < parser.getRuleNames().length) {
                ruleName = parser.getRuleNames()[ruleIndex];
            }
        }

        writer.write(indent + prefix + ruleName + " (" + nodeText + ")\n");

        for (int i = 0; i < node.getChildCount(); i++) {
            writeTreeIndented(node.getChild(i), parser, level + 1, writer);
        }
    }

    /**
     * Parseia código e imprime AST hierárquica.
     * 
     * AJUSTE: Salva árvore em arquivo automaticamente.
     * 
     * @param codigo Código Mini-Pascal a ser parseado
     * @param testName Nome do teste (usado no nome do arquivo)
     * @return Árvore sintática gerada
     */
    private ParseTree parseAndPrintTree(String codigo, String testName) {
        miniPascalParser parser = criarParserSemErros(codigo);
        ParseTree tree = parser.program();

        System.out.flush(); 
        System.out.println("\n> TESTANDO CODIGO:");
        System.out.println(codigo);
        System.out.println("=== ARVORE PARSE (hierarquica) ===");
        printTreeIndented(tree, parser, 0);
        System.out.println("==================================");
        System.out.flush();
        
        // AJUSTE: Salvar árvore em arquivo
        saveTreeToFile(tree, parser, testName + "_tree.txt");

        return tree;
    }

    /**
     * Imprime AST de forma hierárquica com indentação.
     * Mostra estrutura de árvore visual para debugging.
     * 
     * @param node Nó atual da árvore
     * @param parser Parser para obter nomes de regras
     * @param level Nível de indentação
     */
    private void printTreeIndented(ParseTree node, miniPascalParser parser, int level) {
        String indent = "  ".repeat(level);
        String prefix = level == 0 ? "" : (level == 1 ? "+-- " : "|-- ");
        String nodeText = node.getText().replace("\n", "\\n").replace("\r", "\r");

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

    /**
     * AJUSTE: Salva erros semânticos em pasta específica.
     * Anteriormente salvava na raiz do projeto, agora salva em test-reports/semantic-errors/
     * 
     * @param codigo Código fonte testado
     * @param testName Nome do teste
     * @param errorMessage Mensagem de erro
     */
    private void saveSemanticErrorToFile(String codigo, String testName, String errorMessage) {
        String outputDir = "test-reports/semantic-errors/";
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        String fullPath = outputDir + testName + "_error.txt";
        
        try (FileWriter writer = new FileWriter(fullPath, false)) { // false = sobrescrever
            writer.write("=== ERRO SEMÂNTICO DETECTADO ===\n");
            writer.write("Data/Hora: " + LocalDateTime.now() + "\n");
            writer.write("Teste: " + testName + "\n");
            writer.write("================================\n\n");
            writer.write("CÓDIGO FONTE:\n");
            writer.write(codigo + "\n\n");
            writer.write("ERRO DETECTADO:\n");
            writer.write(errorMessage + "\n");
            
            System.out.println("✅ Erro semântico salvo em: " + fullPath);
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar erro semântico: " + e.getMessage());
        }
    }

    // === TESTES SINTÁTICOS (1-4) ===

    /**
     * Teste 1: Programa Mínimo Válido
     * Valida estrutura básica: program ... begin end.
     */
    @Test
    @Order(1)
    void programaValidoNaoDaErro() {
        String codigo = """
            program p;
            begin
            end.
            """;
        ParseTree tree = parseAndPrintTree(codigo, "teste1_programa_valido");
        assertNotNull(tree);
        assertEquals("program", tree.getChild(0).getText());
    }

    /**
     * Teste 2: IF/ELSE Válido
     * Valida estrutura condicional com variável declarada.
     */
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
        ParseTree tree = parseAndPrintTree(codigo, "teste2_if_else");
        assertNotNull(tree);
    }

    /**
     * Teste 3: WHILE Válido
     * Valida laço de repetição.
     */
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
        ParseTree tree = parseAndPrintTree(codigo, "teste3_while_valido");
        assertNotNull(tree);
    }

    /**
     * Teste 4: FOR Válido
     * Valida laço FOR com TO.
     */
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
        ParseTree tree = parseAndPrintTree(codigo, "teste4_for_valido");
        assertNotNull(tree);
    }

    // === TESTES SEMÂNTICOS (5-7) ===

    /**
     * Teste 5: Variável Não Declarada
     * Valida detecção de uso de variável sem declaração.
     */
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
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage());
        System.out.println("----------------------------------");
        System.out.flush();
        
        // AJUSTE: Salva em pasta correta
        saveSemanticErrorToFile(codigo, "teste5_variavel_nao_declarada", ex.getMessage());
        
        assertEquals("Variavel 'x' nao declarada", ex.getMessage());
    }

    /**
     * Teste 6: Redeclaração no Mesmo Escopo
     * Valida detecção de variável declarada duas vezes.
     */
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
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage());
        System.out.println("----------------------------------");
        System.out.flush();
        
        // AJUSTE: Salva em pasta correta
        saveSemanticErrorToFile(codigo, "teste6_redeclaracao_mesmo_escopo", ex.getMessage());
        
        assertEquals("Variavel 'x' ja declarada neste escopo", ex.getMessage());
    }

    /**
     * Teste 7: Escopo Aninhado
     * Valida que variável local não é visível fora do escopo.
     */
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
        
        System.out.println("MSG ERRO SEMANTICO: " + ex.getMessage());
        System.out.println("----------------------------------");
        System.out.flush();
        
        // AJUSTE: Salva em pasta correta
        saveSemanticErrorToFile(codigo, "teste7_escopo_aninhado", ex.getMessage());
        
        assertEquals("Variavel 'y' nao declarada", ex.getMessage());
    }
}