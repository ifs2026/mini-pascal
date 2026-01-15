grammar miniPascal;                         // Define o nome da gramática ANTLR4 (gera miniPascalLexer/miniPascalParser).

options { caseInsensitive = true; }        // Liga modo case-insensitive: letras maiúsculas/minúsculas são tratadas como equivalentes.

// ============ PROGRAMA PRINCIPAL ============

program                                     // Regra inicial que representa um programa completo.
    : PROGRAM identifier SEMI block DOT EOF // Sintaxe: PROGRAM <identificador> ; <bloco> . e fim de arquivo.
    ;

identifier                                  // Regra sintática para identificadores.
    : IDENT                                 // Um identificador é um token IDENT reconhecido pelo léxico.
    ;

// ============ BLOCO (DECLARAÇÕES + COMANDOS) ============

block                                       // Bloco principal do programa.
    : (variableDeclarationPart)? compoundStatement // Opcionalmente declarações de variáveis seguidas do bloco de comandos.
    ;

variableDeclarationPart                     // Seção de declaração de variáveis.
    : VAR variableDeclaration (SEMI variableDeclaration)* (SEMI)? // Palavra VAR seguida de uma ou mais declarações separadas por ';', último ';' opcional.
    ;

variableDeclaration                         // Declaração de um grupo de variáveis de mesmo tipo.
    : identifierList COLON type_            // Lista de identificadores, dois pontos e um tipo.
    ;

identifierList                              // Lista de variáveis.
    : identifier (COMMA identifier)*        // Um identificador seguido de zero ou mais ", identificador".
    ;

type_                                       // Conjunto de tipos primitivos suportados.
    : INTEGER                               // Tipo inteiro.
    | REAL                                  // Tipo real (ponto flutuante).
    | STRING                                // Tipo string.
    | BOOLEAN                               // Tipo booleano.
    ;

// ============ COMANDOS E CONTROLE DE FLUXO ============

compoundStatement                           // Bloco de comandos delimitado por BEGIN/END.
    : BEGIN statements? (SEMI)? END         // BEGIN, lista opcional de comandos, ';' opcional antes de END.
    ;

statements                                  // Lista de comandos dentro de um bloco.
    : statement (SEMI statement)*           // Um comando seguido de zero ou mais "; comando".
    ;

statement                                   // Um comando genérico.
    : simpleStatement                       // Comando simples.
    | structuredStatement                   // Comando estruturado (controle de fluxo ou novo bloco).
    ;

simpleStatement                             // Comandos que não abrem novos blocos.
    : assignmentStatement                   // Atribuição.
    | ioStatement                           // Entrada/saída.
    ;

assignmentStatement                         // Comando de atribuição.
    : identifier ASSIGN expression          // "<identificador> := <expressão>".
    ;

ioStatement                                 // Comandos de E/S definidos na linguagem.
    : READ LPAREN identifier (COMMA identifier)* RPAREN        // "read(x, y, ...)" lê variáveis.
    | PRINT LPAREN expression (COMMA expression)* RPAREN       // "print(expr1, expr2, ...)" escreve expressões.
    ;

structuredStatement                         // Comandos com estrutura de controle.
    : ifStatement                           // Estrutura condicional if.
    | whileStatement                        // Laço while.
    | forStatement                          // Laço for.
    | compoundStatement                     // Bloco BEGIN...END aninhado.
    ;

ifStatement                                 // Comando condicional.
    : IF expression THEN statement (ELSE statement)? // "if <expr> then <stmt> [else <stmt>]".
    ;

whileStatement                              // Comando de repetição while.
    : WHILE expression DO statement         // "while <expr> do <stmt>".
    ;

forStatement                                // Comando de repetição for.
    : FOR identifier ASSIGN expression (TO | DOWNTO) expression DO statement // "for i := e1 to/downto e2 do <stmt>".
    ;

// ============ EXPRESSÕES COM PRECEDÊNCIA ============

expression                                  // Expressão relacional possivelmente encadeada.
    : simpleExpression (relationalOperator simpleExpression)* // Uma expressão simples seguida de comparações.
    ;

relationalOperator                          // Operadores relacionais.
    : EQUAL                                 // '=' igual.
    | NOT_EQUAL                             // '<>' diferente.
    | LT                                    // '<' menor.
    | LE                                    // '<=' menor ou igual.
    | GE                                    // '>=' maior ou igual.
    | GT                                    // '>' maior.
    ;

simpleExpression                            // Expressão com +, -, or.
    : term (additiveOperator term)*         // Termo seguido de zero ou mais (+|-|or termo).
    ;

additiveOperator                            // Operadores aditivos/lógico de baixo nível.
    : PLUS                                  // '+' soma.
    | MINUS                                 // '-' subtração.
    | OR                                    // 'or' lógico.
    ;

term                                        // Nível de multiplicação/divisão/and.
    : factor (multiplicativeOperator factor)* // Fator seguido de zero ou mais (*|/|and fator).
    ;

multiplicativeOperator                      // Operadores multiplicativos e AND lógico.
    : STAR                                  // '*' multiplicação.
    | SLASH                                 // '/' divisão.
    | AND                                   // 'and' lógico.
    ;

