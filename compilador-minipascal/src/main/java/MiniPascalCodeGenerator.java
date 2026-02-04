import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe percorre a árvore gerada pelo ANTLR e traduz os comandos para
 * Código de Três Endereços (C3E). Cada método 'visit' lida com uma regra da gramática.
 * 
 * AJUSTES FEITOS:
 * - Comentários expandidos em blocos e linhas
 * - Implementação completa de FOR (TO/DOWNTO)
 * - Correção de acesso a factor() sem índice
 * - Suporte a operadores lógicos (AND, OR, NOT)
 */
public class MiniPascalCodeGenerator extends miniPascalBaseVisitor<String> {

    // Lista que armazena todas as instruções geradas para serem exibidas ao final
    private final List<Instruction> instructions = new ArrayList<>();
    
    // Contadores para garantir que cada temporário (t0, t1...) e rótulo (L0, L1...) seja único
    private int tempCount = 0;
    private int labelCount = 0;

    /**
     * Gera um novo nome de variável temporária (ex: t0, t1, t2...)
     * Usado para armazenar resultados intermediários de expressões.
     */
    private String newTemp() {
        return "t" + (tempCount++);
    }

    /**
     * Gera um novo nome de rótulo para desvios/saltos (ex: L0, L1, L2...)
     * Usado em estruturas de controle (IF, WHILE, FOR).
     */
    private String newLabel() {
        return "L" + (labelCount++);
    }

    /**
     * Retorna a lista de instruções geradas.
     * Usado para exibir o código intermediário.
     */
    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Percorre a lista de instruções acumuladas e as imprime formatadas.
     * Cada instrução é convertida para string via toString() de Instruction.
     */
    public void printInstructions() {
        System.out.println("\n=== CÓDIGO INTERMEDIÁRIO GERADO (C3E) ===");
        for (Instruction instr : instructions) {
            System.out.println(instr);
        }
    }

    // === ATRIBUIÇÃO: x := expressão ===

    /**
     * Processa comando de atribuição (ex: x := 10 + 5).
     * 
     * Fluxo:
     * 1. Identifica variável de destino (lado esquerdo)
     * 2. Resolve expressão (lado direito) - pode gerar múltiplas instruções
     * 3. Gera instrução de atribuição final
     * 
     * Exemplo:
     * x := a + b * c
     * Gera:
     * t0 := b * c
     * t1 := a + t0
     * x := t1
     */
    @Override
    public String visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String variable = ctx.identifier().getText(); // Identifica o destino (lado esquerdo)
        String value = visit(ctx.expression());       // Resolve a expressão (lado direito)
        
