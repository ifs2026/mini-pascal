/**
 * Enumeração que define os Tipos de Dados suportados pela Mini-Pascal.
 * 
 * Estes tipos são utilizados pela Tabela de Símbolos para realizar a 
 * verificação semântica e garantir a integridade das operações.
 * 
 */
public enum Type {
    
    /**
     * INTEGER: Representa números inteiros (ex: 10, -5, 0).
     * 
     * Mapeado na gramática como o token INTEGER.
     * Usado em operações aritméticas e comparações.
     * 
     * Exemplos de uso:
     * var x: integer;
     * x := 10;
     * x := x + 5;
     */
    INTEGER,
    
    /**
     * REAL: Representa números de ponto flutuante/reais (ex: 5.5, .12, 3.14).
     * 
     * Mapeado na gramática como o token REAL.
     * Usado em cálculos que requerem precisão decimal.
     * 
     * Exemplos de uso:
     * var altura: real;
     * altura := 1.75;
     * altura := altura * 2.0;
     */
    REAL,
    
    /**
     * STRING: Representa sequências de caracteres literais (ex: 'Olá Mundo', 'João').
     * 
     * Mapeado na gramática como o token STRING.
     * Usado em operações de texto e saída formatada.
     * Suporta escape de apóstrofo com '' (ex: 'O nome é ''João''').
     * 
     * Exemplos de uso:
     * var nome: string;
     * nome := 'Maria';
     * print('Olá, ', nome);
     */
    STRING,
    
    /**
     * BOOLEAN: Representa valores lógicos (TRUE ou FALSE).
     * 
     * Utilizado principalmente em condições de IF e WHILE.
     * Resultado de operações relacionais e lógicas.
     * 
     * Exemplos de uso:
     * var ativo: boolean;
     * ativo := true;
     * if ativo then print('Ativo');
     * ativo := (x > 5) and (y < 10);
     */
    BOOLEAN;
    
    // EXTENSÕES FUTURAS (comentadas para referência):
    
    /**
     * Verifica se este tipo é compatível com outro tipo.
     * Útil para validação de atribuições e operações.
     * 
     * Regras de compatibilidade:
     * - Tipos iguais são sempre compatíveis
     * - INTEGER pode ser promovido para REAL (promoção implícita)
     * - STRING e BOOLEAN não são compatíveis com outros tipos
     * 
     * @param other Tipo a ser comparado
     * @return true se compatível, false caso contrário
     */
    /*
    public boolean isCompatibleWith(Type other) {
        if (this == other) return true;
        if (this == REAL && other == INTEGER) return true; // Promoção implícita
        return false;
    }
    */
    
    /**
     * Retorna o valor padrão para este tipo.
     * Útil para inicialização de variáveis.
     * 
     * @return String representando o valor padrão
     */
    /*
    public String getDefaultValue() {
        return switch(this) {
            case INTEGER -> "0";
            case REAL -> "0.0";
            case STRING -> "''";
            case BOOLEAN -> "false";
        };
    }
    */
}