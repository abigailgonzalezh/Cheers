package com.example.cheers.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cheers.BluetoothConnectionManager;
import com.example.cheers.DBHandler;
import com.example.cheers.LoadFavorites;
import com.example.cheers.Objetos.DrinkIngredient;
import com.example.cheers.Objetos.Ingredients;
import com.example.cheers.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends Activity {

    public Button btnScanner;
    public static ArrayList<DrinkIngredient> drinks;
    public static ArrayList< Ingredients > ingredients;
    public static DBHandler handler;
    public static String MAC_ADDRESS;
    BluetoothConnectionManager manager;

    LoadFavorites favorites;

    public static boolean connectionSecured;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScanner = findViewById(R.id.btnScanner);

        drinks = new ArrayList<>();
        ingredients = new ArrayList<>();
        handler = new DBHandler(this,null,null,1);
        loadData();
        connectionSecured = false;
        favorites = new LoadFavorites(this);
        favorites.loadFavorites();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0 || resultCode == -1){
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        MAC_ADDRESS = result.getContents();
        manager = BluetoothConnectionManager.getInstance(MAC_ADDRESS);
        String br = "\n";
        String f = MAC_ADDRESS.concat(br);
        byte[] b = f.getBytes();
        manager.send(b);

        if(result != null && resultCode == RESULT_OK && connectionSecured){
                Intent intent = new Intent(MainActivity.this, homeActivity.class);
                startActivity(intent);
                //rfinish();
        }
    }

    public void lector(View v){
        new IntentIntegrator(MainActivity.this).initiateScan();
    }

    public static void loadData(){
        //reset all the data
        ingredients.clear();

        //fill data in ArrayList
        ingredients.addAll(handler.getIngredients());

        if(ingredients.isEmpty()){
            handler.addIngredient("Ron",0);
            handler.addIngredient("Vino Tinto",0);
            handler.addIngredient("Mezcal",0);
            handler.addIngredient("Ginebra",0);
            handler.addIngredient("Brandy",0);
            handler.addIngredient("Vodka",0);
            handler.addIngredient("Agua Mineral",1);
            handler.addIngredient("Agua Tónica",1);
            handler.addIngredient("Jugo de Uva",1);
            handler.addIngredient("Jarabe",1);
            handler.addIngredient("Crema de Cacao",1);
            handler.addIngredient("Nata Liquida",1);
            handler.addIngredient("Jugo de Limón",1);
            ingredients.clear();
            ingredients.addAll(handler.getIngredients());
        }

        for(int i = 0; i < ingredients.size(); i++){
            System.out.println(ingredients.get(i).getId() + ": " + ingredients.get(i).getName() + " --> " + ingredients.get(i).getType());
        }
    }
}
