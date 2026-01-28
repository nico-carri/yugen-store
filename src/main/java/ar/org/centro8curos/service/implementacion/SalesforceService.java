package ar.org.centro8curos.service.implementacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ar.org.centro8curos.model.Pedido;
import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.model.enums.Genero;
import ar.org.centro8curos.model.enums.MetodoPago;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jakarta.annotation.PostConstruct;

@Service
public class SalesforceService {

    @Value("${app.data.seed.enabled:false}")
    private boolean seedEnabled;

    @Value("${salesforce.client-id}")
    private String clientId;

    @Value("${salesforce.client-secret}")
    private String clientSecret;

    @Value("${salesforce.login-url}")
    private String loginUrl;

    // no se están usando, pero los dejo por si acaso
    @Value("${salesforce.username}")
    private String username;
    @Value("${salesforce.password}")
    private String password;

    // --- 1. OBTENER TOKEN ---
    public String obtenerToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, request, Map.class);
            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            System.err.println("Detalle del error al obtener token: " + e.getMessage());
            return null;
        }
    }

    // --- 2. MÉTODO CENTRALIZADO DE ENVÍO ---
    private void enviarPedidoASalesforce(Map<String, Object> body, String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String instanceUrl = loginUrl.split("/services")[0];
            String url = instanceUrl + "/services/data/v60.0/sobjects/Pedido_Ecommerce__c/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(url, entity, String.class);
            
        } catch (Exception e) {
            System.err.println("Error enviando a Salesforce: " + e.getMessage());
        }
    }

    // --- 3. PROCESAR VENTA REAL ---
    public void procesarVentaParaSalesforce(Pedido pedido) {
        System.out.println("Sincronizando pedido real #" + pedido.getIdPedido() + " con Salesforce...");
        String token = obtenerToken();
        if (token == null) return;

        Usuario user = pedido.getUsuario();
        
        int edad = java.time.Period.between(user.getFechaNacimiento(), LocalDate.now()).getYears();

        Map<String, Object> body = new java.util.HashMap<>();

        body.put("Name", "Pedido #" + pedido.getIdPedido());
        body.put("Total__c", pedido.getTotal());
        body.put("Estado__c", "COMPLETADO");
        body.put("Fecha_Pedido__c", LocalDate.now().toString());
        body.put("Cliente_Email__c", user.getEmail());
        body.put("Ciudad_Cliente__c", user.getCiudad());
        body.put("Genero_Cliente__c", user.getGenero().toString());
        body.put("Edad_Cliente__c", edad);
        body.put("Metodo_Pago__c", "Tarjeta de Crédito"); 
        body.put("Cantidad_Items__c", 1); 

        enviarPedidoASalesforce(body, token); 
    }

    // --- 4. SIMULADOR ---
    public void simularVentasHistoricas(Usuario usuario) {
        System.out.println("Iniciando siembra de datos masiva...");
        String token = obtenerToken();
        if (token == null) return;

        Random random = new Random();
        String[] ciudades = {"Buenos Aires", "Córdoba", "Rosario", "Mendoza", "Salta", "San Miguel de Tucumán"};
        MetodoPago[] metodos = MetodoPago.values();
        int edadReal = java.time.Period.between(usuario.getFechaNacimiento(), LocalDate.now()).getYears();
        String[] categoriasDisponibles = {
            "Manga", "Figuras", "Accesorios", "Peluches", 
            "Indumentaria", "Escolar", "Multimedia", "Otros", "Ofertas"
        };

        Genero[] generos = Genero.values();


        for (int i = 1; i <= 60; i++) {
            long diasAtras = random.nextInt(180); 
            LocalDate fechaAleatoria = LocalDate.now().minus(diasAtras, ChronoUnit.DAYS);
            BigDecimal montoAleatorio = BigDecimal.valueOf(5000 + (random.nextDouble() * 145000));
            String categoriaAleatoria = categoriasDisponibles[random.nextInt(categoriasDisponibles.length)];
            MetodoPago metodoAleatorio = metodos[random.nextInt(metodos.length)];
            Genero generoAleatorio = generos[random.nextInt(generos.length)];
            int edadAleatoria = 18 + random.nextInt(42);

            Map<String, Object> body = new java.util.HashMap<>();

            body.put("Categoria__c", categoriaAleatoria);
            body.put("Name", "Pedido Histórico #" + (3000 + i));
            body.put("Total__c", montoAleatorio);
            body.put("Estado__c", "COMPLETADO");
            body.put("Fecha_Pedido__c", fechaAleatoria.toString());
            body.put("Cliente_Email__c", usuario.getEmail());
            body.put("Ciudad_Cliente__c", ciudades[random.nextInt(ciudades.length)]);
            body.put("Genero_Cliente__c", usuario.getGenero().toString());
            body.put("Edad_Cliente__c", edadReal); 
            body.put("Metodo_Pago__c", metodoAleatorio.getDescripcion());
            body.put("Cantidad_Items__c", 1 + random.nextInt(5));
            body.put("Genero_Cliente__c", generoAleatorio.toString());
            body.put("Edad_Cliente__c", edadAleatoria);
            
            enviarPedidoASalesforce(body, token);
            
            if (i % 10 == 0) System.out.println("Procesados " + i + " de 60 registros...");
        }
        System.out.println("Inyección completada. Datos listos para Tableau.");
    }

    // --- 5. LIMPIEZA ---
    public void limpiarPedidosEnSalesforce() {
        System.out.println("Limpiando registros antiguos en Salesforce...");
        String token = obtenerToken();
        if (token == null) return;

        RestTemplate restTemplate = new RestTemplate();
        String instanceUrl = loginUrl.split("/services")[0];
        
        String queryUrl = instanceUrl + "/services/data/v60.0/query/?q=SELECT+Id+FROM+Pedido_Ecommerce__c";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(queryUrl, HttpMethod.GET, entity, Map.class);
            List<Map<String, String>> records = (List<Map<String, String>>) response.getBody().get("records");

            if (records != null && !records.isEmpty()) {
                System.out.println("Encontrados " + records.size() + " registros antiguos. Eliminando...");
                for (Map<String, String> record : records) {
                    String id = record.get("Id");
                    String deleteUrl = instanceUrl + "/services/data/v60.0/sobjects/Pedido_Ecommerce__c/" + id;
                    restTemplate.exchange(deleteUrl, HttpMethod.DELETE, entity, String.class);
                }
                System.out.println("Salesforce está limpio.");
            } else {
                System.out.println("Nada que limpiar.");
            }
        } catch (Exception e) {
            System.err.println("Error al limpiar (puede que no haya datos): " + e.getMessage());
        }
    }

    // --- 6. EJECUCIÓN AL INICIO ---
    @PostConstruct
    public void testConnection() {
        System.out.println("--- INICIANDO PRUEBA DE CONEXIÓN SALESFORCE ---");
        String token = obtenerToken();

        if (token != null) {
            System.out.println("ÉXITO: Conectado a Salesforce.");

            Usuario testUser = new Usuario();
            testUser.setEmail("analisis@datastudio.com");
            testUser.setNombre("Admin");
            testUser.setFechaNacimiento(LocalDate.of(1990, 5, 15)); 
            testUser.setGenero(ar.org.centro8curos.model.enums.Genero.MASCULINO); 

            if (seedEnabled) {
                System.out.println("--- MODALIDAD SIEMBRA ACTIVADA ---");
                
                limpiarPedidosEnSalesforce(); 
                simularVentasHistoricas(testUser); 
                
                System.out.println("¡Simulación terminada! Revisa Salesforce.");
            } else {
                System.out.println("--- MODALIDAD CONSULTA: Se mantienen los datos existentes ---");
            }
        } else {
            System.err.println("ERROR: No se pudo conectar. Revisa tus credenciales.");
        }
    }

    
}