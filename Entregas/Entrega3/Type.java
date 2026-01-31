/**
 * Enumeração que define os Tipos de Dados suportados pela Mini-Pascal.
 * Estes tipos são utilizados pela Tabela de Símbolos para realizar a 
 * verificação semântica e garantir a integridade das operações.
 */
public enum Type {
    
    // Representa números inteiros (ex: 10, -5). 
    // Mapeado na gramática como o token INTEGER.
    INTEGER,
    
    // Representa números de ponto flutuante/reais (ex: 5.5, .12). 
    // Mapeado na gramática como o token REAL.
    REAL,
    
    // Representa sequências de caracteres literais (ex: 'Olá Mundo'). 
    // Mapeado na gramática como o token STRING.
    STRING,
    
    // Representa valores lógicos (TRUE ou FALSE). 
    // Utilizado principalmente em condições de IF e WHILE.
    BOOLEAN
}