        // Gera: variable := value
        instructions.add(new Instruction(variable, value, null, null));
        return variable;
    }

    // === EXPRESSÕES RELACIONAIS (ex: x > 15) ===

    /**
     * Processa expressões com operadores relacionais (>, <, =, etc).
     * 
     * Exemplo:
     * x > 15
     * Gera:
     * t0 := x > 15
     */
    @Override
    public String visitExpression(miniPascalParser.ExpressionContext ctx) {
        // Verifica se há operadores relacionais (comparação)
        if (ctx.relationalOperator() != null && !ctx.relationalOperator().isEmpty()) {
            String left = visit(ctx.simpleExpression(0));
            String op = ctx.relationalOperator(0).getText(); 
            String right = visit(ctx.simpleExpression(1));
            
            String target = newTemp(); // Cria temporário para armazenar o resultado booleano
            instructions.add(new Instruction(target, left, op, right)); // Ex: t2 := x > 15
            return target;
        }
        // Se não houver comparação, apenas repassa a expressão simples
        return visit(ctx.simpleExpression(0));
    }

    // === EXPRESSÕES ARITMÉTICAS (Soma, Subtração, OR) ===

    /**
     * Processa expressões com operadores aditivos (+, -, OR).
     * Quebra expressões complexas em instruções de 3 endereços.
     * 
     * Exemplo:
     * a + b - c
     * Gera:
     * t0 := a + b
     * t1 := t0 - c
     */
    @Override
    public String visitSimpleExpression(miniPascalParser.SimpleExpressionContext ctx) {
        String lastResult = visit(ctx.term(0)); // Pega o primeiro termo
        
        // Percorre os operadores (+ ou - ou OR) e termos subsequentes
        for (int i = 0; i < ctx.additiveOperator().size(); i++) {
            String op = ctx.additiveOperator(i).getText();
            String nextTerm = visit(ctx.term(i + 1));
            String target = newTemp();
            
            // Quebra a expressão em passos: t1 := t0 + 5
            instructions.add(new Instruction(target, lastResult, op, nextTerm));
            lastResult = target;
        }
        return lastResult;
    }

    // === OPERAÇÕES DE ALTA PRECEDÊNCIA (Multiplicação, Divisão, AND) ===

    /**
     * Processa expressões com operadores multiplicativos (*, /, AND).
     * Executado antes de operadores aditivos (precedência).
     * 
     * Exemplo:
     * a * b / c
     * Gera:
     * t0 := a * b
     * t1 := t0 / c
     */
    @Override
    public String visitTerm(miniPascalParser.TermContext ctx) {
        String lastResult = visit(ctx.factor(0));
        
        for (int i = 0; i < ctx.multiplicativeOperator().size(); i++) {
            String op = ctx.multiplicativeOperator(i).getText();
            String nextFactor = visit(ctx.factor(i + 1));
            String target = newTemp();
            
            instructions.add(new Instruction(target, lastResult, op, nextFactor));
            lastResult = target;
        }
        return lastResult;
    }

    // === FATORES (Base da expressão: números, variáveis, expressões entre parênteses) ===

    /**
     * Processa elementos básicos de expressões.
     * 
     * Casos tratados:
     * - Identificadores (variáveis): x, y, nome
     * - Números: 10, 3.14
     * - Strings: 'texto'
     * - Booleanos: true, false
     * - Expressões entre parênteses: (a + b)
     * - Operadores unários: NOT, +, -
     */
    @Override
    public String visitFactor(miniPascalParser.FactorContext ctx) {
        // Identificador (variável)
        if (ctx.identifier() != null) {
            return ctx.identifier().getText();
        }
        
        // Número (inteiro ou real)
        if (ctx.unsignedNumber() != null) {
            return ctx.unsignedNumber().getText();
        }
        
        // String literal
        if (ctx.stringLiteral() != null) {
            return ctx.stringLiteral().getText();
        }
        
        // Booleano (true/false)
        if (ctx.boolLiteral() != null) {
            return ctx.boolLiteral().getText();
        }
        
        // Expressão entre parênteses: (expr)
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        
        // Operador unário NOT
        if (ctx.NOT() != null) {
            String operand = visit(ctx.getChild(1)); // Pega o factor após NOT
            String target = newTemp();
            instructions.add(new Instruction(target, operand, "NOT", null));
            return target;
        }
        
        // Operador unário + (positivo)
        if (ctx.PLUS() != null) {
            return visit(ctx.getChild(1)); // Pega o factor após +
        }
        
        // Operador unário - (negativo)
        if (ctx.MINUS() != null) {
            String operand = visit(ctx.getChild(1)); // Pega o factor após -
            String target = newTemp();
            instructions.add(new Instruction(target, "0", "-", operand));
            return target;
        }

        
        // Fallback: retorna texto bruto
        return ctx.getText();
    }

    // === CONTROLE DE FLUXO: IF THEN ELSE ===

    /**
     * Gera código para estrutura condicional IF/THEN/ELSE.
     * 
     * Exemplo:
     * if x > 5 then y := 1 else y := 0
     * 
     * Gera:
     * t0 := x > 5
     * IF_FALSE t0 goto L0
     * y := 1
     * goto L1
     * L0:
     * y := 0
     * L1:
     */
    @Override
    public String visitIfStatement(miniPascalParser.IfStatementContext ctx) {
        String condition = visit(ctx.expression()); // Avalia a condição booleana
        String labelElse = newLabel(); // Rótulo para onde pular se for falso
        String labelEnd = newLabel();  // Rótulo para o fim do IF

        // Se a condição for falsa, vai para o ELSE
        instructions.add(new Instruction(labelElse, condition, "IF_FALSE", null));
        
        visit(ctx.statement(0)); // Executa o bloco do THEN
        instructions.add(new Instruction(labelEnd, null, "GOTO", null)); // Pula o ELSE
        
        instructions.add(new Instruction(labelElse, null, "LABEL", null)); // Marca o início do ELSE
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1)); // Executa o bloco ELSE se existir
        }
        instructions.add(new Instruction(labelEnd, null, "LABEL", null)); // Marca o fim de tudo
        
        return null;
    }

    // === LAÇO DE REPETIÇÃO: WHILE DO ===

    /**
     * Gera código para laço WHILE.
     * 
     * Exemplo:
     * while i < 10 do i := i + 1
     * 
     * Gera:
     * L0:
     * t0 := i < 10
     * IF_FALSE t0 goto L1
     * t1 := i + 1
     * i := t1
     * goto L0
     * L1:
     */
    @Override
    public String visitWhileStatement(miniPascalParser.WhileStatementContext ctx) {
        String labelStart = newLabel(); // Rótulo para voltar ao teste da condição
        String labelEnd = newLabel();   // Rótulo para sair do laço

        instructions.add(new Instruction(labelStart, null, "LABEL", null)); // Início do loop
        String condition = visit(ctx.expression()); // Testa a condição
        
        instructions.add(new Instruction(labelEnd, condition, "IF_FALSE", null)); // Sai se for falso
        visit(ctx.statement()); // Executa o corpo do loop
        
        instructions.add(new Instruction(labelStart, null, "GOTO", null)); // Volta ao topo
        instructions.add(new Instruction(labelEnd, null, "LABEL", null)); // Alvo da saída
        
        return null;
    }

    // === LAÇO DE REPETIÇÃO: FOR TO/DOWNTO ===

    /**
     * Gera código para laço FOR.
     * 
     * Exemplo (FOR TO):
     * for i := 1 to 5 do print(i)
     * 
     * Gera:
     * i := 1
     * L0:
     * t0 := i > 5
     * IF_TRUE t0 goto L1
     * PRINT i
     * t1 := i + 1
     * i := t1
     * goto L0
     * L1:
     * 
     * Exemplo (FOR DOWNTO):
     * for i := 5 downto 1 do print(i)
     * 
     * Gera:
     * i := 5
     * L0:
     * t0 := i < 1
     * IF_TRUE t0 goto L1
     * PRINT i
     * t1 := i - 1
     * i := t1
     * goto L0
     * L1:
     */
    @Override
    public String visitForStatement(miniPascalParser.ForStatementContext ctx) {
        String var = ctx.identifier().getText();
        String start = visit(ctx.expression(0));
        String end = visit(ctx.expression(1));
        
        // Inicialização: var := start
        instructions.add(new Instruction(var, start, null, null));
        
        String labelStart = newLabel();
        String labelEnd = newLabel();
        
        // L0: (início do loop)
        instructions.add(new Instruction(labelStart, null, "LABEL", null));
        
        // Condição: if var > end goto L1 (para TO) ou if var < end goto L1 (para DOWNTO)
        String condition = newTemp();
        String op = ctx.TO() != null ? ">" : "<";
        instructions.add(new Instruction(condition, var, op, end));
        instructions.add(new Instruction(labelEnd, condition, "IF_TRUE", null));
        
        // Corpo do loop
        visit(ctx.statement());
        
        // Incremento/Decremento: var := var + 1 (TO) ou var := var - 1 (DOWNTO)
        String temp = newTemp();
        String incOp = ctx.TO() != null ? "+" : "-";
        instructions.add(new Instruction(temp, var, incOp, "1"));
        instructions.add(new Instruction(var, temp, null, null));
        
        // Volta ao início: goto L0
        instructions.add(new Instruction(labelStart, null, "GOTO", null));
        
        // L1: (fim do loop)
        instructions.add(new Instruction(labelEnd, null, "LABEL", null));
        
        return null;
    }

    // === ENTRADA E SAÍDA (READ e WRITE/PRINT) ===

    /**
     * Gera código para comandos de I/O.
     * 
     * Exemplo READ:
     * read(x, y)
     * Gera:
     * READ x
     * READ y
     * 
     * Exemplo WRITE/PRINT:
     * print('Ola', x)
     * Gera:
     * PRINT 'Ola'
     * PRINT x
     */
    @Override
    public String visitIoStatement(miniPascalParser.IoStatementContext ctx) {
        if (ctx.READ() != null) {
            // Gera uma instrução de leitura para cada variável: READ x
            for (miniPascalParser.IdentifierContext idCtx : ctx.identifier()) {
                instructions.add(new Instruction("READ", idCtx.getText(), null, null));
            }
        } else if (ctx.WRITE() != null || ctx.PRINT() != null) {
            // Gera uma instrução de escrita para cada expressão: PRINT 'Ola'
            for (miniPascalParser.ExpressionContext exprCtx : ctx.expression()) {
                String val = visit(exprCtx); 
                instructions.add(new Instruction("PRINT", val, null, null));
            }
        }
        return null;
    }
    /**
     * Salva as instruções geradas em um arquivo de texto. (não acumula).
     * 
     * @param filename Nome do arquivo de saída
     */
    public void saveToFile(String filename) {
        // Salvar em pasta específica
        String outputDir = "test-reports/tac-output/";
        java.io.File dir = new java.io.File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria pasta se não existir
        }
        
        String fullPath = outputDir + filename;
        
        // AJUSTE: false = sobrescrever (não append)
        try (java.io.FileWriter writer = new java.io.FileWriter(fullPath, false)) {
            writer.write("=== CÓDIGO INTERMEDIÁRIO GERADO (C3E) ===\n");
            writer.write("Data/Hora: " + java.time.LocalDateTime.now() + "\n");
            writer.write("Total de Instruções: " + instructions.size() + "\n");
            writer.write("===========================================\n\n");
            
            for (Instruction instr : instructions) {
                writer.write(instr.toString() + "\n");
            }
            
            System.out.println(" Código salvo em: " + fullPath);
        } catch (java.io.IOException e) {
            System.err.println(" Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}