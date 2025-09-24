# Análise do Serviço Java — Desafio Sistema de Pagamento (V1)

Data: 2025-09-24

---

### Compatibilidade com Java 21 e oportunidades de modernização

**Descrição do problema ou ponto de atenção:**  
> O projeto já está configurado para Java 21 (pom.xml com `<java.version>21</java.version>` e Spring Boot 3.5.5), e faz bom uso de `record` no domínio e DTOs. Há oportunidades de modernização adicionais (virtual threads, pattern matching, sealed) que não estão sendo exploradas. 

**Trecho de código (se aplicável):**
```xml
<!-- pom.xml -->
<properties>
  <java.version>21</java.version>
  <mapstruct.version>1.6.3</mapstruct.version>
  <lombok.version>1.18.38</lombok.version>
</properties>
```

**Sugestão de melhoria ou correção:**  
> - Ativar virtual threads: `spring.threads.virtual.enabled=true` para ganhos de escalabilidade em IO-bound (Spring Boot 3.2+).  
> - Avaliar `sealed` para hierarquias de exceções de domínio.  
> - Usar pattern matching para `instanceof` e `switch` quando aplicável.  
> - Preferir `record` também para payloads de integração (ex.: `AuthorizationDataApiResponse`).

**Nível de severidade:**  
> 🟢 Baixa

---

### Aderência à Arquitetura Hexagonal (Ports and Adapters)

**Descrição do problema ou ponto de atenção:**  
> A estrutura do projeto segue bem os conceitos de hexagonal: `core` com domínio e ports, `adapters` com inbound (REST) e outbound (JPA e HTTP), e `infrastructure` para config. Há um desvio: uso de `@Service` no core, acoplando ao Spring. Além disso, há uma porta (`TransferRepositoryPort`) não usada pelo serviço, e o modelo de domínio de `Transfer` não carrega `id`, enquanto a porta expõe `findById(Long)`, criando inconsistência conceitual.

**Trecho de código (se aplicável):**
```text
src/main/java/com/lucascosta/desafiopagamento/
  core/{application,domain,ports}
  adapters/inbound{controller,dto,mapper}
  adapters/outbound{authorization,persistence}
  infrastructure/config
```

**Sugestão de melhoria ou correção:**  
> - Remover anotações Spring do core e instanciar beans na infraestrutura.  
> - Alinhar ports com modelos (incluir `id` em `Transfer` se a identidade for necessária no domínio; ou ajustar o port removendo `findById` do agregado se não fizer sentido).  
> - Garantir que todas as portas expostas são consumidas pelo application service.

**Nível de severidade:**  
> 🟠 Média

---

### Acoplamento do Core ao Spring (@Service no core)

**Descrição do problema ou ponto de atenção:**  
> A classe `TransferService` no core está anotada com `@Service`, acoplando a camada de domínio/aplicação ao framework. Na arquitetura hexagonal, o core deve ser independente de framework. 

**Trecho de código (se aplicável):**
```java
// TransferService.java
@Service // Unica referencia ao Spring no core, para ser injetado em outros lugares
public class TransferService implements TransferUseCase {
  // ...
}
```

**Sugestão de melhoria ou correção:**  
> Remover a anotação do core e registrar o bean na camada de infraestrutura/configuração (ex.: `@Configuration` cria `TransferUseCase transferUseCase(...)`). 

**Nível de severidade:**  
> 🟠 Média

---

### Método principal de negócio não implementado (retornando null)

**Descrição do problema ou ponto de atenção:**  
> `TransferService.execute` retorna `null` e contém apenas TODOs, o que quebra o contrato do `TransferUseCase` e provavelmente gerará NPEs ou respostas inconsistentes. 

**Trecho de código (se aplicável):**
```java
// TransferService.java
@Override
public TransferResult execute(Transfer transfer) {
    validateTransfer(transfer);
    autorizeTransfer(transfer);
    // TODO: debitar/creditar, registrar, notificar
    return null; // BUG
}
```

