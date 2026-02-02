/**
 * Classe de Exceção Personalizada para Erros Semânticos.
 * 
 * No ciclo de compilação, uma SemanticException é lançada quando o código 
 * está sintaticamente correto (passou pelo Parser), mas viola regras da linguagem.
 * 
 * EXEMPLOS DE QUANDO ESTA EXCEÇÃO É USADA:
 * 1. Uso de uma variável que não foi declarada (Variável não encontrada).
 * 2. Declaração de duas variáveis com o mesmo nome no mesmo escopo.
 * 3. (Opcional) Incompatibilidade de tipos em operações.
 * 4. (Opcional) Uso de variável fora do escopo onde foi declarada.
 * 
 * AJUSTES IMPLEMENTADOS:
 * - Comentários expandidos com exemplos de uso
 * - Estrutura preparada para extensões futuras (linha, coluna)
 */
public class SemanticException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro específica.
     * 
     * @param msg Detalhes do erro (ex: "Variavel 'x' nao declarada").
     * 
     * Exemplos de mensagens:
     * - "Variavel 'x' nao declarada"
     * - "Variavel 'x' ja declarada neste escopo"
     * - "Tipos incompativeis: INTEGER vs STRING" (futuro)
     */
    public SemanticException(String msg) {
        // Repassa a mensagem para a classe pai (RuntimeException),
        // permitindo que o erro interrompa a execução da análise semântica.
        super(msg);
    }

    /**
     * Construtor alternativo com informações de localização (FUTURO).
     * Permite indicar linha e coluna do erro no código fonte.
     * 
     * @param msg Mensagem de erro.
     * @param line Linha do código onde ocorreu o erro.
     * @param column Coluna do código onde ocorreu o erro.
     * 
     * Exemplo de uso futuro:
     * throw new SemanticException("Variavel 'x' nao declarada", 5, 10);
     */
    /*
    public SemanticException(String msg, int line, int column) {
        super("Linha " + line + ":" + column + " - " + msg);
    }
    */

    /**
     * FLUXO DE USO:
     * 
     * 1. SymbolTable.lookup("x") não encontra variável
     *    → throw new SemanticException("Variavel 'x' nao declarada");
     * 
     * 2. MiniPascalSemanticVisitor.visit() propaga exceção
     * 
     * 3. MiniPascalParserTest captura com assertThrows:
     *    assertThrows(SemanticException.class, 
     *        () -> MiniPascalSemanticVisitor.checkSemantics(codigo));
     * 
     * 4. Mensagem é validada:
     *    assertEquals("Variavel 'x' nao declarada", ex.getMessage());
     */
}