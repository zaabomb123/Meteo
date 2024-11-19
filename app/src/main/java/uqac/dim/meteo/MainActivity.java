package uqac.dim.meteo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {
private static final String API_KEY = "74b67fd8bca533ff5d9400a4b3c02720";
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
                    loadWeatherByCityName(city);
                }
            }
        });
    }

    private void loadWeatherByCityName(String city) {

        Ion.with(this)
                .load("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&&units=metric&appid=" + API_KEY)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error



                    }
                });





    // Classe interne pour exécuter la requête HTTP en arrière-plan





    }

}
