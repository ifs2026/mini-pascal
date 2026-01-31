# Entrega 1: Definição da Gramática (BNF)

## Artefato Principal
- Arquivo de Gramática: `Gramatica EBNF.txt` (contendo a sintaxe formal em EBNF).
- Arquivo de Gramática: `miniPascal.g4` (Gramática em formato ANTLR).

## Definição Formal
A linguagem Mini-Pascal foi definida com suporte a:
- **Precedência de Operadores:** Multiplicação e divisão possuem prioridade sobre soma e subtração.
- **Estruturas:** Programas iniciados com `program`, declarações em bloco `var` e comandos entre `begin...end`.

## Exemplos de Código
### Válido
```pascal
program Valido;
var x: integer;
begin
  x := 10 + 5 * 2;
end.

### Invalido
```pascal
program Invalido;
          { Erro: Faltou a seção VAR }
begin
  x := 10 { Erro: Faltou o ponto e vírgula }
end.