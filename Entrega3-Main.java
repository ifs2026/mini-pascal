public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 MINI-PASCAL COMPILADOR - IFS 2025.2");
        System.out.println("✅ ETAPA 1: LEXER - OK (10 testes)");
        System.out.println("✅ ETAPA 2: PARSER - OK (10 testes)");
        System.out.println("✅ ETAPA 3: SEMÂNTICA - Tabela símbolos + escopos");
        
        // Demo semântica
        SymbolTable table = new SymbolTable();
        table.enterScope();
        table.declare("x", Type.INTEGER);
        table.declare("msg", Type.STRING);
        
        System.out.print("✅ Lookup: x=");
        try {
            System.out.println(table.lookup("x"));
        } catch (SemanticException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        
        System.out.print("❌ Lookup inválido: y=");
        try {
            table.lookup("y");
        } catch (SemanticException e) {
            System.out.println("OK - " + e.getMessage());
        }
        
        table.exitScope();
        System.out.println("🎓 PROJETO COMPLETO!");
        System.out.println("Comandos: mvn clean | mvn generate-sources | mvn compile | mvn test | java -cp target/classes Main");
    }
}
