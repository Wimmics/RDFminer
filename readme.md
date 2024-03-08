## Datasets

SPARQL endpoint: http://ns.inria.fr/rdfminer/sparql

The [**Corese**](https://github.com/Wimmics/corese) semantic web factory server instance, used by **RDFminer-core**, provides two distincts named RDF data graphs:

- https://example.org/graph#1 : subgraph of the 
It is an RDF dataset produced from the [covid-on-the-web dataset](https://github.com/Wimmics/CovidOnTheWeb). It describes scientific articles and named entities identified in these articles and linked to *Wikidata* entities. We consider a subset containing 18.79% of the articles and 0.01% of the named entities. 

<center>

| #RDF triples | #distinct articles | #distinct named entities |
|-------------:|-------------------:|-------------------------:|
|    266,647   |     20,912         |         6,331            |

</center>

- https://example.org/graph#2 : 1% of full instance of *DBPedia 2015.04* (english version) > 6,534,658 RDF triples

<!-- <center>

| #RDF triples   | #distinct subject  | #distinct properties     | #distinct object     |
|---------------:|-------------------:|-------------------------:|---------------------:|
|    6,534,658   |              |                     |                      |

</center> -->