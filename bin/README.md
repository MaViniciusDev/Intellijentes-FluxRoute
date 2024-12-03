# Intellijentes-FluxRoute

Trabalho para a matéria de **Programação Orientada a Objetos Avançada**. Código Java que usa o padrão de projeto **Command** em um sistema de rotas dinâmico com suporte à **injeção de dependências**.

## Objetivo Geral

Implementar a carga dinâmica de rotas e a injeção de dependências com uso de **anotações** e **reflexão**. O sistema deve ser capaz de:

1. **Detectar e processar automaticamente as rotas**, carregando métodos anotados com `@Rota("/rota")`.
2. **Injetar automaticamente dependências** em classes, identificando os campos anotados com `@Inject`.
3. **Manter os repositórios em memória como Singletons**, carregando classes anotadas com `@Singleton`.

---

## Parte 1: Roteamento com Command

### 1. Command Único para Roteamento

- **Criação de um servlet único** responsável por capturar todas as requisições. Esse servlet central utilizará o padrão **Command** para despachar cada requisição ao método correto.
- Cada rota do sistema será representada por um método anotado com `@Rota("/rota")`.
- Usando **reflexão**, o sistema deve ser capaz de detectar e associar métodos de classes de controle às rotas específicas, sem a necessidade de configuração manual.

### 2. Anotação `@Rota`

- **Criação da anotação `@Rota`** que receberá como parâmetro o path da rota (e.g., `@Rota("/usuario")`).
- Durante a execução, o sistema deve escanear as classes de controle, identificar os métodos anotados com `@Rota` e mapeá-los para as respectivas rotas, associando-as ao Command correto.

**Objetivo do Item A**: Implementar a carga dinâmica de métodos anotados com `@Rota` usando reflexão, de forma que o servlet central mapeie as requisições para o método correto.

---

## Parte 2: Repositórios e Injeção de Dependências

### 1. Repositórios em Memória e HSQLDB

- **Criação de duas implementações de repositório em memória** e uma implementação que utiliza o **HSQLDB**.
- Uso do padrão **Factory Method** para permitir a troca entre esses repositórios. A implementação ativa será **injetada automaticamente** nas classes que necessitam de acesso ao repositório, dependendo do tipo de persistência que está em uso.

### 2. Anotação `@Inject`

- **Definição da anotação `@Inject`** para ser usada nos campos das classes que dependem de instâncias de repositório.
- Usando reflexão, o sistema deve identificar campos anotados com `@Inject` e injetar automaticamente a implementação correta do repositório.

**Objetivo do Item B**: Implementar a injeção dinâmica de dependências para os campos anotados com `@Inject`, utilizando reflexão para carregar as instâncias de repositório corretas conforme configurado no Factory Method.

---

## Parte 3: Implementação de Singleton para Repositórios em Memória

### 1. Singleton para Repositórios em Memória

- As classes de repositório em memória devem ser implementadas seguindo o padrão **Singleton**, garantindo uma única instância durante toda a execução do sistema.
- Essa configuração deve ser feita usando uma anotação `@Singleton`, indicando que a classe deve ser carregada como uma instância única.

### 2. Anotação `@Singleton`

- **Criação da anotação `@Singleton`** e aplicação nas classes de repositório em memória.
- Usando reflexão, o sistema deve verificar essa anotação para assegurar que uma única instância da classe está sendo utilizada durante toda a execução.

**Objetivo do Item C**: Implementar a carga de classes anotadas com `@Singleton`, garantindo que repositórios em memória sigam o padrão Singleton.

---

## Entregáveis

1. **Código Fonte**: O projeto Java completo, contendo o servlet, a configuração das rotas, os repositórios e as classes com anotações.
2. **Diagrama de Classes e Sequência**: Diagrama que explica a arquitetura do sistema, a aplicação das anotações e a troca dinâmica entre repositórios.
3. **Documentação do Projeto**: Descrição do funcionamento do sistema, incluindo:
   - Explicação de cada anotação criada e seu propósito.
   - Explicação sobre o uso de reflexão para a carga dinâmica de rotas e dependências.
   - Descrição de como o Factory Method realiza a troca entre repositórios e como o padrão Singleton foi implementado.

---
