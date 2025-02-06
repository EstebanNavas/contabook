package com.contabook.ServiceApi;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.contabook.Model.dbaquamovil.CertificadoResponse;

@Service
public class ApiCertificado {
	
	@Autowired
    private Gson gson;

	public CertificadoResponse consumirApi(String xToken) {
        String apiUrl = "https://mobile-tic.apifacturacionelectronica.xyz/api/ubl2.1/config/certificate";
        //String token = "ZStM8vj2gCMDvQMfXyVu4UFKIYsRgtjJ58peAUbOMFk1hmngz7BGxdbsdJOAPZrWdxdS5ujuaKC1Tp3F";

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);
        
        System.out.println("hola desde ApiCertificado ");

        // Agrega el token de autorización 
        httpGet.addHeader("Authorization", "Bearer " + xToken);

        try {
            HttpResponse response = httpClient.execute(httpGet);


            System.out.println("Código de respuesta: " + response.getStatusLine().getStatusCode());
            
            // Lee y muestra el cuerpo de la respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            
            System.out.println("Cuerpo de la respuesta: " + content.toString());
            
            // Parseamos el JSON a la clase CertificadoResponse
            CertificadoResponse certificadoResponse = gson.fromJson(content.toString(), CertificadoResponse.class);
            
            
          //  System.out.println("certificadoResponse " + certificadoResponse);
            
            // Imprime los resultados
           // System.out.println("Valor de 'is_valid': " + certificadoResponse.isIs_valid());
            //System.out.println("Fecha de expiración: " + certificadoResponse.getExpiration_date());
            
         // Verificar si certificadoResponse es nulo
            if (certificadoResponse == null) {
                System.err.println("Error al consumir API: respuesta nula.");
                // Manejar el error (registrar mensaje, valor predeterminado, etc.)
                return null;
            }

          
            
            return certificadoResponse;

        } catch (Exception e) {
            e.printStackTrace();
            
            
            
        } finally {
            // Cerramos el HttpClient 
            try {
                ((Closeable) httpClient).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return null;
    }

}
