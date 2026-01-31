Entrega 3: Analisador Sintático e Semântico (Parser)

Descrição
Validação da estrutura do código conforme a gramática (Sintático) e verificação de regras de lógica e tipos (Semântico).

Arquivos da Entrega
MiniPascalSemanticVisitor.java: Visitor para análise de regras semânticas.

SymbolTable.java: Gerenciamento de variáveis e escopos.

Type.java: Definição do sistema de tipos da linguagem.

SemanticException.java: Exceção personalizada para erros semânticos.

MiniPascalParserTest.java: Testes unitários de sintaxe e semântica.

Verificações Implementadas
Sintática: Geração da Árvore Sintática Abstrata (AST).

Semântica: Verificação de variáveis não declaradas, redeclarações e compatibilidade de tipos.