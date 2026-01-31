Entrega 4: Gerador de Código Intermediário

Descrição
Tradução da árvore sintática para uma representação linear e simplificada utilizando Código de Três Endereços (C3E).

Arquivos da Entrega
MiniPascalCodeGenerator.java: Gerador de instruções C3E.

Instruction.java: Modelo de quádruplos de instrução.

Exemplo de Tradução
O comando x := a + b * c é traduzido para:

Plaintext
t0 := b * c
t1 := a + t0
x := t1
Objetivo
Esta etapa gera um código agnóstico de máquina, preparando os dados para futuras otimizações ou tradução para linguagens de baixo nível.