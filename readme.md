<p align="center">
    <a href="https://ns.inria.fr/rdfminer/">
        <img src="https://raw.githubusercontent.com/Wimmics/RDFminer/main/RDFminer-front/v2/public/rdfminer.jpg" width="300" height="300" alt="rdfminer-logo">
    </a>
    <br>
    <h1 align="center">RDFminer</h1> 
</p>

# Introduction

**RDFminer** is an open Web application to automatically discovering SHACL shapes representative of an [RDF data graph](#avalaible-datasets). It is composed of the following components:

- [`RDFminer-core`](https://github.com/Wimmics/RDFminer/tree/main/RDFminer-core) an API to exploit the evolutionary algorithm implemented in Java. The server relies on an implementation of a RESTful web service using the JAX-RS framework.
- [`RDFminer-front`](https://github.com/Wimmics/RDFminer/tree/main/RDFminer-front) a VueJS application to control the mining process interactively: it enables to parameterize and launch the discovery process, monitor
its execution, inspect and analyze its results.
- [`RDFminer-server`](https://github.com/Wimmics/RDFminer/tree/main/RDFminer-front) a server to provide web services: interaction between `front` and `core`, db, ... It relies on an Express server for web services and **socket.io** server for websockets transport.

# Discovering SHACL shapes  

## Build candidates shapes using BNF grammar

RDFminer builds candidate shapes using BNF grammar writted by the user (in the *Creation form*), it must be compliant with the SHACL W3C recommendation. They are composed of **static** and **dynamic** rules.

Considering the following grammar:

```js
Shape           := ' a ' NodeShape
NodeShape       := 'sh:NodeShape ; ' ShapeBody
ShapeBody       := 'sh:targetClass ' Class ' ; ' ShapeProperty
ShapeProperty   := 'sh:property [ ' PropertyBody ' ] . '
PropertyBody    := 'sh:path rdf:type ; sh:hasValue ' Class ' ; '
Class           := 'SPARQL ?x a ?Class .'
```

Here, the rule `Class` is *dynamic* and includes the SPARQL keyword for mining all classes from the named RDF data graph. Every classes are associated to this rule:

```js
// It becomes:
Class           := '<Class_1>' | '<Class_2>' | ... | '<Class_n>'
```

Consequently, the rules `ShapeBody` and `PropertyBody` will evolve between each candidates:

```js
// candidate shape 1
'a sh:NodeShape ; sh:targetClass <Class_21> ; sh:property [ sh:path rdf:type ; sh:hasValue <Class_4> ] .'
// candidate shape 2
'a sh:NodeShape ; sh:targetClass <Class_641> ; sh:property [ sh:path rdf:type ; sh:hasValue <Class_89> ] .'
// ...
```

As only two characters evolve, the `ChromosomeSize` value must be **2**

More complex grammar can be designed to build rich shapes:

```js
Shape := ' a ' NodeShape
NodeShape := 'sh:NodeShape ; ' ShapeBody
ShapeBody := ClassTarget | SubjectsOfTarget | ObjectsOfTarget
ClassTarget := 'sh:targetClass ' Class ' ; ' ShapeProperty
SubjectsOfTarget := 'sh:targetSubjectsOf ' Property ' ; ' ShapeProperty
// Here, we will directly target the objects using ObjectsOfTarget, as a consequence we will specify the ValueTypeConstraintComponent fragment
ObjectsOfTarget := 'sh:targetObjectsOf ' Property ' ; ' ValueTypeConstraintComponent ' . '
//---------------------------------------------------------------
// Value type Constraint Components
//---
ShapeProperty := 'sh:property [ ' PropertyBody ' ] . '
PropertyBody := 'sh:path ' Property ' ; ' ValueTypeConstraintComponent ' ; '
//
ValueTypeConstraintComponent := ClassConstraint | DatatypeConstraint | NodeKindConstraint
// Constraint Component IRI: sh:ClassConstraintComponent
// The condition specified by sh:class is that each value node is a SHACL instance of a given type.
ClassConstraint := 'sh:class ' Class
// Constraint Component IRI: sh:DatatypeConstraintComponent
// sh:datatype specifies a condition to be satisfied with regards to the datatype of each value node.
DatatypeConstraint := 'sh:datatype ' DataType
// Constraint Component IRI: sh:NodeKindConstraintComponent
// sh:nodeKind specifies a condition to be satisfied by the RDF node kind of each value node.
NodeKindConstraint := 'sh:nodeKind ' NodeKind
// It can be: sh:BlankNode, sh:IRI, sh:Literal sh:BlankNodeOrIRI, sh:BlankNodeOrLiteral and sh:IRIOrLiteral
NodeKind := 'sh:BlankNode' | 'sh:IRI' | 'sh:Literal' | 'sh:BlankNodeOrIRI' | 'sh:BlankNodeOrLiteral' | 'sh:IRIOrLiteral'
Class := 'SPARQL ?x a ?Class .'
Property := 'SPARQL ?subj ?Property ?obj . FILTER ( isIRI(?Property) ) .'
DataType := 'SPARQL { SELECT distinct ?o WHERE { ?s ?p ?o . FILTER ( isLiteral(?o) ) } } BIND( datatype(?o) as ?DataType ) .'
```

Due to the complexity, we suggest to experiment different value for `ChromosomeSize`: e.g. **20**

# Avalaible datasets

**SPARQL endpoint:** http://ns.inria.fr/rdfminer/sparql

The [**corese-server**](https://github.com/Wimmics/RDFminer/tree/main/corese/corese-server/build-docker), used by **RDFminer-core**, provides distincts named RDF data graphs:

## covid-on-the-web

https://example.org/graph#1 

It is an RDF dataset produced from the [covid-on-the-web dataset](https://github.com/Wimmics/CovidOnTheWeb). It describes scientific articles and named entities identified in these articles and linked to *Wikidata* entities. We consider a subset containing 18.79% of the articles and 0.01% of the named entities. 

<center>

| #RDF triples | #distinct articles | #distinct named entities |
|-------------:|-------------------:|-------------------------:|
|    266,647   |     20,912         |         6,331            |

</center>

## DBpedia 2015.04 (small)

https://example.org/graph#2 

1% of full instance of *DBPedia 2015.04* (english version) > 6,534,658 RDF triples

<!-- <center>

| #RDF triples   | #distinct subject  | #distinct properties     | #distinct object     |
|---------------:|-------------------:|-------------------------:|---------------------:|
|    6,534,658   |              |                     |                      |

</center> -->