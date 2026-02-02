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












