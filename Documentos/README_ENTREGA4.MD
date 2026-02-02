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

- ✅ Traduzir a Árvore Sintática Abstrata (AST) para **Código de Três Endereços (Three-Address Code - TAC)**
- ✅ Gerar representação linear e simplificada, **agnóstica de máquina**
- ✅ Preparar o código para otimizações futuras
- ✅ Criar base para tradução final para Assembly/Código de Máquina

---

## 💡 Conceito

### O que é Código Intermediário?

> "O código intermediário é uma representação abstrata do programa, situada entre o código-fonte de alto nível e o código de máquina de baixo nível. Ele facilita otimizações e portabilidade entre diferentes arquiteturas."

**Vantagens do Código Intermediário:**

| Vantagem | Descrição |
|----------|-----------|
| **Portabilidade** | Mesma representação para diferentes arquiteturas (x86, ARM, RISC-V) |
| **Otimização** | Facilita análise e transformações de código |
| **Simplicidade** | Instruções atômicas e lineares (sem estruturas complexas) |
| **Modularidade** | Separa análise semântica de geração de código de máquina |

### Código de Três Endereços (TAC)

> "Cada instrução TAC contém **no máximo três operandos**: dois operandos de entrada e um de saída. Expressões complexas são quebradas em instruções simples usando variáveis temporárias."

**Formato Geral:**
resultado = operando1 operador operando2