**Sugestão de melhoria ou correção:**  
> - Implementar a transação completa com `@Transactional` na aplicação: debitar pagador (com lock/otimismo), creditar recebedor, persistir transferência via `TransferRepositoryPort`, e retornar `TransferResult`.  
> - Realizar rollback em qualquer falha.  
> - Adicionar notificação assíncrona após commit (outbox/evento ou fila). 

**Nível de severidade:**  
> 🔴 Alta

---

### Porta de repositório não utilizada no serviço

**Descrição do problema ou ponto de atenção:**  
> Existe a porta `TransferRepositoryPort`, implementada no adaptador JPA, mas ela não é injetada/consumida por `TransferService`. 

**Trecho de código (se aplicável):**
```java
// ports/outbound/TransferRepositoryPort.java
public interface TransferRepositoryPort {
  Transfer save(Transfer transfer);
  Transfer findById(Long id);
}
```

**Sugestão de melhoria ou correção:**  
> Injetar `TransferRepositoryPort` em `TransferService` e usá-la para persistir a transferência (status PENDING -> COMPLETED/FAILED) dentro da mesma transação. 

**Nível de severidade:**  
> 🟠 Média

---

### Inconsistência de modelos de domínio sem IDs para atualização

**Descrição do problema ou ponto de atenção:**  
> Modelos de domínio como `Wallet` e `WalletHolder` não possuem identificadores. Isso inviabiliza updates corretos (JPA depende do `id` para distinguir insert/update). O mapper pode gerar sempre inserts e criar duplicidade. Além disso, o agregado `Transfer` também não possui `id` no domínio, porém a porta expõe `findById(Long)`, tornando o retorno incapaz de carregar sua própria identidade — inviabilizando round-trip e auditoria adequada no domínio.

**Trecho de código (se aplicável):**
```java
// Wallet.java
public record Wallet(
  Long version,
  BigDecimal balance
) { }

// Transfer.java (sem id)
public record Transfer(
  Long payerId,
  Long payeeId,
  BigDecimal amount,
  TransferStatus status,
  Instant createdAt,
  Instant finishedAt,
  String failureReason
) { }
```

**Sugestão de melhoria ou correção:**  
> - Introduzir campo `id` nos modelos de domínio ligados a persistência (ao menos `Wallet` e `Transfer`; considerar `WalletHolder`). Ajustar mapeamentos MapStruct para incluir o `id`.  
> - Alternativamente, se o domínio não quer carregar `id`, remodele a porta para não expor `findById` ou retorne um tipo que carregue a identidade (DTO/Entity) fora do domínio. 

**Nível de severidade:**  
> 🔴 Alta

---

### Mapeamentos MapStruct e tipos numéricos no DTO

**Descrição do problema ou ponto de atenção:**  
> O DTO `TransferRequest` usa `Double` para valor, enquanto o domínio usa `BigDecimal`. Isso implica perdas de precisão e conversões implícitas no MapStruct. 

**Trecho de código (se aplicável):**
```java
// TransferRequest.java
public record TransferRequest(
  Long payerId,
  Long payeeId,
  Double value
) {}
```

**Sugestão de melhoria ou correção:**  
> Trocar para `BigDecimal` e validações como `@DecimalMin("0.01")`. Configurar MapStruct para mapear precisamente `BigDecimal` <-> `BigDecimal`. 

**Nível de severidade:**  
> 🟠 Média

---

### Nome de método com erro de digitação (autorizeTransfer)

**Descrição do problema ou ponto de atenção:**  
> O método `autorizeTransfer` possui typo em inglês/português, o que reduz legibilidade. 

**Trecho de código (se aplicável):**
```java
private void autorizeTransfer(Transfer transfer) { /* ... */ }
```

**Sugestão de melhoria ou correção:**  
> Renomear para `authorizeTransfer`. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Duplicação/confusão de tipo AuthorizationResult

**Descrição do problema ou ponto de atenção:**  
> Há dois tipos `AuthorizationResult`: um no domínio (`core.domain.payment.model.AuthorizationResult`) e outro no adaptador (`adapters.outbound.authorization.AuthorizationResult`) definido como `record` vazio. Isso gera confusão e risco de import incorreto. 

