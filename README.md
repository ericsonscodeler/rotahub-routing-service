# routing-service

Serviço de Roteirização do RotaHub. Recebe um conjunto de paradas (pedido + endereço + coordenadas)
e devolve a ordem otimizada de visita.

- **Stack:** Java 21 + Spring Boot 4.1 (Spring Web, Spring Data JPA, Bean Validation, Testcontainers)
- **Banco:** PostgreSQL (próprio, não compartilhado com outros serviços)
- **Comunicação:** expõe REST síncrono, consumido pelo BFF (`rotahub-bff`)

## Sobre o algoritmo

A otimização usa a heurística do **vizinho mais próximo** sobre distância **haversine** (linha
reta entre coordenadas, considerando a curvatura da Terra). Isso é **honestamente diferente** de
uma rota real de estrada — não há integração com nenhuma API de mapas/roteamento real (Google
Maps, OSRM, etc.), o que exigiria uma chave de API paga fora do escopo deste projeto de portfólio.
O objetivo aqui é demonstrar um serviço de domínio real com um algoritmo real, não simular uma
integração externa que não existe.

## Status da rota

`PLANNED → IN_PROGRESS → COMPLETED`

## Rodando localmente

```bash
./mvnw spring-boot:run   # :8083, precisa do Postgres em localhost:5433 (rotahub-infra)
```
