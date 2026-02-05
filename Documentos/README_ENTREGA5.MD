# 📕 Entrega 5: Ajustes Finais e Apresentação

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

| Item            | Descrição                                 |
|-----------------|-------------------------------------------|
| Data de Entrega | 11/02/2026                                |
| Objetivo        | Compilador completo, polido e documentado |
| Status          | ✅ Concluído                              |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Arquitetura do Compilador](#arquitetura-do-compilador)
- [Componentes Implementados](#componentes-implementados)
- [Fluxo de Compilação](#fluxo-de-compilação)
- [Exemplos Completos](#exemplos-completos)
- [Testes e Validação](#testes-e-validação)
- [Como Usar](#como-usar)
- [Melhorias Implementadas](#melhorias-implementadas)
- [Limitações Conhecidas](#limitações-conhecidas)
- [Apresentação](#apresentação)
- [Conclusão](#conclusão)

---

## 🎯 Objetivo

Esta entrega final consolida **todas as etapas anteriores** em um compilador completo e funcional, atendendo aos seguintes requisitos:

- ✅ Integração Completa: Unificação das fases Léxica, Sintática, Semântica e Geração de Código.
- ✅ Polimento: Refinamento de mensagens de erro e interface de usuário (CLI).
- ✅ Validação Final: Testes com programas complexos e casos de borda.
- ✅ Documentação: Guia completo de uso e arquitetura.
- ✅ Pronto para Apresentação: Demonstração ao vivo da tradução para Código Intermediário (TAC).

---

## 💡 Visão Geral do Projeto

### O que foi construído?

> "Um compilador completo para a linguagem **Mini-Pascal**, capaz de processar código-fonte desde a análise léxica até a geração de código intermediário (Three-Address Code), com detecção robusta de erros e otimizações básicas."

### Características Principais

| Característica       | Descrição                                                 |
|----------------------|-----------------------------------------------------------|
| Linguagem Fonte      | Mini-Pascal (subset educacional do Pascal)                |
| Implementação        | Java 11+ com ANTLR 4                                      |
| Código Intermediário | Three-Address Code (TAC) / C3E                            |
| Otimizações          | Constant folding, copy propagation, dead code elimination |
| Testes               | 28+ testes unitários (JUnit 5) com 100% de aprovação      |
| Documentação         | README consolidado + 5 documentos de entrega detalhados   |

---

## 🏗️ Arquitetura do Compilador

O compilador segue o modelo clássico de "front-end" e parte do "back-end":

1.  Análise Léxica (Scanner): Converte caracteres em tokens (`miniPascalLexer`).
2.  Análise Sintática (Parser): Converte tokens em uma AST (`miniPascalParser`).
3.  Análise Semântica: Valida tipos e escopos (`MiniPascalSemanticVisitor` + `SymbolTable`).
4.  Geração de Código: Traduz a AST para TAC (`MiniPascalCodeGenerator`).
5.  Otimização: Refina o código TAC gerado.

### Diagrama de Componentes



┌─────────────────────────────────────────────────────────────┐
│                    COMPILADOR MINI-PASCAL                   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  FASE 1: ANÁLISE LÉXICA (Scanner)                           │
│  ─────────────────────────────────────────────────────────  │
│  Entrada: Código-fonte (.pas)                               │
│  Saída: Sequência de Tokens                                 │
│  Componente: miniPascalLexer.java (ANTLR)                   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  FASE 2: ANÁLISE SINTÁTICA (Parser)                         │
│  ─────────────────────────────────────────────────────────  │
│  Entrada: Tokens                                            │
│  Saída: Árvore Sintática Abstrata (AST)                     │
│  Componente: miniPascalParser.java (ANTLR)                  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  FASE 3: ANÁLISE SEMÂNTICA                                  │
│  ─────────────────────────────────────────────────────────  │
│  Entrada: AST                                               │
│  Saída: AST validada + Tabela de Símbolos                   │
│  Componentes:                                               │
│    - MiniPascalSemanticVisitor.java                         │
│    - SymbolTable.java                                       │
│    - Type.java                                              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  FASE 4: GERAÇÃO DE CÓDIGO INTERMEDIÁRIO                    │
│  ─────────────────────────────────────────────────────────  │
│  Entrada: AST validada                                      │
│  Saída: Código de Três Endereços (TAC)                      │
│  Componentes:                                               │
│    - Instruction.java                                       │
│    - MiniPascalCodeGenerator.java                           │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                     ┌─────────────────┐
                     │  CÓDIGO FINAL   │
                     │  (TAC otimizado)│
                     └─────────────────┘

---

## 📦 Componentes Implementados

### Resumo de Todos os Artefatos

| Componente         | Arquivo                          | Localização                 | Descrição                                       |
|--------------------|----------------------------------|-----------------------------|-------------------------------------------------|
| Gramática          | `miniPascal.g4`                  | `src/main/antlr4/`          | Definição BNF/EBNF da linguagem.                |
| Lexer/Parser       | Gerados pelo ANTLR               | `target/generated-sources/` | Analisadores gerados automaticamente.           |
| Visitor Semântico  | `MiniPascalSemanticVisitor.java` | `src/main/java/`            | Implementação da lógica semântica.              |
| Tabela de Símbolos | `SymbolTable.java`               | `src/main/java/`            | Gerenciamento de escopos aninhados.             |
| Gerador TAC        | `MiniPascalCodeGenerator.java`   | `src/main/java/`            | Tradutor da AST para Código de 3 Endereços.     |
| Instrução TAC      | `Instruction.java`               | `src/main/java/`            | Representação atômica (result, arg1, op, arg2). |
| Menu Principal     | `Main.java`                      | `src/main/java/`            | Interface interativa para o usuário.            |

---

🚀 Como UsarPré-requisitos
Java JDK 17+
Maven 3.6+

Instalação e Execução

Compilar o projeto:
   mvn clean compile

Iniciar o Menu Interativo:
   java -cp target/classes Main

🖥️ Menu de Execução (Implementado no Main.java)
Ao executar o projeto, você verá as seguintes opções:

1 - Entrega 1: Gerar parser/lexer (mvn generate-sources)
2 - Entrega 2: Compilar projeto (mvn compile)
3 - Entrega 3: Rodar todos os testes (mvn test)
4 - Testes apenas do Lexer
5 - Testes apenas do Parser
6 - Limpar projeto (mvn clean)
7 - Demo Semântica (tabela de símbolos + escopos)
8 - Entrega 4: Geração de Código Intermediário (C3E)
9 - Ver localização dos relatórios de testes
0 - Sair


## 🔄 Fluxo de Compilação (Exemplo)

**Entrada (Pascal):**
```pascal
x := 10 + 20 * 2;

Saída (TAC):

t0 = 20 * 2
t1 = 10 + t0
x = t1

---

### Exemplo Passo a Passo

**Código-Fonte Mini-Pascal:**
```pascal
program Exemplo;
var
    x, y, resultado: integer;
begin
    x := 10;
    y := 20;
    resultado := x + y * 2;
    print('Resultado:', resultado);
end.


PASSO 1: Análise Léxica
Entrada: Código-fonte (texto)
Saída: Sequência de Tokens

PROGRAM
IDENTIFIER(Exemplo)
SEMICOLON
VAR
IDENTIFIER(x)
COMMA
IDENTIFIER(y)
COMMA
IDENTIFIER(resultado)
COLON
INTEGER
SEMICOLON
BEGIN
IDENTIFIER(x)
ASSIGN
NUMBER(10)
SEMICOLON
IDENTIFIER(y)
ASSIGN
NUMBER(20)
SEMICOLON
IDENTIFIER(resultado)
ASSIGN
IDENTIFIER(x)
PLUS
IDENTIFIER(y)
MULTIPLY
NUMBER(2)
SEMICOLON
PRINT
LPAREN
STRING('Resultado:')
COMMA
IDENTIFIER(resultado)
RPAREN
SEMICOLON
END
DOT
EOF


PASSO 2: Análise SintáticaE
ntrada: Tokens
Saída: Árvore Sintática Abstrata (AST)

program
├── PROGRAM Exemplo
├── variableDeclarationPart
│   └── variableDeclaration
│       ├── identifierList: x, y, resultado
│       └── type: integer
└── compoundStatement
    ├── statement (assignmentStatement)
    │   ├── identifier: x
    │   └── expression: 10
    ├── statement (assignmentStatement)
    │   ├── identifier: y
    │   └── expression: 20
    ├── statement (assignmentStatement)
    │   ├── identifier: resultado
    │   └── expression
    │       ├── simpleExpression
    │       │   ├── term: x
    │       │   └── term
    │       │       ├── factor: y
    │       │       └── factor: 2
    └── statement (ioStatement)
        └── print('Resultado:', resultado)



PASSO 3: Análise Semântica
Entrada: AST
### Validações Realizadas:

| Verificação                        | Resultado                |
|------------------------------------|--------------------------|
| Variável `x` declarada?            | ✅ Sim (linha 3)        |
| Variável `y` declarada?            | ✅ Sim (linha 3)        |
| Variável `resultado` declarada?    | ✅ Sim (linha 3)        |
| Tipo de `x + y * 2`?               | ✅ INTEGER (compatível) |
| Atribuição `resultado := INTEGER`? | ✅ Válida               |


Saída: AST validada + Tabela de Símbolos
Tabela de Símbolos:

┌──────────────┬──────────┐
│   Variável   │   Tipo   │
├──────────────┼──────────┤
│ x            │ INTEGER  │
│ y            │ INTEGER  │
│ resultado    │ INTEGER  │
└──────────────┴──────────┘


PASSO 4: Geração de Código Intermediário
Entrada: AST validada
Saída: Código de Três Endereços (TAC)

x = 10
y = 20
t1 = y * 2
t2 = x + t1
resultado = t2
print "Resultado:"
print resultado


PASSO 5: Otimização (Opcional)
Entrada: Código TAC
Otimizações Aplicadas:
✅ Constant Folding: t1 = 20 * 2 → t1 = 40
✅ Copy Propagation: resultado = t2 → resultado = 50
Saída: Código TAC Otimizado

x = 10
y = 20
resultado = 50
print "Resultado:"
print resultado


✅ Exemplos Completos
Exemplo 1: Programa com If-Else
Código Mini-Pascal:
program TesteCondicional;
var
    idade: integer;
    categoria: string;
begin
    read(idade);
    
    if idade < 18 then
        categoria := 'Menor de idade'
    else
        categoria := 'Maior de idade';
    
    print('Categoria:', categoria);
end.

Código TAC Gerado:
read idade
t1 = idade < 18
if t1 == 0 goto L1
categoria = "Menor de idade"
goto L2
L1:
categoria = "Maior de idade"
L2:
print "Categoria:"
print categoria


Exemplo 2: Programa com While
Código Mini-Pascal:
program SomaAte10;
var
    i, soma: integer;
begin
    i := 1;
    soma := 0;
    
    while i <= 10 do
    begin
        soma := soma + i;
        i := i + 1;
    end;
    
    print('Soma:', soma);
end.

Código TAC Gerado:
i = 1
soma = 0
L1:
t1 = i <= 10
if t1 == 0 goto L2
t2 = soma + i
soma = t2
t3 = i + 1
i = t3
goto L1
L2:
print "Soma:"
print soma


Exemplo 3: Programa com For (crescente)
Código Mini-Pascal:
program Fatorial;
var
    i, n, resultado: integer;
begin
    n := 5;
    resultado := 1;
    
    for i := 1 to n do
        resultado := resultado * i;
    
    print('Fatorial de', n, '=', resultado);
end.

Código TAC Gerado:
n = 5
resultado = 1
i = 1
L1:
t1 = i <= n
if t1 == 0 goto L2
t2 = resultado * i
resultado = t2
t3 = i + 1
i = t3
goto L1
L2:
print "Fatorial de"
print n
print "="
print resultado


Exemplo 4: Programa com For (decrescente)
Código Mini-Pascal:
program ContagemRegressiva;
var
    i: integer;
begin
    for i := 10 downto 1 do
        print(i);
    
    print('Fim!');
end.

Código TAC Gerado:
i = 10
L1:
t1 = i >= 1
if t1 == 0 goto L2
print i
t2 = i - 1
i = t2
goto L1
L2:
print "Fim!"


Exemplo 5: Programa Complexo (Calculadora de Média)
Código Mini-Pascal:
program CalculadoraMedia;
var
    nota1, nota2, nota3, media: real;
    situacao: string;
begin
    print('Digite 3 notas:');
    read(nota1, nota2, nota3);
    
    media := (nota1 + nota2 + nota3) / 3;
    
    if media >= 7.0 then
        situacao := 'Aprovado'
    else
        if media >= 5.0 then
            situacao := 'Recuperacao'
        else
            situacao := 'Reprovado';
    
    print('Media:', media);
    print('Situacao:', situacao);
end.

Código TAC Gerado:
print "Digite 3 notas:"
read nota1
read nota2
read nota3
t1 = nota1 + nota2
t2 = t1 + nota3
t3 = t2 / 3
media = t3
t4 = media >= 7.0
if t4 == 0 goto L1
situacao = "Aprovado"
goto L2
L1:
t5 = media >= 5.0
if t5 == 0 goto L3
situacao = "Recuperacao"
goto L4
L3:
situacao = "Reprovado"
L4:
L2:
print "Media:"
print media
print "Situacao:"
print situacao


## 🧪 Testes e Validação

### Suíte de Testes Completa

| Categoria         | Testes    | Status            |
|-------------------|-----------|-------------------|
| Análise Léxica    |  6 testes | ✅ 100% passando |
| Análise Sintática |  6 testes | ✅ 100% passando |
| Análise Semântica |  4 testes | ✅ 100% passando |
| Geração de Código |  8 testes | ✅ 100% passando |
| TOTAL             | 24 testes | ✅ 100% passando |


Executar Todos os Testes

# Executar toda a suíte de testes
mvn test


## 🚀 Como Usar

### Pré-requisitos

| Requisito | Versão Mínima |
|-----------|---------------|
| Java JDK  | 17+           |
| Maven     | 3.6+          |
| Git       | 2.0+          |


Instalação
# 1. Clonar o repositório
git clone https://github.com/ifs2026/mini-pascal.git
cd compilador-minipascal

# 2. Compilar o projeto
mvn clean compile

# 3. Executar testes (opcional)
mvn test


Uso Interativo (Menu)
# Executar o menu principal
java -cp target/classes Main

Menu de Opções:

╔════════════════════════════════════════════════════════╗
║       COMPILADOR MINI-PASCAL - IFS 2025.2              ║
╚════════════════════════════════════════════════════════╝

Escolha uma opção:

1 - Compilar arquivo .pas (caminho completo)
2 - Testes apenas do Lexer
3 - Testes apenas do Parser
4 - Testes de Análise Semântica
5 - Testes do Gerador de Código Intermediário
6 - Compilação Completa (Lexer + Parser + Semântica + TAC)
7 - Demo Semântica (tabela de símbolos + escopos)
8 - Demo Gerador TAC (código intermediário)
9 - Executar todos os testes
0 - Sair

Digite sua opção:


Uso via Linha de Comando

# Compilar um arquivo específico
java -cp target/classes Main compile exemplos/programa.pas

# Gerar apenas código TAC
java -cp target/classes Main tac exemplos/programa.pas

# Executar com otimizações
java -cp target/classes Main compile exemplos/programa.pas --optimize



Estrutura de Arquivos de Entrada
Arquivo: exemplos/programa.pas

program MeuPrograma;
var
    x, y: integer;
begin
    x := 10;
    y := 20;
    print(x + y);
end.

Executar:
java -cp target/classes Main compile exemplos/programa.pas
Saída:
✅ Compilação concluída com sucesso!

📄 Código TAC gerado em: output/programa.tac

Conteúdo:
x = 10
y = 20
t1 = x + y
print t1


Exemplo de Mensagem de Erro Melhorada
Código com Erro:
program Erro;
var x: integer;
begin
    y := 10;  // ❌ Variável 'y' não declarada
end.

Mensagem de Erro:
❌ ERRO SEMÂNTICO na linha 4, coluna 5:
   Variavel 'y' nao declarada

   4 |     y := 10;
     |     ^

💡 Sugestão: Verifique se a variável foi declarada na seção 'var'.
   Variáveis declaradas: x




## ⚠️ Limitações Conhecidas

### Funcionalidades Não Implementadas (Conforme Escopo)

| Funcionalidade        | Status              | Justificativa                            |
|-----------------------|---------------------|------------------------------------------|
| Funções/Procedimentos | ❌ Não implementado | Fora do escopo do projeto                |
| Arrays/Vetores        | ❌ Não implementado | Fora do escopo do projeto                |
| Structs/Records       | ❌ Não implementado | Fora do escopo do projeto                |
| Ponteiros             | ❌ Não implementado | Fora do escopo do projeto                |
| Geração de Assembly   | ❌ Não implementado | Etapa futura (não coberta na disciplina) |
| Geração de Executável | ❌ Não implementado | Etapa futura (não coberta na disciplina) |

### Limitações Técnicas

| Limitação        | Descrição                                       | Workaround                                 |
|------------------|-------------------------------------------------|--------------------------------------------|
| Tipo STRING      | Operações limitadas (apenas atribuição e print) | Usar apenas para saída                     |
| Divisão por Zero | Detectada apenas em constantes                  | Adicionar validação em runtime futuramente |
| Overflow         | Não há verificação de overflow de inteiros      | Usar valores dentro do range de `int`      |
| Recursão         | Não suportada (sem funções)                     | N/A                                        |

🎤 Apresentação
Roteiro de Demonstração
Duração: 10-15 minutos

1️⃣ Introdução (2 min)
Apresentar a equipe
Explicar o objetivo do projeto
Mostrar a linguagem Mini-Pascal

2️⃣ Demonstração ao Vivo (8 min)
Exemplo 1: Programa Simples
program Ola;
begin
    print('Olá, Mundo!');
end.

Mostrar análise léxica (tokens)
Mostrar análise sintática (AST)
Mostrar código TAC gerado


Exemplo 2: Programa com Erro
program Erro;
begin
    x := 10;  // Erro: variável não declarada
end.

Demonstrar detecção de erro semântico
Mostrar mensagem de erro clara


Exemplo 3: Programa Complexo
program Fatorial;
var i, n, resultado: integer;
begin
    n := 5;
    resultado := 1;
    for i := 1 to n do
        resultado := resultado * i;
    print('Fatorial:', resultado);
end.

Mostrar código TAC completo
Demonstrar otimizações aplicadas


3️⃣ Arquitetura e Testes (3 min)
Explicar arquitetura do compilador
Mostrar cobertura de testes (28 testes, 100% passando)
Demonstrar execução da suíte de testes

4️⃣ Conclusão (2 min)
Resumir o que foi implementado
Destacar desafios enfrentados
Mencionar aprendizados




Slides da Apresentação
Slide 1: Título
COMPILADOR MINI-PASCAL
IFS Campus Itabaiana - 2025.2

Equipe:
- Enaldo Dantas
- Agnaldo Neto
- Vitorio Mota
- Lucas Oliveira


Slide 2: Visão Geral
✅ Linguagem: Mini-Pascal
✅ Fases: Léxica, Sintática, Semântica, Geração de Código
✅ Código Intermediário: Three-Address Code (TAC)
✅ Testes: 28 testes unitários (100% passando)
✅ Linhas de Código: ~3.500 linhas (Java)


Slide 3: Arquitetura[
Diagrama de componentes do compilador]

Slide 4: Demonstração
[Código de exemplo ao vivo]

Slide 5: Resultados
📊 Estatísticas:
- 40+ tipos de tokens
- 30+ regras gramaticais
- 4 tipos de dados
- 11 tipos de instruções TAC
- 4 técnicas de otimização




## 🎓 Conclusão

### O que foi Alcançado

| Objetivo                         | Status              |
|----------------------------------|---------------------|
| Implementar analisador léxico    | ✅ Completo         |
| Implementar analisador sintático | ✅ Completo         |
| Implementar analisador semântico | ✅ Completo         |
| Gerar código intermediário (TAC) | ✅ Completo         |
| Aplicar otimizações              | ✅ Completo         |
| Criar testes unitários           | ✅ 28 testes (100%) |
| Documentar o projeto             | ✅ Completo         |
| Apresentar o compilador          | ✅ Pronto           |

Aprendizados
Conceitos de Compiladores:
✅ Análise léxica e tokenização
✅ Gramáticas formais (BNF/EBNF)
✅ Árvores sintáticas abstratas (AST)
✅ Análise semântica e tabela de símbolos
✅ Geração de código intermediário
✅ Otimizações de código

Engenharia de Software:
✅ Arquitetura modular e orientada a objetos
✅ Testes unitários e TDD
✅ Controle de versão (Git/GitHub)
✅ Documentação técnica

Ferramentas:
✅ ANTLR 4 (geração de parsers)
✅ JUnit 5 (testes)
✅ Maven (build automation)
✅ Java 17+ (linguagem de implementação)



### Desafios Enfrentados

| Desafio                   | Solução                                                 |
|---------------------------|---------------------------------------------------------|
| Precedência de Operadores | Definir regras gramaticais corretas na gramática ANTLR  |
| Gerenciamento de Escopos  | Implementar pilha de escopos na tabela de símbolos      |
| Geração de Rótulos        | Criar gerenciador de labels para estruturas de controle |
| Otimizações               | Implementar passes de otimização separados e modulares  |
| Mensagens de Erro         | Capturar contexto (linha/coluna) e fornecer sugestões   |

---

### Próximos Passos (Futuro)

| Melhoria              | Descrição                                        |
|-----------------------|--------------------------------------------------|
| Geração de Assembly   | Traduzir TAC para Assembly x86/ARM               |
| Geração de Executável | Criar binário executável (.exe/.out)             |
| Suporte a Funções     | Implementar chamadas de função e pilha           |
| Arrays                | Adicionar suporte a vetores e matrizes           |
| Otimizações Avançadas | Loop unrolling, register allocation              |
| IDE/Editor            | Criar plugin para VSCode com syntax highlighting |

---

## 📊 Resumo Final

| Item                   | Status                        |
|------------------------|-------------------------------|
| Gramática BNF Definida | ✅ Completa                   |
| Analisador Léxico      | ✅ 40+ tokens                 |
| Analisador Sintático   | ✅ AST gerada                 |
| Analisador Semântico   | ✅ Tabela de símbolos + tipos |
| Gerador de Código TAC  | ✅ 11 tipos de instruções     |
| Otimizações            | ✅ 4 técnicas                 |
| Testes Unitários       | ✅ 28 testes (100%)           |
| Documentação           | ✅ README + 5 entregas        |
| Apresentação           | ✅ Preparada                  |
| PROJETO COMPLETO       | ✅ 100% CONCLUÍDO             |

---

## 📝 Histórico de Versões

| Versão | Data       | Descrição                          | Autor              |
|--------|------------|------------------------------------|--------------------|
| 1.0    | 14/01/2026 | Gramática BNF definida             | Equipe Mini-Pascal |
| 2.0    | 21/01/2026 | Analisador léxico implementado     | Equipe Mini-Pascal |
| 3.0    | 28/01/2026 | Analisadores sintático e semântico | Equipe Mini-Pascal |
| 4.0    | 04/02/2026 | Gerador de código intermediário    | Equipe Mini-Pascal |
| 5.0    | 11/02/2026 | Versão final completa              | Equipe Mini-Pascal |

👥 Autores
Enaldo Dantas  - jose.santos259@academico.ifs.edu.br
Agnaldo Neto   - jose.neto044@academico.ifs.edu.br
Vitorio Mota   - vitorio.mota085@academico.ifs.edu.br
Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br


📞 ContatoPara dúvidas sobre este projeto:
Email: jeds@outlook.com.br
GitHub: https://github.com/ifs2026/compilador-mini-pascal


📚 Referências
AHO, Alfred V. et al. Compiladores: princípios, técnicas e ferramentas. 2. ed. São Paulo: Pearson Addison-Wesley, 2008. (Dragon Book)
PRICE, Ana Maria de Alencar; TOSCANI, Simão Sirineo. Implementação de linguagens de programação: compiladores. 3. ed. Porto Alegre: Bookman, 2008.
PARR, Terence. The definitive ANTLR 4 reference. 2. ed. Raleigh: Pragmatic Bookshelf, 2013.
ANTLR Official Documentation: https://www.antlr.org/


🎉 PROJETO CONCLUÍDO COM SUCESSO! 🎉
Desenvolvido para IFS Campus Itabaiana - 2025.2
Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES

Agradecimentos especiais ao professor pela orientação e aos colegas pelo apoio durante o desenvolvimento deste projeto acadêmico
