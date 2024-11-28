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

import java.util.concurrent.Executor;

public class GeminiActivity extends AppCompatActivity {
    TextView tvResultado;
    EditText edGemini;
    Button btnGenerar;
    ProgressBar progressBar; // Agregado para el spinner
    Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gemini);

        // Configuración del layout principal para evitar solapamientos con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los elementos del layout
        tvResultado = findViewById(R.id.tvResultado);
        edGemini = findViewById(R.id.edGemini);
        btnGenerar = findViewById(R.id.btnGenerar);
        progressBar = findViewById(R.id.progressBar); // Referencia al ProgressBar

        // Configurar el ejecutor para las operaciones asíncronas
        executor = MoreExecutors.directExecutor();

        // Configurar el evento de clic del botón
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto ingresado por el usuario
                String userInput = edGemini.getText().toString();

                // Validar que no esté vacío
                if (userInput.isEmpty()) {
                    tvResultado.setText("Por favor, ingresa una pregunta.");
                    return;
                }

                // Mostrar el spinner y deshabilitar el botón mientras carga
                progressBar.setVisibility(View.VISIBLE);
                btnGenerar.setEnabled(false);

                // Configuración del modelo generativo
                GenerativeModel gm = new GenerativeModel(
                        /* Nombre del modelo */ "gemini-1.5-flash",
                        /* Clave de API */ "AIzaSyCIqA5bxuUuc4wlrcHoN78cUhcsFQnLQ_0"
                );
                GenerativeModelFutures model = GenerativeModelFutures.from(gm);

                // Crear el contenido basado en la pregunta del usuario
                Content content = new Content.Builder()
                        .addText(userInput)
                        .build();

                // Llamada asíncrona al modelo para generar contenido
                ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

                Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        // Mostrar la respuesta en el TextView
                        String resultText = result.getText();
                        runOnUiThread(() -> {
                            tvResultado.setText(resultText);
                            progressBar.setVisibility(View.GONE);
                            btnGenerar.setEnabled(true);
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Manejar errores y mostrar un mensaje
                        runOnUiThread(() -> {
                            tvResultado.setText("Error al generar la respuesta.");
                            progressBar.setVisibility(View.GONE);
                            btnGenerar.setEnabled(true);
                        });
                        t.printStackTrace();
                    }
                }, executor);
            }
        });
    }
}