**Trecho de código (se aplicável):**
```java
// adapters/outbound/authorization/AuthorizationResult.java
public record AuthorizationResult(

) { }
```

**Sugestão de melhoria ou correção:**  
> Remover o tipo duplicado no adaptador e usar somente o tipo do domínio. 

**Nível de severidade:**  
> 🟠 Média

---

### Propriedades de timeouts não aplicadas no RestClient

**Descrição do problema ou ponto de atenção:**  
> `AuthorizationProperties` possui `connectTimeout` e `readTimeout`, porém `AuthorizationClientConfig` não aplica esses valores ao `RestClient`. 

**Trecho de código (se aplicável):**
```java
// AuthorizationClientConfig.java
return RestClient.builder()
  .baseUrl(props.baseUrl())
  .build(); // timeouts ignorados
```

**Sugestão de melhoria ou correção:**  
> Configurar `ClientHttpRequestFactory`/`HttpClient` com timeouts e injetar no `RestClient`. Avaliar também política de retry/backoff via Resilience4j (`spring-cloud-circuitbreaker` ou `resilience4j-spring-boot`). 

**Nível de severidade:**  
> 🟠 Média

---

### Tratamento de erros do autorizador externo genérico

**Descrição do problema ou ponto de atenção:**  
> O adaptador captura `RestClientResponseException` e lança `RuntimeException` genérico. Isso dificulta o tratamento semântico no core e respostas HTTP adequadas.

**Trecho de código (se aplicável):**
```java
// TransferAuthorizationPortAdapter.java
} catch (RestClientResponseException ex) {
  throw new RuntimeException("Erro ao consultar autorizador externo: " + ex.getStatusCode(), ex);
}
```

**Sugestão de melhoria ou correção:**  
> Lançar exceção específica (ex.: `ExternalTransferUnauthorizedException` ou `ExternalSystemException`) e mapeá-la em `@ControllerAdvice` para status coerentes (ex.: 503/502). 

**Nível de severidade:**  
> 🟠 Média

---

### Falta de `@ControllerAdvice` para tratamento consistente de erros REST

**Descrição do problema ou ponto de atenção:**  
> Não há handlers globais para mapear `DomainException`, `ValidationException`, `UserNotFoundException`, etc. Isso pode resultar em 500 para erros de domínio. 

**Trecho de código (se aplicável):**
```java
// TransferController.java
transferService.execute(transfer); // exceções propagam sem mapeamento
```

**Sugestão de melhoria ou correção:**  
> Adicionar `@RestControllerAdvice` para traduzir:  
> - `ValidationException` -> 400,  
> - `UserNotFoundException` -> 404,  
> - `ExternalTransferUnauthorizedException`/timeouts -> 503,  
> - fallback -> 500. 

**Nível de severidade:**  
> 🟠 Média

---

### Design REST do endpoint (versionamento, payload e status)

**Descrição do problema ou ponto de atenção:**  
> O endpoint não é versionado, retorna `String` "OK" e sempre `200`. Isso é pouco RESTful e dificulta evolução. 

**Trecho de código (se aplicável):**
```java
@RestController
@RequestMapping("/transfer")
@PostMapping
public ResponseEntity<String> createTransfer(...) {
  // ...
  return ResponseEntity.ok("OK");
}
```

**Sugestão de melhoria ou correção:**  
> - Versionar: `@RequestMapping("/api/v1/transfers")`.  
> - Responder `201 Created` com representação (`TransferResult`) ou `202 Accepted` se assíncrono.  
> - Padrão de erro estruturado (RFC-7807 Problem Details). 

**Nível de severidade:**  
> 🟠 Média

---

### Validações e mensagens centralizadas — positivo com pequenas melhorias

**Descrição do problema ou ponto de atenção:**  
> O uso de cadeia de validação no core é positivo. As mensagens estão centralizadas em `Constants`, mas a classe poderia ser `final` com construtor privado, e podemos internacionalizar no futuro. 

**Trecho de código (se aplicável):**
```java
public class Constants {
  public static final String MSG_SAME_PARTICIPANT = "...";
  // ...
}
```

**Sugestão de melhoria ou correção:**  
> Tornar `Constants` final com construtor privado; opcionalmente migrar mensagens para i18n (MessageSource) ou Problem Details. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Propriedades de JPA e Flyway incorretas ou redundantes

**Descrição do problema ou ponto de atenção:**  
> - `spring.jpa.hibernate.dialect` não é a chave correta; a forma suportada é `spring.jpa.properties.hibernate.dialect` (ou deixar auto).  
> - `spring.flyway.databaseType` não é propriedade padrão do Spring Boot.  
> - DSN duplicado em Flyway e Datasource. 

**Trecho de código (se aplicável):**
```yaml
# application.yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none
      dialect: org.hibernate.dialect.PostgreSQLDialect # incorreto
  flyway:
    databaseType: postgresql # inválido
```

**Sugestão de melhoria ou correção:**  
> - Remover `dialect` ou mover para `spring.jpa.properties.hibernate.dialect`.  
> - Remover `databaseType`.  
> - Preferir usar apenas o datasource padrão (Flyway herda se `spring.flyway.url` não for definido). 

**Nível de severidade:**  
> 🟠 Média

---

### Externalização de configuração e Docker Compose

**Descrição do problema ou ponto de atenção:**  
> As credenciais/host do DB estão fixos em `application.yaml`. O `docker-compose.yml` define variáveis de ambiente, mas elas não são consumidas pela configuração. 

**Trecho de código (se aplicável):**
```yaml
# application.yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/desafio_sis_pag
    username: user
    password: password
```

**Sugestão de melhoria ou correção:**  
> Usar placeholders com defaults:  
> `jdbc:postgresql://${DB_SERVER:localhost}:${DB_PORT:5432}/${DB_NAME:desafio_sis_pag}` e ler `DB_USER`, `DB_PASSWORD`. Separar perfis (`LOCAL`, `docker`, `test`). 

**Nível de severidade:**  
> 🟠 Média

---

### Ausência de observabilidade (logs, métricas, tracing)

**Descrição do problema ou ponto de atenção:**  
> Não há `spring-boot-starter-actuator`, métricas Micrometer, nem tracing (OpenTelemetry). Logs não estruturados e sem correlação. 

**Trecho de código (se aplicável):**
```xml
<!-- pom.xml: não há actuator/micrometer -->
```

**Sugestão de melhoria ou correção:**  
> - Adicionar `spring-boot-starter-actuator` e expor endpoints essenciais.  
> - Micrometer + Prometheus (`micrometer-registry-prometheus`).  
> - OpenTelemetry (OTLP) para tracing distribuído.  
> - Correlation/trace-id em logs e campos de negócio (payerId, transferId). 

**Nível de severidade:**  
> 🟠 Média

---

### Ausência de testes além de `contextLoads`

**Descrição do problema ou ponto de atenção:**  
> Só há um teste de contexto. Não há testes unitários das regras de validação, do serviço de transferência, nem de integração do controlador e do adaptador externo. 

**Trecho de código (se aplicável):**
```java
// MainApplicationTests.java
@SpringBootTest
@ActiveProfiles("test")
class MainApplicationTests {
  @Test void contextLoads() {}
}
```

**Sugestão de melhoria ou correção:**  
> - Unit tests para cada handler (Distinct, LoadPayer/Payee, PayerType, SufficientBalance).  
> - Unit/integration para `TransferService` cobrindo sucesso/falhas (com mocks dos ports).  
> - WebMvcTest/RestAssured para o controller.  
> - Testcontainers (PostgreSQL) e WireMock para o autorizador. 

**Nível de severidade:**  
> 🟠 Média

---

### Persistência: concorrência e atomicidade na atualização de saldos

**Descrição do problema ou ponto de atenção:**  
> Apesar de `@Version` em `WalletEntity` (bom, para lock otimista), o fluxo de débito/crédito não está implementado. Sem transação no serviço e sem reprocessamento de conflitos, é alto o risco de inconsistência. 

**Trecho de código (se aplicável):**
```java
// WalletEntity.java
@Version
@Column(name = "version")
private Long version;
```

