# nattguld-http
An advanced HTTP library built on raw sockets with a focus on web automation.

## Dependencies
This repository uses the following dependencies:  
**dec:** For decoding brotli responses. https://github.com/google/brotli/tree/master/java  
**gson:** For parsing JSON payloads. https://github.com/google/gson  
**jsoup:** For parsing HTML. https://github.com/jhy/jsoup  
**nattguld-util:** For various helper methods. https://github.com/nattguld/nattguld-util  
**nattguld-data:** For saving configurations (optional) https://github.com/nattguld/nattguld-data  

## About
Nattguld HTTP is a library built on raw sockets for maximum customizibility.
This project was made with the focus on web automation.
There's user agents and other browser mimic functionality built in and every request can be built exactly how you want it.
You'll rarely find yourself needing to extend on existing code as most situations are already managed by default and customization options are present. Next to the default plain text & gzip also brotli encoded responses can be handled.
Cookies are managed per HTTP client instance to allow you to create complete automation flows.
Below you can find some basic example usage. You should have no troubles with advanced usage as everything is implemented in the default requests & methods. Response bodies have built-in support for parsing HTML documents, plain text or JSON.

For saving configurations for software deployment the library depends on the nattguld-data library. This can very easily be stripped out if you don't need or want this.

## Examples
### HttpClient constructors
```java
//Constructors
new HttpClient(Browser browser, HttpProxy proxy, ConnectionPolicy... policies);
new HttpClient(HttpProxy proxy, ConnectionPolicy... policies);
new HttpClient(Browser browser, ConnectionPolicy... policies);
new HttpClient(ConnectionPolicy... policies);

//Methods
File dl = c.download(String savePath, Request req);
RequestResponse rr = c.dispatchRequest(Request req, Request... backgroundRequests);
```

#### Use of proxies
For HTTP(S) proxies you can simply use the proxymanager class to parse your proxy information and obtain a HttpProxy object which you then pass in the HttpClient constructor. For SOCKS (untested) you need to instantiate a HttpProxy object manually.
```java
HttpProxy proxy = ProxyManager.parse("host:port");
HttpProxy proxy = ProxyManager.parse("host:port:user:pass");
HttpProxy proxy = new HttpProxy(ProxyType.SOCKS, "host", port);
HttpProxy proxy = new HttpProxy(ProxyType.SOCKS, "host", port, "username", "password");
```

### HttpClient GET & Download example
```java
try (HttpClient c = new HttpClient()) {
    RequestResponse rr = c.dispatchRequest(new GetRequest("https://github.com/randqm/");
    
    if (!rr.validate(200)) {
      System.out.println("Unexpected response code: " + rr.getCode());
      return;
    }
    Document doc = rr.getAsDoc();
    JsonElement jsonEl = rr.getAsJsonElement();
    String content = rr.getResponseContent();
    String endpoint = rr.getEndpoint();
    Headers headers = rr.getHeaders();
    
    File dl = c.download("savepath", new GetRequest("url"));
}
```

### HttpClient POST examples
```java
//Form
FormBody fb = new FormBody();
fb.add("key", "value");
fb.add("file", file);
fb.add("number", Number);

//Multipart
MultipartBody mb = new MultipartBody();
mb.add("key", "value");
mb.add("file", file);
mb.add("bytes", new byte[]{});
mb.add("number", Number);

//Stream
new StreamBody(File f);
//Json
new StringBody(EncType.JSON, jsonStr);
//XML
new StringBody(EncType.XML, xmlStr);
//Plain text
new StringBody(EncType.PLAIN_TEXT, xmlStr);

//Request
RequestResponse rr = c.dispatchRequest(new PostRequest("url", body));
```

### Full custom request example
```java
Headers headers = new Headers();
headers.add("key", "value");

RequestResponse rr = c.dispatchRequest(new Request("url", expectedResponseCode, body, customHeaders)
  .setResponseEncType(EncType.JSON).setXMLHttpRequest(true));
  
JsonObject response = rr.getAsJsonElement().getAsJsonObject();
String code = response.get("code").getAsString();
```

