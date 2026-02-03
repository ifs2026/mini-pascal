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

import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe percorre a árvore gerada pelo ANTLR e traduz os comandos para
 * Código de Três Endereços (C3E). Cada método 'visit' lida com uma regra da gramática.
 */
public class MiniPascalCodeGenerator extends miniPascalBaseVisitor<String> {

    private final List<Instruction> instructions = new ArrayList<>();
    
    private int tempCount = 0;
    private int labelCount = 0;

    private String newTemp() {
        return "t" + (tempCount++);
    }

    private String newLabel() {
        return "L" + (labelCount++);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void printInstructions() {
        System.out.println("\n=== CÓDIGO INTERMEDIÁRIO GERADO (C3E) ===");
        for (Instruction instr : instructions) {
            System.out.println(instr);
        }
    }

    // === ATRIBUIÇÃO: x := expressão ===

    @Override
    public String visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String variable = ctx.identifier().getText(); // Identifica o destino (lado esquerdo)
        String value = visit(ctx.expression());       // Resolve a expressão (lado direito)
        
        instructions.add(new Instruction(variable, value, null, null));
        return variable;
    }

    // === EXPRESSÕES RELACIONAIS (ex: x > 15) ===

    @Override
    public String visitExpression(miniPascalParser.ExpressionContext ctx) {

        if (ctx.relationalOperator() != null && !ctx.relationalOperator().isEmpty()) {
            String left = visit(ctx.simpleExpression(0));
            String op = ctx.relationalOperator(0).getText(); 
            String right = visit(ctx.simpleExpression(1));
            
            String target = newTemp(); // Cria temporário para armazenar o resultado booleano
            instructions.add(new Instruction(target, left, op, right)); // Ex: t2 := x > 15
            return target;
        }

        return visit(ctx.simpleExpression(0));
    }

    // === EXPRESSÕES ARITMÉTICAS (Soma, Subtração, OR) ===

    @Override
    public String visitSimpleExpression(miniPascalParser.SimpleExpressionContext ctx) {
        String lastResult = visit(ctx.term(0)); // Pega o primeiro termo
        
        for (int i = 0; i < ctx.additiveOperator().size(); i++) {
            String op = ctx.additiveOperator(i).getText();
            String nextTerm = visit(ctx.term(i + 1));
            String target = newTemp();
            
            instructions.add(new Instruction(target, lastResult, op, nextTerm));
            lastResult = target;
        }
        return lastResult;
    }

    // === OPERAÇÕES DE ALTA PRECEDÊNCIA (Multiplicação, Divisão, AND) ===

    @Override
    public String visitTerm(miniPascalParser.TermContext ctx) {
        String lastResult = visit(ctx.factor(0));
        
        for (int i = 0; i < ctx.multiplicativeOperator().size(); i++) {
            String op = ctx.multiplicativeOperator(i).getText();
            String nextFactor = visit(ctx.factor(i + 1));
            String target = newTemp();
            
            instructions.add(new Instruction(target, lastResult, op, nextFactor));
            lastResult = target;
        }
        return lastResult;
    }

    // === FATORES (Base da expressão: números, variáveis, expressões entre parênteses) ===

    @Override
    public String visitFactor(miniPascalParser.FactorContext ctx) {

        if (ctx.identifier() != null) {
            return ctx.identifier().getText();
        }
        
        if (ctx.unsignedNumber() != null) {
            return ctx.unsignedNumber().getText();
        }
        
        if (ctx.stringLiteral() != null) {
            return ctx.stringLiteral().getText();
        }
        
        if (ctx.boolLiteral() != null) {
            return ctx.boolLiteral().getText();
        }
        
        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }
        
        if (ctx.NOT() != null) {
            String operand = visit(ctx.getChild(1)); // Pega o factor após NOT
            String target = newTemp();
            instructions.add(new Instruction(target, operand, "NOT", null));
            return target;
        }
        
        if (ctx.PLUS() != null) {
            return visit(ctx.getChild(1)); // Pega o factor após +
        }
        
        if (ctx.MINUS() != null) {
            String operand = visit(ctx.getChild(1)); // Pega o factor após -
            String target = newTemp();
            instructions.add(new Instruction(target, "0", "-", operand));
            return target;
        }

