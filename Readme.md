#swbLogin

Modulo de autenticación para Tomcat con datos en SWBDataManager

##Configuración

###1. Copiar las bibliotecas de swbLogin y SWBDataManager a la carpeta `lib` de Tomcat


###2. Crear un archivo de configuración JAAS
La configuración deberá contener los parámetros necesarios para el módulo:
```
SWBLoginModule
{
  org.semanticwb.auth.SWBDMLoginModule required
    debug=true
    roles="Admin,User,Device"
    number="1357";
};
```
Indicale a Tomcat en donde se encuentra el archivo:
```
export JAVA_OPTS=-Djava.security.auth.login.config==$CATALINA_HOME/conf/login.config
```

###3. Asegura que tu aplicación tiene configuradas las restricciones de seguridad

###4. Configura el JAASRealm en el archivo server.xml de Tomcat

```
<Realm className="org.apache.catalina.realm.JAASRealm"
            appName="SWBLoginModule"
            userClassNames="org.semanticwb.auth.types.SWBPrincipal"
            roleClassNames="org.semanticwb.auth.types.SWBRole" />
```
En donde lo único que podría cambiar es appName el que debe coincidir con el nombre del módulo configurado en el paso 2
