# 📗 Entrega 3: Analisador Sintático e Semântico (Parser)

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

|       Item      |                          Descrição                              |
|-----------------|-----------------------------------------------------------------|
| Data de Entrega | 28/01/2026                                                      |
| Objetivo        | Validar estrutura gramatical e verificar consistência semântica |
| Status          | ✅ Concluído                                                   |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Conceito](#conceito)
- [Artefatos Entregues](#artefatos-entregues)
- [Analisador Sintático](#analisador-sintático)
- [Analisador Semântico](#analisador-semântico)
- [Tabela de Símbolos](#tabela-de-símbolos)
- [Sistema de Tipos](#sistema-de-tipos)
- [Exemplos de Validação](#exemplos-de-validação)
- [Testes Implementados](#testes-implementados)
- [Como Executar](#como-executar)
- [Relatórios Gerados](#relatórios-gerados)

---

## 🎯 Objetivo

Esta entrega implementa **duas camadas críticas** do compilador:

### 1️⃣ Analisador Sintático (Parser)
- Validar se a sequência de tokens obedece à gramática BNF definida na Entrega 1
- Gerar a Árvore Sintática Abstrata (AST)
- Detectar erros sintáticos com mensagens descritivas

### 2️⃣ Analisador Semântico
- Verificar compatibilidade de tipos em operações
- Validar declaração de variáveis antes do uso
- Controlar escopos (variáveis locais vs. globais)
- Detectar redeclarações de variáveis

---

## 💡 Conceito

### Análise Sintática
> "O parser verifica se a estrutura do código está correta. É como verificar se uma frase em português tem sujeito, verbo e predicado na ordem certa."

**Exemplo de Erro Sintático:**
```pascal
if x > 10 print('erro');  // ❌ Falta "then"

Análise Semântica
"O analisador semântico verifica se o código faz sentido. 
É como verificar se uma frase gramaticalmente correta também tem lógica."

Exemplos de Erros Semânticos:
❌ "Estou tentando somar um número com um texto?"
❌ "A variável x foi declarada antes de ser usada?"
❌ "Estou atribuindo um valor real a uma variável integer?"

## 📦 Artefatos Entregues

### Arquivos Principais

| Arquivo                          | Localização                        | Descrição                                 |
|----------------------------------|------------------------------------|-------------------------------------------|
| `miniPascalParser.java`          | `target/generated-sources/antlr4/` | Parser gerado pelo ANTLR                  |
| `MiniPascalSemanticVisitor.java` | `src/main/java/`                   | Visitor para análise semântica            |
| `SymbolTable.java`               | `src/main/java/`                   | Tabela de símbolos com suporte a escopos  |
| `Type.java`                      | `src/main/java/`                   | Enumeração de tipos da linguagem          |
| `SemanticException.java`         | `src/main/java/`                   | Exceção customizada para erros semânticos |
| `MiniPascalParserTest.java`      | `src/test/java/`                   | Testes unitários do parser                |
| `README_ENTREGA3.md`             | Raiz do projeto                    | Este documento                            |


### Arquivos Gerados

| Arquivo          | Localização                      | Descrição                   |
|------------------|----------------------------------|-----------------------------|
| Árvores de Parse | `test-reports/parse-trees/`      | Representação visual da AST |
| Erros Semânticos | `test-reports/semantic-errors/`  | Log de erros detectados     |
| Relatórios JUnit | `test-reports/`                  | Resultados dos testes       |

## 🌳 Analisador Sintático

### Funcionalidades Implementadas

| Funcionalidade         | Descrição                                    | Status           |
|------------------------|----------------------------------------------|------------------|
| Validação de Estrutura | Verifica conformidade com gramática BNF      | ✅ Implementado |
| Geração de AST         | Cria árvore sintática abstrata               | ✅ Implementado |
| Detecção de Erros      | Identifica erros sintáticos com linha/coluna | ✅ Implementado |
| Mensagens Descritivas  | Erros com contexto e sugestões               | ✅ Implementado |

### Regras Sintáticas Validadas

// Estrutura do Programa
program
    : PROGRAM identifier SEMICOLON 
      variableDeclarationPart? 
      compoundStatement DOT EOF
    ;

// Declaração de Variáveis
variableDeclarationPart
    : VAR variableDeclaration (SEMICOLON variableDeclaration)* SEMICOLON?
    ;

// Comandos
statement
    : assignmentStatement
    | ifStatement
    | whileStatement
    | forStatement
    | ioStatement
    | compoundStatement
    | // vazio
    ;

// Expressões com Precedência
expression
    : simpleExpression (relationalOperator simpleExpression)?
    ;

simpleExpression
    : term (additiveOperator term)*
    ;

term
    : factor (multiplicativeOperator factor)*
    ;


// Exemplo de Detecção de Erro Sintático
// Código Inválido:

program Teste;
var x: integer
begin  // ❌ ERRO: Falta ';' após declaração de variável
  x := 10;
end.

Mensagem de Erro:
line 2:14 mismatched input 'begin' expecting {';', ','}
❌ ERRO DE SINTAXE: Esperado ';' após declaração de variável

## 🔬 Analisador Semântico

### Funcionalidades Implementadas

| Funcionalidade           | Descrição                                       | Status           |
|--------------------------|-------------------------------------------------|------------------|
| Verificação de Tipos     | Valida compatibilidade em operações             | ✅ Implementado |
| Declaração de Variáveis  | Verifica se variável foi declarada antes do uso | ✅ Implementado |
| Controle de Escopos      | Gerencia escopos aninhados (global/local)       | ✅ Implementado |
| Detecção de Redeclaração | Impede declarar mesma variável duas vezes       | ✅ Implementado |


// Implementação do Visitor Semântico

Arquivo: src/main/java/MiniPascalSemanticVisitor.java

public class MiniPascalSemanticVisitor extends miniPascalBaseVisitor<Type> {
    private SymbolTable symbolTable;

    public MiniPascalSemanticVisitor() {
        this.symbolTable = new SymbolTable();
    }

    @Override
    public Type visitVariableDeclaration(miniPascalParser.VariableDeclarationContext ctx) {
        Type varType = getType(ctx.type());
        
        for (var idCtx : ctx.identifierList().identifier()) {
            String varName = idCtx.getText();
            
            // Verifica redeclaração
            try {
                symbolTable.declare(varName, varType);
            } catch (SemanticException e) {
                System.err.println("ERRO SEMÂNTICO linha " + ctx.start.getLine() + 
                                   ": " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Type visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.identifier().getText();
        
        // Verifica se variável foi declarada
        Type varType;
        try {
            varType = symbolTable.lookup(varName);
        } catch (SemanticException e) {
            System.err.println("ERRO SEMÂNTICO linha " + ctx.start.getLine() + 
                               ": Variavel '" + varName + "' nao declarada");
            return null;
        }
        
        // Verifica tipo da expressão
        Type exprType = visit(ctx.expression());
        
        if (varType != exprType && exprType != null) {
            System.err.println("ERRO SEMÂNTICO linha " + ctx.start.getLine() + 
                               ": Tipos incompativeis na atribuicao");
        }
        
        return null;
    }
}

📊 Tabela de Símbolos
📊 Estrutura

Arquivo: src/main/java/SymbolTable.java

public class SymbolTable {
    private List<Map<String, Type>> scopes;
    
    public SymbolTable() {
        scopes = new ArrayList<>();
        enterScope(); // Escopo global
    }
    
    public void enterScope() {
        scopes.add(new HashMap<>());
    }
    
    public void exitScope() {
        if (scopes.size() > 1) {
            scopes.remove(scopes.size() - 1);
        }
    }
    
    public void declare(String name, Type type) throws SemanticException {
        Map<String, Type> currentScope = scopes.get(scopes.size() - 1);
        
        if (currentScope.containsKey(name)) {
            throw new SemanticException("Variavel '" + name + "' ja declarada neste escopo");
        }
        
        currentScope.put(name, type);
    }
    
    public Type lookup(String name) throws SemanticException {
        // Busca do escopo mais interno para o mais externo
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        
        throw new SemanticException("Variavel '" + name + "' nao declarada");
    }
}


### Funcionalidades

| Método                | Descrição                                                            |
|-----------------------|----------------------------------------------------------------------|
| `enterScope()`        | Cria novo escopo (ao entrar em bloco `begin...end`)                  |
| `exitScope()`         | Remove escopo atual (ao sair de bloco)                               |
| `declare(name, type)` | Declara variável no escopo atual                                     |
| `lookup(name)`        | Busca variável em todos os escopos (do mais interno ao mais externo) |


🎨 Sistema de Tipos

Arquivo: src/main/java/Type.java

public enum Type {
    INTEGER,    // Números inteiros
    REAL,       // Números reais (ponto flutuante)
    STRING,     // Texto
    BOOLEAN,    // Verdadeiro/Falso
    VOID        // Sem tipo (para comandos)
}


### Regras de Compatibilidade

| Operação                        | Tipos Aceitos         | Tipo Resultante | Exemplo           |
|---------------------------------|-----------------------|-----------------|-------------------|
| `+`, `-`, `*`, `/`              | `INTEGER` + `INTEGER` | `INTEGER`       | `5 + 3 = 8`       |
| `+`, `-`, `*`, `/`              | `REAL` + `REAL`       | `REAL`          | `5.5 + 2.3 = 7.8` |
| `+`, `-`, `*`, `/`              | `INTEGER` + `REAL`    | `REAL`          | `5 + 2.5 = 7.5`   |
| `=`, `<>`, `<`, `>`, `<=`, `>=` | Mesmo tipo            | `BOOLEAN`       | `x > 10`          |
| `and`, `or`, `not`              | `BOOLEAN`             | `BOOLEAN`       | `true and false`  |

✅ Exemplos de Validação

Exemplo 1: Programa Válido
program ExemploValido;
var
    x, y: integer;
    z: real;
begin
    x := 10;
    y := 20;
    z := x + y;  // ✅ OK: INTEGER + INTEGER = INTEGER, atribuído a REAL
    
    if z > 15.0 then
        print('Maior que 15');
end.

Análise Semântica:

✅ Todas as variáveis declaradas antes do uso
✅ Tipos compatíveis nas operações
✅ Conversão implícita de INTEGER para REAL permitida

Exemplo 2: Variável Não Declarada
program ErroNaoDeclarada;
begin
    x := 10;  // ❌ ERRO: 'x' não foi declarada
end.

Saída:
❌ ERRO SEMÂNTICO linha 3: Variavel 'x' nao declarada

Exemplo 3: Redeclaração de Variável
program ErroRedeclaracao;
var
    x: integer;
    x: real;  // ❌ ERRO: 'x' já foi declarada
begin
    x := 10;
end.

Saída:
❌ ERRO SEMÂNTICO linha 4: Variavel 'x' ja declarada neste escopo


Exemplo 4: Tipos Incompatíveis
program ErroTipos;
var
    x: integer;
    nome: string;
begin
    x := nome;  // ❌ ERRO: Não pode atribuir STRING a INTEGER
end.

Saída:
❌ ERRO SEMÂNTICO linha 6: Tipos incompativeis na atribuicao

Exemplo 5: Escopos Aninhados
program ExemploEscopos;
var
    x: integer;
begin
    x := 10;  // ✅ Variável global
    
    begin
        var y: integer;
        y := 20;  // ✅ Variável local
        x := x + y;  // ✅ Acessa variável global
    end;
    
    print(x);  // ✅ OK
    print(y);  // ❌ ERRO: 'y' não existe fora do escopo interno
end.


🧪 Testes Implementados

Arquivo: src/test/java/MiniPascalParserTest.java

### Testes Sintáticos

| Teste                         | Descrição                        | Status     |
|-------------------------------|----------------------------------|------------|
| `testProgramaValido()`        | Valida programa completo         | ✅ Passou |
| `testIfElse()`                | Valida estrutura condicional     | ✅ Passou |
| `testWhile()`                 | Valida laço while                | ✅ Passou |
| `testFor()`                   | Valida laço for (to/downto)      | ✅ Passou |
| `testExpressoesAritmeticas()` | Valida precedência de operadores | ✅ Passou |
| `testErroSintaxe()`           | Detecta erro sintático           | ✅ Passou |


### Testes Semânticos

| Teste                        | Descrição                             | Status     |
|------------------------------|---------------------------------------|------------|
| `testVariavelNaoDeclarada()` | Detecta uso de variável não declarada | ✅ Passou |
| `testRedeclaracao()`         | Detecta redeclaração de variável      | ✅ Passou |
| `testTiposIncompativeis()`   | Detecta incompatibilidade de tipos    | ✅ Passou |
| `testEscoposAninhados()`     | Valida controle de escopos            | ✅ Passou |


🚀 Executar Testes
# Todos os testes do parser

mvn test -Dtest=MiniPascalParserTest

# Teste específico
mvn test -Dtest=MiniPascalParserTest#testVariavelNaoDeclarada

🚀 Como Executar
Passo 1: Compilar o Projeto

mvn clean compile

Passo 2: Executar Menu Interativo

mvn exec:java
ou
java -cp target/classes Main

Selecionar Opção 5:
5 - Testes apenas do Parser

Passo 3: Demo de Análise Semântica
Selecionar Opção 7:
7 - Demo Semântica (tabela de símbolos + escopos)

Saída Esperada:
╔════════════════════════════════════════════════════════╗
║   DEMO SEMÂNTICA (Tabela de Símbolos + Escopos)        ║
╚════════════════════════════════════════════════════════╝

📌 Escopo global:
✅ Declarado: x (INTEGER), y (REAL), nome (STRING)

🔍 Lookup: x = INTEGER

🔍 Lookup inválido: z = ✅ OK - Variavel 'z' nao declarada

📌 Entrando em escopo interno:
✅ Declarado: contador (INTEGER) no escopo local

🔍 Lookup contador (interno): INTEGER

🔍 Lookup x (visível do global): INTEGER

✅ Simulação de atribuição: contador := contador + 1 (válido)

🔍 Tentativa de redeclaração: contador já existe → ✅ OK - Variavel 'contador' ja declarada neste escopo

📌 Saindo do escopo interno:

🔍 Lookup contador (fora do escopo): ✅ OK - Variavel 'contador' nao declarada

✅ Demo finalizada!

📂 Relatórios Gerados
Localização dos Arquivos

test-reports/
├── parse-trees/                    # Árvores sintáticas
│   ├── teste1_programa_valido_tree.txt
│   ├── teste2_if_else_tree.txt
│   ├── teste3_while_tree.txt
│   ├── teste4_for_tree.txt
│   └── teste5_expressoes_tree.txt
│
├── semantic-errors/                # Erros semânticos
│   ├── test_variavel_nao_declarada.txt
│   ├── test_redeclaracao.txt
│   └── test_tipos_incompativeis.txt
|
│                                   # Relatórios JUnit
├── MiniPascalParserTest.txt
└── TEST-MiniPascalParserTest.xml

Exemplo de Árvore Sintática
Arquivo: test-reports/parse-trees/teste1_programa_valido_tree.txt

(program 
  program ExemploValido ; 
  (variableDeclarationPart 
    var 
    (variableDeclaration 
      (identifierList x , y) : integer) ;) 
  (compoundStatement 
    begin 
    (statement 
      (assignmentStatement x := (expression (simpleExpression (term (factor 10)))))) ; 
    (statement 
      (assignmentStatement y := (expression (simpleExpression (term (factor 20)))))) 
    end) 
  . 
  <EOF>)

## 🎯 Resumo da Entrega

| Item                | Status                    |
|---------------------|---------------------------|
| Parser Implementado | ✅ Completo              |
| AST Gerada          | ✅ Sim                   |
| Análise Semântica   | ✅ Completa              |             
| Tabela de Símbolos  | ✅ Com suporte a escopos |
| Sistema de Tipos    | ✅ 4 tipos implementados |
| Testes Unitários    | ✅ 10 testes passando    |
| Relatórios Gerados  | ✅ Árvores e erros       |
| Documentação        | ✅ Completa              |

## 🎓 Conceitos Aplicados

| Conceito             | Descrição                                  | Implementação                     |
|----------------------|--------------------------------------------|-----------------------------------|
| AST                  | Árvore Sintática Abstrata                  | Gerada automaticamente pelo ANTLR |
| Visitor Pattern      | Padrão de projeto para travessia da AST    | `MiniPascalSemanticVisitor.java`  |
| Tabela de Símbolos   | Estrutura para armazenar variáveis e tipos | `SymbolTable.java`                |
| Escopos Aninhados    | Suporte a blocos `begin...end`             | `enterScope()` / `exitScope()`    |
| Verificação de Tipos | Compatibilidade em operações               | Regras no Visitor                 |

📚 Referências

Dragon Book: Compilers: Principles, Techniques, and Tools (Aho, Sethi, Ullman)
ANTLR 4 Documentation: https://www.antlr.org/
Visitor Pattern: Design Patterns (Gang of Four)

## 📝 Histórico de Versões

| Versão | Data       | Descrição                 | Autor              |
|--------|------------|---------------------------|--------------------|
| 1.0    | 28/01/2026 | Versão inicial da entrega | Equipe Mini-Pascal |

👥 Autores

Enaldo Dantas  - jose.santos259@academico.ifs.edu.br
Agnaldo Neto   - jose.neto044@academico.ifs.edu.br
Vitorio Mota   - vitorio.mota085@academico.ifs.edu.br
Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br

📞 ContatoPara dúvidas sobre esta entrega:
Email: jeds@outlook.com.br
GitHub: https://github.com/ifs2026

Entrega 3 concluída com sucesso! ✅
Desenvolvido para IFS Campus Itabaiana - 2025.2
Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES
