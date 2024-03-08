# How to use RDFminer-core web services

`RDFminer-core` relies on a Jetty server and provides a REST API to exploit the evolutionary algorithm for discovering SHACL shapes.

## Services

### 1. Launch an RDFminer project

Endpoint: https://ns.inria.fr/rdfminer/core/start

It launch the evolutionary process according to the provided `params`: it is a JSON object as **string** and must contains the following keys:

| key                     | required                    | type   | description                                                               | values                                                                                                           |
|-------------------------|-----------------------------|--------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| projectName             | :white_check_mark:          | String | the name of the project                                                   | e.g. "*my_wonderful project*"                                                                                    |
| mod                     |                             | number | the feature to use                                                        | **3** (default) to enable *Shape Mining*                                                                         |
| prefixes                |                             | String | set of prefixes to use                                                    | e.g. `PREFIX  psh:   <http://ns.inria.fr/probabilistic-shacl/> [...]`                                            |
| namedDataGraph          | :white_check_mark:          | String | the RDF data graph to use for mining                                      | (see datasets informations)                                                                                      |
| grammar                 | :white_check_mark:          | String | the BNF grammar to use for building candidate shapes                      | (see grammar informations)                                                                                       |
| stopCriterion           |                             | number | the stopping criterion for the evolutionary algorithm                     | **1** (default) for *clock-world* stop; **2** for *effort* stop                                                  |
| maxMiningTime           | **if `stopCriterion` == 1** | number | the maximum time allocated for mining (in min.)                           | **30** minutes (default)                                                                                         |
| effort                  | **if `stopCriterion` == 2** | number | the effort invested                                                       | **5,000** (default)                                                                                              |
| populationSize          |                             | number | the population size (number of candidate shapes)                          | **100** (default)                                                                                                |
| sizeChromosome          | :white_check_mark:          | number | Size of chromosomes array: usefull for individuals building and operators | (see grammar informations)                                                                                       |
| maxWrap                 |                             | number | the maximum wrapping: usefull to reconstruct non-conforming individuals   | **1000** (default)                                                                                               |
| eliteSelectionRate      |                             | number | the proportion of individuals selected for **elitism**                    | **0.2** (default)                                                                                                |
| selectionType           |                             | number | the type of **crossover** to perform for **recombination**                | **1** for *proportional roulette wheel* ; **2** for *scaled roulette wheel* and **3** (default) for *Tournament* |
| tournamentSelectionRate | **if `selectionType` == 3** | number | the proportion of individuals selected for **tournament**                 | **0.1** (default)                                                                                                |
| selectionRate           |                             | number | the proportion of individuals selected for **recombination**              | **0.5** (default)                                                                                                |
| mutationType            |                             | number | the type of **mutation** to perform                                       | **1** (default) for *int flip* ; **2** for *int flip byte* ; **3** for *nodal* and **4** for *subtree*           |
| proMutation             |                             | number | the probability of **mutation** of an individual                          | **0.01** (default)                                                                                               |
| crossoverType           |                             | number | the type of **crossover** to perform                                      | **1** (default) for *single point* ; **2** for *two point* ; **3** for *subtree* and **4** for *swap*            |
| proCrossover            |                             | number | the probability of **crossover** between *two* individual                 | **0.75** (default)                                                                                               |
| probShaclAlpha          |                             | number | level of significance                                                     | **0.05** (default)                                                                                               |
| probShaclP              |                             | number | p-value                                                                   | **0.05** (default)                                                                                               |

#### Example:

```json
{
  "projectName": "my_wonderful_project",
  "mod": 3,
  "namedDataGraph": "https://example.org/graph#1",
  "grammar": "Shape := ' a ' NodeShape\nNodeShape := 'sh:NodeShape ; ' ShapeBody\nShapeBody := 'sh:targetClass ' Class ' ; ' ShapeProperty\nShapeProperty := ' sh:property [ ' PropertyBody ' ] . '\nPropertyBody := ' sh:path rdf:type ; sh:hasValue ' Class ' ; '\n#---------------------------------------------------------------\nClass := 'SPARQL ?x a ?Class .'",
  "populationSize": 20,
  "time": 5,
  "sizeChromosome": 2
}
```

#### Recommendation 

Please, choose a strong naming (e.g. MyVerySecretProject) to avoid any side-effect from other services

### 2. Stop this RDFminer project

Endpoint: https://ns.inria.fr/rdfminer/core/stop

This service just take the ```projectName``` value to find the process and stop it. It returns the current results. 

## YAML

Here is the YAML (OpenAPI 3.0) definition of the web services:

```yml
openapi: 3.0.0
info:
  title: RDFminer-core API
  version: 1.0.0

paths:
  /core/start:
    post:
      summary: Start RDFminer project
      consumes:
        - application/x-www-form-urlencoded
      produces:
        - application/json
      parameters:
        - name: params
          in: formData
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successful operation
        '503':
          description: RDFminer-core is already in use
          content:
            application/json:
              example:
                message: RDFminer-core is already in use, please try again later

  /core/stop:
    get:
      summary: Stop RDFminer project execution
      parameters:
        - name: projectName
          in: query
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Successful operation
        '404':
          description: No RDFminer execution to stop
          content:
            application/json:
              example:
                message: No RDFminer execution to stop
```

