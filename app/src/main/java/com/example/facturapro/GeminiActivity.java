package com.example.facturapro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class GeminiActivity extends AppCompatActivity {
    TextView tvResultado;
    EditText edGemini;
    Button btnGenerar;
    ProgressBar progressBar; // Spinner de carga
    Executor executor;

    // Datos estáticos para simular consulta
    private final List<String> productos = Arrays.asList("Audífonos", "DDS", "Celular", "Tablet", "Laptop");
    private final List<String> vendedores = Arrays.asList("Carlos Morales", "Tatiana Salas", "Camila Guzman", "Sergio Torres", "Luisa Gomez", "Daniel Salinas", "Ana Florez", "Andres Escobar", "Juan Quiroz", "Marcela Ocampo");
    private final List<String> ciudades = Arrays.asList("Bogotá", "Medellín", "Cali", "Barranquilla", "Manizales");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gemini);

        // Configuración del layout principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        tvResultado = findViewById(R.id.tvResultado);
        edGemini = findViewById(R.id.edGemini);
        btnGenerar = findViewById(R.id.btnGenerar);
        progressBar = findViewById(R.id.progressBar);

        executor = MoreExecutors.directExecutor();

        // Configurar evento de clic
        btnGenerar.setOnClickListener(view -> procesarEntrada());
    }

    private void procesarEntrada() {
        String userInput = edGemini.getText().toString().toLowerCase().trim();

        if (userInput.isEmpty()) {
            tvResultado.setText("Por favor, ingresa una pregunta.");
            return;
        }

        // Verificar si la entrada corresponde a "productos", "vendedores" o "ciudades"
        if (userInput.contains("productos")) {
            mostrarDatos("productos");
        } else if (userInput.contains("vendedores")) {
            mostrarDatos("vendedores");
        } else if (userInput.contains("ciudades")) {
            mostrarDatos("ciudades");
        } else {
            generarContenido(userInput); // Usar el modelo generativo si no coincide
        }
    }

    private void mostrarDatos(String tipo) {
        String resultado;
        switch (tipo) {
            case "productos":
                resultado = "Productos disponibles:\n" + String.join(", ", productos);
                break;
            case "vendedores":
                resultado = "Vendedores disponibles:\n" + String.join(", ", vendedores);
                break;
            case "ciudades":
                resultado = "Ciudades disponibles:\n" + String.join(", ", ciudades);
                break;
            default:
                resultado = "Consulta no válida.";
                break;
        }

        tvResultado.setText(resultado);
    }

    private void generarContenido(String userInput) {
        progressBar.setVisibility(View.VISIBLE);
        btnGenerar.setEnabled(false);

        GenerativeModel gm = new GenerativeModel("gemini-1.5-flash", "API_KEY_AQUI");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText(userInput)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                runOnUiThread(() -> {
                    tvResultado.setText(resultText);
                    progressBar.setVisibility(View.GONE);
                    btnGenerar.setEnabled(true);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    tvResultado.setText("Error al generar la respuesta.");
                    progressBar.setVisibility(View.GONE);
                    btnGenerar.setEnabled(true);
                });
                t.printStackTrace();
            }
        }, executor);
    }
}