**Sugestão de melhoria ou correção:**  
> - Encapsular atualização de saldo em transação com lock otimista e retry limitado.  
> - Persistir `Transfer` como auditoria/ledger (PENDING -> COMPLETED) na mesma transação.  
> - Se for idempotente por request, armazenar idempotency-key. 

**Nível de severidade:**  
> 🔴 Alta

---

### Controller REST: Payload e validação

**Descrição do problema ou ponto de atenção:**  
> O DTO usa `Double` e validações de precisão (`@Digits`) que são mais adequadas a `BigDecimal`. Também não há validação de limites de valor (mín./máx.). 

**Trecho de código (se aplicável):**
```java
public record TransferRequest(
  @Positive @Digits(integer = 10, fraction = 2)
  @NotNull Double value
) {}
```

**Sugestão de melhoria ou correção:**  
> Usar `BigDecimal` + `@DecimalMin("0.01")`, e considerar regras de limites. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Serialização da resposta do autorizador

**Descrição do problema ou ponto de atenção:**  
> `AuthorizationDataApiResponse` é uma classe com campo `boolean authorization` sem getters/setters. Embora Jackson consiga popular campos, é mais robusto usar `record` com componentes nomeados. 

**Trecho de código (se aplicável):**
```java
public class AuthorizationDataApiResponse {
  boolean authorization; // sem accessor
}
```

**Sugestão de melhoria ou correção:**  
> Converter para `record AuthorizationDataApiResponse(boolean authorization) {}`. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Ausência de Kafka (se for requisito) e recomendações

**Descrição do problema ou ponto de atenção:**  
> Não há integração com Kafka no projeto (nenhuma dependência/uso). Se a arquitetura pretendida envolve eventos (ex.: notificação, integração assíncrona), isso ainda não foi implementado. 

**Trecho de código (se aplicável):**
```xml
<!-- pom.xml: sem spring-kafka -->
```

**Sugestão de melhoria ou correção:**  
> - Publicar evento `TransferCompleted` após commit (outbox + relay para Kafka) para garantir idempotência e consistência.  
> - Configurar serialização (JSON/Avro), chaves por `payeeId` para particionamento, e política de retries/DMQ.  
> - Consumidores idempotentes (chave de deduplicação) e observabilidade de lag. 

**Nível de severidade:**  
> 🟢 Baixa (caso Kafka não seja requisito atual)

---

### Melhoria na camada REST: segurança e hardening

**Descrição do problema ou ponto de atenção:**  
> Não há Spring Security. Mesmo sem autenticação, recomenda-se hardening básico (headers, rate-limit em endpoints sensíveis). 

**Trecho de código (se aplicável):**
```xml
<!-- pom.xml: sem spring-boot-starter-security -->
```

**Sugestão de melhoria ou correção:**  
> - Adicionar Spring Security (stateless), CORS controlado, headers de segurança.  
> - Rate limiting (ex.: Bucket4j) para proteção de endpoint de transferência. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Coesão de exceções de domínio

**Descrição do problema ou ponto de atenção:**  
> `ExternalTransferUnauthorizedException` não herda de `DomainException`, gerando tratamento diferenciado. 

**Trecho de código (se aplicável):**
```java
public class ExternalTransferUnauthorizedException extends RuntimeException { }
```

**Sugestão de melhoria ou correção:**  
> Herdar de `DomainException` (ou criar hierarquia selada para erros de negócio vs. erros externos) e mapear em `@ControllerAdvice`. 

**Nível de severidade:**  
> 🟢 Baixa

---

### Boas práticas de Clean Code

**Descrição do problema ou ponto de atenção:**  
> Há alguns pontos de melhoria de legibilidade e coesão:  
> - Typos/nomenclatura: `autorizeTransfer` (inglês/português misto).  
> - Tipos redundantes/duplicados: `AuthorizationResult` em dois pacotes diferentes.  
> - Retornos `null` em repositórios (preferir `Optional`).  
> - Classe `Constants` pode ser `final` com construtor privado.  
> - Serviços devem ter responsabilidade única e clara (aplicação vs integração vs persistência).

