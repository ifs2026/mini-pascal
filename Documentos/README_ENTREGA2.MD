# 📗 Entrega 2: Analisador Léxico (Scanner)

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

| Item | Descrição |
|------|-----------|
| **Data de Entrega** | 21/01/2026 |
| **Objetivo** | Implementar o módulo de tokenização do código fonte |
| **Status** | ✅ Concluído |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Conceito](#conceito)
- [Artefatos Entregues](#artefatos-entregues)
- [O que é um Token](#o-que-é-um-token)
- [Tokens Reconhecidos](#tokens-reconhecidos)
- [Implementação](#implementação)
- [Testes Unitários](#testes-unitários)
- [Exemplos de Tokenização](#exemplos-de-tokenização)
- [Tratamento de Erros Léxicos](#tratamento-de-erros-léxicos)
- [Como Executar](#como-executar)
- [Validação](#validação)
- [Histórico de Versões](#histórico-de-versões)

---

## 🎯 Objetivo

Implementar o **Analisador Léxico (Scanner)** capaz de:

1. **Ler o código fonte** caractere por caractere
2. **Agrupar caracteres** em unidades léxicas (tokens)
3. **Reconhecer** palavras-reservadas, identificadores, operadores, literais
4. **Ignorar** espaços em branco, quebras de linha e comentários
5. **Detectar erros léxicos** (caracteres inválidos, strings não fechadas, etc.)

---

## 💡 Conceito

> **"O computador não lê palavras, ele lê caracteres."**

O Analisador Léxico é a **primeira etapa** da compilação. Ele transforma uma sequência de caracteres em uma sequência de **tokens** (unidades léxicas significativas).

### Exemplo de Transformação

**Entrada (caracteres):**

x := 10 + y;


**Saída (tokens):**

IDENTIFIER(x)
ASSIGN(:=)
INTEGER_LITERAL(10)
PLUS(+)
IDENTIFIER(y)
SEMICOLON(;)


---

## 📦 Artefatos Entregues

### Arquivos Principais

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| `miniPascalLexer.java` | `target/generated-sources/antlr4/` | Lexer gerado pelo ANTLR |
| `MiniPascalLexerTest.java` | `src/test/java/` | Testes unitários do Lexer |
| `miniPascal.g4` | `src/main/antlr4/` | Gramática ANTLR (regras léxicas) |
| `README_ENTREGA2.md` | Raiz do projeto | Este documento |

### Relatórios Gerados

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| Tokens gerados | `test-reports/lexer-tokens/` | Arquivos `.txt` com tokens |
| Relatórios JUnit | `test-reports/surefire-reports/` | Resultados dos testes |

---

## 🔤 O que é um Token?

Um **token** é a menor unidade léxica significativa de um programa. Cada token possui:

1. **Tipo** (categoria do token)
2. **Lexema** (texto original do código)
3. **Posição** (linha e coluna no código fonte)

### Exemplo

Para o código `x := 10;`:

| Token | Tipo | Lexema | Posição |
|-------|------|--------|---------|
| 1 | IDENTIFIER | `x` | Linha 1, Coluna 1 |
| 2 | ASSIGN | `:=` | Linha 1, Coluna 3 |
| 3 | INTEGER_LITERAL | `10` | Linha 1, Coluna 6 |
| 4 | SEMICOLON | `;` | Linha 1, Coluna 8 |

---

## 🏷️ Tokens Reconhecidos

### 1. Palavras-Reservadas

| Token | Lexema | Descrição |
|-------|--------|-----------|
| `PROGRAM` | `program` | Início do programa |
| `VAR` | `var` | Declaração de variáveis |
| `BEGIN` | `begin` | Início de bloco |
| `END` | `end` | Fim de bloco |
| `IF` | `if` | Condicional |
| `THEN` | `then` | Parte verdadeira do if |
| `ELSE` | `else` | Parte falsa do if |
| `WHILE` | `while` | Laço while |
| `DO` | `do` | Corpo do laço |
| `FOR` | `for` | Laço for |
| `TO` | `to` | Direção crescente do for |
| `DOWNTO` | `downto` | Direção decrescente do for |
| `READ` | `read` | Entrada de dados |
| `PRINT` | `print` | Saída de dados |
| `INTEGER_TYPE` | `integer` | Tipo inteiro |
| `REAL_TYPE` | `real` | Tipo real |
| `STRING_TYPE` | `string` | Tipo string |
| `BOOLEAN_TYPE` | `boolean` | Tipo booleano |
| `TRUE` | `true` | Valor booleano verdadeiro |
| `FALSE` | `false` | Valor booleano falso |
| `AND` | `and` | Operador lógico E |
| `OR` | `or` | Operador lógico OU |
| `NOT` | `not` | Operador lógico NÃO |

### 2. Operadores

| Token | Lexema | Descrição |
|-------|--------|-----------|
| `ASSIGN` | `:=` | Atribuição |
| `EQUAL` | `=` | Igualdade |
| `NOT_EQUAL` | `<>` | Diferente |
| `LESS_THAN` | `<` | Menor que |
| `LESS_EQUAL` | `<=` | Menor ou igual |
| `GREATER_THAN` | `>` | Maior que |
| `GREATER_EQUAL` | `>=` | Maior ou igual |
| `PLUS` | `+` | Adição |
| `MINUS` | `-` | Subtração |
| `MULTIPLY` | `*` | Multiplicação |
| `DIVIDE` | `/` | Divisão |

### 3. Delimitadores

| Token | Lexema | Descrição |
|-------|--------|-----------|
| `LPAREN` | `(` | Parêntese esquerdo |
| `RPAREN` | `)` | Parêntese direito |
| `SEMICOLON` | `;` | Ponto e vírgula |
| `COLON` | `:` | Dois pontos |
| `COMMA` | `,` | Vírgula |
| `DOT` | `.` | Ponto final |

### 4. Literais

| Token | Padrão | Exemplo |
|-------|--------|---------|
| `IDENTIFIER` | `[a-zA-Z_][a-zA-Z0-9_]*` | `x`, `soma`, `_temp` |
| `INTEGER_LITERAL` | `[0-9]+` | `123`, `0`, `9999` |
| `REAL_LITERAL` | `[0-9]+\.[0-9]+` | `3.14`, `0.5`, `100.0` |
| `STRING_LITERAL` | `'[^']*'` | `'texto'`, `'Hello'` |

### 5. Ignorados (Whitespace e Comentários)

| Padrão | Descrição |
|--------|-----------|
| `[ \t\r\n]+` | Espaços, tabs, quebras de linha |
| `{...}` | Comentário de bloco (estilo Pascal) |
| `(*...*)` | Comentário de bloco alternativo |
| `//...` | Comentário de linha (estilo C++) |

---

## 🔧 Implementação

### Regras Léxicas no ANTLR (miniPascal.g4)
```antlr
// ===== PALAVRAS-RESERVADAS =====
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

// ===== OPERADORES =====
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

// ===== DELIMITADORES =====
LPAREN      : '(';
RPAREN      : ')';
SEMICOLON   : ';';
COLON       : ':';
COMMA       : ',';
DOT         : '.';

// ===== LITERAIS =====
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

// ===== IGNORADOS =====
COMMENT
    : '{' .*? '}' -> skip
    | '(*' .*? '*)' -> skip
    | '//' ~[\r\n]* -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;


🧪 Testes Unitários
Localização
src/test/java/MiniPascalLexerTest.java

Estrutura dos Testes
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MiniPascalLexerTest {

    /**
     * Método auxiliar para tokenizar código e salvar em arquivo
     */
    private List<Token> tokenize(String input, String outputFileName) throws IOException {
        CharStream charStream = CharStreams.fromString(input);
        miniPascalLexer lexer = new miniPascalLexer(charStream);
        List<Token> tokens = new ArrayList<>();
        
        Token token;
        while ((token = lexer.nextToken()).getType() != Token.EOF) {
            tokens.add(token);
        }
        
        // Salvar tokens em arquivo
        saveTokensToFile(tokens, outputFileName);
        
        return tokens;
    }

    /**
     * Salva tokens em arquivo de texto
     */
    private void saveTokensToFile(List<Token> tokens, String fileName) throws IOException {
        String dir = "test-reports/lexer-tokens/";
        new java.io.File(dir).mkdirs();
        
        try (FileWriter writer = new FileWriter(dir + fileName)) {
            writer.write("=== TOKENS GERADOS ===\n\n");
            for (int i = 0; i < tokens.size(); i++) {
                Token t = tokens.get(i);
                writer.write(String.format("%d. %s: '%s' (linha %d, coluna %d)\n",
                    i + 1,
                    miniPascalLexer.VOCABULARY.getSymbolicName(t.getType()),
                    t.getText(),
                    t.getLine(),
                    t.getCharPositionInLine()
                ));
            }
        }
    }

    @Test
    public void testTokensSimples() throws IOException {
        String input = "x := 10;";
        List<Token> tokens = tokenize(input, "teste1_tokens_simples.txt");
        
        assertEquals(4, tokens.size());
        assertEquals(miniPascalLexer.IDENTIFIER, tokens.get(0).getType());
        assertEquals(miniPascalLexer.ASSIGN, tokens.get(1).getType());
        assertEquals(miniPascalLexer.INTEGER_LITERAL, tokens.get(2).getType());
        assertEquals(miniPascalLexer.SEMICOLON, tokens.get(3).getType());
    }

    @Test
    public void testPalavrasReservadas() throws IOException {
        String input = "program begin end if then else while do for";
        List<Token> tokens = tokenize(input, "teste2_palavras_reservadas.txt");
        
        assertEquals(9, tokens.size());
        assertEquals(miniPascalLexer.PROGRAM, tokens.get(0).getType());
        assertEquals(miniPascalLexer.BEGIN, tokens.get(1).getType());
        assertEquals(miniPascalLexer.END, tokens.get(2).getType());
    }

    @Test
    public void testOperadores() throws IOException {
        String input = ":= = <> < <= > >= + - * /";
        List<Token> tokens = tokenize(input, "teste3_operadores.txt");
        
        assertEquals(11, tokens.size());
        assertEquals(miniPascalLexer.ASSIGN, tokens.get(0).getType());
        assertEquals(miniPascalLexer.EQUAL, tokens.get(1).getType());
        assertEquals(miniPascalLexer.NOT_EQUAL, tokens.get(2).getType());
    }

    @Test
    public void testLiterais() throws IOException {
        String input = "123 3.14 'texto' true false";
        List<Token> tokens = tokenize(input, "teste4_literais.txt");
        
        assertEquals(5, tokens.size());
        assertEquals(miniPascalLexer.INTEGER_LITERAL, tokens.get(0).getType());
        assertEquals(miniPascalLexer.REAL_LITERAL, tokens.get(1).getType());
        assertEquals(miniPascalLexer.STRING_LITERAL, tokens.get(2).getType());
        assertEquals(miniPascalLexer.TRUE, tokens.get(3).getType());
        assertEquals(miniPascalLexer.FALSE, tokens.get(4).getType());
    }

    @Test
    public void testComentariosIgnorados() throws IOException {
        String input = "x := 10; { comentário } y := 20; // outro comentário";
        List<Token> tokens = tokenize(input, "teste5_comentarios.txt");
        
        // Comentários devem ser ignorados
        assertEquals(8, tokens.size());
        assertEquals("x", tokens.get(0).getText());
        assertEquals("y", tokens.get(4).getText());
    }

    @Test
    public void testProgramaCompleto() throws IOException {
        String input = """
            program Teste;
            var x, y: integer;
            begin
                x := 10;
                y := x + 5;
                print(x, y);
            end.
        """;
        
        List<Token> tokens = tokenize(input, "teste6_programa_completo.txt");
        
        assertTrue(tokens.size() > 0);
        assertEquals(miniPascalLexer.PROGRAM, tokens.get(0).getType());
        assertEquals(miniPascalLexer.DOT, tokens.get(tokens.size() - 1).getType());
    }
}


📊 Exemplos de Tokenização
Exemplo 1: Atribuição Simples
Código:x := 10;

Tokens Gerados:
1. IDENTIFIER: 'x' (linha 1, coluna 0)
2. ASSIGN: ':=' (linha 1, coluna 2)
3. INTEGER_LITERAL: '10' (linha 1, coluna 5)
4. SEMICOLON: ';' (linha 1, coluna 7)

Exemplo 2: Expressão Aritmética
Código:
z := x + y * 2;

Tokens Gerados:
1. IDENTIFIER: 'z' (linha 1, coluna 0)
2. ASSIGN: ':=' (linha 1, coluna 2)
3. IDENTIFIER: 'x' (linha 1, coluna 5)
4. PLUS: '+' (linha 1, coluna 7)
5. IDENTIFIER: 'y' (linha 1, coluna 9)
6. MULTIPLY: '*' (linha 1, coluna 11)
7. INTEGER_LITERAL: '2' (linha 1, coluna 13)
8. SEMICOLON: ';' (linha 1, coluna 14)

Exemplo 3: Condicional
Código:
if x > 10 then y := 1 else y := 0;

Tokens Gerados:
1. IF: 'if' (linha 1, coluna 0)
2. IDENTIFIER: 'x' (linha 1, coluna 3)
3. GREATER_THAN: '>' (linha 1, coluna 5)
4. INTEGER_LITERAL: '10' (linha 1, coluna 7)
5. THEN: 'then' (linha 1, coluna 10)
6. IDENTIFIER: 'y' (linha 1, coluna 15)
7. ASSIGN: ':=' (linha 1, coluna 17)
8. INTEGER_LITERAL: '1' (linha 1, coluna 20)
9. ELSE: 'else' (linha 1, coluna 22)
10. IDENTIFIER: 'y' (linha 1, coluna 27)
11. ASSIGN: ':=' (linha 1, coluna 29)
12. INTEGER_LITERAL: '0' (linha 1, coluna 32)
13. SEMICOLON: ';' (linha 1, coluna 33)

Exemplo 4: Laço For
Código:
for i := 1 to 5 do print(i);

Tokens Gerados:
1. FOR: 'for' (linha 1, coluna 0)
2. IDENTIFIER: 'i' (linha 1, coluna 4)
3. ASSIGN: ':=' (linha 1, coluna 6)
4. INTEGER_LITERAL: '1' (linha 1, coluna 9)
5. TO: 'to' (linha 1, coluna 11)
6. INTEGER_LITERAL: '5' (linha 1, coluna 14)
7. DO: 'do' (linha 1, coluna 16)
8. PRINT: 'print' (linha 1, coluna 19)
9. LPAREN: '(' (linha 1, coluna 24)
10. IDENTIFIER: 'i' (linha 1, coluna 25)
11. RPAREN: ')' (linha 1, coluna 26)
12. SEMICOLON: ';' (linha 1, coluna 27)


Exemplo 5: Programa Completo
Código:
program Exemplo;
var x: integer;
begin
    x := 10;
end.

Tokens Gerados:
1. PROGRAM: 'program' (linha 1, coluna 0)
2. IDENTIFIER: 'Exemplo' (linha 1, coluna 8)
3. SEMICOLON: ';' (linha 1, coluna 15)
4. VAR: 'var' (linha 2, coluna 0)
5. IDENTIFIER: 'x' (linha 2, coluna 4)
6. COLON: ':' (linha 2, coluna 5)
7. INTEGER_TYPE: 'integer' (linha 2, coluna 7)
8. SEMICOLON: ';' (linha 2, coluna 14)
9. BEGIN: 'begin' (linha 3, coluna 0)
10. IDENTIFIER: 'x' (linha 4, coluna 4)
11. ASSIGN: ':=' (linha 4, coluna 6)
12. INTEGER_LITERAL: '10' (linha 4, coluna 9)
13. SEMICOLON: ';' (linha 4, coluna 11)
14. END: 'end' (linha 5, coluna 0)
15. DOT: '.' (linha 5, coluna 3)

❌ Tratamento de Erros Léxicos
Erro 1: Caractere Inválido
Código:
x := 10 @ y;
Erro:
❌ ERRO LÉXICO (linha 1, coluna 8): Caractere inválido '@'

Erro 2: String Não Fechada
Código:
print('texto sem fechar);
Erro:❌ ERRO LÉXICO (linha 1, coluna 6): String literal não fechada

Erro 3: Número Real Malformado
Código:
x := 3.14.15;
Erro:
❌ ERRO LÉXICO (linha 1, coluna 5): Número real malformado '3.14.15'

🚀 Como Executar

Passo 1: Gerar o Lexer
mvn clean generate-sources

Passo 2: Compilar o Projeto
mvn compile

Passo 3: Executar Testes do Lexer
mvn test -Dtest=MiniPascalLexerTest

Saída esperada:
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running MiniPascalLexerTest
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

Passo 4: Verificar Relatórios
dir test-reports\lexer-tokens

Arquivos gerados:
teste1_tokens_simples.txt
teste2_palavras_reservadas.txt
teste3_operadores.txt
teste4_literais.txt
teste5_comentarios.txt
teste6_programa_completo.txt

✅ Validação
Checklist de Validação
✅ Lexer gerado pelo ANTLR sem erros
✅ Todos os tokens reconhecidos corretamente
✅ Palavras-reservadas identificadas
✅ Operadores tokenizados
✅ Literais (inteiros, reais, strings) reconhecidos
✅ Comentários ignorados
✅ Espaços em branco ignorados
✅ Testes unitários passando (6/6)
✅ Relatórios de tokens gerados
✅ Erros léxicos detectados

## 🎯 Resumo da Entrega

| Item | Status |
|------|--------|
| Lexer Implementado | ✅ Completo |
| Tokens Reconhecidos | ✅ 40+ tipos |
| Testes Unitários | ✅ 6 testes passando |
| Relatórios Gerados | ✅ 6 arquivos |
| Erros Léxicos Detectados | ✅ Sim |
| Documentação | ✅ Completa |

## 📝 Histórico de Versões

| Versão | Data | Descrição | Autor |
|--------|------|-----------|-------|
| 1.0 | 21/01/2026 | Versão inicial da entrega | Equipe Mini-Pascal |


📞 Suporte
Para dúvidas sobre esta entrega:
Email: jeds@outlook.com.br
GitHub: https://github.com/jeds2025

👥 Equipe
Enaldo Dantas  - jose.santos259@academico.ifs.edu.br
Agnaldo Neto   - jose.neto044@academico.ifs.edu.br
Vitorio Mota   - vitorio.mota085@academico.ifs.edu.br
Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br

Entrega 2 concluída com sucesso! ✅
Desenvolvido para IFS Campus Itabaiana - 2025.2
Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES


