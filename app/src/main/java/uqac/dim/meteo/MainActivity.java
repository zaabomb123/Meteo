package uqac.dim.meteo;



import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText etCityName;
    private Button btnSearch;
    private ImageView iconWeather;
    private TextView tvTemp, tvCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cacher l'ActionBar
        getSupportActionBar().hide();

        // Initialisation des éléments
        btnSearch = findViewById(R.id.btnSearch);
        etCityName = findViewById(R.id.etCityName);
        iconWeather = findViewById(R.id.iconWeather);
        tvTemp = findViewById(R.id.tvTemp);
        tvCity = findViewById(R.id.tvCity);

        // Listener pour le bouton
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCityName.getText().toString().trim();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                } else {
                    // Charger la météo
                    new FetchWeatherTask().execute(city);
                }
            }
        });
    }

    // Classe interne pour exécuter la requête HTTP en arrière-plan
    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        private static final String API_KEY = "VOTRE_CLE_API";
        private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";

        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            String urlString = API_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric";
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    // Parsing du JSON
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject main = jsonObject.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    String cityName = jsonObject.getString("name");

                    // Mise à jour des TextViews
                    tvTemp.setText(String.format("%.1f°C", temp));
                    tvCity.setText(cityName);

                    // (Facultatif) Mettre à jour une icône météo selon le code reçu
                    // int weatherCode = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
                    // Utilisez weatherCode pour changer l'icône de iconWeather

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error parsing weather data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
