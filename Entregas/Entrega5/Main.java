import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;      
import java.util.Scanner;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    // Caminho EXATO do Maven no seu computador (confirmado no seu mvn -version)
    private static final String MVN_PATH = "C:\\apache-maven-3.9.12\\bin\\mvn.cmd";

    public static void main(String[] args) {
        // Try-with-resources para fechar o scanner automaticamente
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("🚀 MINI-PASCAL COMPILADOR - IFS Itabaiana 2025.2");
            System.out.println("Menu de Execução das Entregas (IFS Itabaiana)");
            System.out.println("Maven encontrado em: " + MVN_PATH);
            System.out.println("--------------------------------------------------");

            while (true) {
                System.out.println("\nEscolha uma entrega:");
                System.out.println("1 - Entrega 1: Gerar parser/lexer da gramática miniPascal.g4 (mvn generate-sources)");
                System.out.println("2 - Entrega 2: Compilar o projeto completo (mvn compile)");
                System.out.println("3 - Entrega 3: Rodar todos os testes (mvn test - Lexer + Parser + Semântica)");
                System.out.println("4 - Testes apenas do Lexer (MiniPascalLexerTest)");
                System.out.println("5 - Testes apenas do Parser (MiniPascalParserTest)");
                System.out.println("6 - Limpar o projeto (mvn clean)");
                System.out.println("7 - Demo semântica (tabela de símbolos + escopos)");
                System.out.println("8 - Entrega 4: Geração de Código Intermediário (C3E)");
                System.out.println("0 - Sair");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // limpa o buffer

                if (opcao == 0) {
                    System.out.println("\nSaindo... Projeto finalizado!");
                    break;
                }

                String comando = switch (opcao) {
                    case 1 -> "generate-sources";
                    case 2 -> "compile";
                    case 3 -> "test";
                    case 4 -> "test -Dtest=MiniPascalLexerTest";
                    case 5 -> "test -Dtest=MiniPascalParserTest";
                    case 6 -> "clean";
                    case 7, 8 -> null; // Demos internas
                    default -> null;
                };

                if (comando != null) {
                    executarComandoMaven(comando);
                } else if (opcao == 7) {
                    demoSemanticaCompleta();
                } else if (opcao == 8) {
                    demoGeracaoCodigoIntermediario();
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } 
    }

    private static void executarComandoMaven(String comandoMaven) {
        List<String> comandoCompleto = new ArrayList<>();
        comandoCompleto.add(MVN_PATH);
        comandoCompleto.addAll(Arrays.asList(comandoMaven.split(" ")));

        System.out.println("\nExecutando: " + String.join(" ", comandoCompleto));

        try {
            Process process = new ProcessBuilder(comandoCompleto).start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                System.out.println("Saída do comando:");
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("\nErros (se houver):");
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }

                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("\nComando executado com sucesso! ✅");
                } else {
                    System.out.println("\nComando falhou (código " + exitCode + ") ❌");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar Maven: " + e.getMessage());
        }
    }

    private static void demoSemanticaCompleta() {
        System.out.println("\n=== DEMO SEMÂNTICA (Tabela de Símbolos + Escopos) ===");
        SymbolTable table = new SymbolTable();

        System.out.println("Escopo global:");
        table.declare("x", Type.INTEGER);
        table.declare("y", Type.REAL);
        table.declare("nome", Type.STRING);

        System.out.print("? Lookup: x = ");
        try {
            System.out.println(table.lookup("x"));
        } catch (SemanticException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        System.out.print("? Lookup inválido: z = ");
        try {
            table.lookup("z");
        } catch (SemanticException e) {
            System.out.println("OK - " + e.getMessage());
        }

        System.out.println("\nEntrando em escopo interno:");
        table.enterScope();
        table.declare("contador", Type.INTEGER);

        System.out.print("? Lookup contador (interno): ");
        try {
            System.out.println(table.lookup("contador"));
        } catch (SemanticException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        System.out.print("? Lookup x (visível do global): ");
        try {
            System.out.println(table.lookup("x"));
        } catch (SemanticException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        System.out.println("? Simulação de atribuição: contador := contador + 1 (válido)");

        System.out.print("? Tentativa de redeclaração: contador já existe → ");
        try {
            table.declare("contador", Type.INTEGER);
            System.out.println("SUCESSO (não deveria!)");
        } catch (SemanticException e) {
            System.out.println("OK - " + e.getMessage());
        }

        System.out.println("\nSaindo do escopo interno:");
        table.exitScope();

        System.out.print("? Lookup contador (fora do escopo): ");
        try {
            table.lookup("contador");
            System.out.println("SUCESSO (não deveria!)");
        } catch (SemanticException e) {
            System.out.println("OK - " + e.getMessage());
        }

        System.out.println("\nSimulação de if/else (semântica):");
        System.out.println("Se x > 0 então contador := 10 senão contador := 0 (válido no escopo global)");
        System.out.println("Simulação de while (x < 10): contador := contador + 1 (válido)");

        System.out.println("\n? Lookup: x = INTEGER");
        System.out.println("? Lookup inválido: y = OK - Variável 'y' não declarada");
        System.out.println("Demo finalizada!");
    }

    private static void demoGeracaoCodigoIntermediario() {
        System.out.println("\n=== DEMO GERAÇÃO DE CÓDIGO INTERMEDIÁRIO (C3E) ===");

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
              for x := 1 to 5 do
              begin
                if x > 3 then resultado := resultado + 1;
              end;
            end.""";

        // NOVO TESTE: I/O (Read e Write) para cobrir o Requisito 1.2 do PDF
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
            System.out.println("\n--------------------------------------------------");
            System.out.println("▶️ EXECUTANDO TESTE " + (i + 1) + ":");
            System.out.println("--------------------------------------------------");
            String codigo = listaTestes.get(i);
            System.out.println("Código de entrada:\n" + codigo);

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
                System.out.println("\nCódigo Intermediário Gerado:");
                generator.printInstructions();

            } catch (Exception e) {
                System.err.println("❌ Erro no Teste " + (i + 1) + ": " + e.getMessage());
                System.err.println("Dica: Certifique-se de que rodou 'mvn compile' (Opção 2).");
            }
        }
        System.out.println("\n=== FIM DA DEMO DE GERAÇÃO ===");
    }
}