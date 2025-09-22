
### **1. Criar Cliente (POST)**

- **Método:** POST
- **URL:** `http://localhost:8081/api/v1/customers`
- **Headers:**
    - Content-Type: `application/json`
- **Body (raw/json):**
```json
{
  "name": "Yuri",
  "zipCode": "38400000",
  "cpf": "12345678910"
}
```

---

### **2. Buscar Cliente por ID (GET)**

no terminal
docker ps,

pegar o id do container mongo e rodar comando:
docker exec -it ID /bin/bash,

mongosh -u root -p
password: example, está no docker compose
após aparecer no terminal
test>

use hexagonal

show collections (verificar se criou customers)

db.customers.find()
isso mostra todos os customers cadastrados, só pegar o ID

- **Método:** GET
- **URL:** `http://localhost:8081/api/v1/customers/{id}`  
  (substitua `{id}` pelo ID no banco de dados)

---

### **3. Atualizar Cliente por ID (PUT)**

- **Método:** PUT
- **URL:** `http://localhost:8081/api/v1/customers/{id}`  
  (substitua `{id}` pelo ID do cliente)
- **Headers:**
    - Content-Type: `application/json`
- **Body (raw/json):**
```json
{
  "name": "Yuri Atualizado",
  "cpf": "12345678910",
  "zipCode": "38400000"
}
```

---

### **4. Remover Cliente (DELETE)**

- **Método:** DELETE
- **URL:** `http://localhost:8081/api/v1/customers/{id}`  
  (substitua `{id}` pelo ID do cliente)