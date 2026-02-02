# 📘 Entrega 1: Gramática da Linguagem (BNF/EBNF)

**Compilador Mini-Pascal - IFS Campus Itabaiana 2025.2**

*Disciplina: LINGUAGENS FORMAIS, AUTÔMATOS E COMPILADORES*

---

## 📅 Informações da Entrega

| Item | Descrição |
|------|-----------|
| **Número da Entrega** | 1 |
| **Data de Entrega** | 14/01/2026 |
| **Objetivo** | Definir formalmente a Mini-Linguagem utilizando BNF/EBNF |
| **Status** | ✅ Concluído |
| **Responsáveis** | Enaldo Dantas, Agnaldo Neto, Vitorio Mota, Lucas Oliveira |

---

## 📋 Índice

- [Objetivo](#objetivo)
- [Conceito](#conceito)
- [Artefatos Entregues](#artefatos-entregues)
- [Gramática BNF/EBNF Completa](#gramática-bnfebnf-completa)
- [Comandos Suportados](#comandos-suportados)
- [Estrutura da Gramática ANTLR](#estrutura-da-gramática-antlr)
- [Exemplos de Código Válido](#exemplos-de-código-válido)
- [Exemplos de Código Inválido](#exemplos-de-código-inválido)
- [Precedência de Operadores](#precedência-de-operadores)
- [Como Gerar o Parser/Lexer](#como-gerar-o-parserlexer)
- [Validação e Testes](#validação-e-testes)
- [Checklist de Entrega](#checklist-de-entrega)

---

## 🎯 Objetivo

Definir as **"regras do jogo"** da linguagem Mini-Pascal através de uma gramática formal que especifica:

- ✅ **Estrutura de um programa válido**: Como organizar declarações e comandos
- ✅ **Declaração de variáveis e tipos**: Suporte a `integer`, `real`, `string`, `boolean`
- ✅ **Comandos de controle**: Atribuição, entrada/saída, condicionais e laços
- ✅ **Expressões**: Aritméticas, relacionais e lógicas com precedência definida
- ✅ **Regras léxicas**: Identificadores, números, strings, palavras-reservadas

---

## 💡 Conceito

A gramática é a **especificação formal** da linguagem Mini-Pascal. Ela define:

### 1. **Estrutura do Programa**
Todo programa Mini-Pascal segue a estrutura:
