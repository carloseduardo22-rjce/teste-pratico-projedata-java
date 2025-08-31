## 📋 Descrição do Projeto

O projeto foi desenvolvido para atender aos requisitos solicitados pelo teste técnico de desenvolvedor java júnior da empresa Projedata. O sistema implementa o **Repository Pattern** para simular cenários reais de desenvolvimento, proporcionando melhor separação de responsabilidades e facilidade de manutenção.

## 🏗️ Estrutura do Projeto

```
testepratico/
├── src/
│   ├── file/
│   │   └── employeesdata.csv        # Arquivo CSV com dados dos funcionários
│   ├── main/
│   │   └── Main.java                # Classe principal para execução
│   ├── models/
│   │   ├── Employee.java            # Classe funcionário (herda de Person)
│   │   └── Person.java              # Classe base para pessoa
│   ├── repository/
│   │   └── employee/
│   │       ├── EmployeeRepository.java     # Interface do repositório
│   │       └── CsvEmployeeRepository.java  # Implementação para CSV
│   ├── services/
│   │   └── EmployeeService.java     # Serviço com regras de negócio
│   ├── test/
│   │   └── EmployeeServiceTest.java # Testes unitários das funcionalidades
│   └── util/
│       └── FormatterUtil.java       # Utilitários de formatação
├── pom.xml                          # Configuração Maven
└── README.md                        # Documentação do projeto
```

## 🎯 Requisitos Implementados

## 🏛️ Arquitetura - Repository Pattern

### Interface EmployeeRepository

**Objetivo**: Definir contrato para acesso aos dados de funcionários.

```java
public interface EmployeeRepository {
    LinkedHashMap<String, Employee> loadEmployees();
}
```

### Implementação CsvEmployeeRepository

**Objetivo**: Implementação específica para leitura de dados via arquivo CSV.

- **Classe**: `CsvEmployeeRepository`
- **Implementa**: `EmployeeRepository`
- **Responsabilidade**:
  - Leitura do arquivo `employeesdata.csv`
  - Parsing dos dados para objetos `Employee`
  - Tratamento de exceções de I/O

**Vantagens da Implementação**:

- 🔄 **Extensibilidade**: Outras implementações podem ser criadas (DatabaseEmployeeRepository, JsonEmployeeRepository, etc.)
- 🛡️ **Isolamento**: Mudanças na forma de ler dados não afetam a lógica de negócio
- 🧪 **Testabilidade**: Service pode ser testado independentemente da fonte de dados

### Injeção de Dependência

**Implementação**: O `EmployeeService` recebe uma instância de `EmployeeRepository` via construtor.

```java
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
```

**Benefícios**:

- 🔧 **Baixo Acoplamento**: Service não depende de implementação específica
- 🔄 **Inversão de Controle**: Dependências são injetadas externamente
- 🧩 **Modularidade**: Cada componente tem responsabilidade bem definida

## 🛠️ Utilitários de Formatação

### FormatterUtil

**Objetivo**: Centralizar formatações de datas e valores monetários.

- **Classe**: `FormatterUtil`
- **Métodos**:
  - `formatCurrency(BigDecimal value)`: Formata valores monetários em padrão brasileiro
  - `formatDate(LocalDate date)`: Formata datas no padrão dd/MM/yyyy

**Benefícios**:

- 🎯 **Centralização**: Todas as formatações em um local
- 🔄 **Reutilização**: Métodos estáticos disponíveis para toda aplicação
- 🧹 **Código Limpo**: Remove duplicação de código de formatação
- 🌐 **Consistência**: Garantia de formatação uniforme

## 🎯 Requisitos Implementados

### 1. Classe Person

**Requisito**: Classe Pessoa com os atributos: nome (String) e data nascimento (LocalDate).

- **Atributos**:
  - `name` (String): Nome da pessoa
  - `dateOfBirth` (LocalDate): Data de nascimento
- **Métodos**:
  - `getName()`: Retorna o nome
  - `getDateOfBirth()`: Retorna a data de nascimento

### 2. Classe Employee

**Requisito**: Classe Funcionário que estenda a classe Pessoa, com os atributos: salário (BigDecimal) e função (String).

- **Herança**: Estende a classe Person
- **Atributos adicionais**:
  - `salary` (BigDecimal): Salário do funcionário
  - `role` (String): Função/cargo do funcionário
- **Métodos**:
  - `getSalary()`: Retorna o salário
  - `getRole()`: Retorna a função
  - `setSalary(BigDecimal salary)`: Define novo salário

### 3. Funcionalidades Principais

#### 3.1 Inserção de Funcionários

**Requisito**: Inserir todos os funcionários, na mesma ordem e informações da tabela.

- **Método**: `CsvEmployeeRepository.loadEmployees()`
- **Implementação**:
  - Lê o arquivo CSV `employeesdata.csv`
  - Usa `LinkedHashMap` para manter a ordem de inserção
  - Converte strings em tipos apropriados (LocalDate, BigDecimal)
- **Decisão de Design**: LinkedHashMap foi escolhido para manter a ordem de inserção e permitir operações de exclusão rápidas (O(1))

