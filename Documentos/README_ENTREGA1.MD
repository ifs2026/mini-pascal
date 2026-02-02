# 📘 Entrega 1: Gramática da Linguagem (BNF/EBNF)

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

| Item | Descrição |
|------|-----------|
| **Número da Entrega** | 1 |
| **Data de Entrega** | 14/01/2026 |
| **Objetivo** | Definir formalmente a Mini-Linguagem utilizando BNF/EBNF |
| **Status** | ✅ Concluído |
| **Responsáveis** | Enaldo Dantas, Agnaldo Neto, Vitorio Mota, Lucas Oliveira |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Conceito](#conceito)
- [Artefatos Entregues](#artefatos-entregues)
- [Gramática BNF/EBNF Completa](#gramática-bnfebnf-completa)
- [Comandos Suportados](#comandos-suportados)
- [Estrutura da Gramática ANTLR](#estrutura-da-gramática-antlr)
- [Exemplos de Código Válido](#exemplos-de-código-válido)
- [Exemplos de Código Inválido](#exemplos-de-código-inválido)
- [Precedência de Operadores](#precedência-de-operadores)
- [Como Gerar o Parser/Lexer](#como-gerar-o-parserlexer)
- [Validação e Testes](#validação-e-testes)
- [Checklist de Entrega](#checklist-de-entrega)

---

## 🎯 Objetivo

Definir as **"regras do jogo"** da linguagem Mini-Pascal através de uma gramática formal que especifica:

- ✅ **Estrutura de um programa válido**: Como organizar declarações e comandos
- ✅ **Declaração de variáveis e tipos**: Suporte a `integer`, `real`, `string`, `boolean`
- ✅ **Comandos de controle**: Atribuição, entrada/saída, condicionais e laços
- ✅ **Expressões**: Aritméticas, relacionais e lógicas com precedência definida
- ✅ **Regras léxicas**: Identificadores, números, strings, palavras-reservadas

---

## 💡 Conceito

A gramática é a **especificação formal** da linguagem Mini-Pascal. Ela define:

### 1. **Estrutura do Programa**
Todo programa Mini-Pascal segue a estrutura:

program ;
[var <declarações>]
begin

end.


### 2. **Tipos de Dados Suportados**
- `integer` - Números inteiros
- `real` - Números reais (ponto flutuante)
- `string` - Cadeias de caracteres
- `boolean` - Valores lógicos (`true`, `false`)

### 3. **Comandos Suportados**
- **Atribuição**: `x := expressão;`
- **Entrada**: `read(var1, var2, ...);`
- **Saída**: `print(expr1, expr2, ...);`
- **Condicional**: `if expressão then comando [else comando]`
- **Laço While**: `while expressão do comando`
- **Laço For**: `for var := expr to/downto expr do comando`

### 4. **Expressões com Precedência**
- Operadores relacionais: `=`, `<>`, `<`, `<=`, `>`, `>=`
- Operadores aditivos: `+`, `-`, `or`
- Operadores multiplicativos: `*`, `/`, `and`
- Operadores unários: `not`, `+` (unário), `-` (unário)

---

## 📦 Artefatos Entregues

### Arquivos Principais

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| `miniPascal.g4` | `src/main/antlr4/` | Gramática ANTLR completa do Mini-Pascal |
| `README_ENTREGA1.md` | Raiz do projeto | Este documento de entrega |

### Arquivos Gerados (após `mvn generate-sources`)

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| `miniPascalLexer.java` | `target/generated-sources/antlr4/` | Analisador léxico gerado pelo ANTLR |
| `miniPascalParser.java` | `target/generated-sources/antlr4/` | Analisador sintático gerado pelo ANTLR |
| `miniPascalBaseVisitor.java` | `target/generated-sources/antlr4/` | Classe base para visitor pattern |
| `miniPascalVisitor.java` | `target/generated-sources/antlr4/` | Interface do visitor pattern |
| `miniPascal.tokens` | `target/generated-sources/antlr4/` | Arquivo de tokens gerados |
| `miniPascalLexer.tokens` | `target/generated-sources/antlr4/` | Tokens do lexer |

---

## 📝 Gramática BNF/EBNF Completa

### Notação Utilizada

- `"texto"` - Terminal (palavra-reservada ou símbolo)
- `regra` - Não-terminal
- `|` - Alternativa (ou)
- `[ ... ]` - Opcional (zero ou uma vez)
- `{ ... }` - Repetição (zero ou mais vezes)
- `( ... )` - Agrupamento

---

1. Programa e Bloco
```ebnf
program =
    "program", identifier, ";", block, "." ;

block =
    [ variable-declaration-part ], compound-statement ;

variable-declaration-part =
    "var", variable-declaration, { ";", variable-declaration }, ";" ;

variable-declaration =
    identifier-list, ":", type ;

identifier-list =
    identifier, { ",", identifier } ;

type =
    "integer"
  | "real"
  | "string"
  | "boolean" ;

identifier =
    letter, { letter | digit | "_" } ;




Descrição:

Todo programa inicia com a palavra-reservada program seguida de um identificador
Declarações de variáveis são opcionais (indicado por [ ... ])
Múltiplas variáveis podem ser declaradas em uma única linha separadas por vírgula
O bloco principal (compound-statement) é obrigatório
O programa termina com ponto final (.)

Exemplo:

program MeuPrograma;
var
    x, y: integer;
    z: real;
begin
    x := 10;
end.

2. Comandos

compound-statement =
    "begin", statement-list, "end" ;

statement-list =
    statement, { ";", statement } ;

statement =
    simple-statement
  | structured-statement
  | (* vazio *) ;

simple-statement =
    assignment-statement
  | io-statement ;

assignment-statement =
    identifier, ":=", expression ;

io-statement =
    "read", "(", identifier, { ",", identifier }, ")"
  | "print", "(", expression, { ",", expression }, ")" ;

structured-statement =
    if-statement
  | while-statement
  | for-statement
  | compound-statement ;

if-statement =
    "if", expression, "then", statement, [ "else", statement ] ;

while-statement =
    "while", expression, "do", statement ;

for-statement =
    "for", identifier, ":=", expression,
    ( "to" | "downto" ), expression,
    "do", statement ;

Descrição:
Comandos Simples: Atribuição (x := 10) e I/O (read, print)
Comandos Estruturados: Condicionais (if-then-else), laços (while, for) e blocos compostos
Comandos Vazios: Permitidos (ponto e vírgula duplo ;;)

Exemplos:
Atribuição:
x := 10 + y * 2;

Entrada/Saída:
read(x, y);
print('Resultado:', x + y);

Condicional:
if x > 0 then
    y := 1
else
    y := 0;

Laço While:
while i < 10 do
    i := i + 1;

Laço For:
for i := 1 to 5 do
    print(i);

for i := 5 downto 1 do
    print(i);

3. Expressões (com Precedência)

expression =
    simple-expression, [ relational-operator, simple-expression ] ;

relational-operator =
    "="  | "<>" | "<" | "<=" | ">" | ">=" ;

simple-expression =
    term, { additive-operator, term } ;

additive-operator =
    "+" | "-" | "or" ;

term =
    factor, { multiplicative-operator, factor } ;

multiplicative-operator =
    "*" | "/" | "and" ;

factor =
    identifier
  | unsigned-number
  | string-literal
  | boolean-literal
  | "(", expression, ")"
  | "not", factor
  | "+", factor
  | "-", factor ;


Descrição:
A gramática define a precedência de operadores através da hierarquia de regras:
Expressões relacionais (menor precedência): =, <>, <, <=, >, >=
Expressões aditivas: +, -, or
Termos multiplicativos: *, /, and
Fatores (maior precedência): not, + (unário), - (unário), parênteses

Exemplos de Avaliação:
z := 10 + 5 * 2;     // Equivale a: z := 10 + (5 * 2) = 20
z := (10 + 5) * 2;   // Equivale a: z := 15 * 2 = 30
z := not x and y;    // Equivale a: z := (not x) and y


4. Léxico (Tokens Básicos)

unsigned-number =
    integer
  | real ;

integer =
    digit, { digit } ;

real =
    digit, { digit }, ".", digit, { digit } ;

string-literal =
    "'", { character - "'" | "''" }, "'" ;

boolean-literal =
    "true" | "false" ;

letter =
    "A" | "B" | ... | "Z"
  | "a" | "b" | ... | "z" ;

digit  =
    "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;


Descrição:
Números Inteiros: Sequência de dígitos (123, 0, 999)
Números Reais: Dígitos, ponto, dígitos (123.45, 0.5, 3.14159)
Strings: Delimitadas por aspas simples ('texto', 'Hello World')
Aspas simples dentro de strings são escapadas duplicando: 'it''s' → it's
Booleanos: Apenas true ou false
Identificadores: Iniciam com letra, podem conter letras, dígitos e underscore (_)
Válidos: x, contador, valor_total, Nota1
Inválidos: 1x (inicia com dígito), x-y (hífen não permitido)

🛠️ Comandos Suportados

ComandoSintaxeDescriçãoExemploAtribuiçãovar := expr;Atribui valor a uma variávelx := 10 + y * 2;Entradaread(var1, var2, ...);Lê valores do usuárioread(x, y);Saídaprint(expr1, expr2, ...);Imprime valores na telaprint('Resultado:', x);Condicionalif expr then cmd [else cmd]Executa comando condicionalmenteif x > 0 then y := 1 else y := 0;Laço Whilewhile expr do cmdRepete enquanto condição for verdadeirawhile i < 10 do i := i + 1;Laço For (crescente)for var := expr to expr do cmdLaço com contador crescentefor i := 1 to 5 do print(i);Laço For (decrescente)for var := expr downto expr do cmdLaço com contador decrescentefor i := 5 downto 1 do print(i);



🔧 Estrutura da Gramática ANTLR
Localização do Arquivosrc/main/antlr4/miniPascal.g4
Estrutura Geral do Arquivo
O arquivo miniPascal.g4 está organizado em duas seções principais:
Regras do Parser (sintáticas) - Definem a estrutura da linguagem
Regras do Lexer (léxicas) - Definem os tokens

Principais Regras do Parser

grammar miniPascal;

// ===== REGRAS DO PARSER (Sintáticas) =====

program
    : PROGRAM identifier SEMICOLON 
      variableDeclarationPart? 
      compoundStatement DOT EOF
    ;

variableDeclarationPart
    : VAR variableDeclaration (SEMICOLON variableDeclaration)* SEMICOLON?
    ;

variableDeclaration
    : identifierList COLON type
    ;

identifierList
    : identifier (COMMA identifier)*
    ;

type
    : INTEGER_TYPE
    | REAL_TYPE
    | STRING_TYPE
    | BOOLEAN_TYPE
    ;

compoundStatement
    : BEGIN statement (SEMICOLON statement)* END
    ;

statement
    : assignmentStatement
    | ifStatement
    | whileStatement
    | forStatement
    | ioStatement
    | compoundStatement
    | // comando vazio
    ;

assignmentStatement
    : identifier ASSIGN expression
    ;

ifStatement
    : IF expression THEN statement (ELSE statement)?
    ;

whileStatement
    : WHILE expression DO statement
    ;

forStatement
    : FOR identifier ASSIGN expression (TO | DOWNTO) expression DO statement
    ;

ioStatement
    : READ LPAREN identifierList RPAREN
    | PRINT LPAREN expressionList RPAREN
    ;

expressionList
    : expression (COMMA expression)*
    ;

expression
    : simpleExpression (relationalOperator simpleExpression)?
    ;

relationalOperator
    : EQUAL
    | NOT_EQUAL
    | LESS_THAN
    | LESS_EQUAL
    | GREATER_THAN
    | GREATER_EQUAL
    ;

simpleExpression
    : term (additiveOperator term)*
    ;

additiveOperator
    : PLUS
    | MINUS
    | OR
    ;

term
    : factor (multiplicativeOperator factor)*
    ;

multiplicativeOperator
    : MULTIPLY
    | DIVIDE
    | AND
    ;

factor
    : identifier
    | unsignedNumber
    | stringLiteral
    | boolLiteral
    | LPAREN expression RPAREN
    | NOT factor
    | (PLUS | MINUS) factor
    ;

identifier
    : IDENTIFIER
    ;

unsignedNumber
    : INTEGER_LITERAL
    | REAL_LITERAL
    ;

stringLiteral
    : STRING_LITERAL
    ;

boolLiteral
    : TRUE
    | FALSE
    ;

// ===== REGRAS DO LEXER (Tokens) =====

// Palavras-reservadas
PROGRAM     : 'program';
VAR         : 'var';
BEGIN       : 'begin';
END         : 'end';
IF          : 'if';
THEN        : 'then';
ELSE        : 'else';
WHILE       : 'while';
DO          : 'do';
FOR         : 'for';
TO          : 'to';
DOWNTO      : 'downto';
READ        : 'read';
PRINT       : 'print';
INTEGER_TYPE: 'integer';
REAL_TYPE   : 'real';
STRING_TYPE : 'string';
BOOLEAN_TYPE: 'boolean';
TRUE        : 'true';
FALSE       : 'false';
AND         : 'and';
OR          : 'or';
NOT         : 'not';

// Operadores
ASSIGN      : ':=';
EQUAL       : '=';
NOT_EQUAL   : '<>';
LESS_THAN   : '<';
LESS_EQUAL  : '<=';
GREATER_THAN: '>';
GREATER_EQUAL: '>=';
PLUS        : '+';
MINUS       : '-';
MULTIPLY    : '*';
DIVIDE      : '/';

// Delimitadores
LPAREN      : '(';
RPAREN      : ')';
SEMICOLON   : ';';
COLON       : ':';
COMMA       : ',';
DOT         : '.';

// Literais
IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

INTEGER_LITERAL
    : [0-9]+
    ;

REAL_LITERAL
    : [0-9]+ '.' [0-9]+
    ;

STRING_LITERAL
    : '\'' (~['\r\n])* '\''
    ;

// Comentários e espaços em branco
COMMENT
    : '{' .*? '}' -> skip
    | '(*' .*? '*)' -> skip
    | '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;


✅ Exemplos de Código Válido

Exemplo 1: Programa Completo com Todos os Recursos

program exemploValido;
var
    x, y: integer;
    z: real;
begin
    read(x, y);
    z := x + y * 2;
    if z > 10 then
        print('maior que 10', z)
    else
        print('menor ou igual a 10', z);
    for x := 1 to 5 do
        print(x);
end.

Análise:
✅ Declaração de programa: program exemploValido;
✅ Declaração de múltiplas variáveis: x, y: integer; e z: real;
✅ Entrada de dados: read(x, y);
✅ Expressão aritmética com precedência: x + y * 2 → x + (y * 2)
✅ Condicional completa: if-then-else
✅ Saída com múltiplos valores: print('maior que 10', z)
✅ Laço for crescente: for x := 1 to 5 do
✅ Finalização correta: end.

Exemplo 2: Atribuição e Saída Simples

program Exemplo1;
var
    x, y : integer;
begin
    x := 10;
    y := x + 5;
    print(x, y);
end.

Análise:
✅ Atribuições simples
✅ Expressão aritmética básica
✅ Saída de múltiplos valores
Saída Esperada:
10 15

Exemplo 3: Condicional com Entrada de Dados

program Exemplo2;
var
    nota : real;
begin
    read(nota);
    if nota >= 7.0 then
        print('Aprovado')
    else
        print('Reprovado');
end.

Análise:
✅ Variável do tipo real
✅ Entrada de dados do usuário
✅ Comparação relacional: >=
✅ Strings literais em aspas simples
✅ Estrutura if-then-else completa
Execução:

Entrada: 8.5
Saída: Aprovado

Entrada: 5.0
Saída: Reprovado

Exemplo 4: Laço While com Bloco Composto

program Exemplo3;
var
    i : integer;
begin
    i := 1;
    while i <= 5 do
    begin
        print('Valor de i:', i);
        i := i + 1;
    end;
end.

Análise:
✅ Laço while com condição
✅ Bloco composto: begin...end dentro do while
✅ Incremento de variável
✅ Saída com string e variável
Saída Esperada:

Valor de i: 1
Valor de i: 2
Valor de i: 3
Valor de i: 4
Valor de i: 5

Exemplo 5: Laços For Crescente e Decrescente

program Exemplo4;
var
    i : integer;
begin
    for i := 1 to 3 do
        print('Contando:', i);

    for i := 3 downto 1 do
        print('Decrescendo:', i);
end.

Análise:
✅ Laço for crescente com to
✅ Laço for decrescente com downto
✅ Múltiplos comandos sequenciais
Saída Esperada:

Contando: 1
Contando: 2
Contando: 3
Decrescendo: 3
Decrescendo: 2
Decrescendo: 1

❌ Exemplos de Código Inválido
Erro Completo: Múltiplos Erros Sintáticos

program exemploInvalido
var
    x, y integer     { ERRO: falta ":" entre identificadores e tipo }
    z: real

begin
    read(x y);       { ERRO: falta "," entre os parâmetros }
    z := x + ;       { ERRO: expressão incompleta, operador sem operando }
    if z > 10        { ERRO: falta "then" }
        print('maior que 10', z)
    else
        print('menor ou igual a 10' z);  { ERRO: falta "," antes de z }

    for i := 1 to 5  { ERRO: variável i não declarada e falta "do" }
        print(i)
end                  { ERRO: falta "." ao final do programa }


Erros Identificados:



































































LinhaErroDescriçãoCorreção1Falta ;Após nome do programaprogram exemploInvalido;3Falta :Entre identificadores e tipox, y: integer;4Falta ;Após declaração de variávelz: real;7Falta ,Entre parâmetros do readread(x, y);8Expressão incompletaOperador + sem operando direitoz := x + y;9Falta thenApós condição do ifif z > 10 then12Falta ,Antes do segundo argumentoprint('...', z);14Variável não declaradai não foi declaradaAdicionar i: integer;14Falta doApós expressão do forfor i := 1 to 5 do16Falta .Ao final do programaend.






