        return ctx.getText();
    }

    // === CONTROLE DE FLUXO: IF THEN ELSE ===

    @Override
    public String visitIfStatement(miniPascalParser.IfStatementContext ctx) {
        String condition = visit(ctx.expression()); // Avalia a condição booleana
        String labelElse = newLabel(); // Rótulo para onde pular se for falso
        String labelEnd = newLabel();  // Rótulo para o fim do IF

        instructions.add(new Instruction(labelElse, condition, "IF_FALSE", null));
        
        visit(ctx.statement(0)); // Executa o bloco do THEN
        instructions.add(new Instruction(labelEnd, null, "GOTO", null)); // Pula o ELSE
        
        instructions.add(new Instruction(labelElse, null, "LABEL", null)); // Marca o início do ELSE
        if (ctx.statement().size() > 1) {
            visit(ctx.statement(1)); // Executa o bloco ELSE se existir
        }
        instructions.add(new Instruction(labelEnd, null, "LABEL", null)); // Marca o fim de tudo
        
        return null;
    }

    // === LAÇO DE REPETIÇÃO: WHILE DO ===

    @Override
    public String visitWhileStatement(miniPascalParser.WhileStatementContext ctx) {
        String labelStart = newLabel(); // Rótulo para voltar ao teste da condição
        String labelEnd = newLabel();   // Rótulo para sair do laço

        instructions.add(new Instruction(labelStart, null, "LABEL", null)); // Início do loop
        String condition = visit(ctx.expression()); // Testa a condição
        
        instructions.add(new Instruction(labelEnd, condition, "IF_FALSE", null)); // Sai se for falso
        visit(ctx.statement()); // Executa o corpo do loop
        
        instructions.add(new Instruction(labelStart, null, "GOTO", null)); // Volta ao topo
        instructions.add(new Instruction(labelEnd, null, "LABEL", null)); // Alvo da saída
        
        return null;
    }

    // === LAÇO DE REPETIÇÃO: FOR TO/DOWNTO ===

    @Override
    public String visitForStatement(miniPascalParser.ForStatementContext ctx) {
        String var = ctx.identifier().getText();
        String start = visit(ctx.expression(0));
        String end = visit(ctx.expression(1));
        
        instructions.add(new Instruction(var, start, null, null));
        
        String labelStart = newLabel();
        String labelEnd = newLabel();
        
        instructions.add(new Instruction(labelStart, null, "LABEL", null));
        
        String condition = newTemp();
        String op = ctx.TO() != null ? ">" : "<";
        instructions.add(new Instruction(condition, var, op, end));
        instructions.add(new Instruction(labelEnd, condition, "IF_TRUE", null));
        
        visit(ctx.statement());
        
        String temp = newTemp();
        String incOp = ctx.TO() != null ? "+" : "-";
        instructions.add(new Instruction(temp, var, incOp, "1"));
        instructions.add(new Instruction(var, temp, null, null));
        
        instructions.add(new Instruction(labelStart, null, "GOTO", null));
        
        instructions.add(new Instruction(labelEnd, null, "LABEL", null));
        
        return null;
    }

    // === ENTRADA E SAÍDA (READ e WRITE/PRINT) ===

    @Override
    public String visitIoStatement(miniPascalParser.IoStatementContext ctx) {
        if (ctx.READ() != null) {
            for (miniPascalParser.IdentifierContext idCtx : ctx.identifier()) {
                instructions.add(new Instruction("READ", idCtx.getText(), null, null));
            }
        } else if (ctx.WRITE() != null || ctx.PRINT() != null) {
            for (miniPascalParser.ExpressionContext exprCtx : ctx.expression()) {
                String val = visit(exprCtx); 
                instructions.add(new Instruction("PRINT", val, null, null));
            }
        }
        return null;
    }

    public void saveToFile(String filename) {
        String outputDir = "test-reports/tac-output/";
        java.io.File dir = new java.io.File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria pasta se não existir
        }
        
        String fullPath = outputDir + filename;
        
        try (java.io.FileWriter writer = new java.io.FileWriter(fullPath, false)) {
            writer.write("=== CÓDIGO INTERMEDIÁRIO GERADO (C3E) ===\n");
            writer.write("Data/Hora: " + java.time.LocalDateTime.now() + "\n");
            writer.write("Total de Instruções: " + instructions.size() + "\n");
            writer.write("===========================================\n\n");
            
            for (Instruction instr : instructions) {
                writer.write(instr.toString() + "\n");
            }
            
            System.out.println(" Código salvo em: " + fullPath);
        } catch (java.io.IOException e) {
            System.err.println(" Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}


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



