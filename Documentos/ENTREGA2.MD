Entrega 2: Analisador Léxico (Scanner)
Descrição
Implementação do módulo capaz de ler o arquivo fonte (caracteres) e transformá-lo em uma sequência de Tokens, ignorando espaços em branco e comentários.

Arquivos da Entrega
pom.xml: Configurações do Maven e dependências do projeto.

miniPascal.g4: Definições léxicas e tokens.

MiniPascalLexerTest.java: Testes unitários de tokenização.

Conceitos Aplicados
Reconhecimento de Tokens: Agrupamento de caracteres em categorias como PALAVRA_RESERVADA, IDENTIFICADOR e OPERADOR.

Tratamento de Comentários: Suporte aos estilos {...}, (*...*) e //.