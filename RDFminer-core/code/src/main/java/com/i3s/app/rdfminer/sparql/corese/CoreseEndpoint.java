package com.i3s.app.rdfminer.sparql.corese;

import com.i3s.app.rdfminer.Global;
import com.i3s.app.rdfminer.Parameters;
import com.i3s.app.rdfminer.sparql.RequestBuilder;
import io.searchbox.client.http.apache.HttpGetWithEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Sparql endpoint to manage and request the Corese server
 * @author Rémi FELIN
 */
public class CoreseEndpoint {

    private static final Logger logger = Logger.getLogger(CoreseEndpoint.class.getName());

    /**
     * The URL of the SPARQL endpoint.
     */
    public String url;

    /**
     * The named data graph to query
     */
    public String namedGraphUri;

    /**
     * The prefixes that will be used to query the SPARQL endpoint.
     */
    private String prefixes;

    private final Parameters parameters;

    /**
     * Constructor of SparqlEndpoint
     * @param namedGraphUri the named graph uri to query
     * @param prefixes the prefixes used for the queries
     */
    public CoreseEndpoint(String namedGraphUri, String prefixes) {
        this.url = Global.SPARQL_ENDPOINT;
        this.namedGraphUri = namedGraphUri;
        this.prefixes = prefixes;
        this.parameters = Parameters.getInstance();
    }

    /**
     * SPARQL 'SELECT' query.<br>Template: "select "<b>var</b>" where { "<b>body</b>" }"
     * @param var
     * @param body
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public List<String> select(String var, String body, boolean distinct, Integer... limitOffset) throws URISyntaxException, IOException {
        String request = this.parameters.getPrefixes() + "\n";
        request += "@timeout " + this.parameters.getSparqlTimeOut() + "\n";
        if (distinct) {
            request += "SELECT distinct " + var + " WHERE {  " + body + " } ";
        } else {
            request += "SELECT " + var + " WHERE {  " + body + " } ";
        }
        // mapping limitOffset variables
        if (limitOffset.length > 0) {
            request += "LIMIT " + limitOffset[0];
        }
        if (limitOffset.length > 1) {
            request += " OFFSET " + limitOffset[1];
        }
        logger.debug(request);
        String resultAsJSON = query(Format.JSON, request);
        return ResultParser.getResultsFromVariable(var, resultAsJSON);
    }


    public boolean ask(String sparql) throws URISyntaxException, IOException {
        String request = RequestBuilder.ask(sparql, true);
        String resultAsJSON = query(Format.JSON, request);
        return ResultParser.getResultFromAskQuery(resultAsJSON);
    }

    /**
     * SELECT (count([<i>distinct</i>] <b>var</b>) as ?n) WHERE { <b>body</b> }
     */
    public int count(String var, String sparql, boolean distinct) throws URISyntaxException, IOException {
        Parameters parameters = Parameters.getInstance();
        String request = parameters.getPrefixes() + "\n";
        request += "@timeout " + parameters.getSparqlTimeOut() + "\n";
        if (distinct) {
            request += "SELECT (count(distinct " + var + ") as ?n) WHERE {  " + sparql + " }";
        } else {
            request += "SELECT (count(" + var + ") as ?n) WHERE {  " + sparql + " }";
        }
        String resultAsJSON = query(Format.JSON, request);
        if(resultAsJSON.contains("Read timed out") || resultAsJSON.contains("connect timed out")) {
            // time out
            // i.e. SocketTimeoutException from Corese server
            return -1;
        }
        try {
            return Integer.parseInt(Objects.requireNonNull(ResultParser.getResultsFromVariable("?n", resultAsJSON)).get(0));
        } catch (NullPointerException e) {
            logger.error("Error during the counting ...");
            logger.error("Result as JSON: " + resultAsJSON);
            // return an error
            return -1;
        }
    }

    /**
     * Build a HTTP Request on Corese server : SELECT query
     * @param format the expected format
     * @param sparql the SPARQL Request
     * @return the result in the desired format
     * @throws URISyntaxException Error concerning the syntax of the given URL
     * @throws IOException Error concerning the execution of the POST request
     */
    private String query(String format, String sparql) throws URISyntaxException, IOException {
        HashMap<String, String> params = new HashMap<>();
        // specify SPARQL and Format in parameters
        params.put("query", sparql);
        params.put("default-graph-uri", this.namedGraphUri);
        params.put("format", format);
        // call the get method and return it result
        return get(this.url, params);
    }

    public String getValidationReportFromServer(String content) throws URISyntaxException, IOException {
        Parameters parameters = Parameters.getInstance();
        // fill params
        ArrayList<BasicHeader> bodyMap = new ArrayList<>();
        // 'query' is a mandatory param !
        bodyMap.add(new BasicHeader("query", "construct where {?s ?p ?o}"));
//        if(parameters.getMod() == Mod.SHAPE_MINING) {
        bodyMap.add(new BasicHeader("mode", CoreseService.PROBABILISTIC_SHACL_EVALUATION));
        bodyMap.add(new BasicHeader("default-graph-uri", this.namedGraphUri));
        bodyMap.add(new BasicHeader("param", "p:" + parameters.getProbShaclP() + ";nT:" + Global.nTriples));
        bodyMap.add(new BasicHeader("content", content));
//        } else {
//            bodyMap.add(new BasicHeader("mode", CoreseService.SHACL_EVALUATION));
//        }
        // create form: body
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(bodyMap, StandardCharsets.UTF_8);
        // send POST request
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(this.url);
        HttpPost post = new HttpPost(builder.build());
        // Accept header
        post.setHeader("Accept", "text/turtle");
        post.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED);
        // specify params
        post.setEntity(urlEncodedFormEntity);
        // exec
//        System.out.println(get.getRequestLine());
        HttpResponse response = httpClient.execute(post);
        // catch status code of the request
        if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            // the request has a 200 OK response from the server
            // read the content of the response
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            return sb.toString();
        } else {
            // this section handle the error from the request
            logger.error("request: " + post.getRequestLine());
            logger.error("Request fail (code " + response.getStatusLine().getStatusCode() + ")");
            return null;
        }
    }

    /**
     * GET Request send to the server using a given service
     * @param service URL of the SPARQL endpoint
     * @param params [OPTIONAL] the query params of the request
     * @return the result of the given request
     * @throws URISyntaxException Error concerning the syntax of the given URL
     * @throws IOException Error concerning the execution of the POST request
     */
    @SafeVarargs
    private final String get(String service, HashMap<String, String>... params) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(service);
        // params
        if(params.length > 0) {
            // take the first elem of params parameter to get all keys and values
            for(String param : params[0].keySet()) {
                builder.setParameter(param, params[0].get(param));
            }
        }
        // GET request
        HttpGetWithEntity get = new HttpGetWithEntity(builder.build());
        // Accept header
        get.setHeader("Accept", "*/*");
        get.setEntity(MultipartEntityBuilder.create().addTextBody("content", "").build());
//        logger.info("HTTP Request: " + get);
        // exec
        HttpResponse response = httpClient.execute(get);
        // catch status code of the request
        if(response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
            // this section handle the error from the request
            logger.error("request: " + get.getRequestLine());
            logger.error("Request fail (code " + response.getStatusLine().getStatusCode() + ")");
            return null;
        }
        // the request has a 200 OK response from the server
        // read the content of the response
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();
        return sb.toString();
    }

    public String getPrefixes() {
        return prefixes;
    }

}
