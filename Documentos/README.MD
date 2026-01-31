# \# Compilador Mini-Pascal (Subconjunto Pascal)

# > \*\*Projeto Acadêmico\*\* desenvolvido para a disciplina de LINGUAGENS FORMAIS E AUTOMATOS (Compiladores) - IFS Campus Itabaiana (2025.2).

# 

# Este projeto implementa um compilador funcional para a linguagem \*\*Mini-Pascal\*\*, cobrindo as fases de Análise Léxica, Sintática, Semântica e Geração de Código Intermediário (C3E).

# 

# \## 🚀 Funcionalidades Suportadas

# \- \*\*Tipos de Dados:\*\* INTEGER, REAL, STRING, BOOLEAN.

# \- \*\*Estruturas de Controle:\*\* IF-THEN-ELSE, WHILE-DO, FOR-TO-DO.

# \- \*\*Entrada e Saída:\*\* Comandos `read()` e `write()`.

# \- \*\*Expressões:\*\* Aritméticas (+, -, \*, /) e Relacionais (>, <, =, >=, <=, <>).

# \- \*\*Escopo:\*\* Suporte a escopos aninhados e variáveis globais/locais.

# 

# \## 🛠️ Tecnologias Utilizadas

# \- \*\*Java 17+\*\*

# \- \*\*ANTLR 4.13.1\*\* (Gerador de Parser)

# \- \*\*Maven\*\* (Gerenciamento de Dependências e Build)

# \- \*\*JUnit 5\*\* (Testes Automatizados)

# 

# \## 📂 Estrutura do Repositório

# \- `/src/main/antlr4`: Gramática oficial `.g4`.

# \- `/src/main/java`: Código fonte (Visitors, Tabela de Símbolos, Main).

# \- `/src/test/java`: Conjunto de testes unitários para cada fase.

# \- `pom.xml`: Configurações do Maven e dependências do ANTLR.

# 

# \## ⚙️ Como Executar

# O projeto possui um menu interativo no arquivo `Main.java` que facilita a execução de cada etapa:

# 

# 1\. Clone o repositório.

# 2\. Certifique-se de que o Maven está configurado no seu `PATH` (ou ajuste o `MVN\\\_PATH` na `Main.java`).

# 3\. Execute a classe `Main.java`.

# 4\. Escolha as opções:

#    - \*\*Opção 1 e 2:\*\* Preparam o ambiente e compilam.

#    - \*\*Opção 3 a 5:\*\* Testes Léxica, Sintática e Semântica.

#    - \*\*Opção 7:\*\* Demonstração da análise semântica.

#    - \*\*Opção 8:\*\* Demonstração da geração de código (C3E).

# 

# \## 🎓 Autores

* # Enaldo Dantas - jose.santos259@academico.ifs.edu.br    
* # Agnaldo Neto - jose.neto044@academico.ifs.edu.br      
* # Vitorio Mota - vitorio.mota085@academico.ifs.edu.br  
* # Lucas Oliveira - lucas.oliveira031@academico.ifs.edu.br 
