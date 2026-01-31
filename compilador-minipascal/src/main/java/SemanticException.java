/**
 * Classe de Exceção Personalizada para Erros Semânticos.
 * * No ciclo de compilação, uma SemanticException é lançada quando o código 
 * está sintaticamente correto (passou pelo Parser), mas viola regras da linguagem.
 * * Exemplos de quando esta exceção é usada:
 * - Uso de uma variável que não foi declarada (Variável não encontrada).
 * - Declaração de duas variáveis com o mesmo nome no mesmo escopo.
 * - (Opcional) Incompatibilidade de tipos em operações.
 */
public class SemanticException extends RuntimeException {

    /**
     * Construtor que recebe a mensagem de erro específica.
     * @param msg Detalhes do erro (ex: "Erro: Variável 'x' não declarada").
     */
    public SemanticException(String msg) {
        // Repassa a mensagem para a classe pai (RuntimeException),
        // permitindo que o erro interrompa a execução da análise semântica.
        super(msg);
    }
}