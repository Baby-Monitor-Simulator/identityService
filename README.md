# KeyCloak Must do als je ermee aan de slag gaat!

> [!IMPORTANT]  
> Informatie na het pullen van deze repository.

> [!WARNING]  
> Allereerst project clonen naar lokale omgeving.


## KeyCloak runnen

1. Ga naar de juiste directory waarin het project opgeslagen staat.
2. Run volgende commando: 

Docker compose up --build 

(alleen bij de eerste keer moet --build erbij)

3. Keycloack met Postgres Database is te vinden in uw Docker omgeving lokaal.

## Juiste data importeren naar Keycloak omgeving

In de map keycloak-export is een bestand te vinden. Dit is de keycloak configuratie die we gebruiken voor dit porject. Deze moet u daarom importeren als je met Keycloak en dit project aan de slag gaat om up to date te zijn.

### Stappen in importeren van Keycloak bestand

#### Checken of Import map te vinden is in keycloak container

1. Run in terminal: docker exec -it identityservice-keycloak-1 /bin/bash --> Dit zorgt ervoor dat u in de directory komt van uw keycloak container in Docker.
2. Run in terminal: cd /opt/keycloak/data/ 
3. Run in terminal: ls

> [!IMPORTANT]  
> Als de Import map niet bestaat.
4. Run in terminal: mkdir -p /opt/keycloak/data/import

#### Importeer Keycloak document

> [!WARNING]  
> Check de naam van uw Keycloak container in Docker.

5. Run in terminal: docker cp ./keycloak-export/Babymonitor-realm.json identityservice-keycloak-1:/opt/keycloak/data/import/Babymonitor-realm.json
6. Run in terminal: docker exec -it identityservice-keycloak-1 /opt/keycloak/bin/kc.sh import --dir=/opt/keycloak/data/import

> [!NOTE]  
> Dat was m!
