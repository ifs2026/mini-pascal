# 📘 Entrega 4: Gerador de Código Intermediário

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

| Item | Descrição |
|------|-----------|
| **Data de Entrega** | 04/02/2026 |
| **Objetivo** | Traduzir AST para representação intermediária (Three-Address Code) |
| **Status** | ✅ Concluído |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Conceito](#conceito)
- [Artefatos Entregues](#artefatos-entregues)
- [Código de Três Endereços](#código-de-três-endereços)
- [Gerador de Código Implementado](#gerador-de-código-implementado)
- [Otimizações Aplicadas](#otimizações-aplicadas)
- [Exemplos de Tradução](#exemplos-de-tradução)
- [Testes Implementados](#testes-implementados)
- [Como Executar](#como-executar)
- [Relatórios Gerados](#relatórios-gerados)

---

## 🎯 Objetivo

Esta entrega implementa o **Gerador de Código Intermediário**, responsável por:

### Funcionalidades Principais

- ✅ Traduzir a Árvore Sintática Abstrata (AST) para **Código de Três Endereços (Three-Address Code - TAC)**.
- ✅ Gerar representação linear e simplificada, **agnóstica de máquina**.
- ✅ Preparar o código para otimizações futuras.
- ✅ Criar base para tradução final para Assembly/Código de Máquina.

---

## 💡 Conceito

### O que é Código Intermediário?

> "O código intermediário é uma representação abstrata do programa, situada entre o código-fonte de alto nível e o código de máquina de baixo nível. Ele facilita otimizações e portabilidade entre diferentes arquiteturas."

**Vantagens do Código Intermediário:**

| Vantagem | Descrição |
|----------|-----------|
| **Portabilidade** | Mesma representação para diferentes arquiteturas (x86, ARM, RISC-V). |
| **Otimização** | Facilita a análise e transformações de código para ganho de performance. |
| **Simplicidade** | Instruções atômicas e lineares, sem estruturas de controle complexas. |
| **Modularidade** | Separa a análise semântica da geração de código de máquina final. |

### Código de Três Endereços (TAC)

Cada instrução TAC contém **no máximo três operandos**: dois operandos de entrada e um de saída. Expressões complexas são quebradas em instruções simples usando variáveis temporárias.

**Formato Geral:**
`resultado = operando1 operador operando2`

**Exemplo:**
```pascal
// Código Mini-Pascal
x := a + b * c;

// Código de Três Endereços (TAC)
t0 = b * c
t1 = a + t0
x = t1


📦 Artefatos Entregues
### Arquivos Principais

| Arquivo | Localização | Descrição |
| :--- | :--- | :--- |
| `MiniPascalCodeGenerator.java` | `src/main/java/` | Gerador de código (Visitor) que traduz a AST para TAC. |
| `Instruction.java` | `src/main/java/` | Representação de uma instrução TAC (result, arg1, op, arg2). |
| `Main.java` | `src/main/java/` | Ponto de entrada com menu interativo atualizado. |
| `README_ENTREGA4.md` | Raiz do projeto | Este documento de documentação. |

### Arquivos Gerados (Evidências)

| Arquivo | Localização | Descrição |
| :--- | :--- | :--- |
| `teste1_output.tac` | `test-reports/tac-output/` | Código TAC gerado para expressões aritméticas. |
| `teste2_output.tac` | `test-reports/tac-output/` | Código TAC gerado para estruturas If-Else. |
| `teste3_output.tac` | `test-reports/tac-output/` | Código TAC gerado para laços While. |

🔧 Código de Três Endereços
### Tipos de Instruções TAC Suportadas

| Tipo | Formato | Exemplo | Descrição |
| :--- | :--- | :--- | :--- |
| **Atribuição Binária** | `x = y op z` | `t1 = a + b` | Operações aritméticas, lógicas e relacionais. |
| **Atribuição Unária** | `x = op y` | `t1 = not a` | Operadores `NOT`, `+` e `-` unários. |
| **Cópia (Simples)** | `x = y` | `x = t1` | Atribuição direta de valor ou variável. |
| **Salto Incondicional** | `goto L` | `goto L1` | Desvio direto para um rótulo. |
| **Salto Condicional** | `if x goto L` | `if t0 goto L2` | Pula para o rótulo se a condição for verdadeira. |
| **Rótulo (Label)** | `L:` | `L1:` | Marcador de posição para saltos. |
| **Entrada (Read)** | `read x` | `read idade` | Leitura de dados do usuário. |
| **Saída (Print)** | `print x` | `print t1` | Escrita de dados na tela. |


🛠️ Gerador de Código Implementado
Estrutura do Gerador

Arquivo: src/main/java/MiniPascalCodeGenerator.java

O gerador utiliza o Visitor Pattern para percorrer a AST. Para cada nó da árvore, ele emite uma ou mais instruções TAC.

Temporárias: Geradas automaticamente como t0, t1, t2... através do método newTemp().
Rótulos: Gerados como L0, L1, L2... através do método newLabel(), essenciais para IF, WHILE e FOR.

Codigo do Gerador
=
=
=
=
=
=
=
=
=
=
=
=

🚀 Como Executar

Passo 1: Compilar o Projeto
mvn clean compile

Passo 2: Executar o Menu Interativo
mvn exec:java
ou
java -cp target/classes Main

Passo 3: Gerar Código Intermediário (Demo)
No menu principal, selecione a Opção 8:

ESCOLHA UMA OPÇÃO:
...
7 - Demo Semântica (tabela de símbolos + escopos)
8 - Entrega 4: Geração de Código Intermediário (C3E)
9 - Ver localização dos relatórios de testes
0 - Sair

Opção: 8

Saída Esperada (Exemplo):

╔════════════════════════════════════════════════════════╗
║   DEMO GERAÇÃO DE CÓDIGO INTERMEDIÁRIO (C3E)           ║
╚════════════════════════════════════════════════════════╝

EXECUTANDO TESTE 1:
Código de entrada:
program Exemplo;
var x, y: integer;
begin
  x := 10 + 5 * 2;
  print(x);
end.

Código Intermediário Gerado:
t0 = 5 * 2
t1 = 10 + t0
x = t1
print x

✅ Geração concluída com sucesso!
📄 Arquivos salvos em: test-reports/tac-output/

📂 Relatórios Gerados
Localização dos Arquivos

test-reports/
├── tac-output/                     # Código TAC gerado
    ├── teste1_output.tac
    ├── teste2_output.tac
    └── teste3_output.tac

Exemplo de Arquivo TAC
Arquivo: test-reports/tac-output/teste2_output.tac

x := 5
y := 10
t0 := x + 5
t1 := y - 2
t2 := t0 * t1
resultado := t2
L0:
t3 := resultado < 100
if t3 FALSE  goto L1
t4 := resultado + 1
resultado := t4
goto L0
L1:

🧪 Testes Implementados

| Teste | Descrição | Status |
| :--- | :--- | :--- |
| `testAtribuicao` | Valida TAC para x := 10. | ✅ Passou |
| `testExpressoes` | Valida precedência em a + b * c. | ✅ Passou |
| `testIfElse` | Valida saltos if e goto em condicionais. | ✅ Passou |
| `testWhile` | Valida estrutura de repetição com rótulos. | ✅ Passou |
| `testFor` | Valida laços to e downto. | ✅ Passou |
| `testIO` | Valida instruções read e print. | ✅ Passou |


📊 Resumo da Entrega

| Item | Status |
| :--- | :--- |
| Gerador TAC Implementado | ✅ Completo |
| Suporte a Estruturas de Controle | ✅ IF, WHILE, FOR |
| Gerenciamento de Temporárias | ✅ Implementado |
| Gerenciador de Rótulos | ✅ Implementado |
| Relatórios em Arquivo | ✅ Gerados em .tac |
| Documentação | ✅ Completa |


📚 Referências
Dragon Book: Compilers: Principles, Techniques, and Tools (Aho, Sethi, Ullman) - Capítulo 6 (Intermediate-Code Generation)
Engineering a Compiler: Keith Cooper & Linda Torczon - Capítulo 5 (Intermediate Representations)
Modern Compiler Implementation in Java: Andrew W. Appel - Capítulo 7 (Translation to Intermediate Code)


## 📝 Histórico de Versões

| Versão | Data | Descrição | Autor |
|--------|------|-----------|-------|
| 1.0 | 04/02/2026 | Versão inicial da entrega 4 | Equipe Mini-Pascal |


👥 Autores
Enaldo Dantas  - jose.santos259@academico.ifs.edu.br
Agnaldo Neto   - jose.neto044@academico.ifs.edu.br
Vitorio Mota   - vitorio.mota085@academico.ifs.edu.br
Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br


📞 ContatoPara dúvidas sobre esta entrega:
Email: jeds@outlook.com.br
GitHub: https://github.com/jeds2025

Entrega 4 concluída com sucesso! ✅
Desenvolvido para IFS Campus Itabaiana - 2025.2
Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES



