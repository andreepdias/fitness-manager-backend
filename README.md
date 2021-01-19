# FitnessManager

Aplicação de gerenciamento de treinos para academia e planejador de refeições com contagem de macro nutrientes (calorias, carboidratos, proteínas e gorduras).

Esta aplicação foi desenvolvida para praticar conhecimentos de Angular e Spring Boot. O frontend pode ser encontrado em: https://github.com/andreepdias/fitness-manager-frontend

O sistema está implantado no GitHub Pages e pode ser acessado por: https://andreepdias.github.io/fitness-manager-frontend. OBS.: O backend está hospeado em plano GRATUITO no Heroku, portanto a primeira requisição pode demorar (até que a máquina virtural do Heroku seja iniciada).

## Tecnologias utilizadas

A aplicação foi desenvolvida utilizando Spring Boot 2 e banco de dados relacionado SQL (MySQL). 

* Especificação JPA com Hibernate foi utilizada para a persistência dos dados; 
* Spring Security com OAuth 2 e token JWT para autenticação e autorização;
* Dependências como ModelMapper e Lombok para o desenvolvimento do projeto;
* Padrão de camadas (controller -> serivice -> repository).

## Modelagem das Entidades

#### User
Usuário do sistema.

---

#### UserMealInfo
Armazena informações do usuário relacionadas ao planejamento dietético.

#### DailyMeal
Representa um dia de planejamento de alimentação.

#### Meal
Representa uma refeição.

#### MealEntry
Um alimento de uma refeição.

#### Food
Um possível alimento cadastrado (comida ou bebida).

#### Recipe
Uma receita composta por vários ingredientes.

#### Ingredient
Um ingrediente de uma receita. COmposto por um alimento (Food) e uma quantidade específica.

#### MacrosCount
Entidade para contagem de macro nutrientes (proteínas, carboidratos, gorduras e calorias)

#### MealName
Armazena o nome de uma refeição de um usuário. Utilizado para a criação e edição de DailyMeals.

---

#### UserTrainingInfo
Armazena informações do usuário relacionadas aos treinamentos físicos.

#### TrainingRoutine
Uma rotina de treinamento de um usuário.

#### TrainingSession
Uma sessão de treinamento de uma rotina (um dia na semana, por exemplo)

#### SessionExercise
Um exercício de uma sessão, com séries, repetições e intervalo de descanso.

#### Exercise
Um possível exercício cadastrado para o usuário.

#### Tag
Uma possível tag que um ou mais exercícios podem assumirem. Tem a função de diferenciar os diferentes tipos de exercícios (por agrupamento muscular, objetivo, etc.)

---

### Diagrama de Relacionamento de Entidades

![](https://i.imgur.com/BBmorAQ.png)
