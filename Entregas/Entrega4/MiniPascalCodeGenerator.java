import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe percorre a árvore gerada pelo ANTLR e traduz os comandos para
 * Código de Três Endereços (C3E). Cada método 'visit' lida com uma regra da gramática.
 */
public class MiniPascalCodeGenerator extends miniPascalBaseVisitor<String> {

    // Lista que armazena todas as instruções geradas para serem exibidas ao final
    private final List<Instruction> instructions = new ArrayList<>();
    
    // Contadores para garantir que cada temporário (t0, t1...) e rótulo (L0, L1...) seja único
    private int tempCount = 0;
    private int labelCount = 0;

    // Gera um novo nome de variável temporária (ex: t0, t1, t2...)
    private String newTemp() {
        return "t" + (tempCount++);
    }

    // Gera um novo nome de rótulo para desvios/saltos (ex: L0, L1, L2...)
    private String newLabel() {
        return "L" + (labelCount++);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    /**
     * Percorre a lista de instruções acumuladas e as imprime formatadas.
     */
    public void printInstructions() {
        System.out.println("\n=== CÓDIGO INTERMEDIÁRIO GERADO (C3E) ===");
        for (Instruction instr : instructions) {
            System.out.println(instr);
        }
    }

    // === Atribuição: x := expressão ===
    @Override
    public String visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String variable = ctx.identifier().getText(); // Identifica o destino (lado esquerdo)
        String value = visit(ctx.expression());       // Resolve a expressão (lado direito)
        
        // Gera: variable := value
        instructions.add(new Instruction(variable, value, null, null));
        return variable;
    }

    // === Expressões Relacionais (ex: x > 15) ===
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

    // === Expressões Aritméticas (Soma e Subtração) ===
    @Override
    public String visitSimpleExpression(miniPascalParser.SimpleExpressionContext ctx) {
        String lastResult = visit(ctx.term(0)); // Pega o primeiro termo
        
        // Percorre os operadores (+ ou -) e termos subsequentes
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

    // === Operações de Alta Precedência (Multiplicação e Divisão) ===
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

    // === Fatores (Base da expressão: números ou variáveis) ===
    @Override
    public String visitFactor(miniPascalParser.FactorContext ctx) {
        if (ctx.identifier() != null) return ctx.identifier().getText();
        if (ctx.unsignedNumber() != null) return ctx.unsignedNumber().getText();
        if (ctx.expression() != null) return visit(ctx.expression()); // Trata ( expressao )
        return ctx.getText();
    }

    // === Controle de Fluxo: IF THEN ELSE ===
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

    // === Laço de Repetição: WHILE DO ===
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

    // === Entrada e Saída (READ e WRITE/PRINT) ===
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
}