factor                                      // Unidade básica de expressão.
    : identifier                            // Variável.
    | unsignedNumber                        // Número sem sinal.
    | stringLiteral                         // String literal.
    | boolLiteral                           // Literal booleano.
    | LPAREN expression RPAREN              // Expressão entre parênteses.
    | NOT factor                            // Negação lógica unária.
    | PLUS factor                           // Sinal positivo unário.
    | MINUS factor                          // Sinal negativo unário.
    ;

unsignedNumber                              // Números sem sinal.
    : NUM_INT                               // Inteiro.
    | NUM_REAL                              // Real.
    ;

stringLiteral                               // Wrapper sintático para o token de string.
    : STRING_LITERAL                        // Usa o token STRING_LITERAL.
    ;

boolLiteral                                 // Wrapper para literais booleanas.
    : TRUE                                  // Literal true.
    | FALSE                                 // Literal false.
    ;

// ============ LÉXICO (TOKENS) ============

// Palavras-chave                           // Seção de tokens para palavras reservadas.
PROGRAM : 'PROGRAM';                        // Token da palavra-chave PROGRAM.
VAR     : 'VAR';                            // Token da palavra-chave VAR.
INTEGER : 'INTEGER';                        // Token da palavra-chave INTEGER.
REAL    : 'REAL';                           // Token da palavra-chave REAL.
STRING  : 'STRING';                         // Token da palavra-chave STRING.
BOOLEAN : 'BOOLEAN';                        // Token da palavra-chave BOOLEAN.
READ    : 'READ';                           // Token da palavra-chave READ.
PRINT   : 'PRINT';                          // Token da palavra-chave PRINT.
BEGIN   : 'BEGIN';                          // Token da palavra-chave BEGIN.
END     : 'END';                            // Token da palavra-chave END.
IF      : 'IF';                             // Token da palavra-chave IF.
THEN    : 'THEN';                           // Token da palavra-chave THEN.
ELSE    : 'ELSE';                           // Token da palavra-chave ELSE.
WHILE   : 'WHILE';                          // Token da palavra-chave WHILE.
DO      : 'DO';                             // Token da palavra-chave DO.
FOR     : 'FOR';                            // Token da palavra-chave FOR.
TO      : 'TO';                             // Token da palavra-chave TO.
DOWNTO  : 'DOWNTO';                         // Token da palavra-chave DOWNTO.
TRUE    : 'TRUE';                           // Token do literal TRUE.
FALSE   : 'FALSE';                          // Token do literal FALSE.
NOT     : 'NOT';                            // Token da palavra-chave NOT.
AND     : 'AND';                            // Token da palavra-chave AND.
OR      : 'OR';                             // Token da palavra-chave OR.

// Operadores                               // Seção de tokens de operadores.
PLUS    : '+';                              // Operador de soma.
MINUS   : '-';                              // Operador de subtração.
STAR    : '*';                              // Operador de multiplicação.
SLASH   : '/';                              // Operador de divisão.
ASSIGN  : ':=';                             // Operador de atribuição.


// Símbolos de delimitação                  // Seção de tokens de pontuação/delimitadores.
COMMA   : ',';                              // Vírgula.
SEMI    : ';';                              // Ponto e vírgula.
COLON   : ':';                              // Dois-pontos.
EQUAL   : '=';                              // Igual.
NOT_EQUAL : '<>';                           // Diferente.
LT      : '<';                              // Menor.
LE      : '<=';                             // Menor ou igual.
GE      : '>=';                             // Maior ou igual.
GT      : '>';                              // Maior.
LPAREN  : '(';                              // Parêntese esquerdo.
RPAREN  : ')';                              // Parêntese direito.
DOT     : '.';                              // Ponto final do programa.

// Identificadores e números                // Seção de tokens para nomes e valores numéricos.
IDENT                                      // Token de identificador.
    : [A-Z] [A-Z0-9_]*                     // Primeira letra, seguida de letras/dígitos/underscore (caseInsensitive cuida de minúsculas).
    // = [A-Za-z] [A-Za-z0-9_]*            // Forma equivalente se não houvesse caseInsensitive.
    // porque o options { caseInsensitive = true; } // Explicação da escolha de faixas de caracteres.
    ;

NUM_INT                                    // Token de inteiros.
    : [0-9]+                               // Um ou mais dígitos.
    ;

NUM_REAL                                   // Token de números reais.
    : [0-9]+ '.' [0-9]* | '.' [0-9]+       // Formatos "123.45", "123." ou ".45".
    ;

STRING_LITERAL                             // Token de strings literais.
    : '\'' ('\'\'' | ~'\'')* '\''          // Texto entre apóstrofos; '' representa apóstrofo dentro da string.
    ;

// Ignorar espaços em branco e comentários   // Seção de tokens descartados.

WS                                         // Espaços em branco.
    : [ \t\r\n]+ -> skip                   // Espaço, tab e quebras de linha são ignorados.
    ;

COMMENT_1                                  // Comentário no estilo "(* ... *)".
    : '(*' .*? '*)' -> skip                // Qualquer conteúdo entre "(*" e "*)" é descartado (quantificador não guloso).
    ;

COMMENT_2                                  // Comentário no estilo "{ ... }".
    : '{' .*? '}' -> skip                  // Qualquer conteúdo entre "{" e "}" é descartado (quantificador não guloso).
    ;
