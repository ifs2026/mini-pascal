import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;      
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Classe Principal do Compilador Mini-Pascal.
 * 
 * RESPONSABILIDADES:
 * - Menu interativo para executar todas as entregas
 * - Integração com Maven para build e testes
 * - Demos de análise semântica e geração de código
 * 
 * AJUSTES IMPLEMENTADOS:
 * - Detecção automática do caminho do Maven
 * - Validação de Maven instalado
 * - Comentários expandidos
 * - Tratamento de erros
 * - Método saveToFile() implementado em MiniPascalCodeGenerator
 * - Informação completa sobre relatórios (incluindo semântico)
 */
public class Main {

    // Logger para tratamento de erros profissional
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    // Caminho do Maven (detectado automaticamente ou fallback para PATH)
    private static final String MVN_PATH = detectMavenPath();
    
    // Diretório de relatórios centralizado
    private static final String REPORTS_DIR = "test-reports/";

    /**
     * Detecta automaticamente o caminho do Maven.
     * 
     * PRIORIDADES:
     * 1. Variável de ambiente MAVEN_HOME
     * 2. Maven no PATH do sistema
     * 3. Caminho padrão Windows (fallback)
     * 
     * AJUSTE: Detecção automática para funcionar em diferentes ambientes.
     * 
     * @return Caminho do executável Maven
     */
    private static String detectMavenPath() {
        // Tenta MAVEN_HOME
        String mavenHome = System.getenv("MAVEN_HOME");
        if (mavenHome != null && !mavenHome.isEmpty()) {
            String os = System.getProperty("os.name").toLowerCase();
            String mvnCmd = os.contains("win") ? "mvn.cmd" : "mvn";
            return mavenHome + "/bin/" + mvnCmd;
        }
        
        // Tenta Maven no PATH
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "mvn.cmd"; // Windows
        } else {
            return "mvn"; // Linux/Mac
        }
    }

    /**
     * Verifica se Maven está instalado e acessível.
     * 
     * AJUSTE: Validação antes de executar comandos Maven.
     * 
     * @return true se Maven está disponível, false caso contrário
     */
    private static boolean verificarMaven() {
        try {
            Process p = new ProcessBuilder(MVN_PATH, "-version").start();
            int exitCode = p.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            // ✅ CORRIGIDO: Multicatch ao invés de catch genérico
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Restaura flag de interrupção
            }
            LOGGER.log(Level.WARNING, "Erro ao verificar Maven", e);
            return false;
        }
    }

    /**
     * Exibe informações sobre a localização dos relatórios de testes.
     */
    private static void exibirInfoRelatorios() {
        System.out.println("\n LOCALIZAÇÃO DOS RELATÓRIOS DE TESTES:");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println(" Pasta principal: " + REPORTS_DIR);
        System.out.println("   ├──                       (Relatórios JUnit XML/TXT)");
        System.out.println("   ├── tac-output/           (Código Intermediário .tac)");
        System.out.println("   ├── parse-trees/          (Árvores de Parse .txt)");
        System.out.println("   ├── lexer-tokens/         (Tokens do Lexer .txt)");
        System.out.println("   └── semantic-errors/      (Erros Semânticos .txt)");
        System.out.println("════════════════════════════════════════════════════════");
        System.out.println(" Dica: Execute 'mvn test' para gerar relatórios completos.");
        System.out.println(" Arquivos semânticos são gerados em: test_parser_output.txt");
        System.out.println();
    }

    public static void main(String[] args) {
        // Try-with-resources para fechar o scanner automaticamente
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("╔════════════════════════════════════════════════════════╗");
            System.out.println("║    MINI-PASCAL COMPILADOR - IFS Itabaiana 2025.2       ║");
            System.out.println("║        Menu de Execução das Entregas                   ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");
            
            // Verifica Maven
            if (verificarMaven()) {
                System.out.println(" Maven encontrado em: " + MVN_PATH);
            } else {
                System.out.println("  Maven não encontrado. Comandos Maven podem falhar.");
                System.out.println("   Instale Maven ou configure MAVEN_HOME.");
            }
            
            System.out.println("════════════════════════════════════════════════════════");

            while (true) {
                System.out.println("\n ESCOLHA UMA OPÇÃO:");
                System.out.println("════════════════════════════════════════════════════════");
                System.out.println("1 - Entrega 1: Gerar parser/lexer (mvn generate-sources)");
                System.out.println("2 - Entrega 2: Compilar projeto (mvn compile)");
                System.out.println("3 - Entrega 3: Rodar todos os testes (mvn test)");
                System.out.println("4 - Testes apenas do Lexer");
                System.out.println("5 - Testes apenas do Parser");
                System.out.println("6 - Limpar projeto (mvn clean)");
                System.out.println("7 - Demo Semântica (tabela de símbolos + escopos)");
                System.out.println("8 - Entrega 4: Geração de Código Intermediário (C3E)");
                System.out.println("9 - Ver localização dos relatórios de testes");
                System.out.println("0 - Sair");
                System.out.println("════════════════════════════════════════════════════════");
                System.out.print("Opção: ");

                int opcao;
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // limpa o buffer
                } catch (java.util.InputMismatchException e) {
                    System.out.println("❌ Entrada inválida. Digite um número.");
                    scanner.nextLine(); // limpa buffer
                    continue;
                }

                if (opcao == 0) {
                    System.out.println("\n Saindo... Projeto finalizado!");
                    System.out.println(" Obrigado por usar o Compilador Mini-Pascal!");
                    break;
                }

                String comando = switch (opcao) {
                    case 1 -> "generate-sources";
                    case 2 -> "compile";
                    case 3 -> "test";
                    case 4 -> "test -Dtest=MiniPascalLexerTest";
                    case 5 -> "test -Dtest=MiniPascalParserTest";
                    case 6 -> "clean";
                    case 7, 8, 9 -> null; // Demos internas ou info
                    default -> null;
                };

                if (comando != null) {
                    executarComandoMaven(comando);
                } else if (opcao == 7) {
                    demoSemanticaCompleta();
                } else if (opcao == 8) {
                    demoGeracaoCodigoIntermediario();
                } else if (opcao == 9) {
                    exibirInfoRelatorios();
                } else {
                    System.out.println("❌ Opção inválida. Tente novamente.");
                }
            }
        } catch (Exception e) {
            System.err.println(" Erro inesperado: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro inesperado no main", e);
        }
    }

    /**
     * Executa comandos Maven via ProcessBuilder.
     * 
     * FLUXO:
     * 1. Constrói comando completo
     * 2. Executa via ProcessBuilder
     * 3. Captura stdout e stderr em tempo real
     * 4. Exibe código de saída
     * 
     * @param comandoMaven Comando Maven (ex: "compile", "test")
     */
    private static void executarComandoMaven(String comandoMaven) {
        List<String> comandoCompleto = new ArrayList<>();
        comandoCompleto.add(MVN_PATH);
        comandoCompleto.addAll(Arrays.asList(comandoMaven.split(" ")));

        System.out.println("\n Executando: " + String.join(" ", comandoCompleto));
        System.out.println("════════════════════════════════════════════════════════");

        try {
            Process process = new ProcessBuilder(comandoCompleto).start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                System.out.println(" Saída do comando:");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("\n  Erros (se houver):");
                boolean hasErrors = false;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                    hasErrors = true;
                }
                
                if (!hasErrors) {
                    System.out.println(" Nenhum erro detectado");
                }

                int exitCode = process.waitFor();
                System.out.println("\n════════════════════════════════════════════════════════");
                if (exitCode == 0) {
                    System.out.println(" Comando executado com sucesso!");
                    if (comandoMaven.contains("test")) {
                        System.out.println(" Relatórios salvos em: " + REPORTS_DIR);
                    }
                } else {
                    System.out.println(" Comando falhou (código " + exitCode + ")");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(" Erro ao executar Maven: " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Restaura flag de interrupção
            }
            LOGGER.log(Level.SEVERE, "Erro ao executar Maven", e);
        }
    }

    /**
     * Demo de análise semântica: Tabela de símbolos e escopos.
     * 
     * TESTES:
     * 1. Declaração no escopo global
     * 2. Lookup válido e inválido
     * 3. Criação de escopo interno
     * 4. Shadowing (variável com mesmo nome em escopo diferente)
     * 5. Redeclaração (erro)
     * 6. Saída de escopo (variável local deixa de existir)
     */
    private static void demoSemanticaCompleta() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   DEMO SEMÂNTICA (Tabela de Símbolos + Escopos)       ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        
        SymbolTable table = new SymbolTable();

        System.out.println("\n Escopo global:");
        table.declare("x", Type.INTEGER);
        table.declare("y", Type.REAL);
        table.declare("nome", Type.STRING);
        System.out.println(" Declarado: x (INTEGER), y (REAL), nome (STRING)");

        System.out.print("\n Lookup: x = ");
        try {
            System.out.println(table.lookup("x"));
        } catch (SemanticException e) {
            System.out.println(" ERRO: " + e.getMessage());
        }

        System.out.print(" Lookup inválido: z = ");
        try {
            table.lookup("z");
        } catch (SemanticException e) {
            System.out.println(" OK - " + e.getMessage());
        }

        System.out.println("\n Entrando em escopo interno:");
        table.enterScope();
        table.declare("contador", Type.INTEGER);
        System.out.println(" Declarado: contador (INTEGER) no escopo local");

        System.out.print("\n Lookup contador (interno): ");
        try {
            System.out.println(table.lookup("contador"));
        } catch (SemanticException e) {
            System.out.println(" ERRO: " + e.getMessage());
        }

        System.out.print(" Lookup x (visível do global): ");
        try {
            System.out.println(table.lookup("x"));
        } catch (SemanticException e) {
            System.out.println(" ERRO: " + e.getMessage());
        }

        System.out.println("\n Simulação de atribuição: contador := contador + 1 (válido)");

        System.out.print("\n Tentativa de redeclaração: contador já existe → ");
        try {
            table.declare("contador", Type.INTEGER);
            System.out.println(" SUCESSO (não deveria!)");
        } catch (SemanticException e) {
            System.out.println(" OK - " + e.getMessage());
        }

        System.out.println("\n Saindo do escopo interno:");
        table.exitScope();

        System.out.print("🔍 Lookup contador (fora do escopo): ");
        try {
            table.lookup("contador");
            System.out.println(" SUCESSO (não deveria!)");
        } catch (SemanticException e) {
            System.out.println(" OK - " + e.getMessage());
        }

        System.out.println("\n Demo finalizada!");
    }

    /**
     * Demo de geração de código intermediário (TAC).
     * 
     * TESTES:
     * 1. Expressões aritméticas e if/else
     * 2. Expressões complexas e while (AJUSTE: removido FOR)
     * 3. I/O (read/write)
   
     * AJUSTE: Adicionado salvamento em arquivo via saveToFile().
        */
    private static void demoGeracaoCodigoIntermediario() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║   DEMO GERAÇÃO DE CÓDIGO INTERMEDIÁRIO (C3E)           ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        // 1. Definição dos Testes
        String teste1 = """
            program ExemploBasico;
            var x, y: integer;
            begin
              x := 10 + 5 * 2;
              if x > 15 then y := 1 else y := 0;
            end.""";

        String teste2 = """
            program ExemploAvancado;
            var x, y, resultado: integer;
            begin
              x := 5; y := 10;
              resultado := (x + 5) * (y - 2);
              while resultado < 100 do
              begin
                resultado := resultado + 1;
              end;
            end.""";

        // Teste de I/O (Requisito 1.2 do PDF)
        String teste3 = """
            program TesteIO;
            var idade: integer;
            begin
              write('Digite sua idade:');
              read(idade);
              if idade >= 18 then
                write('Maior de idade')
              else
                write('Menor de idade');
            end.""";

        List<String> listaTestes = Arrays.asList(teste1, teste2, teste3);

        // 2. Execução em Sequência
        for (int i = 0; i < listaTestes.size(); i++) {
            System.out.println("\n════════════════════════════════════════════════════════");
            System.out.println("  EXECUTANDO TESTE " + (i + 1) + ":");
            System.out.println("════════════════════════════════════════════════════════");
            String codigo = listaTestes.get(i);
            System.out.println(" Código de entrada:\n" + codigo);

            try {
                // Fluxo ANTLR
                miniPascalLexer lexer = new miniPascalLexer(CharStreams.fromString(codigo));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                miniPascalParser parser = new miniPascalParser(tokens);
                ParseTree tree = parser.program();

                // Geração de Código
                MiniPascalCodeGenerator generator = new MiniPascalCodeGenerator();
                generator.visit(tree);

                // Resultado
                System.out.println("\n Código Intermediário Gerado:");
                generator.printInstructions();

                // AJUSTE: Salvar em arquivo (método agora existe em MiniPascalCodeGenerator)
                String filename = "teste" + (i + 1) + "_output.tac";
                generator.saveToFile(filename);

            } catch (RuntimeException e) {
                //  CORRIGIDO (linha 432): Apenas RuntimeException (IOException removida)
                System.err.println(" Erro no Teste " + (i + 1) + ": " + e.getMessage());
                System.err.println(" Dica: Certifique-se de que rodou 'mvn compile' (Opção 2).");
                LOGGER.log(Level.SEVERE, "Erro na geração de código intermediário - Teste " + (i + 1), e);
            }
        }
        System.out.println("\n════════════════════════════════════════════════════════");
        System.out.println(" FIM DA DEMO DE GERAÇÃO");
        System.out.println(" Arquivos salvos em: " + REPORTS_DIR + "tac-output/");
        System.out.println("════════════════════════════════════════════════════════");
    }
}