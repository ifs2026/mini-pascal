/**
 * Representa uma instrução de Código de Três Endereços (C3E).
 * Esta estrutura armazena quádruplos no formato: (resultado, argumento1, operador, argumento2).
 */
public class Instruction {
    String result; // Onde o resultado será armazenado ou o nome de um Label
    String arg1;   // Primeiro operando ou condição do IF
    String op;     // Operador (ex: +, -, *, /, GOTO, IF, LABEL)
    String arg2;   // Segundo operando (opcional)

    /**
     * Construtor da instrução.
     * @param result Variável de destino ou nome do Rótulo/Label.
     * @param arg1 Primeiro valor ou variável.
     * @param op Operação a ser realizada.
     * @param arg2 Segundo valor ou variável (pode ser null).
     */
    public Instruction(String result, String arg1, String op, String arg2) {
        this.result = result;
        this.arg1 = arg1;
        this.op = op;
        this.arg2 = arg2;
    }

    /**
     * Traduz o objeto Instruction para a representação textual do Código Intermediário.
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

        // 4. Caso seja uma atribuição simples (ex: x := 10 ou x := y)
        // Ocorre quando não há um operador aritmético associado.
        if (op == null || op.isEmpty()) {
            return result + " := " + arg1;
        }

        // 5. Caso seja uma operação binária padrão (ex: t1 := a + b)
        // Cobre operações aritméticas (+, -, *, /) e lógicas.
        return result + " := " + arg1 + " " + op + " " + (arg2 != null ? arg2 : "");
    }
}