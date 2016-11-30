# Ambiente de Desenvolvimento

Diretório: ~/Library/Assijus:

* bluecrystal.client.chrome.pkcs11-1.0.0.one-jar.jar
* start.sh com o seguinte conteúdo:

```
java -Xdebug -Xrunjdwp:transport=dt_socket,address=127.0.0.1:8000,suspend=y -jar /Users/nato/Library/Assijus/bluecrystal.client.chrome.pkcs11-1.0.0.one-jar.jar)
```

Diretório ~/Library/Application Support/Google/Chrome/NativeMessagingHosts/
* br.jus.trf2.assijus.json com o seguinte conteúdo:

```
{
  "name": "br.jus.trf2.assijus",
  "description": "Assijus PKCS#11 Native Messaging Host",
  "path": "/Users/nato/Library/Assijus/start.sh",
  "type": "stdio",
  "allowed_origins": [
    "chrome-extension://badeikkolbajgcpbpcjboemhappeifmc/"
  ]
}
```

# Logando em arquivo

Mesmo conectando o debugger remoto, não é possível ver os logs no console. Portanto, é necessário configurar o SL4J para fazer output em arquivo.

No artefato NativeMessagingHosts existe um bloco estático que, se descomentado, ativará o log:

```
static {
  System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY,
      "DEBUG");
  System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY,
      "~/Library/Assijus/log.txt");
}
```

# Ativando o log do Chrome

Primeiro saia do Chrome.

Mac:
```
open -a Google\ Chrome --args --enable-logging --v=1
```

Windows:
```
start chrome --enable-logging --v=1
```

Veja o log assim:

Mac:
```
less ~/Library/Application\ Support/Google/Chrome/chrome_debug.log
```

Windows:
```
type "C:\Users\%USERNAME%\AppData\Local\Google\Chrome\User Data\chrome_debug.log"
```

Mais informações em: http://www.chromium.org/for-testers/enable-logging
