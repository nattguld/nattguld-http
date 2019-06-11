# nattguld-http
An advanced HTTP library built on raw sockets with a focus on web automation.

## Dependencies
This repository uses the following dependencies:  
**dec:** For decoding brotli responses. https://github.com/google/brotli/tree/master/java  
**gson:** For parsing JSON payloads. https://github.com/google/gson  
**jsoup:** For parsing HTML. https://github.com/jhy/jsoup  
**nattguld-util:** For various helper methods. https://github.com/randqm/nattguld-util  

## About
Nattguld HTTP is a library built on raw sockets for maximum customizibility.
This project was made with the focus on web automation.
There's user agents and other browser mimic functionality built in and every request can be built exactly how you want it.
You'll rarely find yourself needing to extend on existing code as most situations are already managed by default and customization options are present. Next to the default plain text & gzip also brotli encoded responses can be handled.
Cookies are managed per HTTP client instance to allow you to create complete automation flows.
Below you can find some basic example usage. You should have no troubles with advanced usage as everything is implemented in the default requests & methods.

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

### HttpClient GET & Download example
```java
try (HttpClient c = new HttpClient(ConnectionPolicy.SSL)) {
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

### HttpClient custom headers example
```java
Headers headers = new Headers();
headers.add("key", "value");

RequestResponse rr = c.dispatchRequest(new Request("url", expectedResponseCode, body, customHeaders));
```