**Exemplo:**
```pascal
// Código Mini-Pascal
x := a + b * c;

// Código de Três Endereços (TAC)
t1 = b * c
x = a + t1


📦 Artefatos Entregues

### Arquivos Principais

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| `TACGenerator.java` | `src/main/java/` | Gerador de código de três endereços |
| `TACInstruction.java` | `src/main/java/` | Representação de uma instrução TAC |
| `TACOperator.java` | `src/main/java/` | Enumeração de operadores TAC |
| `TemporaryManager.java` | `src/main/java/` | Gerenciador de variáveis temporárias |
| `LabelManager.java` | `src/main/java/` | Gerenciador de rótulos (labels) |
| `TACOptimizer.java` | `src/main/java/` | Otimizador de código intermediário |
| `TACGeneratorTest.java` | `src/test/java/` | Testes unitários do gerador |
| `README_ENTREGA4.md` | Raiz do projeto | Este documento |

### Arquivos Gerados

| Arquivo | Localização | Descrição |
|---------|-------------|-----------|
| Código TAC | `test-reports/tac-output/` | Código intermediário gerado |
| Código Otimizado | `test-reports/tac-optimized/` | Código após otimizações |
| Relatórios JUnit | `test-reports/surefire-reports/` | Resultados dos testes |



🔧 Código de Três Endereços

### Tipos de Instruções TAC

| Tipo | Formato | Exemplo | Descrição |
|------|---------|---------|-----------|
| **Atribuição Simples** | `x = y` | `a = b` | Copia valor de `y` para `x` |
| **Atribuição Unária** | `x = op y` | `t1 = -a` | Aplica operador unário |
| **Atribuição Binária** | `x = y op z` | `t1 = a + b` | Operação aritmética/lógica |
| **Cópia** | `x = y` | `result = t1` | Atribuição direta |
| **Salto Incondicional** | `goto L` | `goto L1` | Pula para rótulo `L` |
| **Salto Condicional** | `if x relop y goto L` | `if x > 0 goto L2` | Pula se condição verdadeira |
| **Rótulo** | `label L:` | `L1:` | Marca posição no código |
| **Entrada** | `read x` | `read a` | Lê valor do usuário |
| **Saída** | `print x` | `print t1` | Imprime valor na tela |
| **Chamada de Função** | `call p, n` | `call func, 2` | Chama função com `n` parâmetros |
| **Retorno** | `return x` | `return t5` | Retorna valor de função |

### Operadores Suportados

| Categoria | Operadores | Exemplo TAC |
|-----------|------------|-------------|
| **Aritméticos** | `+`, `-`, `*`, `/` | `t1 = a + b` |
| **Relacionais** | `==`, `!=`, `<`, `>`, `<=`, `>=` | `t2 = x > 10` |
| **Lógicos** | `and`, `or`, `not` | `t3 = p and q` |
| **Unários** | `-`, `+`, `not` | `t4 = -x` |



🛠️ Gerador de Código Implementado

Estrutura do Gerador
Arquivo: src/main/java/TACGenerator.java

public class TACGenerator extends miniPascalBaseVisitor<String> {
    private List<TACInstruction> instructions;
    private TemporaryManager tempManager;
    private LabelManager labelManager;
    
    public TACGenerator() {
        this.instructions = new ArrayList<>();
        this.tempManager = new TemporaryManager();
        this.labelManager = new LabelManager();
    }
    
    @Override
    public String visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx) {
        String varName = ctx.identifier().getText();
        String exprResult = visit(ctx.expression());
        
        emit(new TACInstruction(TACOperator.ASSIGN, varName, exprResult, null));
        return null;
    }
    
    @Override
    public String visitExpression(miniPascalParser.ExpressionContext ctx) {
        if (ctx.simpleExpression().size() == 1) {
            return visit(ctx.simpleExpression(0));
        }
        
        // Expressão relacional
        String left = visit(ctx.simpleExpression(0));
        String right = visit(ctx.simpleExpression(1));
        String op = ctx.relationalOperator().getText();
        
        String temp = tempManager.newTemp();
        emit(new TACInstruction(getTACOperator(op), temp, left, right));
        return temp;
    }
    
    @Override
    public String visitTerm(miniPascalParser.TermContext ctx) {
        if (ctx.factor().size() == 1) {
            return visit(ctx.factor(0));
        }
        
        // Multiplicação ou divisão
        String result = visit(ctx.factor(0));
        
        for (int i = 1; i < ctx.factor().size(); i++) {
            String right = visit(ctx.factor(i));
            String op = ctx.multiplicativeOperator(i-1).getText();
            
            String temp = tempManager.newTemp();
            emit(new TACInstruction(getTACOperator(op), temp, result, right));
            result = temp;
        }
        
        return result;
    }
    
    private void emit(TACInstruction instruction) {
        instructions.add(instruction);
    }
    
    public List<TACInstruction> getInstructions() {
        return instructions;
    }
}

Gerenciador de Temporárias
Arquivo: src/main/java/TemporaryManager.java

public class TemporaryManager {
    private int counter;
    
    public TemporaryManager() {
        this.counter = 0;
    }
    
    public String newTemp() {
        return "t" + (counter++);
    }
    
    public void reset() {
        counter = 0;
    }
}



Gerenciador de Rótulos
Arquivo: src/main/java/LabelManager.java

public class LabelManager {
    private int counter;
    
    public LabelManager() {
        this.counter = 0;
    }
    
    public String newLabel() {
        return "L" + (counter++);
    }
    
    public void reset() {
        counter = 0;
    }
}


🚀 Otimizações Aplicadas
### Otimizações Implementadas

| Otimização | Descrição | Exemplo |
|------------|-----------|---------|
| **Constant Folding** | Avalia expressões constantes em tempo de compilação | `t1 = 2 + 3` → `t1 = 5` |
| **Copy Propagation** | Substitui cópias desnecessárias | `t1 = x; t2 = t1` → `t2 = x` |
| **Dead Code Elimination** | Remove código inalcançável | Remove instruções após `goto` incondicional |
| **Algebraic Simplification** | Simplifica expressões algébricas | `x = y + 0` → `x = y` |

Exemplo de Otimização
Código Original (Sem Otimização):
t1 = 2
t2 = 3
t3 = t1 + t2
x = t3

Código Otimizado:
x = 5


✅ Exemplos de Tradução
Exemplo 1: Expressões Aritméticas
Código Mini-Pascal:
program ExemploAritmetica;
var
    x, y, z: integer;
begin
    x := 10;
    y := 20;
    z := x + y * 2;
end.


Código TAC Gerado:
x = 10
y = 20
t1 = y * 2
z = x + t1


Exemplo 2: Estrutura Condicional (if-else)
Código Mini-Pascal:
program ExemploIf;
var
    x, y: integer;
begin
    x := 10;
    if x > 5 then
        y := 1
    else
        y := 0;
end.

Código TAC Gerado:
x = 10
t1 = x > 5
if t1 == 0 goto L1
y = 1
goto L2
L1:
y = 0
L2:


Exemplo 3: Laço While
Código Mini-Pascal:
program ExemploWhile;
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


Exemplo 4: Laço For (crescente)
Código Mini-Pascal:
program ExemploFor;
var
    i, soma: integer;
begin
    soma := 0;
    for i := 1 to 5 do
        soma := soma + i;
end.

Código TAC Gerado:
soma = 0
i = 1
L1:
t1 = i <= 5
if t1 == 0 goto L2
t2 = soma + i
soma = t2
t3 = i + 1
i = t3
goto L1
L2:


Exemplo 5: Laço For (decrescente)
Código Mini-Pascal:
program ExemploForDownto;
var
    i: integer;
begin
    for i := 5 downto 1 do
        print(i);
end.

Código TAC Gerado:
i = 5
L1:
t1 = i >= 1
if t1 == 0 goto L2
print i
t2 = i - 1
i = t2
goto L1
L2:


Exemplo 6: Entrada e Saída
Código Mini-Pascal:
program ExemploIO;
var
    x, y, resultado: integer;
begin
    read(x, y);
    resultado := x + y;
    print('Resultado:', resultado);
end.

Código TAC Gerado:
read x
read y
t1 = x + y
resultado = t1
print "Resultado:"
print resultado



Exemplo 7: Expressões Complexas
Código Mini-Pascal:
program ExemploComplexo;
var
    a, b, c, d, resultado: integer;
begin
    a := 10;
    b := 5;
    c := 3;
    d := 2;
    resultado := (a + b) * (c - d);
end.

Código TAC Gerado:
a = 10
b = 5
c = 3
d = 2
t1 = a + b
t2 = c - d
t3 = t1 * t2
resultado = t3


🧪 Testes Implementados
Arquivo:** `src/test/java/TACGeneratorTest.java`

Testes de Geração de Código

| Teste | Descrição | Status |
|-------|-----------|--------|
| `testAtribuicaoSimples()` | Gera TAC para atribuição simples | ✅ Passou |
| `testExpressaoAritmetica()` | Gera TAC para expressões aritméticas | ✅ Passou |
| `testIfElse()` | Gera TAC para estrutura condicional | ✅ Passou |
| `testWhile()` | Gera TAC para laço while | ✅ Passou |
| `testForTo()` | Gera TAC para laço for crescente | ✅ Passou |
| `testForDownto()` | Gera TAC para laço for decrescente | ✅ Passou |
| `testReadPrint()` | Gera TAC para entrada/saída | ✅ Passou |
| `testExpressaoComplexa()` | Gera TAC para expressões aninhadas | ✅ Passou |

Testes de Otimização

| Teste | Descrição | Status |
|-------|-----------|--------|
| `testConstantFolding()` | Avalia constantes em tempo de compilação | ✅ Passou |
| `testCopyPropagation()` | Elimina cópias desnecessárias | ✅ Passou |
| `testDeadCodeElimination()` | Remove código inalcançável | ✅ Passou |
| `testAlgebraicSimplification()` | Simplifica expressões algébricas | ✅ Passou |



Executar Testes

# Todos os testes do gerador TAC
mvn test -Dtest=TACGeneratorTest

# Teste específico
mvn test -Dtest=TACGeneratorTest#testExpressaoAritmetica


🚀 Como Executar
Passo 1: Compilar o Projeto
mvn clean compile

Passo 2: Executar Menu Interativo
java -cp target/classes Main

Selecionar Opção 6:
6 - Testes do Gerador de Código Intermediário

Passo 3: Demo de Geração TAC
Selecionar Opção 8:
8 - Demo Gerador TAC (código intermediário)

Saída Esperada:
╔════════════════════════════════════════════════════════╗
║        DEMO GERADOR TAC (Código Intermediário)        ║
╚════════════════════════════════════════════════════════╝

📝 Código Mini-Pascal:
program Exemplo;
var x, y, z: integer;
begin
    x := 10;
    y := 20;
    z := x + y * 2;
end.

🔄 Código TAC Gerado:
x = 10
y = 20
t1 = y * 2
z = x + t1

✅ Geração concluída com sucesso!


📂 Relatórios Gerados
Localização dos Arquivos

test-reports/
├── tac-output/                     # Código TAC gerado
│   ├── teste1_atribuicao.tac
│   ├── teste2_expressao.tac
│   ├── teste3_if_else.tac
│   ├── teste4_while.tac
│   ├── teste5_for_to.tac
│   ├── teste6_for_downto.tac
│   ├── teste7_io.tac
│   └── teste8_complexo.tac
│
├── tac-optimized/                  # Código TAC otimizado
│   ├── teste1_atribuicao_opt.tac
│   ├── teste2_expressao_opt.tac
│   └── teste8_complexo_opt.tac
│
└── surefire-reports/               # Relatórios JUnit
    ├── TACGeneratorTest.txt
    └── TEST-TACGeneratorTest.xml




Exemplo de Arquivo TAC
Arquivo: test-reports/tac-output/teste3_if_else.tac

x = 10
t1 = x > 5
if t1 == 0 goto L1
y = 1
goto L2
L1:
y = 0
L2:



## 🎯 Resumo da Entrega

| Item | Status |
|------|--------|
| Gerador TAC Implementado | ✅ Completo |
| Instruções TAC Suportadas | ✅ 11 tipos |
| Gerenciador de Temporárias | ✅ Implementado |
| Gerenciador de Rótulos | ✅ Implementado |
| Otimizações Aplicadas | ✅ 4 técnicas |
| Testes Unitários | ✅ 12 testes passando |
| Relatórios Gerados | ✅ Código TAC e otimizado |
| Documentação | ✅ Completa |

## 🎓 Conceitos Aplicados

| Conceito | Descrição | Implementação |
|----------|-----------|---------------|
| **Three-Address Code** | Representação intermediária com instruções atômicas | `TACInstruction.java` |
| **Visitor Pattern** | Travessia da AST para geração de código | `TACGenerator.java` |
| **Temporárias** | Variáveis auxiliares para expressões complexas | `TemporaryManager.java` |
| **Rótulos (Labels)** | Marcadores para saltos condicionais/incondicionais | `LabelManager.java` |
| **Otimização de Código** | Constant folding, copy propagation, dead code elimination | `TACOptimizer.java` |



📚 Referências
Dragon Book: Compilers: Principles, Techniques, and Tools (Aho, Sethi, Ullman) - Capítulo 6 (Intermediate-Code Generation)
Engineering a Compiler: Keith Cooper & Linda Torczon - Capítulo 5 (Intermediate Representations)
Modern Compiler Implementation in Java: Andrew W. Appel - Capítulo 7 (Translation to Intermediate Code)



## 📝 Histórico de Versões

| Versão | Data | Descrição | Autor |
|--------|------|-----------|-------|
| 1.0 | 04/02/2026 | Versão inicial da entrega | Equipe Mini-Pascal |



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