#### 3.2 Remoção de Funcionário

**Requisito**: Remover o funcionário "João" da lista.

- **Método**: `EmployeeService.deleteEmployee(String name)`
- **Implementação**:
  - Remove o funcionário "João" do LinkedHashMap obtido via repository
  - Exibe mensagem de confirmação
  - Lança exceção caso o funcionário não seja encontrado

#### 3.3 Listagem de Funcionários

**Requisito**: Imprimir todos os funcionários com formatação brasileira.

- **Método**: `EmployeeService.employees()`
- **Implementação**:
  - Exibe todos os funcionários com suas informações
  - Data no formato dd/mm/aaaa usando `FormatterUtil.formatDate()`
  - Valores monetários com formatação brasileira usando `FormatterUtil.formatCurrency()`
  - Remove João automaticamente da listagem

#### 3.4 Aumento Salarial

**Requisito**: Funcionários receberam 10% de aumento de salário.

- **Método**: `EmployeeService.updateSalary()`
- **Método auxiliar**: `increaseSalary(BigDecimal salary)` (privado)
- **Implementação**:
  - Aplica aumento de 10% (multiplicação por 1.10)
  - Utiliza `BigDecimal` para precisão monetária
  - Arredondamento com `RoundingMode.HALF_UP`
  - Exibe lista atualizada com novos salários

#### 3.5 Agrupamento por Função

**Requisito**: Agrupar funcionários por função em um MAP.

- **Método**: `groupEmployeesByFunction()` (privado)
- **Implementação**:
  - Retorna `Map<String, List<Employee>>`
  - Chave: função, Valor: lista de funcionários
  - Utiliza `HashMap` com `getOrDefault()` para agrupamento

#### 3.6 Impressão por Função

**Requisito**: Imprimir funcionários agrupados por função.

- **Método**: `EmployeeService.printByFunction()`
- **Implementação**:
  - Utiliza o método `groupEmployeesByFunction()`
  - Exibe funcionários organizados por cargo
  - Formatação hierárquica (função → funcionários)

#### 3.8 Aniversariantes dos Meses 10 e 12

**Requisito**: Imprimir funcionários que fazem aniversário nos meses 10 e 12.

- **Método**: `EmployeeService.printBirthDay()`
- **Implementação**:
  - Filtra funcionários usando `getMonthValue()`
  - Verifica se mês é 10 (outubro) ou 12 (dezembro)
  - Exibe nome e data de nascimento

#### 3.9 Funcionário com Maior Idade

**Requisito**: Imprimir funcionário com maior idade (nome e idade).

- **Método**: `EmployeeService.olderEmployees()`
- **Implementação**:
  - Usa Stream API com `min(Comparator.comparing())`
  - Compara por data de nascimento (mais antiga = mais velho)
  - Calcula idade usando `Period.between()`
  - Exibe nome e idade calculada

#### 3.10 Ordem Alfabética

**Requisito**: Imprimir funcionários em ordem alfabética.

- **Método**: `EmployeeService.printEmployeesAlphabetically()`
- **Implementação**:
  - Usa Stream API com `sorted(Comparator.comparing())`
  - Ordena por nome
  - Converte para lista ordenada

#### 3.11 Total dos Salários

**Requisito**: Imprimir total dos salários dos funcionários.

- **Método**: `EmployeeService.totalEmployeeSalaries()`
- **Implementação**:
  - Usa Stream API com `reduce()` para somar
  - Utiliza `BigDecimal.ZERO` como valor inicial
  - Formata resultado com padrão brasileiro

#### 3.12 Cálculo em Salários Mínimos

**Requisito**: Calcular quantos salários mínimos cada funcionário ganha (SM = R$1212.00).

- **Método**: `EmployeeService.minimumSalarys()`
- **Implementação**:
  - Divide salário do funcionário pelo salário mínimo
  - Usa `BigDecimal.divide()` com 2 casas decimais
  - Arredondamento `HALF_UP`
  - Exibe resultado para cada funcionário

## 🔧 Métodos Auxiliares

### Formatação Centralizada (FormatterUtil)

- **Classe**: `FormatterUtil`
- **Métodos**:
  - `formatCurrency(BigDecimal value)`: Formatar valores monetários no padrão brasileiro (R$ x.xxx,xx)
  - `formatDate(LocalDate date)`: Formatar datas no padrão dd/MM/yyyy
- **Implementação**: Utiliza `NumberFormat` e `DateTimeFormatter` com configurações brasileiras
- **Benefícios**: Centralização, reutilização e consistência de formatação

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Java JDK 17
- IDE Java (Eclipse, IntelliJ IDEA, VS Code, etc.)

