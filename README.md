# 🔍 Analisador Léxico, Sintático e Semântico para a Linguagem LALG

Este projeto implementa um **analisador léxico**, **analisador sintático preditivo LL(1)** e uma **análise semântica completa** para a linguagem fictícia **LALG**. O sistema realiza validações desde a tokenização até regras de escopo, tipo e uso de variáveis.


## 📁 Estrutura do Projeto

```
├── model/
│   ├── AnalisadorLexico.java
│   ├── AnalisadorSintatico.java
│   ├── TabelaDeSimbolos.java
│   ├── Simbolo.java
│   ├── TipoDado.java
│   ├── TipoSimbolo.java
│   ├── Token.java
│   ├── Erro.java
│   └── TabelaSintatica.java
├── exemplos/
│   └── correto.txt
├── README.md
```

---

## ✅ Funcionalidades

- [x] Tokenização completa da linguagem LALG
- [x] Análise sintática baseada em tabela LL(1)
- [x] Verificação de declaração e escopo de variáveis
- [x] Detecção de variáveis não utilizadas
- [x] Checagem de tipo em expressões, atribuições e condições
- [x] Verificação de chamadas vazias para `read()` e `write()`
- [x] Tratamento de erros léxicos, sintáticos e semânticos com mensagens detalhadas

---

## 🧠 Componentes Principais

### `AnalisadorLexico.java`
Responsável por ler o código-fonte e gerar uma lista de tokens, reconhecendo palavras-chave, identificadores, operadores, números, símbolos e delimitadores.

### `AnalisadorSintatico.java`
Executa a análise sintática com pilha LL(1), expandindo produções com base em uma tabela e acionando ações semânticas.

### `TabelaDeSimbolos.java`
Gerencia escopos e símbolos (variáveis e procedimentos), permitindo controle sobre declarações, escopos e uso.

### `Simbolo.java`
Classe que representa variáveis ou procedimentos com seus tipos, escopos, posição no código, e se foram utilizados.

---

## 🚨 Tipos de Erros Detectados

### 🔹 **Erros Léxicos**
Detectados durante a leitura do código-fonte:

- Comentário sem fechamento
- Identificadores malformados
- Símbolos inválidos
- Tokens inesperados ao final do arquivo

### 🔸 **Erros Sintáticos**
Detectados durante a expansão da gramática LL(1):

- **Token inesperado**: símbolo atual não pertence à produção esperada
- **Produção inexistente**: nenhum caminho para o símbolo encontrado
- **Sincronização (`sinc`)**: remoção de símbolos da pilha para recuperação de erro
- **Fim abrupto de instruções compostas**

### 🔸 **Erros Semânticos**
Detectados pelas ações semânticas do parser:

- **Identificador não declarado**  
- **Declaração duplicada no mesmo escopo**  
- **Tipo incompatível em atribuições** (ex: `boolean := int`)  
- **Tipo incompatível em operações aritméticas** (esperado `int + int`)  
- **Tipo incompatível em operações lógicas** (esperado `boolean and boolean`)  
- **Uso de operadores relacionais entre tipos diferentes**  
- **Uso do operador `not` sobre valores não booleanos**  
- **Chamada de `read()` ou `write()` sem argumentos**  
- **Expressões condicionais (`if`, `while`) com tipo diferente de `boolean`**  
- **Variável declarada e nunca utilizada**  

---

## ▶️ Exemplo de Código LALG

```pascal
int a, b;
boolean d;

begin
    a := 1;
    d := true;
    if (d) then
        a := a + 1;
    read(a);
    write(a)
end
```

---

## 📌 Observações

- A análise léxica gera os tokens utilizados na fase sintática.
- Os escopos são tratados com uma pilha, permitindo o uso correto de variáveis locais e globais.
- Variáveis são marcadas como “usadas” apenas quando participam de uma expressão ou são lidas.
- A detecção de erro semântico ocorre mesmo que o código passe na sintaxe.

---

## 👥 Autoria

Desenvolvido por:

- **Lucas Bernardo**
- **Sara Raquel**

Curso de Ciência da Computação  
Universidade Estadual Paulista (UNESP) – Presidente Prudente
