# 🚀 Compilador Mini-Pascal

**Projeto de Compiladores - IFS Campus Itabaiana - 2025.2**

Desenvolvido para a disciplina de LINGUAGENS FORMAIS, AUTOMATOS E COMPILADORES.

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![ANTLR](https://img.shields.io/badge/ANTLR-4.13.1-blue.svg)](https://www.antlr.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Instalação e Execução](#instalação-e-execução)
- [Entregas do Projeto](#entregas-do-projeto)
- [Exemplos de Uso](#exemplos-de-uso)
- [Estrutura de Diretórios](#estrutura-de-diretórios)
- [Autores](#autores)
- [Licença](#licença)

---

## 📖 Sobre o Projeto

Este projeto implementa um **compilador completo para a linguagem Mini-Pascal**, desenvolvido como trabalho acadêmico da disciplina de Compiladores do Instituto Federal de Sergipe (IFS) - Campus Itabaiana.

O compilador realiza todas as etapas clássicas de compilação:

1. **Análise Léxica** - Tokenização do código fonte
2. **Análise Sintática** - Validação da estrutura gramatical
3. **Análise Semântica** - Verificação de tipos e escopos
4. **Geração de Código Intermediário** - Código de Três Endereços (C3E)

---

## ✨ Funcionalidades

### 🔍 Análise Léxica
- Reconhecimento de tokens (palavras-reservadas, identificadores, operadores)
- Tratamento de números (inteiros e reais)
- Suporte a strings e comentários
- Detecção de erros léxicos

### 🌳 Análise Sintática
- Validação da estrutura gramatical do Mini-Pascal
- Geração de Árvore Sintática Abstrata (AST)
- Suporte a estruturas de controle (IF, WHILE, FOR)
- Detecção de erros sintáticos com mensagens descritivas

### 🔬 Análise Semântica
- Tabela de símbolos com suporte a escopos aninhados
- Verificação de variáveis não declaradas
- Detecção de redeclarações
- Validação de tipos (INTEGER, REAL, STRING, BOOLEAN)

### 🛠️ Geração de Código Intermediário
- Código de Três Endereços (TAC - Three Address Code)
- Otimização de expressões aritméticas
- Geração de rótulos para estruturas de controle
- Salvamento em arquivos `.tac`

### 🎯 Interface Interativa
- Menu de linha de comando intuitivo
- Integração completa com Maven
- Demos interativas de análise semântica e geração de código
- Relatórios automáticos de testes

---

## 🏗️ Estrutura do Projeto

<img width="646" height="758" alt="image" src="https://github.com/user-attachments/assets/bea3e337-7469-43a1-8122-f1d9e8df7a74" />


compilador-minipascal/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Main.java                          # Classe principal com menu interativo
│   │   │   ├── MiniPascalCodeGenerator.java       # Gerador de código intermediário
│   │   │   ├── MiniPascalSemanticVisitor.java     # Analisador semântico
│   │   │   ├── SymbolTable.java                   # Tabela de símbolos
│   │   │   ├── Type.java                          # Enumeração de tipos
│   │   │   ├── SemanticException.java             # Exceção semântica
│   │   │   └── Instruction.java                   # Representação de instruções C3E
│   │   │
│   │   └── antlr4/
│   │       └── miniPascal.g4                      # Gramática ANTLR do Mini-Pascal
│   │
│   └── test/
│       └── java/
│           ├── MiniPascalLexerTest.java           # Testes do Lexer
│           └── MiniPascalParserTest.java          # Testes do Parser
│
├── test-reports/                                   # Relatórios de testes
│   ├── surefire-reports/                          # Relatórios JUnit
│   ├── tac-output/                                # Código intermediário gerado
│   ├── parse-trees/                               # Árvores de parse
│   ├── lexer-tokens/                              # Tokens do lexer
│   └── semantic-errors/                           # Erros semânticos
│
├── pom.xml                                         # Configuração Maven
├── README.md                                       # Este arquivo
├── README_ENTREGA1.md                             # Documentação Entrega 1
├── README_ENTREGA2.md                             # Documentação Entrega 2
├── README_ENTREGA3.md                             # Documentação Entrega 3
├── README_ENTREGA4.md                             # Documentação Entrega 4
└── README_ENTREGA5.md                             # Documentação Entrega 5





---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 25 | Linguagem de programação principal |
| **ANTLR** | 4.13.1 | Gerador de parser e lexer |
| **Maven** | 3.9+ | Gerenciador de dependências e build |
| **JUnit** | 5.10.0 | Framework de testes unitários |

---

## 🚀 Instalação e Execução

### Pré-requisitos

- **Java JDK 25** ou superior
- **Maven 3.9+**
- **Git** (para clonar o repositório)

### Passos de Instalação
```bash
# 1. Clonar o repositório
git clone https://github.com/seu-usuario/compilador-minipascal.git
cd compilador-minipascal

# 2. Gerar parser/lexer com ANTLR
mvn clean generate-sources

# 3. Compilar o projeto
mvn compile

# 4. Executar testes
mvn test

# 5. Executar o compilador
java -cp target/classes Main





Menu Interativo
Ao executar Main.java, você verá o seguinte menu:

╔════════════════════════════════════════════════════════╗
║   🚀 MINI-PASCAL COMPILADOR - IFS Itabaiana 2025.2    ║
║        Menu de Execução das Entregas                   ║
╚════════════════════════════════════════════════════════╝

📋 ESCOLHA UMA OPÇÃO:
════════════════════════════════════════════════════════
1 - Entrega 1: Gerar parser/lexer (mvn generate-sources)
2 - Entrega 2: Compilar projeto (mvn compile)
3 - Entrega 3: Rodar todos os testes (mvn test)
4 - Testes apenas do Lexer
5 - Testes apenas do Parser
6 - Limpar projeto (mvn clean)
7 - Demo Semântica (tabela de símbolos + escopos)
8 - Entrega 4: Geração de Código Intermediário (C3E)
9 - 📂 Ver localização dos relatórios de testes
0 - Sair




📦 Entregas do Projeto
📅 Cronograma de Entregas

## 📅 Cronograma de Entregas

| # | Entrega | Data Limite | Descrição | Artefatos Principais |
|---|---------|-------------|-----------|---------------------|
| **1** | **Gramática da Linguagem (BNF)** | 14/01/2026 | Definição formal da Mini-Linguagem utilizando notação BNF/EBNF | • Arquivo `miniPascal.g4`<br>• Exemplos de códigos válidos/inválidos<br>• Documentação da gramática |
| **2** | **Analisador Léxico (Scanner)** | 21/01/2026 | Módulo de tokenização do código fonte | • `miniPascalLexer` (gerado pelo ANTLR)<br>• `MiniPascalLexerTest.java`<br>• Relatórios de tokens |
| **3** | **Analisador Sintático e Semântico** | 28/01/2026 | Validação sintática (AST) e verificação semântica (tipos, escopos) | • `miniPascalParser` (gerado pelo ANTLR)<br>• `MiniPascalSemanticVisitor.java`<br>• `SymbolTable.java`<br>• `MiniPascalParserTest.java` |
| **4** | **Gerador de Código Intermediário** | 04/02/2026 | Tradução da AST para Código de Três Endereços (C3E) | • `MiniPascalCodeGenerator.java`<br>• `Instruction.java`<br>• Arquivos `.tac` gerados |
| **5** | **Ajustes Finais e Apresentação** | 11/02/2026 | Compilador completo com documentação e apresentação ao vivo | • README.md completo<br>• Documentação de uso<br>• Apresentação funcional |

---

## 📋 Detalhamento das Entregas

### 📌 Entrega 1: Gramática da Linguagem (BNF)
**Data:** 14/01/2026

**Objetivo:** Definir as "regras do jogo" da linguagem Mini-Pascal.

**O que entregar:**
- Definição formal utilizando BNF (Backus-Naur Form) ou EBNF
- Especificação de estruturas: `if`, `while`, `for`, operadores, precedências
- Exemplos de códigos **válidos** e **inválidos**

**Artefatos:**
- `src/main/antlr4/miniPascal.g4` - Gramática ANTLR
- Documentação explicativa da gramática
- Exemplos de teste

**Conceitos Abordados:**
- Regras sintáticas da linguagem
- Precedência de operadores matemáticos
- Estruturas de controle (condicionais, laços)

---

### 📌 Entrega 2: Analisador Léxico (Scanner)
**Data:** 21/01/2026

**Objetivo:** Transformar caracteres em tokens (unidades léxicas).

**O que entregar:**
- Módulo capaz de ler arquivo fonte e gerar sequência de tokens
- Reconhecimento de palavras-reservadas, identificadores, operadores, literais
- Tratamento de espaços em branco e comentários

**Artefatos:**
- `target/generated-sources/antlr4/miniPascalLexer.java` (gerado pelo ANTLR)
- `src/test/java/MiniPascalLexerTest.java` - Testes unitários
- `test-reports/lexer-tokens/` - Relatórios de tokenização

**Requisitos:**
- ✅ Testes unitários verificando tokenização correta
- ✅ Detecção de erros léxicos

**Conceito:**
> "O computador não lê palavras, ele lê caracteres. O analisador agrupa `i`, `n`, `t` no token `PALAVRA_RESERVADA_INT` e ignora espaços em branco e comentários."

---

### 📌 Entrega 3: Analisador Sintático e Semântico (Parser)
**Data:** 28/01/2026

**Objetivo:** Validar estrutura gramatical e verificar consistência semântica.

**O que entregar:**

**1. Analisador Sintático:**
- Validação da sequência de tokens conforme gramática BNF
- Geração de Árvore Sintática Abstrata (AST)
- Detecção de erros sintáticos com mensagens descritivas

**2. Analisador Semântico:**
- Verificação de tipos (compatibilidade de operações)
- Validação de declaração de variáveis antes do uso
- Controle de escopos (variáveis locais vs. globais)

**Artefatos:**
- `target/generated-sources/antlr4/miniPascalParser.java` (gerado pelo ANTLR)
- `src/main/java/MiniPascalSemanticVisitor.java` - Visitor semântico
- `src/main/java/SymbolTable.java` - Tabela de símbolos
- `src/main/java/Type.java` - Enumeração de tipos
- `src/main/java/SemanticException.java` - Exceção customizada
- `src/test/java/MiniPascalParserTest.java` - Testes unitários
- `test-reports/parse-trees/` - Árvores sintáticas geradas

**Requisitos:**
- ✅ Erros sintáticos: "Faltou ponto e vírgula na linha 10"
- ✅ Erros semânticos: "Tipos incompatíveis" ou "Variável 'x' não declarada"

**Exemplos de Verificação Semântica:**
- ❌ "Estou tentando somar um número com um texto?"
- ❌ "A variável `x` foi declarada antes de ser usada?"

---

### 📌 Entrega 4: Gerador de Código Intermediário
**Data:** 04/02/2026

**Objetivo:** Traduzir AST para representação linear e simplificada (agnóstica de máquina).

**O que entregar:**
- Gerador de Código de Três Endereços (Three-Address Code - TAC)
- Tradução de expressões aritméticas, estruturas de controle e I/O
- Salvamento em arquivos `.tac`

**Artefatos:**
- `src/main/java/MiniPascalCodeGenerator.java` - Gerador de código
- `src/main/java/Instruction.java` - Representação de instruções C3E
- `test-reports/tac-output/*.tac` - Arquivos de código intermediário gerado

**Exemplo de Tradução:**

**Código Mini-Pascal:**
```pascal
x := a + b * c;




📌 Entrega 5: Ajustes Finais e ApresentaçãoData: 11/02/2026Objetivo: Compilador completo, polido e documentado.O que entregar:
Compilador funcional com todas as etapas integradas
Documentação completa de uso (README.md)
Apresentação ao vivo demonstrando funcionamento
Artefatos:
README.md - Documentação principal
README_ENTREGA1.md até README_ENTREGA5.md - Documentação detalhada
Main.java - Interface de linha de comando
Apresentação funcional (ao vivo)

Atividade de Apresentação:
✅ Demonstração do compilador funcionando ao vivo
✅ Recebimento de código fonte Mini-Pascal
✅ Geração de código intermediário (C3E)
✅ Explicação das etapas de compilação

Requisitos de Documentação:
Manual de instalação e uso
Exemplos de código Mini-Pascal
Explicação da arquitetura do compilador
Instruções de execução dos testes




📂 Localização dos Arquivos de Entrega

Entrega 1 - Análise Léxica e Sintática
Gramática: src/main/antlr4/miniPascal.g4
Arquivos Gerados: target/generated-sources/antlr4/

Entrega 2 - Testes
Testes do Lexer: src/test/java/MiniPascalLexerTest.java
Testes do Parser: src/test/java/MiniPascalParserTest.java
Relatórios: test-reports/surefire-reports/

Entrega 3 - Análise Semântica
Visitor Semântico: src/main/java/MiniPascalSemanticVisitor.java
Tabela de Símbolos: src/main/java/SymbolTable.java
Tipos: src/main/java/Type.java
Exceção: src/main/java/SemanticException.java

Entrega 4 - Geração de Código
Gerador: src/main/java/MiniPascalCodeGenerator.java
Instruções: src/main/java/Instruction.java
Saída: test-reports/tac-output/*.tac

Entrega 5 - Documentação
README Principal: README.md
READMEs de Entrega: README_ENTREGA*.md
Apresentação: docs/apresentacao.pdf (a ser criado)

💡 Exemplos de UsoExemplo 1:
Programa Simples
Código Mini-Pascal:
program ExemploBasico;
var x, y: integer;
begin
  x := 10 + 5 * 2;
  if x > 15 then 
    y := 1 
  else 
    y := 0;
end.

Código Intermediário Gerado (C3E):
t0 := 5 * 2
t1 := 10 + t0
x := t1
t2 := x > 15
IF_FALSE t2 goto L0
y := 1
goto L1
L0:
y := 0
L1:


Exemplo 2: Laço WHILE
Código Mini-Pascal:
program ExemploWhile;
var i: integer;
begin
  i := 0;
  while i < 10 do
    i := i + 1;
end.

Código Intermediário Gerado (C3E):
i := 0
L0:
t0 := i < 10
IF_FALSE t0 goto L1
t1 := i + 1
i := t1
goto L0
L1:


Exemplo 3: Erro Semântico
Código Mini-Pascal:
program ErroSemantico;
begin
  x := 10;  // Erro: variável 'x' não declarada
end.

Saída:
❌ ERRO SEMÂNTICO: Variavel 'x' nao declarada



📊 Estrutura de Diretórios de Relatórios
test-reports/
├── surefire-reports/          # Relatórios JUnit (XML/TXT)
│   ├── MiniPascalLexerTest.txt
│   ├── MiniPascalParserTest.txt
│   └── TEST-*.xml
│
├── tac-output/                # Código Intermediário (.tac)
│   ├── teste1_output.tac
│   ├── teste2_output.tac
│   └── teste3_output.tac
│
├── parse-trees/               # Árvores de Parse (.txt)
│   ├── teste1_programa_valido_tree.txt
│   ├── teste2_if_else_tree.txt
│   └── ...
│
├── lexer-tokens/              # Tokens do Lexer (.txt)
│   ├── teste1_tokens_simples.txt
│   └── ...
│
└── semantic-errors/           # Erros Semânticos (.txt)
    └── test_parser_output.txt




🎯 Resumo de Artefatos por Entrega

EntregaArquivos PrincipaisLocalização1miniPascal.g4src/main/antlr4/2MiniPascalLexerTest.javasrc/test/java/3MiniPascalSemanticVisitor.javaSymbolTable.javaMiniPascalParserTest.javasrc/main/java/src/test/java/4MiniPascalCodeGenerator.javaInstruction.javasrc/main/java/5README.mdMain.javaTodos os READMEs de entregaRaiz do projetosrc/main/java/


📊 Progresso das Entregas
 Entrega 1 - Gramática da Linguagem (14/01/2026)
 Entrega 2 - Analisador Léxico (21/01/2026)
 Entrega 3 - Analisador Sintático e Semântico (28/01/2026)
 Entrega 4 - Gerador de Código Intermediário (04/02/2026)
 Entrega 5 - Ajustes Finais e Apresentação (11/02/2026)




👥 Autores
* # Enaldo Dantas  - jose.santos259@academico.ifs.edu.br    
* # Agnaldo Neto   - jose.neto044@academico.ifs.edu.br      
* # Vitorio Mota   - vitorio.mota085@academico.ifs.edu.br  
* # Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br 

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE [blocked] para mais detalhes.

🙏 Agradecimentos
Professor Marlos Tacio Silva 
Comunidade ANTLR
Instituto Federal de Sergipe - Campus Itabaiana

📞 ContatoPara dúvidas ou sugestões:
Email: [jeds@outlook.com.br]
GitHub: [https://github.com/jeds2025]

Desenvolvido com ❤️ no IFS Itabaiana - 2025.2
