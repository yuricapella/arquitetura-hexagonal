## **Resumo das Principais Alterações no `kafka` (docker-compose)**

### **Como estava (2020 — config antiga, compatível só até Kafka 2.x):**
```yaml
kafka:
  image: confluentinc/cp-kafka:latest
  networks:
    - broker-kafka
  depends_on:
    - zookeeper
  ports:
    - 9092:9092
  environment:
    KAFKA_BROKER_ID: 1
    KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://localhost:9092
    KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
    KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
    KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

---

### **Como ficou (2025 — config compatível com Kafka moderno CP 7.x+):**
```yaml
kafka:
  image: confluentinc/cp-kafka:latest
  networks:
    - broker-kafka
  depends_on:
    - zookeeper
  ports:
    - "9092:9092"
    - "9093:9093"
  environment:
    CLUSTER_ID: 'testcluster'
    KAFKA_PROCESS_ROLES: broker,controller
    KAFKA_NODE_ID: 1
    KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
    KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
    KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
    KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
    KAFKA_BROKER_ID: 1
    KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

**Principais mudanças:**
- Adição do campo obrigatório `CLUSTER_ID`.
- Uso de `KAFKA_PROCESS_ROLES` (novo modelo Raft de Kafka moderno).
- `KAFKA_NODE_ID` (obrigatório).
- `KAFKA_LISTENERS` e `KAFKA_CONTROLLER_LISTENER_NAMES` para modo KRaft/Quorum.
- `KAFKA_CONTROLLER_QUORUM_VOTERS` define o quorum (mesmo em ambiente single node/teste).
- Portas 9092 e 9093 abertas.


