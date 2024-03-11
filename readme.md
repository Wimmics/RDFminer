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
- [`RDFminer-server`](https://github.com/Wimmics/RDFminer/tree/main/RDFminer-front) a server to provide web services. It relies on an Express server for web services and **socket.io** server for websockets transport.

# Usage

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