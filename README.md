# KeyCloak Must do als je ermee aan de slag gaat!

> [!IMPORTANT]  
> Informatie na het pullen van deze repository.

> [!WARNING]  
> Allereerst project pullen naar lokale omgeving.

Hieronder staan 2 opties omschreven om Keycloak te configureren voor gebruik in het project. 

## Optie 1 

1. Open Keycloak op het internet. Dit zou localhost:8080 moeten zijn.
2. Log in met de credentials die in het overdrachtsdocument staan van gebruiker "Myadmin".
3. Na het succesvol inloggen klik je linksboven op master en kies "Create Realm".
5. Vervolgens kies je in de Identityservice deze file: C:\identityService\keycloak\export\Babymonitor-realm.json
6. Sleep, Upload of Plak deze file in het Partial Import Resource file stukje.
7. Vervolgens klik je op Import.

Nu heb je succesvol de Realm van Babymonitor met alle nodige gegevens geimporteerd. Gebruik deze Realm voor de vervolgstappen in het gehele project.

## Optie 2

1. Ga naar de juiste directory waarin het project opgeslagen staat.
2. Run volgende commando: 

```Docker compose up --build``` 

(alleen bij de eerste keer moet --build erbij)

3. Keycloack met Postgres Database is te vinden in uw Docker omgeving lokaal.

## Juiste data importeren naar Keycloak omgeving

In de map keycloak-export is een bestand te vinden. Dit is de keycloak configuratie die we gebruiken voor dit porject. Deze moet u daarom importeren als je met Keycloak en dit project aan de slag gaat om up to date te zijn.

### Stappen in importeren van Keycloak bestand

#### Checken of Import map te vinden is in keycloak container

1. Run in terminal: ```docker exec -it identity-keycloak-1 /bin/bash``` --> Dit zorgt ervoor dat u in de directory komt van uw keycloak container in Docker.
2. Run in terminal: ```cd /opt/keycloak/data/``` 
3. Run in terminal: ```ls```

> [!IMPORTANT]  
> Als de Import map niet bestaat.
> 4. Run in terminal: ```mkdir -p /opt/keycloak/data/import```

#### Importeer Keycloak document

> [!WARNING]  
> Check de naam van uw Keycloak container in Docker.

5. Run in terminal: ```docker cp ./keycloak/export/Babymonitor-realm.json identity-keycloak-1:/opt/keycloak/data/import/Babymonitor-realm.json```
6. Run in terminal: ```docker exec -it identity-keycloak-1 /opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import```

> [!NOTE]  
> Dat was m!