**Trecho de código (se aplicável):**
```java
private void autorizeTransfer(Transfer transfer) { /* ... */ }
```

**Sugestão de melhoria ou correção:**  
> - Corrigir nomes, remover duplicações, retornar `Optional`, reforçar imutabilidade quando possível (`record`), separar responsabilidades.  
> - Adotar padrões de código consistentes (checkstyle/spotless) e análise estática (SpotBugs, Sonar).

**Nível de severidade:**  
> 🟢 Baixa

---

### Boas práticas de microserviços com Spring

**Descrição do problema ou ponto de atenção:**  
> Alguns pilares de microserviços ainda não estão presentes: versionamento de API, tratamento global de erros, resiliência em chamadas externas, observabilidade, externalização de configuração por perfil/ambiente e segurança básica. 

**Trecho de código (se aplicável):**
```yaml
# application.yaml não versiona API, sem actuator, sem configs por perfil docker
```

**Sugestão de melhoria ou correção:**  
> - Versionar endpoints (`/api/v1`).  
> - `@RestControllerAdvice` + Problem Details.  
> - Resilience4j (timeouts/retries/circuit breaker/bulkhead).  
> - Actuator + métricas + tracing.  
> - Profiles (`local`, `docker`, `test`) e placeholders com `${ENV_VAR:default}`.  
> - Segurança stateless e rate limiting.

**Nível de severidade:**  
> 🟠 Média

---

### Identificação de possíveis bugs ou pontos frágeis (consolidado)

**Descrição do problema ou ponto de atenção:**  
> - `TransferService.execute` retorna `null` (quebra contrato).  
> - DTO `Double` -> perda de precisão.  
> - Ports retornando `null` podem causar NPEs.  
> - Duplicidade de `AuthorizationResult` pode induzir import incorreto.  
> - Falta de `id` nos modelos persistidos inviabiliza updates corretos e `findById` coerente.  
> - `@OneToOne(fetch = LAZY)` pode não ser efetivo; risco de N+1.  
> - Timeouts não aplicados no `RestClient`; risco de travas e baixa resiliência.  
> - Exceptions genéricas (`RuntimeException`) no adaptador externo prejudicam mapeamento correto de erros.

**Trecho de código (se aplicável):**
```java
@Override
public TransferResult execute(Transfer transfer) {
  // ...
  return null; // BUG
}
```

**Sugestão de melhoria ou correção:**  
> Implementar a transação de transferência, ajustar DTOs, usar `Optional`, remover duplicações, introduzir `id` no domínio onde aplicável, revisar fetch strategy, aplicar timeouts e mapear exceções específicas.

**Nível de severidade:**  
> 🔴 Alta

---

## Sumário Executivo

- Itens críticos (Alta):  
  - Implementação da transação de transferência no serviço (retorno `null`, atomicidade e persistência).  
  - Modelos de domínio sem `id` inviabilizam updates corretos em JPA e tornam inconsistente o uso de `findById` para `Transfer`.  
  - Concorrência/consistência de saldos requer transação e lock otimista com retry.

- Itens importantes (Média):  
  - Acoplamento do core ao Spring (@Service).  
  - Ajustes de propriedades JPA/Flyway e externalização de configs.  
  - Tratamento de erros com `@ControllerAdvice`, melhorar REST e timeouts/Resilience.  
  - Alinhar DTOs (`BigDecimal`), usar `TransferRepositoryPort`.

- Itens de oportunidade (Baixa):  
  - Java 21 (virtual threads), observabilidade, segurança básica, Kafka (se necessário), documentação.

## Próximos Passos Recomendados (curto prazo)
- Implementar `TransferService.execute` com transação, persistência e retorno `TransferResult` (+ testes).  
- Adicionar `id` aos modelos de domínio persistentes (incluindo `Transfer`) e ajustar mappers/repos, ou remodelar portas conforme o design do domínio.  
- Introduzir `@RestControllerAdvice` e padronizar erros.  
- Externalizar configs (env), corrigir chaves JPA/Flyway.  
- Aplicar timeouts e retries no `RestClient`.  
- Adicionar actuator + métricas; começar testes unitários das validações.