### Passos para Execução

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/carloseduardo22-rjce/teste-pratico-projedata-java.git
   ```

2. **Importe o projeto na sua IDE**:

   - **Eclipse**: File → Import → Existing Projects into Workspace
   - **IntelliJ**: File → Open → Selecione a pasta do projeto
   - **VS Code**: File → Open Folder → Selecione a pasta do projeto

3. **Execute a aplicação**:
   - Navegue até a classe `Main.java` em `src/main`
   - Execute o método `main`
   - A aplicação processará automaticamente todos os requisitos

## 🧪 Testes Unitários

O projeto inclui uma suíte de testes unitários usando **JUnit 4** para validar as funcionalidades principais.

### Executar os Testes

**Via IDE:**

- Navegue até `src/test/EmployeeServiceTest.java`
- Clique direito → Run As → JUnit Test

**Via Maven:**

```bash
mvn test
```

### Cobertura dos Testes

#### 🔍 Testes Implementados:

1. **`testDeleteEmployee()`**

   - **Objetivo**: Verificar se a exclusão do funcionário "João" funciona corretamente
   - **Validações**:
     - João existe antes da exclusão
     - João não existe após a exclusão
   - **Método testado**: `deleteEmployee(String name)`

2. **`testOlderEmployee()`**

   - **Objetivo**: Validar identificação do funcionário mais velho
   - **Validações**:
     - Funcionário mais velho tem 64 anos
     - Nome do funcionário mais velho é "Caio"
   - **Métodos testados**: `getOldestEmployee()`, `getEmployeeAge(Employee)`

3. **`testTotalSalaryAfterDeletionAndIncrease()`**
   - **Objetivo**: Testar cálculo total após exclusão e aumento salarial
   - **Sequência testada**:
     1. Exclusão do João
     2. Aplicação de aumento de 10%
     3. Cálculo do total dos salários
   - **Valor esperado**: R$ 50.906,82
   - **Métodos testados**: `deleteEmployee()`, `updateSalary()`, `totalSalary()`

### Estrutura de Dados (CSV)

O arquivo `employeesdata.csv` contém os seguintes funcionários:

```
Maria;18/10/2000;2009.44;Operador
Joao;12/05/1990;2284.38;Operador
Caio;02/05/1961;9836.14;Coordenador
Miguel;14/10/1988;19119.88;Diretor
Alice;05/01/1995;2234.68;Recepcionista
Heitor;19/11/1999;1582.72;Operador
Arthur;31/03/1993;4071.84;Contador
Laura;08/07/1994;3017.45;Gerente
Heloisa;24/05/2003;1606.85;Eletricista
Helena;02/09/1996;2799.93;Gerente
```

## 💡 Decisões de Design

### Repository Pattern

A implementação do **Repository Pattern** traz benefícios significativos:

- **Separação de Responsabilidades**: Lógica de negócio separada do acesso aos dados
- **Flexibilidade**: Fácil troca entre diferentes fontes de dados (CSV, JSON, Database)
- **Testabilidade**: Possibilidade de criar implementações mock para testes
- **Extensibilidade**: Novas fontes de dados podem ser adicionadas sem alterar o service
- **Manutenibilidade**: Código mais organizado e fácil de manter

### Injeção de Dependência

- Baixo acoplamento entre componentes
- Facilita testes unitários
- Melhora a modularidade do código

### FormatterUtil - Utility Class

- Centralização de todas as formatações
- Métodos estáticos para fácil acesso
- Configurações brasileiras padronizadas
- Eliminação de duplicação de código

### LinkedHashMap

Foi utilizado `LinkedHashMap<String, Employee>` para armazenar os funcionários porque:

- **Ordem de inserção**: Mantém a ordem original dos dados do CSV
- **Performance**: Operações de busca e exclusão em O(1)
- **Flexibilidade**: Permite acesso direto por nome (chave)

### BigDecimal para Valores Monetários

- Evita problemas de precisão com ponto flutuante
- Controle preciso sobre arredondamentos
- Padrão para aplicações financeiras

### Stream API

- Código mais limpo e funcional
- Operações de filtragem e transformação eficientes
- Melhor legibilidade para operações de coleções

## 📊 Saída Esperada

A execução do programa produz a seguinte sequência de relatórios:

1. Confirmação de exclusão do funcionário João
2. Lista de todos os funcionários restantes
3. Lista com salários atualizados (+10%)
4. Funcionários agrupados por função
5. Aniversariantes de outubro e dezembro
6. Funcionário mais velho
7. Funcionários em ordem alfabética
8. Total dos salários
9. Quantidade de salários mínimos por funcionário

## 🛠️ Tecnologias Utilizadas e Boas Práticas

- **Java 17**: Linguagem principal
- **JUnit 4**: Framework de testes unitários
- **Maven**: Gerenciamento de dependências e build
- **Repository Pattern**: Padrão arquitetural para acesso aos dados
- **Dependency Injection**: Inversão de controle e baixo acoplamento

## 📁 Arquivos Importantes

- `Main.java`: Ponto de entrada da aplicação
- `Person.java`: Classe base para dados pessoais
- `Employee.java`: Extensão com dados profissionais
- `EmployeeRepository.java`: Interface do padrão Repository
- `CsvEmployeeRepository.java`: Implementação para leitura de CSV
- `EmployeeService.java`: Lógica de negócio principal
- `FormatterUtil.java`: Utilitários de formatação centralizados
- `EmployeeServiceTest.java`: Testes unitários das funcionalidades
- `employeesdata.csv`: Base de dados dos funcionários
- `pom.xml`: Configuração Maven com dependências
