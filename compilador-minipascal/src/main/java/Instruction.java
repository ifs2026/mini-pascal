/**
 * Representa uma instrução de Código de Três Endereços (C3E / TAC - Three-Address Code).
 * 
 * Esta estrutura armazena quádruplos no formato: (result, arg1, op, arg2).
 * 
 * ESTRUTURA:
 * - result: Variável de destino ou nome de Label
 * - arg1: Primeiro operando ou condição
 * - op: Operador (+, -, *, /, GOTO, IF, LABEL)
 * - arg2: Segundo operando (pode ser null)
 * 
 * TIPOS DE INSTRUÇÕES SUPORTADAS:
 * 1. LABEL: Marca pontos de salto (ex: L0:)
 * 2. GOTO: Salto incondicional (ex: goto L1)
 * 3. IF: Salto condicional (ex: if x > 5 goto L2)
 * 4. Atribuição simples: (ex: x := 10)
 * 5. Operação binária: (ex: t1 := a + b)
 * 6. I/O: READ e PRINT
 * 
 * AJUSTES IMPLEMENTADOS:
 * - Comentários expandidos com exemplos
 * - Suporte a operadores lógicos (AND, OR, NOT)
 */
public class Instruction {
    String result; // Onde o resultado será armazenado ou o nome de um Label
    String arg1;   // Primeiro operando ou condição do IF
    String op;     // Operador (ex: +, -, *, /, GOTO, IF, LABEL, AND, OR, NOT)
    String arg2;   // Segundo operando (opcional, pode ser null)

    /**
     * Construtor da instrução.
     * 
     * @param result Variável de destino ou nome do Rótulo/Label.
     * @param arg1 Primeiro valor ou variável.
     * @param op Operação a ser realizada.
     * @param arg2 Segundo valor ou variável (pode ser null).
     * 
     * Exemplos de construção:
     * 1. new Instruction("L0", null, "LABEL", null) → L0:
     * 2. new Instruction("L1", null, "GOTO", null) → goto L1
     * 3. new Instruction("t0", "x", "+", "5") → t0 := x + 5
     * 4. new Instruction("x", "10", null, null) → x := 10
     * 5. new Instruction("L2", "t0", "IF_FALSE", null) → if t0 FALSE goto L2
     */
    public Instruction(String result, String arg1, String op, String arg2) {
        this.result = result;
        this.arg1 = arg1;
        this.op = op;
        this.arg2 = arg2;
    }

    /**
     * Traduz o objeto Instruction para a representação textual do Código Intermediário.
     * 
     * CASOS TRATADOS:
     * 1. LABEL: Marca ponto de salto
     * 2. GOTO: Salto incondicional
     * 3. IF: Salto condicional
     * 4. Atribuição simples: Sem operador
     * 5. Operação binária: Com operador aritmético/lógico
     * 6. I/O: READ e PRINT
     * 
     * @return String formatada representando a instrução TAC
     */
    @Override
    public String toString() {
        
        // 1. Caso seja um Rótulo de controle (ex: L0:)
        // Usado para marcar pontos de salto no código.
        if ("LABEL".equals(op)) {
            return result + ":";
        }
        
        // 2. Caso seja um Salto Incondicional (ex: goto L1)
        // Usado ao final de blocos 'then' ou laços de repetição.
        if ("GOTO".equals(op)) {
            return "goto " + result;
        }

        // 3. Caso seja um Salto Condicional (ex: if t0 FALSE goto L1)
        // Verifica se o operador começa com 'IF' para tratar desvios baseados em condições.
        if (op != null && op.startsWith("IF")) {
            // Extrai o tipo de condição (ex: extrai 'FALSE' de 'IF_FALSE')
            String operator = (op.length() > 2) ? op.substring(3) : "";
            return "if " + arg1 + " " + operator + " " + (arg2 != null ? arg2 : "") + " goto " + result;
        }

        // 4. Caso seja I/O (READ ou PRINT)
        // Exemplo: READ x ou PRINT 'mensagem'
        if ("READ".equals(result)) {
            return "READ " + arg1;
        }
        if ("PRINT".equals(result)) {
            return "PRINT " + arg1;
        }

        // 5. Caso seja uma atribuição simples (ex: x := 10 ou x := y)
        // Ocorre quando não há um operador aritmético associado.
        if (op == null || op.isEmpty()) {
            return result + " := " + arg1;
        }

        // 6. Caso seja uma operação binária padrão (ex: t1 := a + b)
        // Cobre operações aritméticas (+, -, *, /) e lógicas (AND, OR).
        return result + " := " + arg1 + " " + op + " " + (arg2 != null ? arg2 : "");
    }

    /**
     * Métodos auxiliares para identificar tipo de instrução (ADIÇÃO).
     * Úteis para análises e otimizações futuras.
     */
    
    /**
     * Verifica se a instrução é um label.
     * @return true se for LABEL, false caso contrário.
     */
    public boolean isLabel() {
        return "LABEL".equals(op);
    }

    /**
     * Verifica se a instrução é um goto.
     * @return true se for GOTO, false caso contrário.
     */
    public boolean isGoto() {
        return "GOTO".equals(op);
    }

    /**
     * Verifica se a instrução é um salto condicional.
     * @return true se for IF_*, false caso contrário.
     */
    public boolean isConditional() {
        return op != null && op.startsWith("IF");
    }

    /**
     * Verifica se a instrução é uma atribuição.
     * @return true se for atribuição, false caso contrário.
     */
    public boolean isAssignment() {
        return !isLabel() && !isGoto() && !isConditional();
    }
}